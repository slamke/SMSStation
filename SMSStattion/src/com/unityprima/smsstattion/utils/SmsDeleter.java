package com.unityprima.smsstattion.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;

import java.util.List;

/**
 * Created by Bobby on 2014/7/11.
 */
public class SmsDeleter {

    private Context context;

    public SmsDeleter(Context _context) {
        this.context = _context;
    }

    private TelephonyManager tm;

    public void smsDelete(List<Long> ids){

        for(Long temp_id : ids){
            context.getContentResolver().delete(Uri.parse("content://sms"), "_id=" + temp_id, null);
        }
    }
}
