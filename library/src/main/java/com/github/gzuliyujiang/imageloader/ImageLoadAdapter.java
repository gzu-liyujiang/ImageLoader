package com.github.gzuliyujiang.imageloader;

import android.widget.ImageView;

public interface ImageLoadAdapter {
    void clearCache();

    <T> void display(ImageView imageView, T t, int i);

    void download(String str, ImageDownloadCallback imageDownloadCallback);

    long getCacheSize();
}
