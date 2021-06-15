/*
 * Copyright (c) 2013-present, 贵州纳雍穿青人李裕江<1032694760@qq.com>, All Rights Reserved.
 */

package com.github.gzuliyujiang.imageloader;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;

/**
 * 类说明
 *
 * @author 贵州山魈羡民 (1032694760@qq.com)
 * @since 2021/3/9 20:24
 */
final class PicassoCornerTrans implements Transformation {

    @Override
    public Bitmap transform(Bitmap source) {
        int widthLight = source.getWidth();
        int heightLight = source.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        Paint paintColor = new Paint();
        paintColor.setFlags(1);
        canvas.drawRoundRect(new RectF(new Rect(0, 0, widthLight, heightLight)),
                ((float) widthLight) / 5.0f, ((float) heightLight) / 5.0f, paintColor);
        Paint paintImage = new Paint();
        paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(source, 0.0f, 0.0f, paintImage);
        source.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "corner";
    }

}