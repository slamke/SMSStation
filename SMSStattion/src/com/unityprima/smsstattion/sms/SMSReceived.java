package com.unityprima.smsstattion.sms;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 短信记录表，对已经转移的短信进行记录，防止重复转移
 * 
 * @author sunke03
 *
 */
@Table(name = "sms_received")
public class SMSReceived  extends Model {
	
	@Column(name = "smsid")
	public long smsid;
	
	@Column(name = "md5")
	public String md5;
	
	@Column(name="add_time")
	public String addTime;

}
