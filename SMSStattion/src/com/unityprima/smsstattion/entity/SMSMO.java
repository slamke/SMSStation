package com.unityprima.smsstattion.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.unityprima.smsstattion.utils.MD5;

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
	
	public SMSMO(String sms, Timestamp times, String mbno, String sendSN) {
		super();
		this.sms = sms;
		this.times = times;
		this.mbno = mbno;
		this.sendSN = sendSN;
	}
	
	public SMSMO() {
		super();
	}
	public static List<SMSMO> split(SMSMO smsmo){
		if (smsmo == null) {
			return null;
		}
		List<SMSMO> result = new ArrayList<SMSMO>();
		if (smsmo.getSms() == null || smsmo.getSms().equals("")) {
			result.add(smsmo);
			return result ;
		}
		int length = smsmo.getSms().getBytes().length;
		if (length < 350) {
			result.add(smsmo);
			return result ;
		} else {
			String type = smsmo.getSms().substring(0, 4);
			int start = 0;
			int end = 170;
			length = smsmo.getSms().length();
			while( start < length){
				StringBuffer buffer = new StringBuffer();
				if (start != 0) {
					buffer.append(type);
				}
				buffer.append(smsmo.getSms().substring(start, end));
				
				SMSMO newSmsmo = new SMSMO(buffer.toString(), smsmo.getTimes(), 
						smsmo.getMbno(), smsmo.getSendSN());
				result.add(newSmsmo);
				
				start = end;
				end = (end + 170 ) > length ? length : end + 170;
			}
			return result;
		}
	}
	
	public static List<SMSMO>  splitList(List<SMSMO> list){
		if (list == null) {
			return null;
		}
		List<SMSMO> result = new ArrayList<SMSMO>();
		for (SMSMO smsmo : list) {
			result.addAll(split(smsmo));
		}
		return result;
	}
	
	public String getMD5(){
		String tmp = String.format("%s_%s_%s", id,times.getTime(),sendSN);
		return new MD5().getMD5Str(tmp);
		
	}
}
