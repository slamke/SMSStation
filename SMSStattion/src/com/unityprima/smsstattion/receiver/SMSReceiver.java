package com.unityprima.smsstattion.receiver;

import static com.unityprima.smsstattion.sms.SMSModel.YES_STATUS;

import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.activeandroid.query.Select;
import com.unityprima.smsstattion.sms.SMSModel;
import com.unityprima.smsstattion.utils.Constants;
import com.unityprima.smsstattion.utils.DateParse;

public class SMSReceiver extends BroadcastReceiver{
	
	public static final String ACTION_SMS_SEND = "SMS_SEND_ACTIOIN"; 
	
	public static final String ACTION_SMS_DELIVERY = "SMS_DELIVERED_ACTION_A";
    
	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		String actionName = intent.getAction();
        Log.e("actionName:", ""+actionName);
        long id = intent.getLongExtra(Constants.SMS_SEND_ID, -1);
        Log.e("id", ""+id);
        int resultCode = getResultCode();  
        if (actionName.equals(ACTION_SMS_SEND)) {  
            switch (resultCode) {  
            case Activity.RESULT_OK:  
                //SMS Send:Successed!
            	SMSModel model = new Select().from(SMSModel.class).where("smsid = ?", id).executeSingle();
            	if (model != null) {
            		model.status = YES_STATUS;
    				model.submitRespTime = new DateParse().date2String(new Date());
    				model.submitStatus = YES_STATUS;
    				model.save();
				}
                break;  
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:  
                //SMS Send:RESULT_ERROR_GENERIC_FAILURE!
                break;  
            case SmsManager.RESULT_ERROR_NO_SERVICE:  
                //SMS Send:RESULT_ERROR_NO_SERVICE! 
                break;  
            case SmsManager.RESULT_ERROR_NULL_PDU:  
                //SMS Send:RESULT_ERROR_NULL_PDU!  
                break;  
            case SmsManager.RESULT_ERROR_RADIO_OFF:  
                break;  
            }  
        } else if (actionName.equals(ACTION_SMS_DELIVERY)) {  
            switch (resultCode) {  
            case Activity.RESULT_OK:  
                //SMS Delivery:Successed!
            	SMSModel model = new Select().from(SMSModel.class).where("smsid = ?", id).executeSingle();
            	if(model != null){
            		model.reveivedTime = new DateParse().date2String(new Date());
    				model.reveivedStatus = YES_STATUS;
    				model.save();
            	}
                break;  
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:  
                //SMS Delivery:RESULT_ERROR_GENERIC_FAILURE! 
                break;  
            case SmsManager.RESULT_ERROR_NO_SERVICE:  
                //SMS Delivery:RESULT_ERROR_NO_SERVICE! 
                break;  
            case SmsManager.RESULT_ERROR_NULL_PDU:  
                //SMS Delivery:RESULT_ERROR_NULL_PDU!
                break;  
            case SmsManager.RESULT_ERROR_RADIO_OFF:  
                //SMS Delivery:RESULT_ERROR_RADIO_OFF!  
                break;  
            }  
        }
	}
}
