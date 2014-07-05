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
	
	public final static String KEY = "key";
	public final static String CONTENT = "content";
	/****************************以下是常量的定义*************************************/
	public static final String ERROR = "error";
	public static final String SUCCESS = "success";
	public final static String NETWORK_FAIL = "network_error";
}
