/*
 * Copyright (c) 2013-present, 贵州纳雍穿青人李裕江<1032694760@qq.com>, All Rights Reserved.
 */

package com.github.gzuliyujiang.imageloader;

import java.io.File;

/**
 * Created by liyujiang on 2020/6/23.
 */
final class Utils {

    public static long obtainLength(File file) {
        long size = 0;
        if (file == null) {
            return size;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    size += obtainLength(f);
                }
            }
        } else {
            size += file.length();
        }
        return size;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            if (files.length == 0) {
                file.delete();
            } else {
                for (File f : files) {
                    deleteFile(f);
                }
            }
        }
        file.delete();
    }

}
