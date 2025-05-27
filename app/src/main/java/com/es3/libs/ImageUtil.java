package com.es3.libs;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.LuminanceSource;

public class ImageUtil {

    public static BufferedImage resizeImage(BufferedImage original, int scale) {
        int newWidth = original.getWidth() / scale;
        int newHeight = original.getHeight() / scale;

        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = resized.createGraphics();
        g.drawImage(original, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resized;
    }

    public static LuminanceSource createLuminanceSource(BufferedImage image) {
        return new BufferedImageLuminanceSource(image);
    }

    public static int[][] luminanceSourceToArray(LuminanceSource source, int width, int height) {
        int[][] pixels = new int[height][width];
        byte[] matrix = source.getMatrix();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y][x] = matrix[y * width + x] & 0xFF;
            }
        }
        return pixels;
    }
}
