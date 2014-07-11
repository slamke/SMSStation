package com.unityprima.smsstattion.service;

import static com.unityprima.smsstattion.receiver.SMSReceiver.ACTION_SMS_DELIVERY;
import static com.unityprima.smsstattion.receiver.SMSReceiver.ACTION_SMS_SEND;
import static com.unityprima.smsstattion.sms.SMSModel.NOT_STATUS;
import static com.unityprima.smsstattion.sms.SMSModel.YES_STATUS;
import static com.unityprima.smsstattion.utils.Constants.SMS_SEND_ID;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.unityprima.smsstattion.entity.SMSMO;
import com.unityprima.smsstattion.entity.SMSSent;
import com.unityprima.smsstattion.entity.SMSWSend;
import com.unityprima.smsstattion.sms.SMSLog;
import com.unityprima.smsstattion.sms.SMSModel;
import com.unityprima.smsstattion.utils.Constants;
import com.unityprima.smsstattion.utils.DateParse;
import com.unityprima.smsstattion.utils.Message;
import com.unityprima.smsstattion.utils.SmsDeleter;
import com.unityprima.smsstattion.utils.SmsReader;
import com.unityprima.smsstattion.webservice.FeedBackWebService;
import com.unityprima.smsstattion.webservice.LoaderWebService;
import com.unityprima.smsstattion.webservice.TransferWebService;
import com.unityprima.smsstattion.webservice.UploadWebService;

public class SMSIntentService extends IntentService {

