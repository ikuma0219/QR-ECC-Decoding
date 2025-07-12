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
        Map<Integer, Integer> symbolConfidenceMap = new HashMap<>();

        for (int symbolIndex = 0; symbolIndex < symbols.size(); symbolIndex++) {
            int[][] symbol = symbols.get(symbolIndex);
            int confidenceSum = 0;

            for (int[] point : symbol) {
                int x = point[0];
                int y = point[1];
                
                if (x >= 0 && x < denoisedImage.length && y >= 0 && y < denoisedImage[0].length) {
                    int brightness = denoisedImage[x][y];

                    // 信頼度判定：30〜240の範囲内なら0、外なら1
                    if (brightness >= 30 && brightness <= 240) {
                        confidenceSum += 0;
                    } else {
                        confidenceSum += 1;
                    }
                }
            }

            symbolConfidenceMap.put(symbolIndex, confidenceSum);
        }

        // 信頼度が低い順に並べて、上位10件のシンボルインデックスを返す
        return symbolConfidenceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();
    }
}