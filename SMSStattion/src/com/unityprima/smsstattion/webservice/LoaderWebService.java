package com.unityprima.smsstattion.webservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.unityprima.smsstattion.entity.SMSWSend;
import com.unityprima.smsstattion.utils.ClassParse;
import com.unityprima.smsstattion.utils.MD5;
import com.unityprima.smsstattion.utils.Message;
/**
 * 对应于短信发送模块，使用httpclient访问服务器的webservice，将数据load至手机
 * load的数据为List<SMSWSend>对应的json格式字符串，使用gson进行解析
 * @author sunke
 *
 */
public class LoaderWebService  extends BasicWebService{
	private final String url = "service/send/list";
	private Context _context;
	public LoaderWebService(Context _context) {
		super();
		this._context = _context;
	}

	public List<SMSWSend> getSMSSendList() {
		SharedPreferences settingInfo = _context.getSharedPreferences(Message.PREFERENCE_NAME, Context.MODE_PRIVATE); 
        String severAddress = settingInfo.getString(Message.SEVER_ADDRESS, "");
        String _url = null;
		if (severAddress.endsWith("/")) {
			_url = severAddress+url;
		}else {
			_url = severAddress+"/"+url;
		}
		String code = Calendar.YEAR+"-"+Calendar.MONTH+"-"+Calendar.DAY_OF_MONTH+"unityprima";
		String key = new MD5().getMD5Str(code);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Message.KEY, key);
		//result可能为：Message.SUCCESS--Message.ERROR--Message.NETWORK_FAIL--content
		String result = sendPostRequest(_url, map); 
		List<SMSWSend> list = new ArrayList<SMSWSend>();
		//表示网络错误或者服务器错误
		if (result.equals(Message.NETWORK_FAIL) ||result.equals(Message.ERROR)) {
			return null; //出现错误
 		}else if(result.equals(Message.SUCCESS) ){  //表示当前没有待发送短信
			return list; //size ==0
		}else {
			//使用gson将string转换为对象
			return new ClassParse().string2SMSWSendList(result);
		}
	}
}
