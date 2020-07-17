package com.vtech.app.moudle.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;

import android.widget.Toast;
import com.vtech.app.R;
import com.vtech.app.largeuidb.Constants;
import com.vtech.app.largeuidb.dbutils.ItemUtil;
import com.vtech.app.largeuidb.largeuibean.LargeUIItem;
import com.vtech.app.moudle.largeui.dialer.DialtactsActivity;
import com.vtech.app.moudle.largeui.PickAppActivity;
import com.vtech.app.moudle.largeui.SubItemActivity;
import com.vtech.app.moudle.largeui.sos.SOSActivity;
import com.vtech.app.moudle.largeui.contacts.ContactsActivity;
import com.vtech.app.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ApplicationAdapterLarge extends RecyclerView.Adapter<ApplicationAdapterLarge.VH> {

    private static final String TAG = "ApplicationAdapterLarge";
    Context context;

    private int mPageIndex;
    private PackageManager pm;
    private ItemUtil mItemUtil;
    private List<LargeUIItem> largeUIItemList = new ArrayList<>();
    private boolean fullScreen = false;

    public static class VH extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        View layout;

        public VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);
            layout = itemView.findViewById(R.id.item_layout);
        }
    }

    public ApplicationAdapterLarge(Context context, int pageIndex, ItemUtil mItemUtil, List<LargeUIItem> largeUIItemList) {
        this.context = context;
        this.mPageIndex = pageIndex;
        this.mItemUtil = mItemUtil;
        this.largeUIItemList = largeUIItemList;
        pm = context.getPackageManager();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_application_large_ui, viewGroup, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH vh, final int i) {
        final int cellX = i / 2;
        final int cellY = i % 2;

        GradientDrawable gradientDrawable = (GradientDrawable) vh.layout.getBackground();
        gradientDrawable.setColor(context.getResources().getColor(R.color.item_app_color_mode_0));
        if (!fullScreen && i == largeUIItemList.size()) {
            vh.icon.setImageResource(R.mipmap.add_new_lui);
            vh.name.setText("");
            gradientDrawable.setColor(context.getResources().getColor(R.color.item_app_color_mode_3));
            vh.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context != null) {
                        Intent intent = new Intent(context, PickAppActivity.class);
                        Bundle posBundle = new Bundle();
                        posBundle.putInt("PAGE_INDEX", mPageIndex);
                        posBundle.putInt("CELLX", cellX);
                        posBundle.putInt("CELLY", cellY);
                        intent.putExtras(posBundle);
                        context.startActivity(intent);
                    }
                }
            });
            return;
        }
        final LargeUIItem item = largeUIItemList.get(i);
        Log.i("ApplicationAdapterLarge", "onBindViewHolder, itemName: " + item.getItemName() + ", type: " + item.getType());
        //vh.name.setText(item.getItemName());
        switch (item.getType()) {
            case Constants.ITEM_TYPE_SOS:
                vh.icon.setImageResource(R.mipmap.sos_lui);
                vh.name.setText(R.string.sos_name);
                gradientDrawable.setColor(context.getResources().getColor(R.color.item_app_color_mode_0));
                vh.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context != null) {
                            Intent intent = new Intent(context, SOSActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(R.anim.push_bottom_in, 0);
                        }
                    }
                });
                break;
            case Constants.ITEM_TYPE_DIALER:
                vh.icon.setImageResource(R.mipmap.phone_lui);
                vh.name.setText(R.string.phone_name);
                gradientDrawable.setColor(context.getResources().getColor(R.color.item_app_color_mode_1));
                vh.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context != null) {
                            Intent intent = new Intent(context, DialtactsActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(R.anim.push_bottom_in, 0);
                        }
                    }
                });
                break;
            case Constants.ITEM_TYPE_CHAT:
                vh.icon.setImageResource(R.mipmap.chat_lui);
                vh.name.setText(R.string.chat_name);
                gradientDrawable.setColor(context.getResources().getColor(R.color.item_app_color_mode_2));
                vh.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context != null) {
                            Intent intent = new Intent(context, SubItemActivity.class);
                            context.startActivity(intent);
                        }
                    }
                });
                break;
            case Constants.ITEM_TYPE_HEALTH:
                vh.icon.setImageResource(R.mipmap.health_lui);
                vh.name.setText(R.string.health_name);
                gradientDrawable.setColor(context.getResources().getColor(R.color.item_app_color_mode_3));
                break;
            case Constants.ITEM_TYPE_MSG:
                vh.icon.setImageResource(R.mipmap.msg_lui);
                vh.name.setText(R.string.msg_name);
                gradientDrawable.setColor(context.getResources().getColor(R.color.item_app_color_mode_4));
                break;
            case Constants.ITEM_TYPE_NEAR:
                vh.icon.setImageResource(R.mipmap.nearby_lui);
                vh.name.setText(R.string.nearby_name);
                gradientDrawable.setColor(context.getResources().getColor(R.color.item_app_color_mode_5));
                break;
            case Constants.ITEM_TYPE_IOT_ALARM:
                vh.icon.setImageResource(R.mipmap.safety_lui);
                gradientDrawable.setColor(context.getResources().getColor(R.color.item_app_color_mode_0));
                vh.name.setText(R.string.iot_alarm_name);
                break;
            case Constants.ITEM_TYPE_SETTINGS:
                vh.icon.setImageResource(R.mipmap.settings_lui);
                vh.name.setText(R.string.settings_name);
                gradientDrawable.setColor(context.getResources().getColor(R.color.item_app_color_mode_1));
                break;
            case Constants.ITEM_TYPE_CONTACTS:
                vh.icon.setImageResource(R.mipmap.contacts_lui);
                vh.name.setText(R.string.contacts_name);
                gradientDrawable.setColor(context.getResources().getColor(R.color.item_app_color_mode_2));
                vh.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context != null) {
                            Intent intent = new Intent(context, ContactsActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(R.anim.push_bottom_in, 0);
                        }
                    }
                });
                break;
            default:
                try {
                    if (!TextUtils.isEmpty(item.getPackageName())) {
                        ApplicationInfo appInfo = pm.getApplicationInfo(item.getPackageName(), PackageManager.GET_META_DATA);
                        /*if (item.getPackageName().contains("com.android.settings")) {
                            vh.icon.setImageResource(R.mipmap.settings_lui);
                        } else */
                        {
                            vh.icon.setImageDrawable(pm.getApplicationIcon(appInfo));
                        }
                        vh.name.setText(pm.getApplicationLabel(appInfo));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;

        }
        if (item.getIsApp()) {
            final String pkgName = item.getPackageName();
            vh.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context != null) {
                        Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
                        if(intent == null ){
                            ToastUtils.show(context,"应用未安装!", Toast.LENGTH_LONG);
                            return;
                        }
                        context.startActivity(intent);
                    }
                }
            });
        }
        vh.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (item.getIsModify() && item.getIsMove()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DeleteDialogStyle);
                                    LayoutInflater inflater = LayoutInflater.from(context);
                                    View view1 = inflater.inflate(R.layout.dialog_confirm_delete_layout, null);
                                    builder.setView(view1);
                                    final AlertDialog dialog = builder.show();
                                    view1.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                    view1.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            mItemUtil.delete(item);
                                        }
                                    });
                                }
                            }
                        };
                        new Handler(Looper.getMainLooper()).post(runnable);
                    }
                }.start();
                return true;
            }
        });

    }

//    public void updateList(List<LargeUIItem> largeUIItemList) {
//        Log.d(TAG, " largeUIItemList 1 is " + largeUIItemList);
//        this.largeUIItemList.clear();
//        Log.d(TAG, " largeUIItemList 2 is " + this.largeUIItemList.size());
//        this.largeUIItemList.addAll(largeUIItemList);
//        Log.d(TAG, " largeUIItemList 3 is " + this.largeUIItemList.size());
//        notifyDataSetChanged();
//
//    }

    @Override
    public int getItemCount() {
        this.largeUIItemList = mItemUtil.getListByScreenNum(mPageIndex);;
        int curPageAppCnt = mItemUtil.getListByScreenNum(mPageIndex).size();
        Log.i("ApplicationAdapterLarge", "curPageAppCnt: " + curPageAppCnt);
        if (curPageAppCnt < Constants.PAGE_APP_MAX_COUNT) {
            fullScreen = false;
            return curPageAppCnt + 1;
        } else {
            fullScreen = true;
            return Constants.PAGE_APP_MAX_COUNT;
        }
    }
}
