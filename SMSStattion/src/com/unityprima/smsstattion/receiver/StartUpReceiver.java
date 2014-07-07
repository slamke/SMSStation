package com.unityprima.smsstattion.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.unityprima.smsstattion.MainActivity;
import com.unityprima.smsstattion.service.MonitorPowerService;


public class StartUpReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		if (arg1.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent i = new Intent(arg0,MainActivity.class);  
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	        //将intent以startActivity传送给操作系统  
	        arg0.startActivity(i);  
	        ////启动监控service
	        Intent moniterPowerService = new Intent(arg0,MonitorPowerService.class);
	        moniterPowerService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        arg0.startService(moniterPowerService);
	        ///启动后台周期执行的service
	        Intent operationService = new Intent(arg0,SMSStationReceiver.class);
	        operationService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        arg0.startService(operationService);
		}
		
	}

}
