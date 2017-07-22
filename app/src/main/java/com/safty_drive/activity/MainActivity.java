package com.safty_drive.activity;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.safty_drive.HttpResponeCallBack;
import com.safty_drive.R;
import com.safty_drive.RequestApiData;
import com.safty_drive.bean.SensorBean;
import com.safty_drive.dao.SensorDao;
import com.safty_drive.util.ChartUtils;
import com.safty_drive.util.TimeUtils;
import com.safty_drive.vo.UserBaseInfo;
import com.safty_drive.vo.UserDriveVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

public class MainActivity extends Activity implements SensorEventListener, HttpResponeCallBack {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }

    private static final String TAG = MainActivity.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView textviewX;
    private TextView textviewY;
    private TextView textviewZ;
    private TextView textViewMessage;

    private TextView textViewLevel;
    private TextView textViewMsg;


    private int mX, mY, mZ;
    private long lasttimestamp = 0;
    Calendar mCalendar;
    private SensorDao sensorDao;
    private LineChart chart;
    private int index = 0;
    private boolean start = false;

    private Button startBtn;
    private Button stopBtn;
//    private Button clearBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textviewX = (TextView) findViewById(R.id.textViewX);
        textviewY = (TextView) findViewById(R.id.textViewY);
        textviewZ = (TextView) findViewById(R.id.textViewZ);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);

        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                start = true;
            }
        });


        stopBtn = findViewById(R.id.stopBtn);

        stopBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                start = false;
            }
        });
//        clearBtn = findViewById(R.id.clearBtn);
//        clearBtn.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                chart.clear();
//                chart.setData(new LineData());
//                chart.invalidate();
//            }
//        });

        textViewLevel = findViewById(R.id.textViewLevel);
        textViewMsg = findViewById(R.id.textViewMsg);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
        if (null == mSensorManager) {
            Log.d(TAG, "deveice not support SensorManager");
        }
        // 参数三，检测的精准度
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAME

        sensorDao = new SensorDao(this);

        chart = findViewById(R.id.chart);
        ChartUtils.initChart(chart);
        List<Entry> xValues = new ArrayList<>();
        List<Entry> yValues = new ArrayList<>();
        List<SensorBean> beens = sensorDao.query();
        if (null == beens || beens.size() == 0) {
            index++;
            xValues.add(new Entry(index, 0));
            yValues.add(new Entry(index, 0));
        } else {
            for (SensorBean been : beens) {
                index++;
                xValues.add(new Entry(index, been.getxValue()));
                yValues.add(new Entry(index, been.getyValue()));
            }
        }

        ChartUtils.notifyDataSetChanged(chart, xValues, ChartUtils.xValue);
        ChartUtils.notifyDataSetChanged(chart, yValues, ChartUtils.yValue);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (!start) {
                return;
            }
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];
            mCalendar = Calendar.getInstance();
            long stamp = mCalendar.getTimeInMillis() / 1000l;// 1393844912

            textviewX.setText(String.valueOf(x));
            textviewY.setText(String.valueOf(y));
            textviewZ.setText(String.valueOf(z));

            int second = mCalendar.get(Calendar.SECOND);// 53

            int px = Math.abs(mX - x);
            int py = Math.abs(mY - y);
            int pz = Math.abs(mZ - z);
            Log.d(TAG, "pX:" + px + "  pY:" + py + "  pZ:" + pz + "    stamp:"
                    + stamp + "  second:" + second);
            if ((stamp - lasttimestamp) > 2) {
                lasttimestamp = stamp;
                Log.d(TAG, " sensor isMoveorchanged....");
                textViewMessage.setText("检测手机在移动..");
                sensorDao.add(x, y, z);
                this.addEntry(0, x, index++);
                this.addEntry(1, y, index++);
            }


            mX = x;
            mY = y;
            mZ = z;

        }
    }

    public void addEntry(Integer index,Integer value, long time) {
        LineData lineData = chart.getData();

        if (lineData != null) {
            ILineDataSet lastSet = lineData.getDataSetByIndex(index);
            // 这里要注意，x轴的index是从零开始的
            // 假设index=2，那么getEntryCount()就等于3了
            int count = lastSet.getEntryCount();
            // add a new x-value first 这行代码不能少

            // 位最后一个DataSet添加entry
            lineData.addEntry(new Entry(time, value), index);

            chart.notifyDataSetChanged();
            chart.moveViewTo(value, count, YAxis.AxisDependency.LEFT);

        }
    }

    private LineDataSet createSet(String label) {
        LineDataSet set = new LineDataSet(null, label);
        set.setLineWidth(2.5f);
        set.setCircleRadius(4.5f);
        if ("X".equals(label)) {
            set.setColor(Color.RED);
        } else {
            set.setColor(Color.BLUE);
        }
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);

        return set;
    }


    @Override
    public void onResponeStart(String apiName) {

    }

    @Override
    public void onLoading(String apiName, long count, long current) {

    }

    @Override
    public void onSuccess(String apiName, Object object) {
        if (null != object && object instanceof UserDriveVo) {
            UserDriveVo driveVo = (UserDriveVo) object;
            textViewLevel.setText(driveVo.getLevel());
            textViewMsg.setText(driveVo.getMessage());
        }
    }

    @Override
    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {
        Toast.makeText(MainActivity.this, strMsg, Toast.LENGTH_SHORT).show();
    }

//    public void notifyDataSetChanged(LineChart chart, List<Entry> values) {
//        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                return df.format(new Date((long) value));
//            }
//        });
//
//        chart.invalidate();
//        setChartData(chart, values);
//    }

}

