package com.unityprima.smsstattion.service;

import static com.unityprima.smsstattion.receiver.SMSReceiver.ACTION_SMS_DELIVERY;
import static com.unityprima.smsstattion.receiver.SMSReceiver.ACTION_SMS_SEND;
import static com.unityprima.smsstattion.sms.SMSModel.NOT_STATUS;
import static com.unityprima.smsstattion.utils.Constants.SMS_SEND_ID;

import java.util.Date;
import java.util.List;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;

import com.unityprima.smsstattion.entity.SMSWSend;
import com.unityprima.smsstattion.sms.SMSLog;
import com.unityprima.smsstattion.sms.SMSModel;
import com.unityprima.smsstattion.utils.Constants;
import com.unityprima.smsstattion.utils.DateParse;
import com.unityprima.smsstattion.webservice.LoaderWebService;
import com.unityprima.smsstattion.webservice.TransferWebService;
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
		// Intent是从Activity发过来的，携带识别参数，根据参数不同执行不同的任务
		String action = intent.getExtras().getString(Constants.OPERATION_TYPE);
		if (action.equals(Constants.OPERATION_RECEIVE)) {
			
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
						SMSModel model = new SMSModel();
						model.mbno = smswSend.getMbno();
						model.reveivedStatus = "0";
						model.reveivedStatus = NOT_STATUS;
						model.reveivedTime = "";
						model.sendsn = smswSend.getSendsn();
						model.sms = smswSend.getSms();
						model.smsid = smswSend.getId();
						model.status = NOT_STATUS;
						model.submitRespTime = NOT_STATUS;
						model.submitStatus = NOT_STATUS;
						model.submitTime = new DateParse().date2String(new Date());
						model.save();
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
