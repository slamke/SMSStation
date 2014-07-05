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
 
    // 由于初始化本服务时系统可能没有发出ACTION_BATTERY_CHANGED广播，那么刚才注册的那个接收器将不会在本服务启动时被激活，这种情况下就无法显示当前电量，因此在这里添加一个匿名广播接收器。  
    new BroadcastReceiver() {  
      @Override 
      public void onReceive(Context context, Intent intent) {  
        int level = intent.getIntExtra("level", 0);  //电池电量等级  
        //int scale = intent.getIntExtra("scale", 100);  //电池满时百分比  
        int status = intent.getIntExtra("status", 0);  //电池状态 
        Log.e("level:",""+level);
        //Log.e("scale:",""+scale);
        Log.e("status:",""+status);
        
        SharedPreferences sp = getSharedPreferences(
				Message.PREFERENCE_NAME, MODE_PRIVATE); // 获得Preferences
        int threshold = sp.getInt(Message.POWER_THRESHOLD,DefaultSettingInfo.DEFAULT_WARNING_POWER_THRESHOLD);
        String tel = sp.getString(Message.NOTICER_PHONE_NUMBER, "");
        String sms = "";
        if (level< threshold && status != BatteryManager.BATTERY_STATUS_CHARGING) {
			sms = "您好，短信中转站的手机电池电量已经低于"+level+"%，请及时充电并且检查手机";
		}else if(level< 5){
			sms = Message.POWER_LOW_AND_NOT_CHARGE;
		}
        SmsManager.getDefault().sendTextMessage(tel,
				null, sms, null, null);
      }  
    };  
  }  
 
  @Override 
  public void onDestroy() { 
	  super.onDestroy();
  }  
 
 
  // 接受电池信息更新的广播  
  private BroadcastReceiver batteryChangedReceiver = new BroadcastReceiver() {  
    public void onReceive(Context context, Intent intent) {  
      int level = intent.getIntExtra("level", 0);  
      //int scale = intent.getIntExtra("scale", 100);  
      int status = intent.getIntExtra("status", 0); 
      SharedPreferences sp = getSharedPreferences(
				Message.PREFERENCE_NAME, MODE_PRIVATE); // 获得Preferences
      int threshold = sp.getInt(Message.POWER_THRESHOLD,DefaultSettingInfo.DEFAULT_WARNING_POWER_THRESHOLD);
      String tel = sp.getString(Message.NOTICER_PHONE_NUMBER, "");
      String sms = "";
      if (level< threshold && status != BatteryManager.BATTERY_STATUS_CHARGING) {
			sms = "您好，短信中转站的手机电池电量已经低于"+level+"%，请及时充电并且检查手机";
		}else if(level< 5){
			sms = Message.POWER_LOW_AND_NOT_CHARGE;
		}
      SmsManager.getDefault().sendTextMessage(tel,
				null, sms, null, null);
    }  
  };  
} 