	public SMSIntentService() {
		// 必须实现父类的构造方法
		super("SMSIntentService");
	}

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("onBind");
		return super.onBind(intent);
	}

	@Override
	public void onCreate() {
		System.out.println("onCreate");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		System.out.println("onStart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void setIntentRedelivery(boolean enabled) {
		super.setIntentRedelivery(enabled);
		System.out.println("setIntentRedelivery");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		//Intent是从Activity发过来的，携带识别参数，根据参数不同执行不同的任务
		String action = intent.getExtras().getString(Constants.OPERATION_TYPE);
		if (action.equals(Constants.OPERATION_FEED_BACK)) {
			List<SMSModel> list = new Select().from(SMSModel.class).where("status = ?", YES_STATUS).execute();
			List<SMSModel> other = new Select().from(SMSModel.class).where("status = ?", NOT_STATUS).execute();
			if (list != null) {
				System.out.println("list size:"+list.size());
			}
			if (other != null) {
				System.out.println("other size:"+other.size());
			}
			
			List<SMSModel> cur = new ArrayList<SMSModel>();
			final long ONEDAY =86400000;
			if (other != null && other.size()>0 ) {
				for (SMSModel smsModel : other) {
					long time = new DateParse().string2Date(smsModel.submitTime).getTime();
					if ((Calendar.getInstance().getTime().getTime() - time) > ONEDAY) {
						cur.add(smsModel);
					}
				}
			}
			if (list != null && list.size() > 0) {
				cur.addAll(list);
			}
			if (cur.size() == 0) {
				return;
			}
			List<SMSSent> sentList = new ArrayList<SMSSent>();
			for (SMSModel model : cur) {
				SMSSent sent = new SMSSent();
				DateParse parse = new DateParse();
				sent.setWsendId(model.smsid);
				sent.setReveivedStatus(model.reveivedStatus);
				Date reveivedTime =parse.string2Date(model.reveivedTime);
				if (reveivedTime != null) {
					sent.setReveivedTime(new Timestamp(reveivedTime.getTime()));
				}
				Date submitTime = parse.string2Date(model.submitTime);
				if (submitTime != null) {
					sent.setSubmitTime(new Timestamp(submitTime.getTime()));
				}
				Date submitRespTime = parse.string2Date(model.submitRespTime);
				if (submitRespTime != null) {
					sent.setSubmitRespTime(new Timestamp(submitRespTime.getTime()));
				}
				sent.setSubmitStatus(model.submitStatus);
				sentList.add(sent);
			}
			FeedBackWebService feedBackWebService = new FeedBackWebService(this);
			String res = feedBackWebService.feedBackWithSuccessSMS(sentList);
			if (res.equals(Message.SUCCESS)) {
				for (SMSModel model : cur) {
					new Delete().from(SMSModel.class).where("smsid = ?", model.smsid).execute();
				}
			}else {
				//记录错误log
				SMSLog log = new SMSLog();
				log.content = Constants.OPERATION_FEED_BACK+" "+ res;
				log.logType = SMSLog.TYPE_SEND;
				log.time = new DateParse().date2String(new Date());
				log.save();
			}
		}else if (action.equals(Constants.OPERATION_RECEIVE)) {
            List<SMSMO> smsList = new SmsReader(this).getSmsInfo();
            UploadWebService uploadWebService = new UploadWebService(this);
            String feedBackFromSever = null;
            if(smsList == null){

            }else{
                feedBackFromSever = uploadWebService.sendSMSMOList(smsList);
                if (feedBackFromSever == Message.SUCCESS){
                    List<Long> id_Upload = new ArrayList<Long>();
                    for(SMSMO temp : smsList){
                        id_Upload.add(temp.getId());
                    }

                    if(id_Upload != null){
                        new SmsDeleter(this).smsDelete(id_Upload);
                    }
                }else if(feedBackFromSever == Message.ERROR){

                }else if(feedBackFromSever == Message.NETWORK_FAIL){

                }
            }



		} else if (action.equals(Constants.OPERATION_SEND)) {
			LoaderWebService loaderWebService = new LoaderWebService(this);
	    	List<SMSWSend> list = loaderWebService.getSMSSendList();
	    	if (list == null) {
				SMSLog log = new SMSLog();
				log.content = "load error";
				log.logType = SMSLog.TYPE_LOAD;
				log.time = new DateParse().date2String(new Date());
				log.save();
			}else {
				for (SMSWSend smswSend : list) {
					if (smswSend.getSms() != null && !smswSend.getSms().equals("")) {
						SMSModel temp = new Select().from(SMSModel.class).where("smsid = ?", smswSend.getId()).executeSingle();
						//该短信已经成功发送
						if (temp != null) {
							continue;
						}
						
						Intent sendIntent = new Intent(ACTION_SMS_SEND);
				        sendIntent.putExtra(SMS_SEND_ID, smswSend.getId());
				        PendingIntent sendPI = PendingIntent.getBroadcast(this, 0, 
				        		sendIntent, 0);
				        Intent deliveryIntent = new Intent(ACTION_SMS_DELIVERY);
				        sendIntent.putExtra(SMS_SEND_ID, smswSend.getId());
				        PendingIntent deliveryPI = PendingIntent.getBroadcast(this, 0,  
				                deliveryIntent, 0); 
						SmsManager.getDefault().sendTextMessage(smswSend.getMbno(),
								null, smswSend.getSms(), sendPI, deliveryPI);
						if (temp == null) {
							SMSModel model = new SMSModel();
							model.mbno = smswSend.getMbno();
							model.reveivedStatus = "0";
							model.reveivedStatus = NOT_STATUS;
							model.reveivedTime = "";
							model.sendsn = smswSend.getSendsn();
							model.sms = smswSend.getSms();
							model.smsid = smswSend.getId();
							Log.e("smsid:", ""+smswSend.getId());
							model.status = NOT_STATUS;
							model.submitRespTime = NOT_STATUS;
							model.submitStatus = NOT_STATUS;
							model.submitTime = new DateParse().date2String(new Date());
							model.save();
						}
					}
				}
			}
		}else if (action.equals(Constants.OPERATION_TRANSFER)) {
			TransferWebService transferWebService = new TransferWebService(this);
			transferWebService.notifyServerToTransfer();
		}
	}

	@Override
	public void onDestroy() {
		System.out.println("onDestroy");
		super.onDestroy();
	}

}
