package com.unityprima.smsstattion;

import com.unityprima.smsstattion.utils.Message;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener{

	private TextView text_save;
	private Button button_return;
	
	private EditText messageLoopClockSetting;
	private EditText powerThresholdSetting;
	private EditText noticerPhoneNumberSetting;
	private EditText severAddressSetting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        
        setupViews();
        initView();
        
        
	}
	
	//加载控件
	protected void setupViews(){ 
		text_save = (TextView)findViewById(R.id.save_setting_button);
		button_return = (Button)findViewById(R.id.btn_back);
		
		messageLoopClockSetting = (EditText)findViewById(R.id.messageLoopClockSetting);
		powerThresholdSetting = (EditText)findViewById(R.id.powerThresholdSetting);
		noticerPhoneNumberSetting = (EditText)findViewById(R.id.noticerPhoneNumberSetting);
		severAddressSetting = (EditText)findViewById(R.id.severAddressSetting);
		
		text_save.setOnClickListener(this);
		button_return.setOnClickListener(this);
	}
	
	//加载数据
	protected void initView(){
		SharedPreferences settingInfo = getSharedPreferences(Message.PREFERENCE_NAME, MODE_PRIVATE); 
		
        String messageLoopClock = settingInfo.getString(Message.MESSAGE_LOOP_CLOCK, "");  
        String powerThreshold = settingInfo.getString(Message.POWER_THRESHOLD, "");
        String noticerPhoneNumber = settingInfo.getString(Message.NOTICER_PHONE_NUMBER, "");
        String severAddress = settingInfo.getString(Message.SEVER_ADDRESS, "");

        messageLoopClockSetting.setText(messageLoopClock);
        powerThresholdSetting.setText(powerThreshold);
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
			
			editor.putString(Message.MESSAGE_LOOP_CLOCK, messageLoopClockSetting.getText().toString());
			editor.putString(Message.POWER_THRESHOLD, powerThresholdSetting.getText().toString());
			editor.putString(Message.NOTICER_PHONE_NUMBER, noticerPhoneNumberSetting.getText().toString());
			editor.putString(Message.SEVER_ADDRESS, severAddressSetting.getText().toString());
		
			break;
			
		default:
			break;
		}
		
		
	}

	private void goBack() {
		finish();
	}
}
