package com.unityprima.smsstattion.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
/**
 * 短信发送表,表名:tb_Sent
 * @author sunke
 *
 */
public class SMSSent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4109981225521405063L;

	public static final String TABLE_NAME = "tb_Sent";
	
	/**
	 * Id	Id	Bigint	主键,标识
	 */
	private long id;
	
	/**
	 * WsendId	待发送表记录Id	Bigint	
	 */
	private long wsendId;
	
	/*
	 * SendSN	发送号码	Nvarchar(20)	
	 */
	private String sendSn;
	/**
	 * Mbno	接收号码	Nvarchar(20)	
	 */
	private String mbno;
	
	/**
	 * SMS	短信内容	Nvarchar(max)	
	 */
	private String sms;
	
	/**
	 * Wtime	短信写入时间	Datetime	
	 */
	private Timestamp wtime;
	
	/**
	 * SubmitTime	发送时间	Datetime	
	 */
	private Date submitTime;
	
	/**
	 * PRI	短信优先级	Tinyint	值越大级别越高,优先发送
	 */
	private short pri;
	
	/**
	 * PsendTime	预发送时间	Datetime	
	 */
	private Date psendTime;
	
	
	/**
	 * PlastSendTime	最后发送时间	Datetime	短信的有效时间,超过该时间不发送
	 */
	private Date plastSendTime;
	
	/**
	 * Status	短信状态	Nvarchar(30)	
	 */
	private String status;
	
	/**
	 * SubmitRespTime	发送状态报告返回时间	Datetime	
	 */
	private Date submitRespTime;
	
	/**
	 * ReceivedTime    	接收状态报告返回时间	Datetime	
	 */
	private Date reveivedTime;
	
	/**
	 * SubmitStatus	发送状态	Nvarchar(30)	
	 */
	private String submitStatus;
	
	/**
	 * ReceivedStatus	接收状态	Nvarchar(30)	
	 */
	private String reveivedStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getWsendId() {
		return wsendId;
	}

	public void setWsendId(long wsendId) {
		this.wsendId = wsendId;
	}

	public String getSendSn() {
		return sendSn;
	}

	public void setSendSn(String sendSn) {
		this.sendSn = sendSn;
	}

	public String getMbno() {
		return mbno;
	}

	public void setMbno(String mbno) {
		this.mbno = mbno;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	

	public Timestamp getWtime() {
		return wtime;
	}

	public void setWtime(Timestamp wtime) {
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

	public Date getSubmitRespTime() {
		return submitRespTime;
	}

	public void setSubmitRespTime(Date submitRespTime) {
		this.submitRespTime = submitRespTime;
	}

	public Date getReveivedTime() {
		return reveivedTime;
	}

	public void setReveivedTime(Date reveivedTime) {
		this.reveivedTime = reveivedTime;
	}

	public String getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}

	public String getReveivedStatus() {
		return reveivedStatus;
	}

	public void setReveivedStatus(String reveivedStatus) {
		this.reveivedStatus = reveivedStatus;
	}

	public SMSSent() {
		super();
	}
	
	public SMSSent(SMSWSend send) {
		super();
		if(send != null){
			this.wsendId  = send.getId();
			this.sendSn = send.getSendsn();
			this.mbno = send.getMbno();
			this.sms = send.getSms();
			this.wtime = send.getWtime();
			this.submitTime = send.getSubmitTime();
			this.pri = send.getPri();
			this.psendTime = send.getPsendTime();
			this.plastSendTime  = send.getPlastSendTime();
			this.status = send.getStatus();
		}
	}
}
