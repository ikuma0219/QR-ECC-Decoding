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
        // 各シンボルごとの信頼度
        Map<Integer, Double> symbolConfidenceMap = new HashMap<>();
        for (int symbolIndex = 0; symbolIndex < symbols.size(); symbolIndex++) {
            int[][] symbol = symbols.get(symbolIndex);
            double totalConfidence = (double) 0;

            for (int[] point : symbol) {
                int x = point[0];
                int y = point[1];
                // 範囲チェックおよび5x5の平均輝度計算
                if (x >= 0 && x < denoisedImage.length && y >= 0 && y < denoisedImage[0].length) {
                    int brightness = denoisedImage[x][y];
                    // System.out.println(brightness);
                    int totalBrightness = 0;
                    int count = 0;

                    // 5x5エリアの平均輝度を計算
                    for (int dx = -3; dx <= 3; dx++) {
                        for (int dy = -3; dy <= 3; dy++) {
                            int nx = x + dx;
                            int ny = y + dy;
                            if (nx >= 0 && nx < denoisedImage.length && ny >= 0 && ny < denoisedImage[0].length) {
                                totalBrightness += denoisedImage[nx][ny];
                                count++;
                            }
                        }
                    }

                    int averageBrightness = totalBrightness / count;
                    totalConfidence += calculateTheta(brightness, averageBrightness);
                }
            }
            // シンボルごとの信頼度を保存
            double averageConfidence = totalConfidence / 8.0;
            symbolConfidenceMap.put(symbolIndex, averageConfidence);
        }

        return symbolConfidenceMap.entrySet().stream()
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