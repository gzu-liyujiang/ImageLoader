/*
 * Copyright (c) 2013-present, 贵州纳雍穿青人李裕江<1032694760@qq.com>, All Rights Reserved.
 */

package com.github.gzuliyujiang.imageloader;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/**
 * Created by liyujiang on 2021/3/6.
 */
final class GlideCircleTrans extends BitmapTransformation {
    private static final String ID = GlideCircleTrans.class.getName();
    private static final byte[] ID_BYTES = ID.getBytes(Key.CHARSET);

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GlideCircleTrans;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

}
