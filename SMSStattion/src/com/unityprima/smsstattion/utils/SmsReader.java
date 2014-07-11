package com.unityprima.smsstattion.utils;

import com.unityprima.smsstattion.entity.SMSMO;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;

/**
 * Created by Bobby on 2014/7/7.
 */
public class SmsReader {

    private Context context;// 这里有个activity对象，不知道为啥以前好像不要，现在就要了。自己试试吧。

    public SmsReader(Context _context) {
        this.context = _context;
    }

    private TelephonyManager tm;

    public List<SMSMO> getSmsInfo(){
        List<SMSMO> infos = new ArrayList<SMSMO>();

        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = resolver.query(uri, new String[] {"_id", "address",
                "person", "date", "type", "body"}, null, null, " date desc ");

        SMSMO smsInfo;

        tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String currentSim = tm.getLine1Number();

        while(cursor.moveToNext()){
            // ALL = 0; INBOX = 1; SENT = 2; DRAFT = 3; OUTBOX = 4; FAILED = 5;
            // QUEUED = 6;
            //条件：1、接收的短信；
            if(cursor.getInt(4) == 1){
                smsInfo = new SMSMO();
                Long id = cursor.getLong(0);
                String mbno = cursor.getString(1);

                String times_ms= cursor.getString(3);
                Date times = new Date(Long.parseLong(times_ms));
                Timestamp ts = new Timestamp(times.getTime());
                String sms = cursor.getString(5);

                smsInfo.setId(id);
                smsInfo.setSms(sms);
                smsInfo.setTimes(ts);
                smsInfo.setMbno(mbno);
                smsInfo.setSendSN(currentSim);

                infos.add(smsInfo);
            }
        }

        cursor.close();
        return infos;
    }




}
