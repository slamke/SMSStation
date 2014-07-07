package com.unityprima.smsstattion.receiver;

import com.unityprima.smsstattion.utils.DefaultSettingInfo;
import com.unityprima.smsstattion.utils.Message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.telephony.SmsManager;
import android.util.Log;

public class BatteryChangedReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		int level = intent.getIntExtra("level", 0);
		// int scale = intent.getIntExtra("scale", 100);
		int status = intent.getIntExtra("status", 0);
		SharedPreferences sp = arg0.getSharedPreferences(
				Message.PREFERENCE_NAME, Context.MODE_PRIVATE); // 获得Preferences
		int threshold = sp.getInt(Message.POWER_THRESHOLD,
				DefaultSettingInfo.DEFAULT_WARNING_POWER_THRESHOLD);
		String tel = sp.getString(Message.NOTICER_PHONE_NUMBER, "");
		String sms = null;
		if (level < threshold
				&& status != BatteryManager.BATTERY_STATUS_CHARGING) {
			sms = "您好，短信中转站的手机电池电量已经低于" + level + "%，请及时充电并且检查手机";
		} else if (level < 5) {
			sms = Message.POWER_LOW_AND_NOT_CHARGE;
		}
		Log.e("smslevel:", "" + level);
		Log.e("smstel:", "123" + tel);
		if(tel != null && !tel.equals("")  && sms!= null){
			SmsManager.getDefault().sendTextMessage(tel, null, sms, null,
					null);
		}
	}

}
