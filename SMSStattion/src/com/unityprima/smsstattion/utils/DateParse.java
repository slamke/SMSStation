package com.unityprima.smsstattion.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateParse {
	public Date string2Date(String dateString)
	{
		if(dateString == null || dateString.equals(""))
		{
			return null;
		}
		SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		try {
	   			return format.parse(dateString);   		
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	public String date2String(Date date)
	{
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
				return format.format(date);
		}
		else {
			return null;
		}
	}
}
