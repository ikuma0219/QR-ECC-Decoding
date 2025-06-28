package com.es3.libs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.*;

import com.es3.utils.PathManager;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;

public class EraseCandidateSelector {
    private static final int CONFIDENCE_THRESHOLD = 200;

    private static final int WINDOW_SIZE_WHITE = 5;
    private static final int RADIUS_WHITE = (WINDOW_SIZE_WHITE - 1) / 2;

    private static final int WINDOW_SIZE_BLACK = 7;
    private static final int RADIUS_BLACK = (WINDOW_SIZE_BLACK - 1) / 2;

    public static List<Integer> getEraseSymbol(int index, String noiseLevel)
            throws IOException, NotFoundException, FormatException {

        String imagePath = PathManager.getDenoisedImagePath(index);
        BufferedImage denoisedImage = ImageIO.read(new File(imagePath));

        BufferedImage resizedImage = ImageUtil.resizeImage(denoisedImage, 8);
        LuminanceSource source = ImageUtil.createLuminanceSource(resizedImage);
        int[][] denoisedArray = ImageUtil.luminanceSourceToArray(source,
                resizedImage.getWidth(), resizedImage.getHeight());

        List<int[][]> symbolList = SymbolList.getSymbolList(index);
        return calculateErases(denoisedArray, symbolList);
    }

    public static List<Integer> calculateErases(int[][] denoisedImage, List<int[][]> symbols) {
        final int height = denoisedImage.length;
        final int width = denoisedImage[0].length;

        Map<Integer, Double> confidenceMap = new HashMap<>();

        for (int i = 0; i < symbols.size(); i++) {
            int[][] symbol = symbols.get(i);
            double confidenceSum = 0;

            for (int[] point : symbol) {
                int x = point[0], y = point[1];
                if (x < 0 || x >= height || y < 0 || y >= width)
                    continue;

                int pixel = denoisedImage[x][y];

                if (pixel >= 253) { // 白モジュール（≒255）
                    int avg = calculateNeighborhoodAverage(denoisedImage, x, y, RADIUS_WHITE, true);
                    if (avg <= CONFIDENCE_THRESHOLD) {
                        confidenceSum += 1.0;
                    } else {
                        // confidenceSum += 0.0;
                        double theta = (double) (255 - avg) / (254 - CONFIDENCE_THRESHOLD); // 正規化：74=最大値（255-181）
                        // System.out.println("avg: " +avg + " theta: " + theta);
                        confidenceSum += theta;
                    }
                } else {
                    int avg = calculateNeighborhoodAverage(denoisedImage, x, y, RADIUS_BLACK, false);
                    confidenceSum += calculateTheta(pixel, avg);
                }
            }
            confidenceMap.put(i, confidenceSum / symbol.length);
            // System.out.println(confidenceMap.get(confidenceMap.size() - 1) + " " + i);
        }
        return confidenceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()) // 信頼度が低い順
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();
    }

    // 周囲の平均輝度値を計算
    private static int calculateNeighborhoodAverage(int[][] image, int x, int y, int radius, boolean excludeCenter) {
        int height = image.length;
        int width = image[0].length;
        int total = 0, count = 0;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int nx = x + dx, ny = y + dy;
                if (nx >= 0 && nx < height && ny >= 0 && ny < width) {
                    if (excludeCenter && dx == 0 && dy == 0)
                        continue;
                    total += image[nx][ny];
                    count++;
                }
            }
        }
        return total / count;
    }

    // 信頼度計算
    public static double calculateTheta(int L, int brightnessThreshold) {
        int s = brightnessThreshold;
        double theta;
        if (L < s) {
            theta = (double) (s - L) / s;
        } else {
            theta = (double) (L - s) / (255 - s);
        }
        return theta;
    }
}