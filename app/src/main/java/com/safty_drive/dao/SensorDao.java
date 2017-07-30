package com.safty_drive.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safty_drive.bean.SensorBean;
import com.safty_drive.helper.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

    public void add(Integer x, Integer y, Integer z, Double latitude, Double longitude, Float speed) {
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO sensor_value(xValue,yValue,zValue,ctime, latitude ,longitude , speed) VALUES( ?, ?, ?,?,?,?,?)", new Object[]{x, y, z, System.currentTimeMillis(), latitude, longitude, speed});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public List<SensorBean> query() {
        List<SensorBean> sensors = new ArrayList<SensorBean>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            SensorBean sensor = new SensorBean();
            sensor.setCtime(new Date(c.getLong(c.getColumnIndex("ctime"))));
            sensor.setxValue(c.getInt(c.getColumnIndex("xValue")));
            sensor.setyValue(c.getInt(c.getColumnIndex("yValue")));
            sensor.setzValue(c.getInt(c.getColumnIndex("zValue")));
            sensor.setLatitude(c.getDouble(c.getColumnIndex("latitude")));
            sensor.setLongitude(c.getDouble(c.getColumnIndex("longitude")));
            sensor.setSpeed(c.getFloat(c.getColumnIndex("speed")));
            sensors.add(sensor);
        }
        c.close();
        return sensors;
    }

    public void deleteAll() {
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("delete from sensor_value");
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM sensor_value", null);
        return c;
    }

    public void closeDB() {
        db.close();
    }
}
