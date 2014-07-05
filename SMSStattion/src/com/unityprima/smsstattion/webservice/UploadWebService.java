package com.unityprima.smsstattion.webservice;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.unityprima.smsstattion.entity.SMSMO;
import com.unityprima.smsstattion.utils.ClassParse;
import com.unityprima.smsstattion.utils.MD5;
import com.unityprima.smsstattion.utils.Message;

/**
 * 对应于短信接收模块，使用httpclient访问服务器的webservice，将数据upload至服务器
 * upload的数据为ArrayList<SMSMO>对应的json格式字符串，使用gson进行解析后发送
 * 
 * @author sunke
 * 
 */
public class UploadWebService extends BasicWebService {
	/**
	 * 服务器其的url地址
	 */
	private final String URL = "service/receive/items";
	private Context _context;

	public UploadWebService(Context _context) {
		super();
		this._context = _context;
	}

	public String sendSMSMOList(List<SMSMO> list) {
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
		String content = new ClassParse().object2String(list);
		Map<String, String> map = new HashMap<String, String>();
		// key--访问服务器的秘钥
		map.put(Message.KEY, key);
		map.put(Message.CONTENT, content);
		// 可能的结果为//Message.SUCCESS（访问成功）--Message.ERROR（服务器错误）--Message.NETWORK_FAIL（网络错误）
		return sendPostRequest(_url, map);
	}
}
