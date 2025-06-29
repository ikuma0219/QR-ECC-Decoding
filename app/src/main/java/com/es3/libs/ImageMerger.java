package com.es3.libs;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageMerger {

    public static BufferedImage mergeSymbolAndOriginal(
            int index,
            BufferedImage originalImage,
            BufferedImage denoisedImage) throws Exception {

        List<int[]> symbolPixels = getSymbolPixels(index);
        BufferedImage result = createGrayImageFrom(originalImage);

        overwriteSymbolArea(result, denoisedImage, symbolPixels);

        return result;
    }

    // originalImageの内容でグレースケール画像を生成
    private static BufferedImage createGrayImageFrom(BufferedImage originalImage) {
        BufferedImage result = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY
        );
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                int value = originalImage.getRaster().getSample(x, y, 0);
                result.getRaster().setSample(x, y, 0, value);
            }
        }
        return result;
    }

    // シンボル領域だけdenoisedImageで上書き
    private static void overwriteSymbolArea(BufferedImage result, BufferedImage denoisedImage, List<int[]> symbolPixels) {
        for (int[] coord : symbolPixels) {
            int x = coord[0], y = coord[1];
            if (x >= 0 && x < denoisedImage.getWidth() && y >= 0 && y < denoisedImage.getHeight()) {
                int value = denoisedImage.getRaster().getSample(x, y, 0);
                result.getRaster().setSample(x, y, 0, value);
            }
        }
    }

    private static List<int[]> getSymbolPixels(int index) throws Exception {
        List<int[]> symbolPixels = new ArrayList<>();
        for (int[][] symbol : SymbolList.getSymbolList(index)) {
            for (int[] coord : symbol) {
                symbolPixels.add(coord);
            }
        }
        return symbolPixels;
    }
}