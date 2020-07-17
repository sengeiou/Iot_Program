package com.vtech.app.moudle.report;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vtech.app.R;
import com.vtech.app.data.bean.HomeSecurityEventBean;
import com.vtech.app.moudle.BaseFragment;
import com.vtech.app.moudle.MainActivity;
import com.vtech.app.moudle.adapter.NormalAdapter;
import com.vtech.app.util.HealthUtils;
import com.vtech.app.util.HomeSecurityEventType;
import com.vtech.app.util.HomeSecurityUtil;
import com.vtech.app.util.Logger;

import java.util.ArrayList;
import java.util.List;


public class ReportFragment extends BaseFragment implements ReportContract.View, RadioGroup.OnCheckedChangeListener {

    TextView maxTv;

    TextView averageTv;

    TextView minTv;

    RadioGroup heartGroup;
    RadioGroup bloodGroup;

    LineChart mLineChar;

    BarChart mBarChart;

    private XYPlot plot;
    private SimpleXYSeries ecgSeries = null;
    // graph plot variables
    private final static int X_RANGE = 50;
    private int mCurrentXRange = X_RANGE;

    RecyclerView recyclerView;

    ReportContract.Presenter mPresenter;


    TextView lable1;

    TextView lable2;

    TextView lable3;

    TextView lable4;

    TextView lable5;

    ScrollView scrollView;

    Handler handler = new Handler();

    public static ReportFragment newInstance() {
        ReportFragment mainFragment = new ReportFragment();
        return mainFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_report;
    }

