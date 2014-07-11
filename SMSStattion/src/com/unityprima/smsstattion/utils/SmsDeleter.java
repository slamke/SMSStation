package com.unityprima.smsstattion.utils;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Bobby on 2014/7/11.
 */
public class SmsDeleter {

    private Context context;

    public SmsDeleter(Context _context) {
        this.context = _context;
    }

    public void smsDelete(List<Long> ids){
    	Log.e("list", ids.toString());
        for(Long temp_id : ids){
            context.getContentResolver().delete(Uri.parse("content://sms"), "_id=" + temp_id, null);
        }
    }
}
