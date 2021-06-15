package com.github.gzuliyujiang.imageloader;

import android.app.Application;
import android.widget.ImageView;

public class ImageLoader {
    private static final String MESSAGE = "Please add dependency `runtimeOnly 'com.github.bumptech.glide:glide:4.10.0'` or `runtimeOnly 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'` in your app/build.gradle";
    private static volatile ImageLoader instance;
    private ImageLoadAdapter adapter;

    private ImageLoader() {
    }

    private static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    public static void setAdapter(ImageLoadAdapter adapter2) {
        getInstance().adapter = adapter2;
    }

    public static void initInApplication(Application application) {
        try {
            Class.forName(com.nostra13.universalimageloader.core.ImageLoader.class.getName());
            setAdapter(new UILImpl(application));
        } catch (Throwable th) {
        }
    }

    public static <T> void display(ImageView imageView, T urlOrRes, int placeholder) {
        getAdapter().display(imageView, urlOrRes, placeholder);
    }

    public static <T> void download(String uri, ImageDownloadCallback callback) {
        getAdapter().download(uri, callback);
    }

    public static void clearCache() {
        getAdapter().clearCache();
    }

    public static long getCacheSize() {
        return getAdapter().getCacheSize();
    }

    private static ImageLoadAdapter getAdapter() {
        ImageLoadAdapter adapter2 = getInstance().adapter;
        if (adapter2 != null) {
            return adapter2;
        }
        throw new NullPointerException(MESSAGE);
    }
}
