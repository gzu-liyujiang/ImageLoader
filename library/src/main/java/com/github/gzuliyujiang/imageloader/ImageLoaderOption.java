/*
 * Copyright (c) 2013-present, 贵州纳雍穿青人李裕江<1032694760@qq.com>, All Rights Reserved.
 */

package com.github.gzuliyujiang.imageloader;

import android.view.View;

import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Px;

/**
 * 参阅 https://github.com/ladingwu/ImageLoaderFramework/.../ImageLoaderOptions.java
 * Created by liyujiang on 2021/3/6.
 */
public class ImageLoaderOption {
    private final View viewContainer;
    private final String url;
    private final Integer drawableRes;
    private final Integer placeholderRes;
    private final ImageSize imageSize;
    private final int imageRadius;
    private final boolean isCircle;

    private ImageLoaderOption(Builder builder) {
        this.viewContainer = builder.viewContainer;
        this.url = builder.url;
        this.drawableRes = builder.drawableRes;
        this.placeholderRes = builder.placeholderRes;
        this.imageSize = builder.imageSize;
        this.imageRadius = builder.imageRadius;
        this.isCircle = builder.isCircle;
    }

    public static Builder create(@NonNull View view, @NonNull String url) {
        return new Builder(view, url);
    }

    public static Builder create(@NonNull View view, @DrawableRes int resource) {
        return new Builder(view, resource);
    }

    public View getViewContainer() {
        return viewContainer;
    }

    public String getUrl() {
        return url;
    }

    @DrawableRes
    public Integer getDrawableRes() {
        return drawableRes;
    }

    @DrawableRes
    public Integer getPlaceholderRes() {
        return placeholderRes;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public int getImageRadius() {
        return imageRadius;
    }

    public final static class Builder {
        private final View viewContainer;
        private String url;
        private Integer drawableRes = -1;
        private Integer placeholderRes = -1;
        private ImageSize imageSize;
        private int imageRadius = 0;
        private boolean isCircle = false;

        public Builder(@NonNull View view, @NonNull String url) {
            this.viewContainer = view;
            this.url = url;
        }

        public Builder(@NonNull View view, @DrawableRes Integer drawableRes) {
            this.viewContainer = view;
            this.drawableRes = drawableRes;
        }

        public Builder placeholder(@DrawableRes Integer resource) {
            this.placeholderRes = resource;
            return this;
        }

        public Builder isCircle() {
            this.isCircle = true;
            return this;
        }

        public Builder imageRadiusPx(@Px int radius) {
            this.imageRadius = (int) (radius * viewContainer.getResources().getDisplayMetrics().density);
            return this;
        }

        public Builder imageRadiusDp(@Dimension(unit = Dimension.DP) int radius) {
            this.imageRadius = (int) (radius * viewContainer.getResources().getDisplayMetrics().density);
            return this;
        }

        public Builder imageSize(int width, int height) {
            this.imageSize = new ImageSize(width, height);
            return this;
        }

        public ImageLoaderOption build() {
            return new ImageLoaderOption(this);
        }

    }

    public final static class ImageSize {
        private final int width;
        private final int height;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

}
