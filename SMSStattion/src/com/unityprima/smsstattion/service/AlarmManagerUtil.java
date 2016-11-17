package com.unityprima.smsstattion.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.unityprima.smsstattion.receiver.SMSStationReceiver;
import com.unityprima.smsstattion.utils.DefaultSettingInfo;
import com.unityprima.smsstattion.utils.Message;

public class AlarmManagerUtil {
	
	public static AlarmManager getAlarmManager(Context ctx) {
		return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
	}

	/**
	 * 指定时间后进行更新赛事信息(有如闹钟的设置) 注意: Receiver记得在manifest.xml中注册
	 * @param ctx
	 */
	public static void sendUpdateBroadcast(Context ctx) {
		Log.i("score", "send to start update broadcase,delay time :" + 60000);

//		AlarmManager am = getAlarmManager(ctx);
//		// 60秒后将产生广播,触发UpdateReceiver的执行,这个方法才是真正的更新数据的操作主要代码
//		Intent i = new Intent(ctx, UpdateReceiver.class);
//		PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
//		am.set(AlarmManager.RTC, System.currentTimeMillis() + 60000,
//				pendingIntent);
	}

	/**
	 * 取消定时执行(有如闹钟的取消)
	 * 
	 * @param ctx
	 */
//	public static void cancelUpdateBroadcast(Context ctx) {
//		AlarmManager am = getAlarmManager(ctx);
//		Intent i = new Intent(ctx, UpdateReceiver.class);
//		PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
//		am.cancel(pendingIntent);
//	}

	/**
	 * 周期性的执行某项操作
	 * 
	 * @param ctx
	 */
	public static void sendUpdateBroadcastRepeat(Context ctx){
		    Intent intent =new Intent(ctx, SMSStationReceiver.class);
		    PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);
		    //开始时间
		    long firstime=SystemClock.elapsedRealtime();
		    AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
		    SharedPreferences settingInfo = ctx.getSharedPreferences(Message.PREFERENCE_NAME, Context.MODE_PRIVATE); 
	        float messageLoopClock = settingInfo.getFloat(Message.MESSAGE_LOOP_CLOCK, DefaultSettingInfo.DEFAULT_LOOP_CYCLE);
		    // FIXME  确定此处正常
	        // int cycle = (int)(messageLoopClock*1000);
	        int cycle = (int)(messageLoopClock*60*1000);
		    Log.i("SMSStation cycle(ms)", ""+cycle);
	        //int cycle = messageLoopClock*1000;
	        //设置一个周期，不停的发送广播
		    am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, cycle, pendingIntent);
		}

	/**
	 * 取消定时执行(有如闹钟的取消)
	 * 
	 * @param ctx
	 */
	public static void cancelUpdateBroadcast(Context ctx) {
		AlarmManager am = getAlarmManager(ctx);
		Intent i = new Intent(ctx, SMSStationReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
		am.cancel(pendingIntent);
	}
}
