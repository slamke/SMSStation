package com.unityprima.smsstattion.sms;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "sms")
public class SMSModel extends Model{
	public static final String YES_STATUS ="YES";
	public static final String NOT_STATUS ="NOT";
	
	/**
	 * Id	Id	bigint	主键,标识
	 */
	@Column(name="smsid")
	public long smsid;
	/**
	 * Mbno	接收号码	Nvarchar(20)	非空
	 */
	@Column(name="mbno")
	public String mbno;
	/**
	 * SendSN	发送号码	Nvarchar(20)	非空
	 */
	@Column(name="sendsn")
	public String sendsn;
	/**
	 * SMS	短信内容	Nvarchar(max)	非空
	 */
	@Column(name="sms")
	public String sms; 
	
	@Column(name="status")
	public String status;  //0-->已反馈 1->未反馈
	
	/**
	 * SubmitTime	发送时间	Datetime	
	 */
	@Column(name="submitTime")
	public String submitTime;
	
	/**
	 * SubmitRespTime	发送状态报告返回时间	Datetime	
	 */
	@Column(name="submitRespTime")
	public String submitRespTime;
	
	/**
	 * ReceivedTime    	接收状态报告返回时间	Datetime	
	 */
	@Column(name="reveivedTime")
	public String reveivedTime;
	
	/**
	 * SubmitStatus	发送状态	Nvarchar(30)	
	 */
	@Column(name="submitStatus")
	public String submitStatus;
	
	/**
	 * ReceivedStatus	接收状态	Nvarchar(30)	
	 */
	@Column(name="reveivedStatus")
	public String reveivedStatus;
}
