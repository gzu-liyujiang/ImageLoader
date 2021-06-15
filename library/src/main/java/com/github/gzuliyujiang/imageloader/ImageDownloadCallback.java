package com.github.gzuliyujiang.imageloader;

import android.graphics.Bitmap;

public interface ImageDownloadCallback {
    void onDownloadComplete(Bitmap bitmap);
}
