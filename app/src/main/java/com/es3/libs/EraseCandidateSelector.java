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
        Map<Integer, Double> confidenceMap = new HashMap<>();
        int height = denoisedImage.length;
        int width = denoisedImage[0].length;

        for (int i = 0; i < symbols.size(); i++) {
            int[][] symbol = symbols.get(i);
            double confidenceSum = 0;

            for (int[] point : symbol) {
                int x = point[0], y = point[1];
                if (x >= 0 && x < height && y >= 0 && y < width) {
                    int brightness = denoisedImage[x][y];
                    int total = 0, count = 0;
                    for (int dx = -3; dx <= 3; dx++) {
                        for (int dy = -3; dy <= 3; dy++) {
                            int nx = x + dx, ny = y + dy;
                            if (nx >= 0 && nx < height && ny >= 0 && ny < width) {
                                total += denoisedImage[nx][ny];
                                count++;
                            }
                        }
                    }
                    int avg = total / count;
                    confidenceSum += calculateTheta(brightness, avg);
                }
            }
            confidenceMap.put(i, confidenceSum / 8.0);
        }

        return confidenceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();
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