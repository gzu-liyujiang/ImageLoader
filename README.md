# ImageLoader

![Release APK](https://github.com/gzu-liyujiang/ImageLoader/workflows/Release%20APK/badge.svg)
[![jitpack](https://jitpack.io/v/gzu-liyujiang/ImageLoader.svg)](https://jitpack.io/#gzu-liyujiang/ImageLoader)

自用的 Android/Java 图片加载组件，面向接口编程，使用接口对各模块进行解耦，增强对第三方库的管控，底层可无缝切换底层的具体实现。默认实现了 Glide、Picasso、Universal-Image-Loader 。


```groovy
    allprojects {
        repositories {
            maven { url 'https://www.jitpack.io' }
        }
    }
```

```groovy
    dependencies {
        implementation 'com.github.gzu-liyujiang:ImageLoader:版本号'
        //runtimeOnly 'com.github.bumptech.glide:glide:4.12.0'
        runtimeOnly 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
        //runtimeOnly 'com.squareup.picasso:picasso:2.8'
}
```
