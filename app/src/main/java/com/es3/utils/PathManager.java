package com.es3.utils;

public class PathManager {
    public static final String ORIGINAL_IMAGE_DIR = "app/data/resourse/ver3/original/";
    public static final String DENOISED_IMAGE_DIR = "app/data/resourse/ver3/denoised/";
    public static final String NOISE_LEVEL = "10";

    public static String getOriginalImagePath(int index) {
        return ORIGINAL_IMAGE_DIR + index + ".png";
    }

    public static String getDenoisedImagePath(int index) {
        return DENOISED_IMAGE_DIR + NOISE_LEVEL + "/" + index + ".png";
    }
}
