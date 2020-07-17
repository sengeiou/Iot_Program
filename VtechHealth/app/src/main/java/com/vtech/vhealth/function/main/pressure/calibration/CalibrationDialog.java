package com.vtech.vhealth.function.main.pressure.calibration;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.vtech.vhealth.R;


public class CalibrationDialog extends Dialog {

    protected Context mContext;

    protected LayoutParams mLayoutParams;

    private Button confirmBtn;

    private TextView titleTv;

    private boolean showTitle = false;


    public CalibrationDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CalibrationDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected CalibrationDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    private CalibrationDialog initView(Context context) {
        mContext = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setBackgroundDrawableResource(R.mipmap.transparent_bg);
        Window window = this.getWindow();
        mLayoutParams = window.getAttributes();
        mLayoutParams.alpha = 1f;
        mLayoutParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        mLayoutParams.gravity = Gravity.CENTER;
        window.setAttributes(mLayoutParams);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_calibration, null);
        titleTv = dialogView.findViewById(R.id.tv_title);
        confirmBtn =  dialogView.findViewById(R.id.confirm);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(dialogView);

        return this;
    }

    public CalibrationDialog setTitle(String message) {
        showTitle = true;
        changeViewState();
        titleTv.setText(message);
        return this;
    }



    void changeViewState() {
        if (showTitle) {
            titleTv.setVisibility(View.VISIBLE);
        } else {
            titleTv.setVisibility(View.GONE);
        }
    }

    public CalibrationDialog setPositiveButton(String str, final View.OnClickListener listener) {
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

    public CalibrationDialog showDialog() {
        show();
        return this;
    }
}
