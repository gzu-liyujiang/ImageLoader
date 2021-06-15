package com.github.gzuliyujiang.imageloader;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.concurrent.Executors;

class GlideImpl implements ImageLoadAdapter {
    private Application application;

    public GlideImpl(Application application2) {
        this.application = application2;
    }

    @Override // com.github.gzuliyujiang.imageloader.ImageLoadAdapter
    public <T> void display(ImageView imageView, T urlOrRes, int placeholder) {
        if (urlOrRes == null) {
            imageView.setImageResource(placeholder);
        } else if (urlOrRes instanceof Integer) {
            imageView.setImageResource((int) urlOrRes);
        } else {
            Glide.with(this.application).applyDefaultRequestOptions(RequestOptions.errorOf(placeholder).placeholder(placeholder)).load(urlOrRes).into(imageView);
        }
    }

    @Override // com.github.gzuliyujiang.imageloader.ImageLoadAdapter
    public void download(String url, final ImageDownloadCallback callback) {
        Glide.with(this.application).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                if (callback != null) {
                    callback.onDownloadComplete(resource);
                }
            }

            @Override
            public void onLoadCleared(Drawable placeholder) {
                if (callback != null) {
                    callback.onDownloadComplete(null);
                }
            }
        });
    }

    @Override // com.github.gzuliyujiang.imageloader.ImageLoadAdapter
    public void clearCache() {
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public final void run() {
                    Glide.get(application).clearMemory();
                    System.gc();
                    handler.removeCallbacksAndMessages(null);
                }
            });
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                public final void run() {
                    Glide.get(application).clearDiskCache();
                }
            });
        } catch (Throwable th) {
        }
    }

    @Override // com.github.gzuliyujiang.imageloader.ImageLoadAdapter
    public long getCacheSize() {
        return Utils.obtainLength(Glide.getPhotoCacheDir(application));
    }
}
