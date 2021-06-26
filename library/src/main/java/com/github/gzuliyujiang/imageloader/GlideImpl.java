/*
 * Copyright (c) 2016-present 贵州纳雍穿青人李裕江<1032694760@qq.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.github.gzuliyujiang.imageloader;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.concurrent.Executors;

/**
 * 参见 https://github.com/bumptech/glide
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2018/8/28 14:56
 */
final class GlideImpl implements IImageLoader {
    private Context context;

    @Override
    public void setup(@NonNull Application application) {
        this.context = application;
    }

    @SuppressLint("CheckResult")
    @Override
    public <T> void display(@NonNull ImageView imageView, @NonNull T imageSource, @DrawableRes int placeholder) {
        RequestBuilder<?> builder = Glide.with(imageView).asBitmap();
        if (imageSource instanceof Integer) {
            builder.load((Integer) imageSource);
        } else {
            builder.load(imageSource.toString());
        }
        RequestOptions requestOptions = new RequestOptions();
        if (imageSource instanceof Integer) {
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        requestOptions.skipMemoryCache(false);
        requestOptions.placeholder(placeholder);
        requestOptions.fallback(placeholder);
        builder.apply(requestOptions).into(imageView);
    }

    @Override
    public void pause() {
        Glide.with(context).pauseRequests();
    }

    @Override
    public void resume() {
        Glide.with(context).resumeRequests();
    }

    @Override
    public void cleanMemory() {
        new Handler(Looper.getMainLooper()).post(() -> {
            //必须运行在UI线程
            Glide.get(context).clearMemory();
            System.gc();
        });
    }

    @Override
    public void clearCache() {
        cleanMemory();
        Executors.newSingleThreadExecutor().execute(() -> {
            //必须运行在后台线程
            Glide.get(context).clearDiskCache();
        });
    }

    @Override
    public long getCacheSize() {
        File directory = Glide.getPhotoCacheDir(context);
        return Utils.obtainLength(directory);
    }

}
