package com.unityprima.smsstattion.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import com.unityprima.smsstattion.utils.DefaultSettingInfo;
import com.unityprima.smsstattion.utils.Message;

/**
 * 监控电量的service
 * Android Service的生命周期只继承了onCreate(),onStart(),onDestroy()三个方法，
 * 当我们第一次启动Service时，先后调用了onCreate(),onStart()这两个方法，
 * 当停止Service时，则执行onDestroy()方法，这里需要注意的是，如果Service已经启动了，当我们再次启动Service时，
 * 不会在执行onCreate()方法，而是直接执行onStart()方法，
 * 当我们想获取启动的Service实例时，我们可以用到bindService和onBindService方法，它们分别执行了Service中onBind()
 * 和onUnbind()方法。
 * @author sunke
 *
 */
public class MonitorPowerService extends Service {
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// 定义电池电量更新广播的过滤器,只接受带有ACTION_BATTERRY_CHANGED事件的Intent
		IntentFilter batteryChangedReceiverFilter = new IntentFilter();
		batteryChangedReceiverFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		// 向系统注册batteryChangedReceiver接收器，本接收器的实现见代码字段处
		registerReceiver(batteryChangedReceiver, batteryChangedReceiverFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(batteryChangedReceiver);
	}

	// 接受电池信息更新的广播
	private BroadcastReceiver batteryChangedReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0);
			// int scale = intent.getIntExtra("scale", 100);
			int status = intent.getIntExtra("status", 0);
			SharedPreferences sp = getSharedPreferences(
					Message.PREFERENCE_NAME, MODE_PRIVATE); // 获得Preferences
			int threshold = sp.getInt(Message.POWER_THRESHOLD,
					DefaultSettingInfo.DEFAULT_WARNING_POWER_THRESHOLD);
			String tel = sp.getString(Message.NOTICER_PHONE_NUMBER, "");
			String sms = null;
			
			boolean isNoticed = sp.getBoolean(Message.POWER_LOW_NOTICE, false);
			
			//电量小于阈值并且没用充电时，进行报警，并且设置标志位，放置多次报警
			if (level < threshold
					&& status != BatteryManager.BATTERY_STATUS_CHARGING && !isNoticed) {
				sms = "您好，短信中转站的手机电池电量已经低于" + level + "%，请及时充电并且检查手机";
				SharedPreferences.Editor editor = sp.edit(); // 获得Editor
				editor.putBoolean(Message.POWER_LOW_NOTICE, true);
				editor.commit();
			} else if (level < 5) {
				sms = Message.POWER_LOW_AND_NOT_CHARGE;
			}else if (level > threshold) {  //电量上升后，重置标志位，进行下一次低电量报警
				SharedPreferences.Editor editor = sp.edit(); // 获得Editor
				editor.putBoolean(Message.POWER_LOW_NOTICE, false);
				editor.commit();
			}
			Log.e("smslevel:", "" + level);
			Log.e("smstel:", "" + tel);
			if(tel != null && !tel.equals("")  && sms!= null){
				SmsManager.getDefault().sendTextMessage(tel, null, sms, null,
						null);
			}
		}
	};
}