package com.safty_drive.activity;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
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
import com.safty_drive.HttpResponeCallBack;
import com.safty_drive.R;
import com.safty_drive.RequestApiData;
import com.safty_drive.bean.SensorBean;
import com.safty_drive.dao.SensorDao;
import com.safty_drive.vo.UserBaseInfo;
import com.safty_drive.vo.UserDriveVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textviewX = (TextView) findViewById(R.id.textViewX);
        textviewY = (TextView) findViewById(R.id.textViewY);
        textviewZ = (TextView) findViewById(R.id.textViewZ);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);

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

        chart = (LineChart) findViewById(R.id.chart);
//        initChart(chart);
    }

    public LineChart initChart(LineChart chart) {
        // 不显示数据描述
        chart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        chart.setNoDataText("暂无数据");
        // 不显示表格颜色
        chart.setDrawGridBackground(false);
        // 不可以缩放
        chart.setScaleEnabled(false);
        // 不显示y轴右边的值
        chart.getAxisRight().setEnabled(false);
        // 不显示图例
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        // 向左偏移15dp，抵消y轴向右偏移的30dp
        chart.setExtraLeftOffset(-15);

        XAxis xAxis = chart.getXAxis();
        // 不显示x轴
        xAxis.setDrawAxisLine(false);
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12);
        xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
        // 设置x轴数据偏移量
        xAxis.setYOffset(-12);

        YAxis yAxis = chart.getAxisLeft();
        // 不显示y轴
        yAxis.setDrawAxisLine(false);
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 不从y轴发出横向直线
        yAxis.setDrawGridLines(false);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
        yAxis.setXOffset(30);
        yAxis.setYOffset(-3);
        yAxis.setAxisMinimum(0);

        //Matrix matrix = new Matrix();
        // x轴缩放1.5倍
        //matrix.postScale(1.5f, 1f);
        // 在图表动画显示之前进行缩放
        //chart.getViewPortHandler().refresh(matrix, chart, false);
        // x轴执行动画
        //chart.animateX(2000);
        chart.invalidate();
        return chart;
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

                List<SensorBean> sensorBeans = sensorDao.query();
                List<Entry> xEntries = new ArrayList<Entry>();
                List<Entry> yEntries = new ArrayList<Entry>();
                List<Entry> zEntries = new ArrayList<Entry>();
                for (SensorBean sensorBean : sensorBeans) {
                    xEntries.add(new Entry(sensorBean.getCtime().getTime(), sensorBean.getxValue()));
                    yEntries.add(new Entry(sensorBean.getCtime().getTime(), sensorBean.getyValue()));
                    zEntries.add(new Entry(sensorBean.getCtime().getTime(), sensorBean.getzValue()));
                }
                LineDataSet xDataSet = new LineDataSet(xEntries, "X");
                xDataSet.setColor(Color.RED);
                LineDataSet yDataSet = new LineDataSet(yEntries, "Y");
                yDataSet.setColor(Color.GREEN);
                LineDataSet zDataSet = new LineDataSet(zEntries, "Z");
                zDataSet.setColor(Color.BLUE);
                LineData lineData = new LineData(xDataSet);
                lineData.addDataSet(yDataSet);
                lineData.addDataSet(zDataSet);
                chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        SimpleDateFormat df = new SimpleDateFormat("dd HH:mm:ss");
                        return df.format(new Date((long) value));
                    }
                });
                chart.setData(lineData);
                chart.invalidate(); // refresh
            }


            mX = x;
            mY = y;
            mZ = z;

        }
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

