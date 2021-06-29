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

/**
 * 面向接口编程，使用接口对各模块进行解耦，增强对第三方库的管控，不强依赖某些三方库，使得三方库可自由搭配组装。
 * <p>
 * 集成第三方图片加载框架（如：Glide、Picasso、Universal-Image-Loader、Fresco），
 * <p>
 * http://github.com/bumptech/glide
 * https://github.com/nostra13/Android-Universal-Image-Loader
 * https://github.com/square/picasso
 * https://github.com/facebook/fresco
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2015/12/9
 * @since 2020/5/14
 */
public interface IImageLoader {

    void setup(Application application);

    <T> void display(ImageView imageView, T imageSource, int placeholder);

    void pause();

    void resume();

    void cleanMemory();

    void clearCache();

    long getCacheSize();

}
