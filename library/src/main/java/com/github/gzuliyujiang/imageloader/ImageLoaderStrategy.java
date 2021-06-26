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
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2020/6/22
 */
@SuppressWarnings("unused")
public class ImageLoaderStrategy implements IImageLoader {
    private static final String MESSAGE = "Please add dependency `runtimeOnly 'com.github.bumptech.glide:glide:xxx'`" +
            " or `runtimeOnly 'com.squareup.picasso:picasso:xxx'` in your app/build.gradle";
    private static final ImageLoaderStrategy INSTANCE = new ImageLoaderStrategy();
    private IImageLoader strategy;

    private ImageLoaderStrategy() {
        try {
            Class.forName(com.bumptech.glide.Glide.class.getName());
            strategy = new GlideImpl();
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            try {
                Class.forName(com.squareup.picasso.Picasso.class.getName());
                strategy = new PicassoImpl();
            } catch (ClassNotFoundException | NoClassDefFoundError ee) {
                try {
                    Class.forName(com.nostra13.universalimageloader.core.ImageLoader.class.getName());
                    strategy = new UniversalImageLoaderImpl();
                } catch (ClassNotFoundException | NoClassDefFoundError ignore) {
                }
            }
        }
    }

    public static IImageLoader getDefault() {
        if (INSTANCE.strategy == null) {
            throw new RuntimeException(MESSAGE);
        }
        return INSTANCE.strategy;
    }

    public static void setStrategy(IImageLoader strategy) {
        INSTANCE.strategy = strategy;
    }

    @Override
    public void setup(@NonNull Application application) {
        getDefault().setup(application);
    }

    @Override
    public <T> void display(@NonNull ImageView imageView, @NonNull T imageSource, @DrawableRes int placeholder) {
        getDefault().display(imageView, imageSource, placeholder);
    }

    @Override
    public void pause() {
        getDefault().pause();
    }

    @Override
    public void resume() {
        getDefault().resume();
    }

    @Override
    public void cleanMemory() {
        getDefault().cleanMemory();
    }

    @Override
    public void clearCache() {
        getDefault().clearCache();
    }

    @Override
    public long getCacheSize() {
        return getDefault().getCacheSize();
    }

}
