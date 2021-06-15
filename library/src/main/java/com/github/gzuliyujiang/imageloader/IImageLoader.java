/*
 * Copyright (c) 2013-present, 贵州纳雍穿青人李裕江<1032694760@qq.com>, All Rights Reserved.
 */

package com.github.gzuliyujiang.imageloader;

import android.app.Application;

/**
 * 面向接口编程，使用接口对各模块进行解耦，增强对第三方库的管控，不强依赖某些三方库，使得三方库可自由搭配组装。
 * <p>
 * 集成第三方图片加载框架（如：Glide、Picasso、Universal-Image-Loader、Fresco），
 * <p>
 * http://github.com/bumptech/glide
 * https://github.com/nostra13/Android-Universal-Image-Loader
 * https://github.com/square/picasso
 * https://github.com/facebook/fresco
 * <p>
 * Created by liyujiang on 2015/12/9.
 * Updated by liyujiang on 2020/5/14.
 */
public interface IImageLoader {

    void setup(Application application);

    <T> void display(ImageLoaderOption options);

    void pause();

    void resume();

    void cleanMemory();

    void clearCache();

    long getCacheSize();

}