    @Override
    protected void initView(final View view, Bundle savedInstanceState) {
        new ReportPresenter(this, getActivity());
        maxTv = (TextView) view.findViewById(R.id.max);
        averageTv = (TextView) view.findViewById(R.id.average);
        minTv = (TextView) view.findViewById(R.id.min);
        lable1 = (TextView) view.findViewById(R.id.lable1);
        lable2 = (TextView) view.findViewById(R.id.lable2);
        lable3 = (TextView) view.findViewById(R.id.lable3);
        lable4 = (TextView) view.findViewById(R.id.lable4);
        lable5 = (TextView) view.findViewById(R.id.lable5);

        //线性图
        mLineChar = view.findViewById(R.id.mLineChar);
        mLineChar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                boolean isScaled = mLineChar.getScaleX() != 1.0 && mLineChar.getScaleY() != 1.0;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setScrollViewScrolling(scrollView, mLineChar);
                    mLineChar.getParent().requestDisallowInterceptTouchEvent(isScaled);
                    return false;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return false;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    scrollView.setEnabled(true);
                    return false;
                } else {
                    return false;
                }
            }
        });

        //条形图
        mBarChart = view.findViewById(R.id.mBarChart);
        mBarChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                boolean isScaled = mBarChart.getScaleX() != 1.0 && mBarChart.getScaleY() != 1.0;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setScrollViewScrolling(scrollView, mBarChart);
                    mBarChart.getParent().requestDisallowInterceptTouchEvent(isScaled);
                    return false;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return false;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    scrollView.setEnabled(true);
                    return false;
                } else {
                    return false;
                }
            }
        });

        recyclerView = view.findViewById(R.id.cost_list_view);

        heartGroup = view.findViewById(R.id.heart_group);
        bloodGroup = view.findViewById(R.id.blood_group);

        scrollView = view.findViewById(R.id.scroll_view);

        heartGroup.setOnCheckedChangeListener(ReportFragment.this);
        bloodGroup.setOnCheckedChangeListener(ReportFragment.this);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initEcgChart(view);
            }
        }, 800);
    }

    /**
     * 设置上下滑动
     *
     * @param scrollView
     * @param lineChart
     */
    public static void setScrollViewScrolling(ScrollView scrollView, BarLineChartBase lineChart) {
        if (lineChart != null) {
            float scaleY = lineChart.getScaleY();
            if (scaleY <= 1.0f) { //可以上下滑动
                if (scrollView != null) scrollView.setEnabled(true);
            } else {
                if (scrollView != null) scrollView.setEnabled(false);
            }
        } else {
            if (scrollView != null) scrollView.setEnabled(true);
        }
    }

    @Override
    protected void initData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                registerReceiver();
                mPresenter.initBarChart(mBarChart, 24f);
                mPresenter.initLineChart(mLineChar, 24f);

                refreshChartData();

                //初始化recycle数据
                LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(new NormalAdapter());
                recyclerView.setNestedScrollingEnabled(false);

                refreshSafeData();

                List<Integer> list = HealthUtils.getEcgData(getActivity());
                if (list != null && list.size() > 0) {
                    updateHeartData(list);
                }
            }
        }, 1600);
    }

    @Override
    protected void onLazyLoad() {

    }

    public void refreshSafeData() {
        ArrayList<HomeSecurityEventBean> datalist = HomeSecurityUtil.getAllHPEvent(getActivity());
        int a = 0, b = 0, c = 0, d = 0, e = 0;
        if (datalist != null && datalist.size() > 0) {
            for (HomeSecurityEventBean bean : datalist) {
                if (bean.getProbeType() == HomeSecurityEventType.MagneticInt || "门磁报警".equals(bean.getEventName())) {
                    a++;
                }
                //红外人体
                if (bean.getProbeType() == HomeSecurityEventType.InfraredInt || "红外探测器报警".equals(bean.getEventName())) {
                    d++;
                }
                //红外对射
                if (bean.getProbeType() == HomeSecurityEventType.InfraredRayInt) {
                    b++;
                }

                //温度对射
                if (bean.getProbeType() == HomeSecurityEventType.TemperatureInt) {
                    c++;
                }
                //联动布防报警
                if (bean.getProbeType() == HomeSecurityEventType.Joint_protection) {
                    e++;
                }

            }
        }
        lable1.setText(String.format(getString(R.string.door_error), a));
        lable2.setText(String.format(getString(R.string.infrared_correlation_error), b));
        lable3.setText(String.format(getString(R.string.temperature_is_too_high), c));
        lable4.setText(String.format(getString(R.string.infrared_intrusion), d));
        lable5.setText(String.format(getString(R.string.protection_alarm), e));
    }

    public void refreshChartData() {
        mPresenter.initLineChart(mLineChar, 24f);
        mPresenter.getLineChartData(1);
        mPresenter.getBarChartData(1);
    }


    public void updateHeartData(final List<Integer> list) {
//        clearAllSeries();
        Log.i(TAG, "updateHeartData  list size == " + list.size());
        for (Integer i : list) {
            AddValueToPlot(ecgSeries, i);
        }
    }

    @Override
    public void refreshLineChartData(ArrayList<Entry> values) {
        // 创建一个数据集,并给它一个类型
        LineDataSet set1 = new LineDataSet(values, "心率(bpm)");
        set1.setColors(new int[]{R.color.red}, getActivity());
        set1.setCircleColor(ColorTemplate.rgb("#FC4C7A"));
        set1.setLineWidth(1.5f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setDrawValues(false);//不显示数值
        set1.setDrawIcons(false);//
        set1.setDrawCircles(true);//不画点
        set1.setValueTextSize(9f);
        set1.setDrawFilled(false);
        set1.setFormLineWidth(1f);
        set1.setFormSize(15.f);
        //设置为弧线
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        //添加数据集
        dataSets.add(set1);
        //创建一个数据集的数据对象
        LineData data = new LineData(dataSets);
        //谁知数据
        mLineChar.setData(data);
        //默认动画
        mLineChar.animateX(1000);
        //刷新
        mLineChar.invalidate();
    }

    @Override
    public void refreshBarChartData(ArrayList<BarEntry> yVals1, ArrayList<BarEntry> yVals2, int xVlalue) {
        BarDataSet set1 = new BarDataSet(yVals1, "舒张压");
        set1.setDrawIcons(false);
        set1.setDrawValues(false);
        set1.setColor(ColorTemplate.rgb("#ADEBE3"));
        BarDataSet set2 = new BarDataSet(yVals2, "收缩压");
        set2.setDrawIcons(false);
        set2.setDrawValues(false);
        set2.setColor(ColorTemplate.rgb("#CB9EF6"));
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        mBarChart.setData(data);

        float groupSpace = 0.2f;
        float barSpace = 0.1f;
        mBarChart.getBarData().setBarWidth(0.3f);
        mBarChart.getXAxis().setAxisMinimum(0);
        mBarChart.getXAxis().setAxisMaximum(mBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * xVlalue + 0);

        mBarChart.groupBars((yVals1 != null && yVals1.size() > 0) ? yVals1.get(0).getX() : 0, groupSpace, barSpace);

        mBarChart.animateY(1000, Easing.Linear);
        mBarChart.animateX(1000, Easing.Linear);
    }

    @Override
    public void setHeartStatisticalData(int max, int avg, int min) {
        String maxStr = " <font color=\"#000000\">" + max + "</font>";
        maxTv.setText(Html.fromHtml(maxStr));

        String avgStr = " <font color=\"#000000\">" + avg + "</font>";
        averageTv.setText(Html.fromHtml(avgStr));

        String minStr = " <font color=\"#000000\">" + min + "</font>";
        minTv.setText(Html.fromHtml(minStr));
    }

    @Override
    public void setPresenter(ReportContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        Logger.i(TAG, "onCheckedChanged checkedId : " + checkedId);
        switch (checkedId) {
            case R.id.day:
                mPresenter.initLineChart(mLineChar, 24f);
                mPresenter.getLineChartData(1);
                break;
            case R.id.week:
                mPresenter.initLineChart(mLineChar, 7f);
                mPresenter.getLineChartData(2);
                break;
            case R.id.month:
                mPresenter.initLineChart(mLineChar, 31f);
                mPresenter.getLineChartData(3);
                break;
            case R.id.day_b:
                mPresenter.getBarChartData(1);
                break;
            case R.id.week_b:
                mPresenter.getBarChartData(2);
                break;
            case R.id.month_b:
                mPresenter.getBarChartData(3);
                break;
        }
    }

    private void initEcgChart(View view) {
        //心电图
        plot = view.findViewById(R.id.ecg_plot);
        removeAllSeriesFromPlot();
        setupPlotWithXRange(-6000, 10000, 1000);
        ecgSeries = mPresenter.createSeries();
        mPresenter.addSeries(plot, ecgSeries, R.xml.line_point_formatter_with_plf1);
        plot.redraw();
    }


    private void AddValueToPlot(SimpleXYSeries series, float value) {
        if (series.size() >= mCurrentXRange) {
            series.removeFirst();
        }
        Number num = value;
        series.addLast(null, num);
        plot.redraw();
    }

    private void removeAllSeriesFromPlot() {
        plot.setVisibility(View.INVISIBLE);
        if (ecgSeries != null) {
            plot.removeSeries(ecgSeries);
        }
    }

    private void clearAllSeries() {
        if (ecgSeries != null) {
            plot.removeSeries(ecgSeries);
//            ecgSeries = null;
        }
    }

    private XYPlot setupPlotWithXRange(Number yMin, Number yMax, Number xMax) {
        // initialize our XYPlot reference:
        mCurrentXRange = xMax.intValue();
        plot.setDomainBoundaries(0, xMax, BoundaryMode.FIXED);

        if ((yMax.intValue() - yMin.intValue()) < 10) {
            plot.setRangeStepValue((yMax.intValue() - yMin.intValue() + 1));
        } else {
            plot.setRangeStepValue(11);
        }
        plot.setRangeBoundaries(yMin.intValue(), yMax.intValue(), BoundaryMode.FIXED);
        //plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        plot.getGraph().getGridBackgroundPaint().setColor(Color.WHITE);

        plot.setPlotPadding(0, 0, 0, 0);
        //Remove legend
        plot.getLayoutManager().remove(plot.getLegend());
        plot.getLayoutManager().remove(plot.getDomainTitle());
        plot.getLayoutManager().remove(plot.getRangeTitle());
        plot.getLayoutManager().remove(plot.getTitle());
        plot.setVisibility(View.VISIBLE);
        return plot;
    }

    public void player(String str) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.player(str);
    }

    void playerSafeReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(lable1.getText().toString().trim().contains("0") ? "" : lable1.getText().toString().trim());
        sb.append(lable2.getText().toString().trim().contains("0") ? "" : lable2.getText().toString().trim());
        sb.append(lable3.getText().toString().trim().contains("0") ? "" : lable3.getText().toString().trim());
        sb.append(lable4.getText().toString().trim().contains("0") ? "" : lable4.getText().toString().trim());
        sb.append(lable5.getText().toString().trim().contains("0") ? "" : lable5.getText().toString().trim());
        if (TextUtils.isEmpty(sb.toString())) {
            player(getString(R.string.no_safe_report));
        } else {
            player(sb.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(reportReceiver);
    }

    ReportBroadcastReceiver reportReceiver;

    void registerReceiver() {
        reportReceiver = new ReportBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_HEALTH_NOTIFY);
        filter.addAction(ACTION_HEALTH_HEART_NOTIFY);
        filter.addAction(ACTION_SAFE_EVENT_NOTIFY);
        filter.addAction(VOICE_DEPLOY_CMD_SAFETY_REPORT);
        getContext().registerReceiver(reportReceiver, filter);
    }

    public static final String ACTION_HEALTH_NOTIFY = "com.vtech.vhealth.stop.notify";

    public static final String ACTION_HEALTH_HEART_NOTIFY = "com.vtech.vhealth.heart.stop.notify";

    public static final String ACTION_SAFE_EVENT_NOTIFY = "com.vtech.homesecurity.event.notify";

    public static final String VOICE_DEPLOY_CMD_SAFETY_REPORT = "com.vtech.voiceassistant.event.safety.report";

    public class ReportBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "ReportBroadcastReceiver is start! ----------------- action : " + intent.getAction());
            if (intent.getAction().equals(ACTION_HEALTH_NOTIFY)) {
                refreshChartData();
            }

            if (intent.getAction().equals(ACTION_HEALTH_HEART_NOTIFY)) {
                String data = intent.getStringExtra("chart");
                List<Integer> list = new Gson().fromJson(data, new TypeToken<List<Integer>>() {
                }.getType());
                Log.i(TAG, "list size == " + list.size() + " , list is : " + new Gson().toJson(list));
                updateHeartData(list);
            }

            if (intent.getAction().equals(ACTION_SAFE_EVENT_NOTIFY)) {
                refreshSafeData();
            }

            if (intent.getAction().equals(VOICE_DEPLOY_CMD_SAFETY_REPORT)) {
                playerSafeReport();
            }
        }
    }
}
