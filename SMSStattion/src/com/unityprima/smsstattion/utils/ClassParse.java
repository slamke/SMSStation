package com.unityprima.smsstattion.utils;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.unityprima.smsstattion.entity.SMSWSend;
/**
 * 类型转换器--使用G送将string和对象进行互转
 * @author sunke
 *
 */
public class ClassParse {

	private Gson gson;

	public ClassParse() {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}

	public String object2String(Object object){
		try {
			if (object != null) {
				String result = gson.toJson(object);
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public String smsList2String(List<SMSWSend> smsList) {
		try {
			if (smsList != null) {
				String result = gson.toJson(smsList);
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public Object string2Class(String content,Type type) {
		try {
			if (content != null) {
				Object record = gson.fromJson(content, type);
				return record;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public List<SMSWSend> string2SMSWSendList(String content) {
		try {
			Type type = new TypeToken<List<SMSWSend>>() {
			}.getType();
			if (content != null) {
				List<SMSWSend> record = gson.fromJson(content, type);
				return record;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
}
