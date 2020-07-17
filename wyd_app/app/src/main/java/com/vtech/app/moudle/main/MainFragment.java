package com.vtech.app.moudle.main;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.vtech.app.App;
import com.vtech.app.R;
import com.vtech.app.data.APIService;
import com.vtech.app.data.Constant;
import com.vtech.app.data.bean.ContactData;
import com.vtech.app.data.bean.HomeProtectZoneStatusBean;
import com.vtech.app.data.bean.HomeSecurityCombineZoneStatusBean;
import com.vtech.app.data.bean.WeatherBean;
import com.vtech.app.moudle.BaseFragment;
import com.vtech.app.moudle.MainActivity;
import com.vtech.app.moudle.adapter.ContactAdapter;
import com.vtech.app.moudle.adapter.SafeActionAdapter;
import com.vtech.app.moudle.adapter.WatchAdapter;
import com.vtech.app.util.HomeSecurityUtil;
import com.vtech.app.util.Logger;
import com.vtech.app.util.PreferenceConstants;
import com.vtech.app.util.PreferenceUtils;
import com.vtech.app.util.Utils;
import com.vtech.app.view.PhonetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainFragment extends BaseFragment implements View.OnClickListener, MainContract.View {
    public static final int TOP = 0;

    public static final int CENTER = 1;

    public static final int BOTTOM = 2;

    //@Bind(R.id.schedle_list_view)
    //ListView schedleListView;

    ListView watchListView;

    GridView contactListView;

    TextView cityTv;

    TextView temperatureTv;

    TextView descripTv;

    TextView airTv;

    ImageView weatherIcon;

    View jointLine;

    View normalLine;

    CheckBox inHomeBox;

    CheckBox outHomeBox;

    CheckBox removalBox;

    TextView emptyView;

    WatchAdapter mWatchAdapter;
//    SchedleAdapter mSchedleAdapter;

    SafeActionAdapter mSafeActionAdapter;

    ContactAdapter mContactAdapter;

    List<ContactData> mContactList = new ArrayList<>();

    Handler handler = new Handler();

    MainContract.Presenter mPresenter;

    TextView timeTv;

    TextView dateTv;

    TextView weekTv;

    public static MainFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_;
    }

    @Override
    protected void initView(final View view, Bundle savedInstanceState) {
        new MainPresenter(this, getActivity());
        watchListView = (ListView) view.findViewById(R.id.watch_list_view);
        contactListView = (GridView) view.findViewById(R.id.contact_list_view);
        cityTv = (TextView) view.findViewById(R.id.city_tv);
        temperatureTv = (TextView) view.findViewById(R.id.temperature_tv);
        descripTv = (TextView) view.findViewById(R.id.descrip_tv);
        airTv = (TextView) view.findViewById(R.id.air_tv);
        weatherIcon = (ImageView) view.findViewById(R.id.weather_icon);
        jointLine = (View) view.findViewById(R.id.joint_line);
        normalLine = (View) view.findViewById(R.id.normal_line);
        inHomeBox = (CheckBox) view.findViewById(R.id.in_home_box);
        outHomeBox = (CheckBox) view.findViewById(R.id.out_home_box);
        removalBox = (CheckBox) view.findViewById(R.id.removal_box);
        emptyView = (TextView) view.findViewById(R.id.empty_view);
        view.findViewById(R.id.safety_icon).setOnClickListener(this);
        view.findViewById(R.id.alarm_icon).setOnClickListener(this);
        view.findViewById(R.id.health_icon).setOnClickListener(this);
        view.findViewById(R.id.call_icon).setOnClickListener(this);

        view.findViewById(R.id.time_tv).setOnClickListener(this);
        view.findViewById(R.id.date_tv).setOnClickListener(this);
        view.findViewById(R.id.week_tv).setOnClickListener(this);

        view.findViewById(R.id.weather_icon).setOnClickListener(this);
        view.findViewById(R.id.temperature_tv).setOnClickListener(this);
        view.findViewById(R.id.air_tv).setOnClickListener(this);
        view.findViewById(R.id.descrip_tv).setOnClickListener(this);

        timeTv = view.findViewById(R.id.time_tv);
        dateTv = view.findViewById(R.id.date_tv);
        weekTv = view.findViewById(R.id.week_tv);
        weatherIcon.setOnClickListener(this);
        temperatureTv.setOnClickListener(this);
        airTv.setOnClickListener(this);
        descripTv.setOnClickListener(this);

        view.findViewById(R.id.normal_protection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSafeList();
            }
        });
        view.findViewById(R.id.joint_protection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSafeActionList();
            }
        });

        inHomeBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    inHomeBox.setClickable(false);
                    mPresenter.setSafeBroadcast(Constant.DEPLOY_CMD_VALUE_HOME);
                } else {
                    inHomeBox.setClickable(true);
                }
            }
        });
        outHomeBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    outHomeBox.setClickable(false);
                    mPresenter.setSafeBroadcast(Constant.DEPLOY_CMD_VALUE_OUTER);
                } else {
                    outHomeBox.setClickable(true);
                }
            }
        });
        removalBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    removalBox.setClickable(false);
                } else {
                    removalBox.setClickable(true);
                }
            }
        });
        removalBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (removalBox.isChecked()) {
                    removalBox.setChecked(false);
                }
                mPresenter.setSafeBroadcast(Constant.DEPLOY_CMD_VALUE_REMOVE);
            }
        });

        if (Utils.getIsFirstBootStates(getActivity())) {
            showPopView(view);
        }

        initContactView();
    }

    private void initContactView() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String contactData = PreferenceUtils.getPrefString(getContext(), PreferenceConstants.CONTACTS, "");
                Log.i(TAG, "contactData ： " + contactData);

                if (!TextUtils.isEmpty(contactData)) {
                    Gson gson = new Gson();
                    JsonParser jsonParser = new JsonParser();
                    JsonArray jsonElements = jsonParser.parse(contactData).getAsJsonArray();//获取JsonArray对象
                    for (JsonElement bean : jsonElements) {
                        ContactData bean1 = gson.fromJson(bean, ContactData.class);//解析
                        mContactList.add(bean1);
                    }
                }

                mContactAdapter = new ContactAdapter(getContext(), mContactList);
                contactListView.setAdapter(mContactAdapter);
                mContactAdapter.setListener(new ContactAdapter.OnClickAdapter() {
                    @Override
                    public void onHeadClick(int i) {
                        showPhoneDialog(i, mContactList.get(i).getName(), mContactList.get(i).getTelephone(), mContactList.get(i).getContact_id());
                    }

                    @Override
                    public void onAddClick(int i) {
                        showPhoneDialog(i, "", "", "");
                    }

                    @Override
                    public void onPhoneClick(int i) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mContactList.get(i).getTelephone()));//跳转到拨号界面，同时传递电话号码
                        startActivity(intent);
                    }

                    @Override
                    public void onMessageClick(int i) {
                        String action = Intent.ACTION_SENDTO;
                        Uri uri = Uri.parse("smsto:" + mContactList.get(i).getTelephone());
                        Intent intent = new Intent(action, uri);
                        intent.putExtra("sms_body", "");
                        startActivity(intent);
                    }

                    @Override
                    public void onWxClick(int i) {
                        Utils.startWx(getContext());
                    }
                });
            }
        }, 300);

    }

    @Override
    protected void initData() {

        registerReceiver();
//        List<Schedule> list = ScheduleUtil.getTodaySchedule(getActivity());
//        Logger.i(TAG, "schedule list : " + new Gson().toJson(list));

//        mSchedleAdapter = new SchedleAdapter(getContext(), list);
//        schedleListView.setAdapter(mSchedleAdapter);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSafeActionList();

//                getWeatherData();
            }
        }, 600);
    }

    @Override
    protected void onLazyLoad() {
    }

    PhonetDialog phonetDialog;

    int button_location;

    void showPhoneDialog(int location, String name, String phone, String contact_id) {
        this.button_location = location;
        phonetDialog = new PhonetDialog(getContext(), name, phone, contact_id)
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ContactData bean = new ContactData(phonetDialog.getNameText(), phonetDialog.getPhoneText(), phonetDialog.getContact_id());
                        ContactData[] array = new ContactData[3];
                        for (int i = 0; i < mContactList.size(); i++) {
                            array[i] = mContactList.get(i);
                        }
                        switch (button_location) {
                            case TOP:
                                array[0] = bean;
                                break;
                            case CENTER:
                                array[1] = bean;
                                break;
                            case BOTTOM:
                                array[2] = bean;
                                break;
                        }
                        mContactList = Arrays.asList(array);
                        mContactAdapter.setDataList(mContactList);
                        phonetDialog.dismiss();
                        Log.i(TAG, "showPhoneDialog contact data : " + new Gson().toJson(mContactList));
                        PreferenceUtils.setPrefString(getActivity(), PreferenceConstants.CONTACTS, new Gson().toJson(mContactList));
                    }
                }).setContactButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.openContact(MainFragment.this);
                    }
                }).showDialog();
    }


    public void refreshScheduleList() {
//        List<Schedule> list = ScheduleUtil.getTodaySchedule(getActivity());
//        mSchedleAdapter.setDataList(list);
    }

    public void getSafeList() {
        normalLine.setVisibility(View.VISIBLE);
        jointLine.setVisibility(View.INVISIBLE);
        ArrayList<HomeProtectZoneStatusBean> list = HomeSecurityUtil.getAllHPZoneStatus(getActivity());
        if (mWatchAdapter == null) {
            mWatchAdapter = new WatchAdapter(getContext(), list);
        }
        mWatchAdapter.setDataList(list);
        watchListView.setAdapter(mWatchAdapter);
        showEmpty(list);
    }

    public void getSafeActionList() {
        normalLine.setVisibility(View.INVISIBLE);
        jointLine.setVisibility(View.VISIBLE);
        ArrayList<HomeSecurityCombineZoneStatusBean> list = HomeSecurityUtil.getAllCombineZoneDatas(getActivity());
        if (mSafeActionAdapter == null) {
            mSafeActionAdapter = new SafeActionAdapter(getActivity(), list);
        }
        mSafeActionAdapter.setDataList(list);
        watchListView.setAdapter(mSafeActionAdapter);
        showEmpty(list);
    }

    public void showEmpty(List list) {
        if (list == null || list.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.INVISIBLE);
        }
    }

    public void refreshHouseStatus(int state) {
        Log.i(TAG, "refreshHouseStatus state : " + state);
        switch (state) {
            case Constant.DEPLOY_CMD_VALUE_REMOVE:
                removalBox.setChecked(true);
                inHomeBox.setChecked(false);
                outHomeBox.setChecked(false);
                break;
            case Constant.DEPLOY_CMD_VALUE_HOME:
                inHomeBox.setChecked(true);
                removalBox.setChecked(false);
                break;
            case Constant.DEPLOY_CMD_VALUE_OUTER:
                outHomeBox.setChecked(true);
                removalBox.setChecked(false);
                break;
            case Constant.DEPLOY_CMD_VALUE_HOME_FAIL:
                inHomeBox.setChecked(false);
                break;
            case Constant.DEPLOY_CMD_VALUE_OUTER_FAIL:
                outHomeBox.setChecked(false);
                break;
            case Constant.DEPLOY_CMD_VALUE_REMOVE_FAIL:
                removalBox.setChecked(false);
                break;
        }
    }

    private int ncount = 0;

    private void showPopView(View view) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // 获取当面界面的主布局
        View rootView = getActivity().getWindow().getDecorView().getRootView();
        View popView = inflater.inflate(R.layout.popwindows_noties, null);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        final PopupWindow pop = new PopupWindow(popView, width, height);

        final ImageView noties = popView.findViewById(R.id.noties);
        noties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (ncount) {
                    case 0:
                        noties.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.noties2));
                        break;
                    case 1:
                        noties.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.noties3));
                        break;
                    case 2:
                        pop.dismiss();
                        break;
                }
                ncount++;
            }
        });
        //设置全屏幕覆盖状态栏
        Utils.fitPopupWindowOverStatusBar(pop, true);
        // 需要设置一下此参数，点击外边可消失
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        //在当前界面正下方显示
        pop.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateWeather(WeatherBean bean) {
        if (bean.getCode() == 2) {
            if (Utils.getLangugeType(getActivity()) == 2) {
                cityTv.setText(Utils.transformPinYin(bean.getData().getCity()));
            } else {
                cityTv.setText(bean.getData().getCity());
            }

            temperatureTv.setText(TextUtils.isEmpty(bean.getData().getWendu()) ? "" : bean.getData().getWendu() + "°");
            descripTv.setText(bean.getData().getType() + " " +
                    (bean.getData().getHigh().startsWith("高温") ? bean.getData().getHigh().substring(3) : bean.getData().getHigh()) + "/" +
                    (bean.getData().getLow().startsWith("低温") ? bean.getData().getLow().substring(3) : bean.getData().getLow()));

            airTv.setText(getString(R.string.air_quality) + bean.getData().getQuality());

            String weather = bean.getData().getType();
            if (weather.startsWith("晴")) {
                weatherIcon.setImageResource(R.mipmap.icon_sun);
            } else if (weather.startsWith("阴")) {
                weatherIcon.setImageResource(R.mipmap.icon_yintian);
            } else if (weather.startsWith("多云")) {
                weatherIcon.setImageResource(R.mipmap.icon_duoyun);
            } else if (weather.startsWith("小雨")) {
                weatherIcon.setImageResource(R.mipmap.icon_yu);
            } else if (weather.startsWith("阵雨")) {
                weatherIcon.setImageResource(R.mipmap.icon_qingyu);
            } else if (weather.startsWith("雷阵雨")) {
                weatherIcon.setImageResource(R.mipmap.icon_leizhenyu);
            } else if (weather.contains("雪")) {
                weatherIcon.setImageResource(R.mipmap.icon_xue);
            } else if (weather.contains("雾")) {
                weatherIcon.setImageResource(R.mipmap.icon_wu);
            }
        } else {
            Logger.e(TAG, "getWeather fail info : " + bean.getMsg());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri uri = data.getData();
            String[] contact = Utils.getPhoneContacts(getActivity(), uri);
            if (contact != null) {
                String name = contact[0];//姓名
                String number = contact[1];//手机号
                String contact_id = contact[2];
                number = Utils.formatPhoneNum(number);
                if (phonetDialog != null) {
                    phonetDialog.setNameEdit(name);
                    phonetDialog.setPhoneEdit(number);
                    phonetDialog.setContact_id(contact_id);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.call_icon:
                Intent dialIntent = new Intent(Intent.ACTION_CALL_BUTTON);//跳转到拨号界面
                startActivity(dialIntent);
                break;
            case R.id.health_icon:
                Intent HealthIntent = new Intent(Intent.ACTION_MAIN);
                ComponentName cn = new ComponentName("com.vtech.vhealth", "com.vtech.vhealth.function.main.MainHealthActivity");
                HealthIntent.setComponent(cn);
                try {
                    startActivity(HealthIntent);
                } catch (Exception e) {
                    //TODO: TOAST   提示敬请期待...
                }
                break;
            case R.id.alarm_icon:
                Intent ScheduleIntent = new Intent(Intent.ACTION_MAIN);
                ScheduleIntent.setComponent(new ComponentName("com.vtech.schedule", "com.vtech.schedule.ScheduleActivity"));
                try {
                    startActivity(ScheduleIntent);
                } catch (Exception e) {
                    //TODO: TOAST   提示敬请期待...
                }
                break;
            case R.id.safety_icon:
                Intent SecIntent = new Intent(Intent.ACTION_MAIN);
                SecIntent.setComponent(new ComponentName("com.vtech.homesecurity", "com.vtech.homesecurity.ui.activity.SafetyForDebugActivity"));
                try {
                    startActivity(SecIntent);
                } catch (Exception e) {
                    //TODO: TOAST   提示敬请期待...
                }
                break;
            case R.id.time_tv:
            case R.id.date_tv:
            case R.id.week_tv:
                StringBuilder sb = new StringBuilder();
                sb.append(dateTv.getText().toString().trim());
                sb.append(weekTv.getText().toString().trim());
                sb.append(timeTv.getText().toString().trim());
                player(sb.toString());
                break;
            case R.id.weather_icon:
            case R.id.temperature_tv:
            case R.id.descrip_tv:
            case R.id.air_tv:
                playerWeather();
                break;
            default:
                break;
        }
    }

    public void player(String str) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.player(str);
    }

    void playerWeather() {
        StringBuilder sb = new StringBuilder();
        sb.append(descripTv.getText().toString().trim());
        sb.append(airTv.getText().toString().trim());
        sb.append(temperatureTv.getText().toString().trim());
        player(sb.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(mainReceiver);
    }

    MainBroadcastReceiver mainReceiver;

    void registerReceiver() {
        mainReceiver = new MainBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SAFE_HOME_NOTIFY);
        filter.addAction(ACTION_SAFE_COMBINE_NOTIFY);
        filter.addAction(ACTION_SAFE_STATUS_NOTIFY);
        filter.addAction(ACTION_ASSISTANT_PLAY_WEATHER);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(mainReceiver, filter);
    }

    public static final String ACTION_SAFE_HOME_NOTIFY = "com.vtech.homesecurity.home.notify";
    //联合布防广播
    public static final String ACTION_SAFE_COMBINE_NOTIFY = "com.vtech.homesecurity.combine.notify";

    public static final String ACTION_SAFE_STATUS_NOTIFY = "com.vtech.homesecurity.deploy_status_notify";

    public static final String ACTION_ASSISTANT_PLAY_WEATHER = "com.vtech.voiceassistant.event.weather";

    public class MainBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "MainBroadcastReceiver is start! ----------------- action : " + intent.getAction());
            if (intent.getAction().equals(ACTION_SAFE_HOME_NOTIFY)) {
                getSafeList();
            }

            if (intent.getAction().equals(ACTION_SAFE_COMBINE_NOTIFY)) {
                getSafeActionList();
            }

            if (intent.getAction().equals(ACTION_SAFE_STATUS_NOTIFY)) {
                int status = intent.getIntExtra("DEPLOY_STATUS", -1);
                refreshHouseStatus(status);
            }

            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (TextUtils.isEmpty(App.getInstance().getToken())) {
                    APIService.getInstance().registDevice(context);
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.getWeather(MainFragment.this);//刷新天气
                    }
                }, 300);
            }

            if (intent.getAction().equals(ACTION_ASSISTANT_PLAY_WEATHER)) {
                playerWeather();
            }
        }
    }
}
