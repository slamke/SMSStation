package com.unityprima.smsstattion;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;
import android.widget.TableRow;

public class MainActivity extends Activity implements OnClickListener{

	private Switch switch_1;//接收模块开关
	private Switch switch_2;//发送模块开关
	private Switch switch_3;//搬运模块开关
	private TableRow tableRow_4;//设置模块入口
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setupViews();
    }

    protected void setupViews(){  
    	switch_1 = (Switch)findViewById(R.id.switch_1);
    	switch_2 = (Switch)findViewById(R.id.switch_2);
    	switch_3 = (Switch)findViewById(R.id.switch_3);
    	tableRow_4 = (TableRow)findViewById(R.id.setting);
    	
    	switch_1.setOnClickListener(this);
    	switch_2.setOnClickListener(this);
    	switch_3.setOnClickListener(this);
    	tableRow_4.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.setting:
				Intent intent = new Intent(this,SettingActivity.class);
				startActivity(intent);
			case R.id.switch_1:

			case R.id.switch_2:
				
			case R.id.switch_3:
		}
	}
    
}
