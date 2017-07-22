package com.safty_drive.util;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Calendar;
import java.util.List;

/**
 * Created by fanwenbin on 2017/7/22.
 */

public class ChartUtils {

    public static int xValue = 0;
    public static int yValue = 1;

    /**
     * 初始化图表
     *
     * @param chart 原始图表
     * @return 初始化后的图表
     */
    public static LineChart initChart(LineChart chart) {
//        // 不显示数据描述
//        chart.getDescription().setEnabled(false);
//        // 没有数据的时候，显示“暂无数据”
//        chart.setNoDataText("暂无数据");
//        // 不显示表格颜色
//        chart.setDrawGridBackground(false);
//        // 不可以缩放
//        chart.setScaleEnabled(false);
//        // 不显示y轴右边的值
//        chart.getAxisRight().setEnabled(false);
//        // 不显示图例
//        Legend legend = chart.getLegend();
//        legend.setEnabled(false);
//        // 向左偏移15dp，抵消y轴向右偏移的30dp
//        chart.setExtraLeftOffset(-15);
//
//        XAxis xAxis = chart.getXAxis();
//        // 不显示x轴
//        xAxis.setDrawAxisLine(false);
//        // 设置x轴数据的位置
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTextColor(Color.WHITE);
//        xAxis.setTextSize(12);
//        xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
//        // 设置x轴数据偏移量
//        xAxis.setYOffset(-12);
//
//        YAxis yAxis = chart.getAxisLeft();
//        // 不显示y轴
//        yAxis.setDrawAxisLine(false);
//        // 设置y轴数据的位置
//        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        // 不从y轴发出横向直线
//        yAxis.setDrawGridLines(false);
//        yAxis.setTextColor(Color.WHITE);
//        yAxis.setTextSize(12);
//        // 设置y轴数据偏移量
//        yAxis.setXOffset(30);
//        yAxis.setYOffset(-3);
//        yAxis.setAxisMinimum(0);
//
//        //Matrix matrix = new Matrix();
//        // x轴缩放1.5倍
//        //matrix.postScale(1.5f, 1f);
//        // 在图表动画显示之前进行缩放
//        //chart.getViewPortHandler().refresh(matrix, chart, false);
//        // x轴执行动画
//        //chart.animateX(2000);
//        chart.invalidate();
        return chart;
    }

    /**
     * 设置图表数据
     *
     * @param chart  图表
     * @param values 数据
     */
    public static void setChartData(LineChart chart, List<Entry> values, Integer index) {
        LineDataSet lineDataSet;
        if (chart.getData() != null) {
            if (null != chart.getData().getDataSetByIndex(index)) {
                lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(index);
                lineDataSet.setValues(values);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                if (index == 0) {
                    lineDataSet = new LineDataSet(values, "X");
                    // 设置曲线颜色
                    lineDataSet.setColor(Color.RED);
                } else {
                    lineDataSet = new LineDataSet(values, "Y");
                    // 设置曲线颜色
                    lineDataSet.setColor(Color.BLUE);
                }

                // 设置平滑曲线

//                // 不显示坐标点的小圆点
//                lineDataSet.setDrawCircles(false);
//                // 不显示坐标点的数据
//                lineDataSet.setDrawValues(false);
//                // 不显示定位线
                lineDataSet.setHighlightEnabled(false);

                LineData data = chart.getData();
                data.addDataSet(lineDataSet);
            }
        } else {
            if (index == 0) {
                lineDataSet = new LineDataSet(values, "X");
                // 设置曲线颜色
                lineDataSet.setColor(Color.RED);
            } else {
                lineDataSet = new LineDataSet(values, "Y");
                // 设置曲线颜色
                lineDataSet.setColor(Color.BLUE);
            }

            // 不显示坐标点的小圆点
//            lineDataSet.setDrawCircles(false);
//            // 不显示坐标点的数据
//            lineDataSet.setDrawValues(false);
//            // 不显示定位线
            lineDataSet.setHighlightEnabled(false);

            LineData data = new LineData(lineDataSet);
            chart.setData(data);
            chart.invalidate();
        }
    }

    /**
     * 更新图表
     *
     * @param chart  图表
     * @param values 数据
     * @param index  数据类型
     */
    public static void notifyDataSetChanged(LineChart chart, List<Entry> values, Integer index) {
//        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return TimeUtils.dateToString((long) value);
//            }
//        });

        chart.invalidate();
        setChartData(chart, values, index);
    }
}

