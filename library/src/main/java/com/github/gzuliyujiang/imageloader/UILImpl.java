package com.github.gzuliyujiang.imageloader;

import android.app.Application;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

class UILImpl implements ImageLoadAdapter {
    public UILImpl(Application application) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(application));
        }
    }

    @Override // com.github.gzuliyujiang.imageloader.ImageLoadAdapter
    public <T> void display(ImageView imageView, T urlOrRes, int placeholder) {
        if (urlOrRes == null) {
            imageView.setImageResource(placeholder);
        } else if (urlOrRes instanceof Integer) {
            imageView.setImageResource((int) urlOrRes);
        } else if (TextUtils.isEmpty(urlOrRes.toString())) {
            imageView.setImageResource(placeholder);
        } else {
            ImageLoader.getInstance().displayImage(urlOrRes.toString(), imageView, new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true).showImageOnLoading(placeholder).showImageOnFail(placeholder).considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY).displayer(new SimpleBitmapDisplayer()).build());
        }
    }

    @Override // com.github.gzuliyujiang.imageloader.ImageLoadAdapter
    public void download(String url, final ImageDownloadCallback callback) {
        ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
            @Override // com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override // com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (callback != null) {
                    callback.onDownloadComplete(null);
                }
            }

            @Override // com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (callback != null) {
                    callback.onDownloadComplete(loadedImage);
                }
            }

            @Override // com.nostra13.universalimageloader.core.listener.ImageLoadingListener
            public void onLoadingCancelled(String imageUri, View view) {
                if (callback != null) {
                    callback.onDownloadComplete(null);
                }
            }
        });
    }

    @Override // com.github.gzuliyujiang.imageloader.ImageLoadAdapter
    public void clearCache() {
        try {
            ImageLoader.getInstance().clearMemoryCache();
            ImageLoader.getInstance().clearDiskCache();
            System.gc();
        } catch (Throwable th) {
        }
    }

    @Override // com.github.gzuliyujiang.imageloader.ImageLoadAdapter
    public long getCacheSize() {
        return Utils.obtainLength(ImageLoader.getInstance().getDiskCache().getDirectory());
    }
}
