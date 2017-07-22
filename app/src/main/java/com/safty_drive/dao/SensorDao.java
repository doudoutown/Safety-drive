package com.safty_drive.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safty_drive.bean.SensorBean;
import com.safty_drive.helper.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanwenbin on 2017/7/22.
 */

public class SensorDao {
    private DBHelper helper;
    private SQLiteDatabase db;

    public SensorDao(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void add(Integer x, Integer y, Integer z) {
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO sensors(xValue,yValue,zValue) VALUES( ?, ?, ?)", new Object[]{x, y, z});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public List<SensorBean> query() {
        List<SensorBean> sensors = new ArrayList<SensorBean>();
        Cursor c = queryTheCursor();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (c.moveToNext()) {
            SensorBean sensor = new SensorBean();
            try {
                sensor.setCtime(df.parse(c.getString(c.getColumnIndex("ctime"))));
                sensor.setxValue(c.getInt(c.getColumnIndex("xValue")));
                sensor.setyValue(c.getInt(c.getColumnIndex("yValue")));
                sensor.setzValue(c.getInt(c.getColumnIndex("zValue")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sensors.add(sensor);
        }
        c.close();
        return sensors;
    }

    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM sensors", null);
        return c;
    }

    public void closeDB() {
        db.close();
    }
}
