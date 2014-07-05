package com.unityprima.smsstattion.webservice;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.unityprima.smsstattion.utils.MD5;
import com.unityprima.smsstattion.utils.Message;
/**
 * 对应于短信转移模块，使用httpclient访问服务器的webservice，提示服务器进行短信转移
 * @author sunke
 *
 */
public class TransferWebService extends BasicWebService{
	/**
	 * 服务器其的url地址
	 */
	private final String URL = "service/transfer/notify";
	private Context _context;
	public TransferWebService(Context _context) {
		super();
		this._context = _context;
	}

	public String notifyServerToTransfer() {
		SharedPreferences settingInfo = _context.getSharedPreferences(Message.PREFERENCE_NAME, Context.MODE_PRIVATE); 
        String severAddress = settingInfo.getString(Message.SEVER_ADDRESS, "");
        String _url = null;
		if (severAddress != null && severAddress.endsWith("/")) {
			_url = severAddress+URL;
		}else {
			_url = severAddress+"/"+URL;
		}
		String code = Calendar.YEAR+"-"+Calendar.MONTH+"-"+Calendar.DAY_OF_MONTH+"unityprima";
		String key = new MD5().getMD5Str(code);
		Map<String, String> map = new HashMap<String, String>();
		//key--访问服务器的秘钥
		map.put(Message.KEY, key);
		// 可能的结果为//Message.SUCCESS（访问成功）--Message.ERROR（服务器错误）--Message.NETWORK_FAIL（网络错误）
		return sendPostRequest(_url, map);  
	}
}
