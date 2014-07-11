package com.unityprima.smsstattion.webservice;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.unityprima.smsstattion.entity.SMSSent;
import com.unityprima.smsstattion.utils.ClassParse;
import com.unityprima.smsstattion.utils.MD5;
import com.unityprima.smsstattion.utils.Message;

/**
 * 对应于短信发送模块，使用httpclient访问服务器的webservice，将成功发送的短信的状态信息反馈至服务器
 * 发送的数据为List<sentList>对应的json格式字符串，使用gson进行解析
 * @author sunke
 * 
 */
public class FeedBackWebService extends BasicWebService{
	/**
	 * 服务器其的url地址
	 */
	private final String URL = "service/send/feedback";
	
	private Context _context;

	public FeedBackWebService(Context _context) {
		super();
		this._context = _context;
	}

	public String feedBackWithSuccessSMS(List<SMSSent> sentList) {
		SharedPreferences settingInfo = _context.getSharedPreferences(
				Message.PREFERENCE_NAME, Context.MODE_PRIVATE);
		String severAddress = settingInfo.getString(Message.SEVER_ADDRESS, "");
		String _url = null;
		if (severAddress != null && severAddress.endsWith("/")) {
			_url = severAddress + URL;
		} else {
			_url = severAddress + "/" + URL;
		}
		String code = Calendar.YEAR + "-" + Calendar.MONTH + "-"
				+ Calendar.DAY_OF_MONTH + "unityprima";
		String key = new MD5().getMD5Str(code);
		Map<String, String> map = new HashMap<String, String>();
		//key--访问服务器的秘钥
		map.put(Message.KEY, key);
		String content = new ClassParse().object2String(sentList);
		map.put(Message.CONTENT, content);
		// result可能为：Message.SUCCESS--Message.ERROR--Message.NETWORK_FAIL
		String result = sendPostRequest(_url, map);
		// 表示网络错误或者服务器错误
		return result;
	}
}
