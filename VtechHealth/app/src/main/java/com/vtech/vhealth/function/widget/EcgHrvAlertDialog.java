package com.vtech.vhealth.function.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.vtech.vhealth.R;


public class EcgHrvAlertDialog extends Dialog {

    protected Context mContext;

    protected LayoutParams mLayoutParams;

    private Button confirmBtn;

    private AppCompatTextView titleTv;

    private AppCompatTextView contentTv;

    private boolean showTitle = false;

    private boolean showMessage = false;

    public EcgHrvAlertDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public EcgHrvAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected EcgHrvAlertDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    private EcgHrvAlertDialog initView(Context context) {
        mContext = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setBackgroundDrawableResource(R.mipmap.transparent_bg);
        Window window = this.getWindow();
        mLayoutParams = window.getAttributes();
        mLayoutParams.alpha = 1f;
        mLayoutParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.gravity = Gravity.CENTER;
        window.setAttributes(mLayoutParams);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_ecg_hrv, null);
//        titleTv = (AppCompatTextView) dialogView.findViewById(R.id.title);
//        contentTv = (AppCompatTextView) dialogView.findViewById(R.id.content);
        confirmBtn = (Button) dialogView.findViewById(R.id.confirm);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);

        return this;
    }

    public EcgHrvAlertDialog setTitle(String message) {
        showTitle = true;
        changeViewState();
        titleTv.setText(message);
        return this;
    }

    public EcgHrvAlertDialog setMessage(String message) {
        showMessage = true;
        changeViewState();
        contentTv.setText(message);
        return this;
    }

    public EcgHrvAlertDialog setMessage(String message, int gravity) {
        showMessage = true;
        changeViewState();
        contentTv.setText(message);
        contentTv.setGravity(gravity);
        return this;
    }

    void changeViewState() {
        if (showTitle) {
            titleTv.setVisibility(View.VISIBLE);
        } else {
            titleTv.setVisibility(View.GONE);
        }
        if (showMessage) {
            contentTv.setVisibility(View.VISIBLE);
        } else {
            contentTv.setVisibility(View.GONE);
        }
    }

    public EcgHrvAlertDialog setPositiveButton(String str, final View.OnClickListener listener) {
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

    public EcgHrvAlertDialog showDialog() {
        show();
        return this;
    }
}
