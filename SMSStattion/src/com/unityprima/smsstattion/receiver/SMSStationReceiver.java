package com.unityprima.smsstattion.receiver;

import static com.unityprima.smsstattion.utils.Constants.OPERATION_RECEIVE;
import static com.unityprima.smsstattion.utils.Constants.OPERATION_SEND;
import static com.unityprima.smsstattion.utils.Constants.OPERATION_TRANSFER;
import static com.unityprima.smsstattion.utils.Constants.OPERATION_TYPE;
import static com.unityprima.smsstattion.utils.Constants.OPERATION_FEED_BACK;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.unityprima.smsstattion.utils.Constants;
import com.unityprima.smsstattion.utils.Message;

public class SMSStationReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		SharedPreferences sp = arg0.getSharedPreferences(Message.PREFERENCE_NAME,
				Context.MODE_PRIVATE); // 获得Preferences
		//获取三个模块的开关记录
    	boolean flagReceive = sp.getBoolean(Message.SWITCH_RECEIVE, true);
    	boolean flagSend = sp.getBoolean(Message.SWITCH_SEND, true);
    	boolean flagTransfer = sp.getBoolean(Message.SWITCH_TRANSFER, true);
    	String severAddress = sp.getString(Message.SEVER_ADDRESS, "");
    	if (severAddress == null || severAddress.equals("")) {
			return;
		}
    	//短信回执模块先执行，防止成功发送的短信再次发送。。。
    	{
	    	Intent startServiceIntent = new Intent(Constants.SMS_INTENT_SERVICE);  
	        Bundle bundle = new Bundle();
	        bundle.putString(OPERATION_TYPE, OPERATION_FEED_BACK);  
	        startServiceIntent.putExtras(bundle);  
	        arg0.startService(startServiceIntent);
    	}
    	 //可以启动多次，每启动一次，就会新建一个work thread，但IntentService的实例始终只有一个  
    	if (flagSend) {
    		Intent startServiceIntent = new Intent(Constants.SMS_INTENT_SERVICE);  
            Bundle bundle = new Bundle();
            bundle.putString(OPERATION_TYPE, OPERATION_SEND);  
            startServiceIntent.putExtras(bundle);  
            arg0.startService(startServiceIntent);  
		}
    	
    	if (flagReceive) {
    		Intent startServiceIntent = new Intent(Constants.SMS_INTENT_SERVICE);  
            Bundle bundle = new Bundle();
            bundle.putString(OPERATION_TYPE, OPERATION_RECEIVE);  
            startServiceIntent.putExtras(bundle);  
            arg0.startService(startServiceIntent);  
		}
    	
    	if (flagTransfer) {
    		Intent startServiceIntent = new Intent(Constants.SMS_INTENT_SERVICE);  
            Bundle bundle = new Bundle();
            bundle.putString(OPERATION_TYPE, OPERATION_TRANSFER);  
            startServiceIntent.putExtras(bundle);  
            arg0.startService(startServiceIntent);    
		}
	}
}
