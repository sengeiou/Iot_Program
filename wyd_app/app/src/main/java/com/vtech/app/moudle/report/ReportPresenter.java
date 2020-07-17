package com.vtech.app.moudle.report;

import android.content.Context;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.vtech.app.data.bean.HealthBean;
import com.vtech.app.data.bean.StatisticBean;
import com.vtech.app.util.DateUtils;
import com.vtech.app.util.HealthUtils;

import java.util.ArrayList;

public class ReportPresenter implements ReportContract.Presenter {
    public final static String TAG = "ReportPresenter";
    private ReportContract.View view;
    private Context context;

    public ReportPresenter(ReportContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    public ReportPresenter(ReportContract.View view, Context context) {
        this.view = view;
        this.context = context;
        view.setPresenter(this);
    }

    @Override
    public void initLineChart(LineChart mLineChar, float xValues) {
        //设置手势滑动事件
//        mLineChar.setOnChartGestureListener(this);
        //设置数值选择监听
//        mLineChar.setOnChartValueSelectedListener(this);
        //后台绘制
        mLineChar.setDrawGridBackground(false);
        //设置描述文本
        mLineChar.getDescription().setEnabled(false);
        //设置支持触控手势
        mLineChar.setTouchEnabled(true);
        //设置缩放
        mLineChar.setDragEnabled(true);
        //设置推动
        mLineChar.setScaleEnabled(true);
        //如果禁用,扩展可以在x轴和y轴分别完成
        mLineChar.setPinchZoom(true);

        // 得到这个文字
        Legend l = mLineChar.getLegend();
        // 修改文字 ...
        l.setForm(Legend.LegendForm.LINE);

        //上面右边效果图的部分代码，设置X轴
        XAxis xAxis = mLineChar.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
//        xAxis.setTypeface(mTf); // 设置字体
        xAxis.setEnabled(true);
        // 上面第一行代码设置了false,所以下面第一行即使设置为true也不会绘制AxisLine
        xAxis.setDrawAxisLine(false);
        xAxis.setAxisMaximum(xValues);
        xAxis.setLabelCount(6, false);
        xAxis.setAxisMinimum(0f);
        // 前面xAxis.setEnabled(false);则下面绘制的Grid不会有"竖的线"（与X轴有关）
        xAxis.setDrawGridLines(false); // 效果如下图

        YAxis leftAxis = mLineChar.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setLabelCount(6, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        //这个替换setStartAtZero(true)
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(160f);

        YAxis rightAxis = mLineChar.getAxisRight();
        rightAxis.setEnabled(false);

        mLineChar.invalidate();
    }

    @Override
    public void initBarChart(BarChart mBarChart, float xValues) {
//        mBarChart.setDoubleTapToZoomEnabled(false);
        //禁止拖拽
        mBarChart.setDragEnabled(true);
        //X轴或Y轴禁止缩放
//        mBarChart.setScaleXEnabled(false);
//        mBarChart.setScaleYEnabled(false);
        mBarChart.setScaleEnabled(true);
        mBarChart.setTouchEnabled(true);

        //设置数值选择监听
//        mBarChart.setOnChartValueSelectedListener(this);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.getDescription().setEnabled(false);
        // 如果60多个条目显示在图表,drawn没有值
        mBarChart.setMaxVisibleValueCount(60);
        // 扩展现在只能分别在x轴和y轴
        mBarChart.setPinchZoom(true);
        //是否显示表格颜色
        mBarChart.setDrawGridBackground(false);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        // 只有1天的时间间隔
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);
        xAxis.setAxisMaximum(xValues);
        xAxis.setAxisMinimum(0f);
//        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setLabelCount(6, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        //这个替换setStartAtZero(true)
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(200f);

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setEnabled(false);

        mBarChart.invalidate();
    }

    @Override
    public void getBarChartData(int type) {
        ArrayList<BarEntry> idData = getBarCharIdData(type);
        ArrayList<BarEntry> isData = getBarCharIsData(type);
        int value = 24;
        switch (type) {
            case 1:
                value = 24;
                break;
            case 2:
                value = 7;
                break;
            case 3:
                value = 31;
                break;
        }
        view.refreshBarChartData(idData, isData, value);
    }

    @Override
    public void getLineChartData(int type) {
        ArrayList<Entry> list = getLineData(type);
        view.refreshLineChartData(list);
    }

    @Override
    public SimpleXYSeries createSeries() {
        // Turn the above arrays into XYSeries':
        SimpleXYSeries series = new SimpleXYSeries(
                null,          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                null);                             // Set the display title of the series
        series.useImplicitXVals();
        return series;
    }

    @Override
    public SimpleXYSeries addSeries(XYPlot plot, SimpleXYSeries series, int formatterId) {
        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // and configure it from xml:
        LineAndPointFormatter seriesFormat = new LineAndPointFormatter();
        seriesFormat.setPointLabelFormatter(null);
        seriesFormat.configure(context, formatterId);
        seriesFormat.setVertexPaint(null);
        series.useImplicitXVals();

        plot.addSeries(series, seriesFormat);
        return series;
    }


    ArrayList<Entry> getLineData(int type) {
        StatisticBean data = HealthUtils.getHealthData(context, type);
        ArrayList<HealthBean> list =  (data.getList() != null && data.getList().size() > 0) ? data.getList() : new ArrayList<HealthBean>();

        ArrayList<Entry> values = new ArrayList<>();

        for (HealthBean bean : list) {
            if (type == 1) {
                values.add(new Entry(DateUtils.getHourOfDay(bean.getCreatTime()), bean.getIhrate()));
            } else if (type == 2) {
                values.add(new Entry(DateUtils.getDayOfWeek(bean.getCreatTime()), bean.getIhrate()));
            } else if (type == 3) {
                values.add(new Entry(DateUtils.getDayOfMonth(bean.getCreatTime()), bean.getIhrate()));
            }
        }

        if (data.getMaxRate() != 0 && data.getAvgRate() != 0 && data.getMinRate() != 0) {
            view.setHeartStatisticalData(data.getMaxRate(), data.getAvgRate(), data.getMinRate());
        } else {
            view.setHeartStatisticalData(0, 0, 0);
        }

        return values;
    }

    //收缩压
    ArrayList<BarEntry> getBarCharIsData(int type) {
        StatisticBean data = HealthUtils.getHealthData(context, type);
        ArrayList<HealthBean> list = (data.getList() != null && data.getList().size() > 0) ? data.getList() : new ArrayList<HealthBean>();
        //设置数据
        //在这里设置自己的数据源,BarEntry 只接收float的参数，
        //图形横纵坐标默认为float形式，如果想展示文字形式，需要自定义适配器，
        ArrayList<BarEntry> values = new ArrayList<>();

        for (HealthBean bean : list) {
            if (type == 1) {
                values.add(new BarEntry(DateUtils.getHourOfDay(bean.getCreatTime()), bean.getIsbp()));
            } else if (type == 2) {
                values.add(new BarEntry(DateUtils.getDayOfWeek(bean.getCreatTime()), bean.getIsbp()));
            } else if (type == 3) {
                values.add(new BarEntry(DateUtils.getDayOfMonth(bean.getCreatTime()), bean.getIsbp()));
            }
        }
        return values;
    }

    //舒张压
    ArrayList<BarEntry> getBarCharIdData(int type) {
        StatisticBean data = HealthUtils.getHealthData(context, type);
        ArrayList<HealthBean> list = (data.getList() != null && data.getList().size() > 0) ? data.getList() : new ArrayList<HealthBean>();
        //设置数据
        //在这里设置自己的数据源,BarEntry 只接收float的参数，
        //图形横纵坐标默认为float形式，如果想展示文字形式，需要自定义适配器，
        ArrayList<BarEntry> values = new ArrayList<>();

        for (HealthBean bean : list) {
            if (type == 1) {
                values.add(new BarEntry(DateUtils.getHourOfDay(bean.getCreatTime()), bean.getIdbp()));
            } else if (type == 2) {
                values.add(new BarEntry(DateUtils.getDayOfWeek(bean.getCreatTime()), bean.getIdbp()));
            } else if (type == 3) {
                values.add(new BarEntry(DateUtils.getDayOfMonth(bean.getCreatTime()), bean.getIdbp()));
            }
        }
        return values;
    }

    public static void main(String[] args) {
        String time = "2019-09-26 18:27:43";

        System.out.println("day : " + time.substring(8, 10));


        System.out.println("hour : " + time.substring(11, 13));
    }
}
