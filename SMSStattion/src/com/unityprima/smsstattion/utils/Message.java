package com.unityprima.smsstattion.utils;

public class Message {

	private Message(){
		
	}
	/******************以下是sharedPreference的key定义***********************/
	public final static String PREFERENCE_NAME = "setting";
	public final static String MESSAGE_LOOP_CLOCK = "messageLoopClock";
	public final static String POWER_THRESHOLD = "powerThreshold";
	public final static String NOTICER_PHONE_NUMBER = "noticerPhoneNumber";
	public final static String SEVER_ADDRESS = "severAddress";
	
	public final static String SWITCH_RECEIVE = "switchReceive";
	public final static String SWITCH_SEND = "switchSend";
	public final static String SWITCH_TRANSFER = "switchTransfer";
	
	public final static String POWER_LOW_NOTICE = "POWER_LOW_NOTICE";
	
	public final static String KEY = "key";
	public final static String CONTENT = "content";
	/****************************以下是常量的定义*************************************/
	public static final String ERROR = "error";
	public static final String SUCCESS = "success";
	public final static String NETWORK_FAIL = "network_error";
	
	/****************************以下是短信提示内容*****************/
	public final static String POWER_LOW_AND_NOT_CHARGE = "您好，短信中转站的手机电池电量已经低于5%，请及时充电并且检查手机";
	
	public final static String SWITCH_OFF_RECEIVE = "短信接收模块已成功关闭";
	
	public final static String SWITCH_ON_RECEIVE = "短信接收模块已成功开启";
	
	public final static String SWITCH_OFF_SEND = "短信发送模块已成功关闭";
	
	public final static String SWITCH_ON_SEND = "短信发送模块已成功开启";
	
	public final static String SWITCH_OFF_TRANSFER = "短信搬运模块已成功关闭";
	
	public final static String SWITCH_ON_TRANSFER = "短信搬运模块已成功开启";
	
	public final static String TIP_NOT_SET_CYCLE = "短信轮询周期设置有误";
	
	public final static String TIP_NOT_SET_THRESHOLD = "电量报警阈值设置有误(1-99)";
	
	public final static String TIP_NOT_SET_TEL = "负责人电话设置有误";
	
	public final static String TIP_NOT_SET_SERVER = "服务器地址设置有误";
	
}
