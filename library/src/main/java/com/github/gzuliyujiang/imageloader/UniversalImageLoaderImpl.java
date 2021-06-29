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
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.io.File;

/**
 * 参见 https://github.com/nostra13/Android-Universal-Image-Loader
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2020/04/20 11:17
 */
final class UniversalImageLoaderImpl implements IImageLoader {

    @Override
    public void setup(Application application) {
        if (application == null) {
            return;
        }
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            //IllegalStateException: ImageLoader must be init with configuration before using
            imageLoader.init(ImageLoaderConfiguration.createDefault(application));
        }
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
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        builder.cacheInMemory(true);
        builder.showImageOnLoading(placeholder);
        builder.showImageOnFail(placeholder);
        builder.showImageForEmptyUri(placeholder);
        builder.displayer(new SimpleBitmapDisplayer());
        String imageUrl;
        if (imageSource instanceof Integer) {
            // 加载res/drawable参阅 https://blog.csdn.net/shaw1994/article/details/47223133
            imageUrl = "drawable://" + imageSource;
            builder.cacheOnDisk(false);
        } else {
            imageUrl = imageSource.toString();
            builder.cacheOnDisk(true);
        }
        // NoSuchFieldException: No field mMaxWidth in class Landroid/widget/ImageView;...
        ImageLoader.getInstance().displayImage(imageUrl, imageView, builder.build());
    }

    @Override
    public void pause() {
        ImageLoader.getInstance().pause();
    }

    @Override
    public void resume() {
        ImageLoader.getInstance().resume();
    }

    @Override
    public void cleanMemory() {
        ImageLoader.getInstance().clearMemoryCache();
        System.gc();
    }

    @Override
    public void clearCache() {
        cleanMemory();
        ImageLoader.getInstance().clearDiskCache();
    }

    @Override
    public long getCacheSize() {
        File directory = ImageLoader.getInstance().getDiskCache().getDirectory();
        return Utils.obtainLength(directory);
    }

}
