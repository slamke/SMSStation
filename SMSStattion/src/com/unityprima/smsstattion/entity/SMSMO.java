package com.unityprima.smsstattion.entity;

import java.sql.Timestamp;

/**
 * 短信接收表,表名tb_MO
 * 功能说明：手机收到短信后，将保存在tb_MO表
 * 在短信保存到该表,后应当删除手机中的记录,以免战用过多手机存储而影响手机运行
 * @author sunke
 *
 */
public class SMSMO {
	public static final String TABLE_NAME = "tb_MO";
	/**
	 * Id	Id	bigint	主键,标识
	 */
	private long id;
	/**
	 * SMS	短信内容	Nvarchar(max)	
	 */
	private String sms;
	/**
	 * Times	接收时间	Datetime	
	 */
	private Timestamp times;
	/**
	 * Mbno	回复手机号	Nvarchar(30)	  发短信的手机号
	 */
	private String mbno;
	/**
	 * SendSN	回复的账号	Nvarchar(30)	当前手机(搬运手机)的号码
	 */
	private String sendSN;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}

	public Timestamp getTimes() {
		return times;
	}
	public void setTimes(Timestamp times) {
		this.times = times;
	}
	public String getMbno() {
		return mbno;
	}
	public void setMbno(String mbno) {
		this.mbno = mbno;
	}
	public String getSendSN() {
		return sendSN;
	}
	public void setSendSN(String sendSN) {
		this.sendSN = sendSN;
	}
}
