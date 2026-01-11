package com.es3.utils;

public class PathManager {
    public static final String ORIGINAL_IMAGE_DIR = "app/data/resourse/blur&scratch/original/";
    public static final String DENOISED_IMAGE_DIR = "app/data/resourse/camera/denoised/8.0/";
    public static final String NOISE_LEVEL = "1100";

    public static String getOriginalImagePath(int index) {
        return ORIGINAL_IMAGE_DIR + index + ".png";
    }

    public static String getDenoisedImagePath(int index) {
        return DENOISED_IMAGE_DIR + NOISE_LEVEL + "/" + index + ".png";
    }
}
