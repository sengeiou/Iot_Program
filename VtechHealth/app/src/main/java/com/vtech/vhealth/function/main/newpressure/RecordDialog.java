package com.vtech.vhealth.function.main.newpressure;

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
import com.vtech.vhealth.function.utils.ToastUtil;


public class RecordDialog extends Dialog {

    protected Context mContext;


    private TextView tvShare;
    private TextView tvSure;

    public RecordDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    protected RecordDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    private RecordDialog initView(Context context) {
        mContext = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setBackgroundDrawableResource(R.mipmap.transparent_bg);
        Window window = this.getWindow();
        LayoutParams mLayoutParams = window.getAttributes();
        mLayoutParams.alpha = 1f;
        mLayoutParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.gravity = Gravity.CENTER;
        window.setAttributes(mLayoutParams);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_record, null);

        tvShare = dialogView.findViewById(R.id.tv_share);
        tvSure = dialogView.findViewById(R.id.tv_sure);
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ToastUtil.showShort("分享");
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ToastUtil.showShort("确定");
            }
        });
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);

        return this;
    }


    public RecordDialog showDialog() {
        show();
        return this;
    }
}
