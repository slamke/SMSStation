package com.unityprima.smsstattion.utils;

import com.unityprima.smsstattion.entity.SMSMO;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bobby on 2014/7/7.
 */
public class SmsReader {

    private Context context;// 这里有个activity对象，不知道为啥以前好像不要，现在就要了。自己试试吧。

    public SmsReader(Context _context) {
        this.context = _context;
    }


    public List<SMSMO> getSmsInfo(){
        List<SMSMO> infos = new ArrayList<SMSMO>();

        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = resolver.query(uri, new String[] {"_id", "sms", "times", "mbno", "sendSN"}, null, null, " date desc ");

        SMSMO smsInfo;

        while(cursor.moveToNext()){
            smsInfo = new SMSMO();
            Long id = cursor.getLong(0);
            String sms = cursor.getString(1);
            String times_test = cursor.getString(2);
            System.out.println(times_test);
            //Date times = cursor.getString(2);
            String mbno = cursor.getString(3);
            String sendSN = cursor.getString(4);

            smsInfo.setId(id);
            smsInfo.setSms(sms);
//            smsInfo.setTimes(times);
            smsInfo.setMbno(mbno);
            smsInfo.setSendSN(sendSN);

            infos.add(smsInfo);
        }

        cursor.close();
        return infos;
    }




}
