package com.vtech.app.moudle.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vtech.app.R;
import com.vtech.app.data.bean.ContactData;
import com.vtech.app.util.Utils;

import java.util.ArrayList;
import java.util.List;




public class ContactAdapter extends BaseAdapter {
    private Context context;
    private List<ContactData> mDataList = new ArrayList<>();

    public ContactAdapter(Context context) {
        this.context = context;
        /*for (int i = 0; i < 3; i++) {
            mDataList.add(new ContactData());
        }*/
    }

    public ContactAdapter(Context context, List<ContactData> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    public void setDataList(List<ContactData> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null || view.getTag() == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_contact, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (mDataList.size() > i && mDataList.get(i) != null
                && (!TextUtils.isEmpty(mDataList.get(i).getName()) && !TextUtils.isEmpty(mDataList.get(i).getTelephone()))) {

            ContactData bean = mDataList.get(i);

            holder.addImg.setVisibility(View.GONE);

            holder.headImg.setVisibility(View.VISIBLE);
            holder.nameTv.setVisibility(View.VISIBLE);
            holder.phoneImg.setVisibility(View.VISIBLE);
            holder.messageImg.setVisibility(View.VISIBLE);
            holder.wxImg.setVisibility(View.VISIBLE);
            holder.nameTv.setText(TextUtils.isEmpty(bean.getName()) ? "" : bean.getName());

            if (!TextUtils.isEmpty(bean.getContact_id()) && Utils.getContactHeadImg(context, bean.getContact_id()) != null) {
                holder.headImg.setImageBitmap(Utils.getContactHeadImg(context, bean.getContact_id()));
            } else {
                holder.headImg.setImageResource(R.mipmap.ic_default_contact);
            }
        } else {
            holder.addImg.setVisibility(View.VISIBLE);

            holder.headImg.setVisibility(View.GONE);
            holder.nameTv.setVisibility(View.GONE);
            holder.phoneImg.setVisibility(View.GONE);
            holder.messageImg.setVisibility(View.GONE);
            holder.wxImg.setVisibility(View.GONE);
        }

        holder.headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onHeadClick(i);
                }
            }
        });

        holder.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onAddClick(i);
                }
            }
        });

        holder.phoneImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onPhoneClick(i);
                }
            }
        });

        holder.messageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onMessageClick(i);
                }
            }
        });

        holder.wxImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onWxClick(i);
                }
            }
        });


        return view;
    }

    class ViewHolder {

        ImageView headImg;

        TextView nameTv;

        ImageView phoneImg;

        ImageView messageImg;

        ImageView wxImg;

        AppCompatImageView addImg;

        ViewHolder(View view) {
            headImg = view.findViewById(R.id.head);
            nameTv = view.findViewById(R.id.name);
            phoneImg = view.findViewById(R.id.phone);
            messageImg = view.findViewById(R.id.message);
            wxImg = view.findViewById(R.id.wx);
            addImg = view.findViewById(R.id.add);
        }
    }

    private OnClickAdapter listener;

    public void setListener(OnClickAdapter listener) {
        this.listener = listener;
    }

    public interface OnClickAdapter {
        void onHeadClick(int i);

        void onAddClick(int i);

        void onPhoneClick(int i);

        void onMessageClick(int i);

        void onWxClick(int i);

    }
}
