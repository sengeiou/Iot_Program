
package com.vtech.vhealth.function.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.vtech.vhealth.R;


/**
 * 页面的顶部。
 * 注意，该类包含元素：左边返回icon 文字、中间标题、右边提示文字、右边图片按钮（参见include_header.xml）。不符合这种顶部的UI自己写布局，符合的都用这个。
 * 默认只显示返回按钮和中间标题。布局文件添加见类头部注释
 */
public class CustomTitleBar extends RelativeLayout {

    private ImageView iv_left, iv_logo,  v_right;
    private View layout_left, layout_right, layout_close;

    private TextView tv_left, tv_center, tv_right;
    private OnClickListener onClickLeftView, onClickRightView, onClickRightText, onClickLeftClose, onClickCenterText;
    private static String defaultTitle;//默认标题

    public CustomTitleBar(Context context) {
        super(context);
        init();
    }

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_title_bar, null);
        iv_left =   view.findViewById(R.id.iv_left);
        tv_left =   view.findViewById(R.id.tv_left);
        iv_logo =  view.findViewById(R.id.iv_logo);
        layout_left = view.findViewById(R.id.layout_left);
        layout_right = view.findViewById(R.id.layout_right);
        layout_close = view.findViewById(R.id.layout_close);
        v_right = view.findViewById(R.id.iv_right);
        tv_center =   view.findViewById(R.id.tv_center);
        tv_right =   view.findViewById(R.id.tv_right);
        layout_right.setVisibility(View.GONE);
        tv_right.setVisibility(View.GONE);
        if (defaultTitle != null) {
            setTextCenter(defaultTitle);
        }
        layout_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickLeftView != null)
                    onClickLeftView.onClick(v);
            }
        });
        layout_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickRightView != null)
                    onClickRightView.onClick(v);
            }
        });
        tv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickRightText != null)
                    onClickRightText.onClick(v);
            }
        });
        layout_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickLeftClose != null) {
                    onClickLeftClose.onClick(v);
                }
            }
        });
        iv_logo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickCenterText != null) {
                    onClickCenterText.onClick(v);
                }
            }
        });
        addView(view);
    }

    /**
     * 设置全局默认标题，如果没有设置默认，则默认为teamlib，所以需要在AppContext中设置默认标题
     */
    public static void setDefaultTitle(String defaultTitle) {
        CustomTitleBar.defaultTitle = defaultTitle;
    }

    /**
     * 设置左上角返回按钮的图片
     */
    public void setLeftImageResource(int resId) {
        iv_left.setImageResource(resId);
    }

    /**
     * 设置标题左边LOGO图片
     */
    public void setLogoImageResource(int resId) {
        iv_logo.setImageResource(resId);
        tv_center.setEms(4);
    }

    /**
     * 设置返回文字
     *
     * @param text
     */
    public void setTextLeft(CharSequence text) {
        if (text != null) {
            tv_left.setText(Html.fromHtml(text.toString().trim()));
        }
    }

    /**
     * 隐藏返回按钮
     */
    public void hideLeftView() {
        layout_left.setVisibility(View.GONE);
    }

    public TextView getLeftText() {
        return tv_left;
    }

    /**
     * 设置返回文字
     *
     * @param textResId 文字id
     */
    public void setTextLeft(int textResId) {
        setTextLeft(getContext().getString(textResId).trim());
    }

    /**
     * 设置返回文字颜色
     *
     * @param textLeftColor 颜色值ID
     */
    public void setTextLeftColor(int textLeftColor) {
        tv_left.setTextColor(textLeftColor);
    }

    /**
     * 设置显隐返回文字
     *
     * @param hide true 隐藏
     */
    public void hideTextLeft(boolean hide) {
        tv_left.setVisibility(hide ? GONE : VISIBLE);
    }

    /**
     * 设置中间标题文字
     *
     * @param text
     */
    public void setTextCenter(CharSequence text) {
        if (text != null) {
            tv_center.setText(Html.fromHtml(text.toString().trim()));
        }
    }

    /**
     * 设置中间标题文字
     *
     * @param textResId
     */
    public void setTextCenter(int textResId) {
        setTextCenter(getContext().getString(textResId).trim());
    }

    /**
     * 设置中间文字大小
     *
     * @param size
     */
    public void setTextCenterSize(int size) {
        tv_center.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 标题右边图片
     *
     * @param resId
     */
    public void setTextCenterDrawableRight(@DrawableRes int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0,  dip2px(getContext(), 14),  dip2px(getContext(), 14));
        tv_center.setCompoundDrawables(null, null, drawable, null);
    }

    /**
     * 标题左右两边图片
     *
     * @param leftResId
     * @param rightResId
     */
    public void setTextCenterDrawableLeftRight(@DrawableRes int leftResId, @DrawableRes int rightResId) {
        Drawable drawable_l = getResources().getDrawable(leftResId);
        drawable_l.setBounds(0, 0,  dip2px(getContext(), 14),  dip2px(getContext(), 14));
        Drawable drawable_r = getResources().getDrawable(rightResId);
        drawable_r.setBounds(0, 0,  dip2px(getContext(), 14),  dip2px(getContext(), 14));
        tv_center.setCompoundDrawables(drawable_l, null, drawable_r, null);
    }

    /**
     * 设置右边提示文字
     */
    public void setTextRight(String text) {
        if (TextUtils.isEmpty(text)) {
            tv_right.setVisibility(View.GONE);
        } else {
            tv_right.setText(text.trim());
            tv_right.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置右边提示文字
     */
    public void setTextRight(int textResId) {
        setTextRight(getContext().getString(textResId).trim());
    }

    /**
     * 设置右边提示文字颜色
     */
    public void setTextRightColor(int color) {
        tv_right.setTextColor(color);
    }

    /**
     * 设置右上角按钮图片
     */
    public void setImageRight(int resId) {
        v_right.setImageResource(resId);
    }

    /**
     * 点击左边图标，一般是返回按钮事件
     */
    public void setOnClickLeftViewListener(OnClickListener onClickLeftView) {
        this.onClickLeftView = onClickLeftView;
    }

    /**
     * 点击右边图标，例如删除按钮事件
     */
    public void setOnClickRightViewListener(OnClickListener onClickRightView) {
        this.onClickRightView = onClickRightView;
    }

    /**
     * 点击中间文字事件
     */
    public void setOnClickCenterTextListener(OnClickListener onClickCenterText) {
        this.onClickCenterText = onClickCenterText;
        tv_center.setOnClickListener(onClickCenterText);
    }

    /**
     * 点击右边文字，例如优惠券说明等
     */
    public void setOnClickRightTextListener(OnClickListener onClickRightText) {
        this.onClickRightText = onClickRightText;
    }

    /**
     * 对于webView，显示关闭按钮，直接关闭activity
     */
    public void setOnClickLeftCloseListener(OnClickListener onClickLeftClose) {
        this.onClickLeftClose = onClickLeftClose;
    }

    /**
     * 对于webView，显示关闭按钮，直接关闭activity，点击返回按钮则web.goback
     */
    public void showLeftCloseView() {
        layout_close.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏右上角文字
     */
    public void hideRightText() {
        tv_right.setVisibility(View.GONE);
    }

    /**
     * 显示右上角文字
     */
    public void showRightText() {
        tv_right.setVisibility(View.VISIBLE);
    }


    public TextView getRightText() {
        return tv_right;
    }

    /**
     * 显示右上角图标按钮
     */
    public void showRightView() {
        layout_right.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏右上角图标按钮
     */
    public void hideRightView() {
        layout_right.setVisibility(View.GONE);
    }


    /**
     * 根据dp大小得到像素大小
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
