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
    private File cacheDir;
    private Picasso picasso;
    private UUID requestTag;

    @Override
    public void setup(Application application) {
        if (application == null) {
            return;
        }
        boolean mounted = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        cacheDir = new File(mounted ? application.getExternalCacheDir() : application.getCacheDir(), "picasso");
    }

    @Override
    public <T> void display(ImageView imageView, T imageSource, int placeholder) {
        if (imageView == null) {
            return;
        }
        if (imageSource == null) {
            imageView.setImageResource(placeholder);
            return;
        }
        if (cacheDir == null) {
            return;
        }
        RequestCreator requestCreator;
        Context context = imageView.getContext();
        picasso = new com.squareup.picasso.Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(cacheDir))
                .memoryCache(new LruCache(context))
                .build();
        if (imageSource instanceof Integer) {
            requestCreator = picasso.load((Integer) imageSource);
        } else {
            requestCreator = picasso.load(imageSource.toString());
        }
        requestTag = UUID.randomUUID();
        requestCreator.tag(requestTag);
        if (imageSource instanceof Integer) {
            requestCreator.networkPolicy(NetworkPolicy.NO_STORE);
        }
        requestCreator.config(Bitmap.Config.RGB_565);
        requestCreator.placeholder(placeholder);
        requestCreator.error(placeholder);
        requestCreator.into(imageView);
    }

    @Override
    public void pause() {
        if (picasso == null) {
            return;
        }
        picasso.pauseTag(requestTag);
    }

    @Override
    public void resume() {
        if (picasso == null) {
            return;
        }
        picasso.resumeTag(requestTag);
    }

    @Override
    public void cleanMemory() {

    }

    @Override
    public void clearCache() {
        if (cacheDir == null) {
            return;
        }
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Utils.deleteFile(cacheDir);
            }
        });
    }

    @Override
    public long getCacheSize() {
        if (cacheDir == null) {
            return 0;
        }
        return Utils.obtainLength(cacheDir);
    }

}
