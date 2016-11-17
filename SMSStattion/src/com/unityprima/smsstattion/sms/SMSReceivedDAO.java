package com.unityprima.smsstattion.sms;

import java.util.List;

import com.activeandroid.query.Select;

public class SMSReceivedDAO {

	public boolean checkExists(SMSReceived smsReceived) {
	    List<SMSReceived> list =  new Select()
	        .from(SMSReceived.class)
	        .where("smsid = ? and md5 = ? ", smsReceived.smsid, smsReceived.md5)
	        .execute();
	    return (list == null || list.isEmpty()) ? false : true;
	}
	
	public long size() {
		List<SMSReceived> list =  new Select()
        .from(SMSReceived.class)
        .execute();
		return (list == null || list.isEmpty()) ? 0 : list.size();
	}
}
