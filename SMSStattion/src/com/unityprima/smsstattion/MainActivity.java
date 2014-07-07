package com.unityprima.smsstattion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;
import android.widget.TableRow;

import com.unityprima.smsstattion.entity.SMSMO;
import com.unityprima.smsstattion.service.MonitorPowerService;
import com.unityprima.smsstattion.utils.Message;
import com.unityprima.smsstattion.utils.SmsReader;

import java.util.List;

public class MainActivity extends Activity implements OnClickListener{

	private Switch switchReceive;//接收模块开关
	private Switch switchSend;//发送模块开关
	private Switch switchTransfer;//搬运模块开关
	private TableRow trSetting;//设置模块入口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        initViews();
        startService();

        SmsReader smsReader = new SmsReader(this);
        List<SMSMO> smsInfos = smsReader.getSmsInfo();
        int acc = 0;
        for(SMSMO temp:smsInfos){
            acc++;
            Log.i(acc + ": 內容", temp.getSms());
            Log.i(acc + ": 接收手机号", temp.getMbno());
            Log.i(acc + ": 发送手机号", temp.getSendSN());

        }

    }

    private void startService(){
    	Intent moniterPowerService = new Intent(this,MonitorPowerService.class);
        startService(moniterPowerService);
    }
    
    protected void setupViews(){  
    	switchReceive = (Switch)findViewById(R.id.switch_receive);
    	switchSend = (Switch)findViewById(R.id.switch_send);
    	switchTransfer = (Switch)findViewById(R.id.switch_transfer);
    	trSetting = (TableRow)findViewById(R.id.setting);
    	
    	switchReceive.setOnClickListener(this);
    	switchSend.setOnClickListener(this);
    	switchTransfer.setOnClickListener(this);
    	trSetting.setOnClickListener(this);
    }
    
    private void initViews(){
    	SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME,
				MODE_PRIVATE); // 获得Preferences
    	//获取三个模块的开关记录
    	boolean flagReceive = sp.getBoolean(Message.SWITCH_RECEIVE, true);
    	boolean flagSend = sp.getBoolean(Message.SWITCH_SEND, true);
    	boolean flagTransfer = sp.getBoolean(Message.SWITCH_TRANSFER, true);
    	switchReceive.setChecked(flagReceive);
    	switchSend.setChecked(flagSend);
    	switchTransfer.setChecked(flagTransfer);
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.setting:
				Intent intent = new Intent(this,SettingActivity.class);
				//设置后，需要接收最新的设置参数实现相关的service重启
				startActivityForResult(intent, 100);
			case R.id.switch_receive:

			case R.id.switch_send:
				
			case R.id.switch_transfer:
		}
	}
	
	 /** 
     * 复写onActivityResult，这个方法 
     */  
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
        //可以根据多个请求代码来作相应的操作  
        if(100 == resultCode)  
        {  
        	//to-do
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    }
}
