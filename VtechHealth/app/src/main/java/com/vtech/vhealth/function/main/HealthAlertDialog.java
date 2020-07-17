package com.vtech.vhealth.function.main;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vtech.vhealth.R;
import com.vtech.vhealth.constant.AnyKey;
import com.vtech.vhealth.function.utils.SpUtils;


public class HealthAlertDialog extends Dialog {

    protected Context mContext;


    private TextView textView;

    public HealthAlertDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public HealthAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected HealthAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    private HealthAlertDialog initView(Context context) {
        mContext = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setBackgroundDrawableResource(R.mipmap.transparent_bg);
        Window window = this.getWindow();
        LayoutParams mLayoutParams = window.getAttributes();
        mLayoutParams.alpha = 1f;
        mLayoutParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.gravity = Gravity.CENTER;
        window.setAttributes(mLayoutParams);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_tips, null);

        textView = dialogView.findViewById(R.id.tv_know);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                SpUtils.set(AnyKey.KEY_HEALTH_HINT,false);
            }
        });

        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);

        return this;
    }


    public HealthAlertDialog showDialog() {
        show();
        return this;
    }
}
