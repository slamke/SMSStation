package com.unityprima.smsstattion.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信待发表,表名:tb_WSend
 * 所有待发送的短信记录都保存在短信待发表中，短信发送进程将轮询这张表
 * 短信的状态改为'Submit'时应当把短信搬运到tb_Sent中,并在该表中删除
 * 短信发送后,应当删除在手机中的记录, 以免战用过多手机存储而影响手机运行
 * 
 * 短信发送后，若有状态报告返回（接收成功或失败），则系统将该短信记录从tb_Wsend表移至tb_Sent表中；
 * 若超过发送时间24小时后状态报告仍未返回，则搬运模块程序也会将该短信记录从tb_Wsend表移至tb_Sent表，
 * 因此所有的短信记录最终都将保存至tb_Sent表
 * 
 * @author sunke
 *
 */
public class SMSWSend implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1123L;

	public static final String TABLE_NAME = "tb_WSend";
	
	/**
	 * Id	Id	bigint	主键,标识
	 */
	private long id;
	/**
	 * Mbno	接收号码	Nvarchar(20)	非空
	 */
	private String mbno;
	/**
	 * SendSN	发送号码	Nvarchar(20)	非空
	 */
	private String sendsn;
	/**
	 * SMS	短信内容	Nvarchar(max)	非空
	 */
	private String sms; 
	/**
	 * Wtime	短信写入时间	Datetime	非空
	 */
	private Date wtime;
	/**
	 * SubmitTime	发送时间	Datetime	
	 */
	private Date submitTime;
	/**
	 * PRI	发送优先级	Tinyint	值越大级别越高,优先发送
	 */
	private short pri;
	/**
	 * PsendTime	预发送时间	Datetime	
	 */
	private Date psendTime;
	/**
	 * PlastSendTime	最后发送时间	Datetime	短信的有效时间,超过该时间不发送
	 * 若短信有“预发送时间”和“最后发送时间”，且当前时间大于“最后发送时间”，无论短信的状态是否为0，短信搬运模块应当把该短信搬运到该表中
	 */
	private Date plastSendTime;
	/**
	 * Status	状态	Nvarchar(30)	初始值应为'0'，表明待发送，发送完毕后该字段会更改为'Submit'
	 * //若，短信的状态为0，则短信搬运模块不能把该短信搬运到该表中；
	 */
	private String status;
	/**
	 * Remark	备注	Nvarchar(max)	
	 */
	private String remark;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMbno() {
		return mbno;
	}
	public void setMbno(String mbno) {
		this.mbno = mbno;
	}
	public String getSendsn() {
		return sendsn;
	}
	public void setSendsn(String sendsn) {
		this.sendsn = sendsn;
	}
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}
	public Date getWtime() {
		return wtime;
	}
	public void setWtime(Date wtime) {
		this.wtime = wtime;
	}
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public short getPri() {
		return pri;
	}
	public void setPri(short pri) {
		this.pri = pri;
	}
	public Date getPsendTime() {
		return psendTime;
	}
	public void setPsendTime(Date psendTime) {
		this.psendTime = psendTime;
	}
	public Date getPlastSendTime() {
		return plastSendTime;
	}
	public void setPlastSendTime(Date plastSendTime) {
		this.plastSendTime = plastSendTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
