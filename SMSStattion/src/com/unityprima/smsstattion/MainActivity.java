package com.unityprima.smsstattion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.unityprima.smsstattion.service.MonitorPowerService;
import com.unityprima.smsstattion.service.SMSStationService;
import com.unityprima.smsstattion.utils.Message;

public class MainActivity extends Activity implements OnClickListener{

	private Switch switchReceive;//接收模块开关
	private Switch switchSend;//发送模块开关
	private Switch switchTransfer;//搬运模块开关
	private TableRow trSetting;//设置模块入口
	private TextView txtTip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        initViews();
        startService();
    }
    /**************************************************/
    protected void onPause() { 
        super.onPause();
    }
  
    protected void onResume() {
        super.onResume();  
    }  
    
    private void startService(){
    	Intent moniterPowerService = new Intent(this,MonitorPowerService.class);
        startService(moniterPowerService);
        Intent operationService = new Intent(this,SMSStationService.class);
        startService(operationService);
    }
    
    protected void setupViews(){  
    	switchReceive = (Switch)findViewById(R.id.switch_receive);
    	switchSend = (Switch)findViewById(R.id.switch_send);
    	switchTransfer = (Switch)findViewById(R.id.switch_transfer);
    	trSetting = (TableRow)findViewById(R.id.setting);
    	txtTip = (TextView)findViewById(R.id.txt_tip);
    	switchReceive.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME,
						MODE_PRIVATE); // 获得Preferences
				SharedPreferences.Editor editor = sp.edit(); // 获得Editor
				editor.putBoolean(Message.SWITCH_RECEIVE, arg1);
				editor.commit();
				if (arg1) {
					Toast.makeText(MainActivity.this, Message.SWITCH_ON_RECEIVE, Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(MainActivity.this, Message.SWITCH_OFF_RECEIVE, Toast.LENGTH_SHORT).show();
				}
			}
		});
    	switchSend.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME,
						MODE_PRIVATE); // 获得Preferences
				SharedPreferences.Editor editor = sp.edit(); // 获得Editor
				editor.putBoolean(Message.SWITCH_SEND, arg1);
				editor.commit();
				if (arg1) {
					Toast.makeText(MainActivity.this, Message.SWITCH_ON_SEND, Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(MainActivity.this, Message.SWITCH_OFF_SEND, Toast.LENGTH_SHORT).show();
				}
			}
		});
    	switchTransfer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME,
						MODE_PRIVATE); // 获得Preferences
				SharedPreferences.Editor editor = sp.edit(); // 获得Editor
				editor.putBoolean(Message.SWITCH_TRANSFER, arg1);
				editor.commit();
				if (arg1) {
					Toast.makeText(MainActivity.this, Message.SWITCH_ON_TRANSFER, Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(MainActivity.this, Message.SWITCH_OFF_TRANSFER, Toast.LENGTH_SHORT).show();
				}
			}
		});
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
    	setTips();
    }

    
    public void setTips(){
    	/***********提示相关的设置信息**************/
    	SharedPreferences settingInfo = getSharedPreferences(Message.PREFERENCE_NAME, MODE_PRIVATE); 
        String noticerPhoneNumber = settingInfo.getString(Message.NOTICER_PHONE_NUMBER, "");
        String severAddress = settingInfo.getString(Message.SEVER_ADDRESS, "");
        
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("提示：");
        if (noticerPhoneNumber == null ||noticerPhoneNumber.equals("")) {
			sBuffer.append("尚未设置负责人联系电话；");
		}
        if (severAddress == null || severAddress.equals("")) {
			sBuffer.append("尚未设置服务器地址信息；");
		}
        if (sBuffer.length() > 5) {
        	txtTip.setVisibility(View.VISIBLE);
        	txtTip.setText(sBuffer.toString());
		}else{
        	txtTip.setVisibility(View.INVISIBLE);
		}
    }
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.setting:
				Intent intent = new Intent(this,SettingActivity.class);
				//设置后，需要接收最新的设置参数实现相关的service重启
				startActivityForResult(intent, 100);
				break;
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
        	setTips();
        	//重新启动主service
        	Intent smsStationService = new Intent(this,SMSStationService.class);
            startService(smsStationService);
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    }
}
