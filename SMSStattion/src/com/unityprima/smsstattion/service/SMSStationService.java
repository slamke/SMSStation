package com.unityprima.smsstattion.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/**
 * IntentService会自己开一个线程，
 * AlarmManager加PendingIntent可以实现每两小时发个广播，
 * 然后Reciever启动这个Service,Service干完活后stop掉。
 * @author sunke
 *
 */
public class SMSStationService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		AlarmManagerUtil.cancelUpdateBroadcast(this);
		AlarmManagerUtil.sendUpdateBroadcastRepeat(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
