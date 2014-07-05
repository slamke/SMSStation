package com.unityprima.smsstattion;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.unityprima.smsstattion.utils.DefaultSettingInfo;
import com.unityprima.smsstattion.utils.Message;

public class SettingActivity extends Activity implements OnClickListener{

	private TextView text_save;
	private TextView text_return;
	
	private EditText messageLoopClockSetting;
	private EditText powerThresholdSetting;
	private EditText noticerPhoneNumberSetting;
	private EditText severAddressSetting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setupViews();
        initViews();//test for git plugin

        //test for git plugin//test for git plugin//test for git plugin


	}
	
	//加载控件
	protected void setupViews(){ 
		text_save = (TextView)findViewById(R.id.save_setting_button);
		text_return = (TextView)findViewById(R.id.btn_back);
		
		messageLoopClockSetting = (EditText)findViewById(R.id.messageLoopClockSetting);
		powerThresholdSetting = (EditText)findViewById(R.id.powerThresholdSetting);
		noticerPhoneNumberSetting = (EditText)findViewById(R.id.noticerPhoneNumberSetting);
		severAddressSetting = (EditText)findViewById(R.id.severAddressSetting);
		
		text_save.setOnClickListener(this);
		text_return.setOnClickListener(this);
	}
	
	//加载数据
	protected void initViews(){
		SharedPreferences settingInfo = getSharedPreferences(Message.PREFERENCE_NAME, MODE_PRIVATE); 
		
        int messageLoopClock = settingInfo.getInt(Message.MESSAGE_LOOP_CLOCK, DefaultSettingInfo.DEFAULT_LOOP_CYCLE);  
        int powerThreshold = settingInfo.getInt(Message.POWER_THRESHOLD, DefaultSettingInfo.DEFAULT_WARNING_POWER_THRESHOLD);
        String noticerPhoneNumber = settingInfo.getString(Message.NOTICER_PHONE_NUMBER, "");
        String severAddress = settingInfo.getString(Message.SEVER_ADDRESS, "");

        messageLoopClockSetting.setText(""+messageLoopClock);
        powerThresholdSetting.setText(""+powerThreshold);
        noticerPhoneNumberSetting.setText(noticerPhoneNumber);
        severAddressSetting.setText(severAddress);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:
			goBack();
			break;
		case R.id.save_setting_button:
			SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME, MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit(); // 获得Editor
			
			String loopCycle = messageLoopClockSetting.getText().toString();
			if(loopCycle != null && !loopCycle.equals("")){
				editor.putInt(Message.MESSAGE_LOOP_CLOCK, Integer.parseInt(loopCycle));
			}else {
				editor.putInt(Message.MESSAGE_LOOP_CLOCK, DefaultSettingInfo.DEFAULT_LOOP_CYCLE);
			}
			String powerThresHold = powerThresholdSetting.getText().toString();
			if(powerThresHold != null && !powerThresHold.equals("")){
				editor.putInt(Message.POWER_THRESHOLD, Integer.parseInt(powerThresHold));
			}else {
				editor.putInt(Message.POWER_THRESHOLD, DefaultSettingInfo.DEFAULT_WARNING_POWER_THRESHOLD);
			}
			editor.putString(Message.NOTICER_PHONE_NUMBER, noticerPhoneNumberSetting.getText().toString());
			editor.putString(Message.SEVER_ADDRESS, severAddressSetting.getText().toString());
			editor.commit();
			goBack();
			break;
		default:
			break;
		}
	}

	private void goBack() {
		setResult(100);
		finish();
	}
}
