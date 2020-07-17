package com.vtech.vhealth.function.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

public class ImageLoad {

    public static final void load(Context context, String url , ImageView imageView){
        Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
    }
}
