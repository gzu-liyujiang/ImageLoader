package com.github.gzuliyujiang.imageloader;

import java.io.File;

class Utils {
    Utils() {
    }

    public static long obtainLength(File file) {
        long size = 0;
        if (file == null) {
            return 0;
        }
        if (!file.isDirectory()) {
            return 0 + file.length();
        }
        File[] files = file.listFiles();
        if (files == null) {
            return 0;
        }
        for (File f : files) {
            size += obtainLength(f);
        }
        return size;
    }
}
