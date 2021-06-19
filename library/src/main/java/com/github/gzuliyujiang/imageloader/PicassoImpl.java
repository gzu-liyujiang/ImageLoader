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

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.Executors;

/**
 * 参见 https://github.com/square/picasso
 *
 * @author 贵州山魈羡民 (1032694760@qq.com)
 * @since 2021/3/9 20:25
 */
final class PicassoImpl implements IImageLoader {
    private Context context;
    private File cacheDir;
    private Picasso picasso;
    private UUID requestTag;

    @Override
    public void setup(@NonNull Application application) {
        this.context = application;
        boolean mounted = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        cacheDir = new File(mounted ? application.getExternalCacheDir() : application.getCacheDir(), "picasso");
    }

    @Override
    public void display(@NonNull ImageLoaderOption option) {
        ImageView imageView = (ImageView) option.getViewContainer();
        if (imageView == null) {
            return;
        }
        RequestCreator requestCreator;
        picasso = new com.squareup.picasso.Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(cacheDir))
                .memoryCache(new LruCache(context))
                .build();
        if (TextUtils.isEmpty(option.getUrl())) {
            if (option.getDrawableRes() != -1) {
                return;
            }
            requestCreator = picasso.load(option.getDrawableRes());
        } else {
            requestCreator = picasso.load(option.getUrl());
        }
        requestTag = UUID.randomUUID();
        requestCreator.tag(requestTag);
        requestCreator.config(Bitmap.Config.RGB_565);
        if (option.getDrawableRes() != -1) {
            requestCreator.networkPolicy(NetworkPolicy.NO_STORE);
        }
        if (option.getPlaceholderRes() != -1) {
            requestCreator.placeholder(option.getPlaceholderRes());
            requestCreator.error(option.getPlaceholderRes());
        }
        if (option.getImageSize() != null) {
            requestCreator.resize(option.getImageSize().getWidth(), option.getImageSize().getHeight());
        }
        if (option.getImageRadius() > 0) {
            requestCreator.transform(new PicassoCornerTrans());
        }
        if (option.isCircle()) {
            requestCreator.transform(new PicassoCircleTrans());
        }
        requestCreator.into(imageView);
    }

    @Override
    public void pause() {
        picasso.pauseTag(requestTag);
    }

    @Override
    public void resume() {
        picasso.resumeTag(requestTag);
    }

    @Override
    public void cleanMemory() {

    }

    @Override
    public void clearCache() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Utils.deleteFile(cacheDir);
            }
        });
    }

    @Override
    public long getCacheSize() {
        return Utils.obtainLength(cacheDir);
    }

}
