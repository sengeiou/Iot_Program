package com.vtech.app.moudle.report;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.vtech.app.moudle.BasePresenter;
import com.vtech.app.moudle.BaseView;

import java.util.ArrayList;

public interface ReportContract {
    interface View extends BaseView<Presenter>{
            void refreshLineChartData(ArrayList<Entry> values);

            void refreshBarChartData(ArrayList<BarEntry> yVals1, ArrayList<BarEntry> yVals2,int xVlalue);

            void setHeartStatisticalData(int max,int avg,int min);
    }

    interface Presenter extends BasePresenter {
        void initLineChart(LineChart mLineChar,float x);

        void initBarChart(BarChart mBarChart,float x);

        void getBarChartData(int type);

        void getLineChartData(int type);

        SimpleXYSeries createSeries();

        SimpleXYSeries addSeries(XYPlot plot, SimpleXYSeries series, int formatterId);
    }
}
