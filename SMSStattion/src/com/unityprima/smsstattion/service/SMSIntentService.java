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
import java.util.LinkedList;
import java.util.List;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.unityprima.smsstattion.entity.SMSMO;
import com.unityprima.smsstattion.entity.SMSSent;
import com.unityprima.smsstattion.entity.SMSWSend;
import com.unityprima.smsstattion.sms.SMSLog;
import com.unityprima.smsstattion.sms.SMSModel;
import com.unityprima.smsstattion.sms.SMSReceived;
import com.unityprima.smsstattion.sms.SMSReceivedDAO;
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

	private Handler h;
	
	public SMSIntentService() {
		// 必须实现父类的构造方法
		super("SMSIntentService");
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i("SMSIntentService", "onBind");
		return super.onBind(intent);
	}

	@Override
	public void onCreate() {
		Log.i("SMSIntentService", "onCreate");
		super.onCreate();
		h=new Handler();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i("SMSIntentService", "onStart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("SMSIntentService", "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void setIntentRedelivery(boolean enabled) {
		super.setIntentRedelivery(enabled);
		Log.i("SMSIntentService", "setIntentRedelivery");
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
			
			try {
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
			} catch (IllegalStateException e) {
				// TODO: handle exception
				e.printStackTrace();
				h.post(new Runnable() {  
                    @Override  
                    public void run() {  
                    	Toast.makeText(getApplicationContext(), Message.TIP_NOT_SET_SERVER, Toast.LENGTH_SHORT).show();
                    }  
                });  
				
			}
		}else if (action.equals(Constants.OPERATION_RECEIVE)) {
			try {
				List<SMSMO> list = new SmsReader(this).getSmsInfo();			
				// 过滤已经转移的短信
				list = filterReceivedSMS(list);
				List<SMSMO> smsList = SMSMO.splitList(list);
	            UploadWebService uploadWebService = new UploadWebService(this);
	            String feedBackFromSever = null;
	            if(smsList != null){
	                feedBackFromSever = uploadWebService.sendSMSMOList(smsList);
	                Log.i("feedBackFromSever:", feedBackFromSever);
	                if (feedBackFromSever.equals(Message.SUCCESS)){
	                    List<Long> id_Upload = new ArrayList<Long>();
	                    for(SMSMO temp : list){
	                        id_Upload.add(temp.getId());
	                        
	                        // 记录转义记录，防止重复转移
	                    	SMSReceived smsReceived = new SMSReceived();
	            			smsReceived.smsid = temp.getId();
	            			smsReceived.md5 = temp.getMD5();
	            			smsReceived.addTime = new DateParse().date2String(new Date());
	            			smsReceived.save();
	                    }
	                    if(id_Upload != null){
	                        new SmsDeleter(this).smsDelete(id_Upload);
	                    }
	                }else{
	                	//记录错误log
	    				SMSLog log = new SMSLog();
	    				log.content = Constants.OPERATION_RECEIVE+" "+ feedBackFromSever;
	    				log.logType = SMSLog.TYPE_LOAD;
	    				log.time = new DateParse().date2String(new Date());
	    				log.save();
	                }
	            }
			} catch (IllegalStateException e) {
				e.printStackTrace();
				h.post(new Runnable() {  
                    @Override  
                    public void run() {  
                    	Toast.makeText(getApplicationContext(), Message.TIP_NOT_SET_SERVER, Toast.LENGTH_SHORT).show();
                    }  
                });  
			}
		} else if (action.equals(Constants.OPERATION_SEND)) {
			try {
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
							//该短信已经发送
							if (temp != null) {
								continue;
							}
							//接收号码为空时，进行忽略
							if(smswSend.getMbno() == null || smswSend.getMbno().equals("")){
								continue;
							}
							Intent sendIntent = new Intent(ACTION_SMS_SEND);
					        sendIntent.putExtra(SMS_SEND_ID, smswSend.getId());
					        PendingIntent sendPI = PendingIntent.getBroadcast(this, (int)smswSend.getId(), 
					        		sendIntent, 0);
					        Intent deliveryIntent = new Intent(ACTION_SMS_DELIVERY);
					        sendIntent.putExtra(SMS_SEND_ID, smswSend.getId());
					        PendingIntent deliveryPI = PendingIntent.getBroadcast(this, (int)smswSend.getId(),  
					                deliveryIntent, 0);
					        
					        SmsManager manager = SmsManager.getDefault();  
				            ArrayList<String> smsList = manager.divideMessage(smswSend.getSms());  //因为一条短信有字数限制，因此要将长短信拆分  
				            for(String text:smsList){  
				                manager.sendTextMessage(smswSend.getMbno(),
										null, text, sendPI, deliveryPI);
				            }  
					        					        
							//SmsManager.getDefault().sendTextMessage(smswSend.getMbno(),
							//		null, smswSend.getSms(), sendPI, deliveryPI);
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
			} catch (IllegalStateException e) {
				// TODO: handle exception
				e.printStackTrace();
				h.post(new Runnable() {  
                    @Override  
                    public void run() {  
                    	Toast.makeText(getApplicationContext(), Message.TIP_NOT_SET_SERVER, Toast.LENGTH_SHORT).show();
                    }  
                });  
			} 
		}else if (action.equals(Constants.OPERATION_TRANSFER)) {
			try {
				TransferWebService transferWebService = new TransferWebService(this);
				transferWebService.notifyServerToTransfer();
			} catch (IllegalStateException e) {
				// TODO: handle exception
				e.printStackTrace();
				h.post(new Runnable() {  
                    @Override  
                    public void run() {  
                    	Toast.makeText(getApplicationContext(), Message.TIP_NOT_SET_SERVER, Toast.LENGTH_SHORT).show();
                    }  
                });  
			}
		}
	}

	@Override
	public void onDestroy() {
		System.out.println("onDestroy");
		super.onDestroy();
	}
	
	private List<SMSMO> filterReceivedSMS(List<SMSMO> smsList){
		if (smsList == null || smsList.isEmpty()) {
			return smsList;
		}
		List<SMSMO> result = new LinkedList<SMSMO>();
		SMSReceivedDAO dao = new SMSReceivedDAO();
		for (SMSMO smsmo : smsList) {
			SMSReceived smsReceived = new SMSReceived();
			smsReceived.smsid = smsmo.getId();
			smsReceived.md5 = smsmo.getMD5();
			smsReceived.addTime = new Date().toString();
			boolean exsits = dao.checkExists(smsReceived);
			if (!exsits) {
				result.add(smsmo);
			}
		}
		return result;
	}

}
