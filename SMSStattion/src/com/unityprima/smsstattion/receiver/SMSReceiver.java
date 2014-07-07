package com.unityprima.smsstattion.receiver;

import com.unityprima.smsstattion.utils.Constants;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver{
	public static final String ACTION_SMS_SEND = "SMS_SEND_ACTIOIN";  
	public static final String ACTION_SMS_DELIVERY = "SMS_DELIVERED_ACTION_A";
    
	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		String actionName = intent.getAction();
        Log.e("actionName:", ""+actionName);
        int id = intent.getIntExtra(Constants.SMS_SEND_ID, -1);
        Log.e("id", ""+id);
        int resultCode = getResultCode();  
        if (actionName.equals(ACTION_SMS_SEND)) {  
            switch (resultCode) {  
            case Activity.RESULT_OK:  
                //("/n[Send]SMS Send:Successed!");  
                break;  
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:  
                //("/n[Send]SMS Send:RESULT_ERROR_GENERIC_FAILURE!");  
                break;  
            case SmsManager.RESULT_ERROR_NO_SERVICE:  
                //("/n[Send]SMS Send:RESULT_ERROR_NO_SERVICE!");  
                break;  
            case SmsManager.RESULT_ERROR_NULL_PDU:  
                //("/n[Send]SMS Send:RESULT_ERROR_NULL_PDU!");  
                break;  
            case SmsManager.RESULT_ERROR_RADIO_OFF:  
                break;  
            }  
        } else if (actionName.equals(ACTION_SMS_DELIVERY)) {  
            switch (resultCode) {  
            case Activity.RESULT_OK:  
                //("/n[Delivery]SMS Delivery:Successed!");  
                break;  
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:  
                //("/n[Delivery]SMS Delivery:RESULT_ERROR_GENERIC_FAILURE!");  
                break;  
            case SmsManager.RESULT_ERROR_NO_SERVICE:  
                //("/n[Delivery]SMS Delivery:RESULT_ERROR_NO_SERVICE!");  
                break;  
            case SmsManager.RESULT_ERROR_NULL_PDU:  
                //("/n[Delivery]SMS Delivery:RESULT_ERROR_NULL_PDU!");  
                break;  
            case SmsManager.RESULT_ERROR_RADIO_OFF:  
                //("/n[Delivery]SMS Delivery:RESULT_ERROR_RADIO_OFF!");  
                break;  
            }  
        }
	}
}
