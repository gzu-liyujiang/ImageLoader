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
    public void setup(Application application) {
        this.context = application;
        boolean mounted = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        cacheDir = new File(mounted ? application.getExternalCacheDir() : application.getCacheDir(), "picasso");
    }

    @Override
    public void display(ImageLoaderOption options) {
        ImageView imageView = (ImageView) options.getViewContainer();
        if (imageView == null) {
            return;
        }
        RequestCreator requestCreator;
        picasso = new com.squareup.picasso.Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(cacheDir))
                .memoryCache(new LruCache(context))
                .build();
        if (TextUtils.isEmpty(options.getUrl())) {
            if (options.getDrawableRes() != -1) {
                return;
            }
            requestCreator = picasso.load(options.getDrawableRes());
        } else {
            requestCreator = picasso.load(options.getUrl());
        }
        requestTag = UUID.randomUUID();
        requestCreator.tag(requestTag);
        requestCreator.config(Bitmap.Config.RGB_565);
        if (options.getDrawableRes() != -1) {
            requestCreator.networkPolicy(NetworkPolicy.NO_STORE);
        }
        if (options.getPlaceholderRes() != -1) {
            requestCreator.placeholder(options.getPlaceholderRes());
            requestCreator.error(options.getPlaceholderRes());
        }
        if (options.getImageSize() != null) {
            requestCreator.resize(options.getImageSize().getWidth(), options.getImageSize().getHeight());
        }
        if (options.getImageRadius() > 0) {
            requestCreator.transform(new PicassoCornerTrans());
        }
        if (options.isCircle()) {
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
