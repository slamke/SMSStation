package com.unityprima.smsstattion.sms;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "smslog")
public class SMSLog extends Model {
	@Column(name = "smsid")
	public String time;
	
	@Column(name = "content")
	public String content;
	
	@Column(name = "logType")
	public String logType; 
	
	public static final String TYPE_LOAD = "load";
	public static final String TYPE_SEND = "send";
	public static final String TYPE_TRANSFER = "transfer";
}
