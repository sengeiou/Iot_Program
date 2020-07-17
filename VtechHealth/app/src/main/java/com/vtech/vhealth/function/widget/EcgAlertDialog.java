package com.vtech.vhealth.function.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vtech.vhealth.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EcgAlertDialog extends Dialog {

    protected Context mContext;

    protected LayoutParams mLayoutParams;

    private TextView confirmBtn;

    private boolean showTitle = false;

    private boolean showMessage = false;

    ListView listView;

    public EcgAlertDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public EcgAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected EcgAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    private EcgAlertDialog initView(Context context) {
        mContext = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setBackgroundDrawableResource(R.mipmap.transparent_bg);
        Window window = this.getWindow();
        mLayoutParams = window.getAttributes();
        mLayoutParams.alpha = 1f;
        mLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.gravity = Gravity.CENTER;
        window.setAttributes(mLayoutParams);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_ecg_new, null);
//        titleTv = (AppCompatTextView) dialogView.findViewById(R.id.title);
//        contentTv = (AppCompatTextView) dialogView.findViewById(R.id.content);

        listView = dialogView.findViewById(R.id.list_view);

        SimpleAdapter myAdapter = new SimpleAdapter(context, getData(context), R.layout.item_simple_ecg,
                new String[]{"name", "descrip"}, new int[]{R.id.name, R.id.descrip});
        listView.setAdapter(myAdapter);

        confirmBtn =  dialogView.findViewById(R.id.confirm);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);

        return this;
    }

    String[] names = {"AFib（房颤）", "heart age（心脏年龄）", "RHR（稳健心率）",
            "HRV（心率变异性）", "mood（情绪）", "stress（压力）"};

    String[] descrips = {"-1-无数据、0-正常、1-房颤", "衡量心脏健康趋势的参数之一", "心率的稳健估计，参考值40-170bmp",
            "逐次心跳周期差异，参考值1-300ms", "1-100，数值越低越平静", "1-100，数值越低越平静"};

    private List<? extends Map<String, ?>> getData(Context context) {
        names = context.getResources().getStringArray(R.array.heart_data_mean_title);
        descrips = context.getResources().getStringArray(R.array.heart_data_mean_describe);
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", names[i]);
            map.put("descrip", descrips[i]);
            list.add(map);
        }
        return list;
    }


    public EcgAlertDialog setPositiveButton(String str, final View.OnClickListener listener) {
        confirmBtn.setText(str);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                listener.onClick(view);
            }
        });
        return this;
    }

    public EcgAlertDialog showDialog() {
        show();
        return this;
    }
}
