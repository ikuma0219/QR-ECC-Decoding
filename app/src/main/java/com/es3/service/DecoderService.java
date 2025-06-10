package com.es3.service;

import com.es3.libs.*;
import com.es3.utils.PathManager;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class DecoderService {
    private static final int MAX_RETRY = 5;

    public boolean tryDecoding(int index) throws Exception {
        for (int attempt = 0; attempt <= MAX_RETRY; attempt++) {
            System.out.println("try" + attempt);
            try {
                String originalData = decodeImage(PathManager.getOriginalImagePath(index));
                List<Integer> eraseSymbols = EraseCandidateSelector.getEraseSymbol(index, PathManager.NOISE_LEVEL);
                List<Integer> trimmed = ErasePositionProcessor.trimErasePositions(eraseSymbols, attempt);
                System.out.println(trimmed);

                ErasePositionHolder.setErasePositions(trimmed);

                BufferedImage originalImage = loadAndResize(PathManager.getOriginalImagePath(index));
                BufferedImage denoisedImage = loadAndResize(PathManager.getDenoisedImagePath(index));
                BufferedImage mergedImage = mergeSymbolAndOriginal(index, originalImage, denoisedImage);

                BufferedImage resized = resizeToFixedSize(mergedImage, 232, 232);

                String mergedData = decodeBufferedImage(resized);

                if (mergedData != null && mergedData.equals(originalData)) {
                    System.out.println(index + ".png: " + mergedData + " 復号成功！！！");
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException | IOException | NotFoundException | ChecksumException
                    | FormatException e) {
                // デコード失敗時はリトライ
            }
        }
        System.out.println(index + ".png: デコード失敗");
        return false;
    }

    private static BufferedImage loadAndResize(String path) throws IOException {
        return ImageUtil.resizeImage(ImageIO.read(new File(path)), 8);
    }

    private static String decodeImage(String filePath)
            throws NotFoundException, ChecksumException, FormatException, IOException {
        BufferedImage image = ImageIO.read(new File(filePath));
        return decodeBufferedImage(image);
    }

    private static String decodeBufferedImage(BufferedImage image)
            throws NotFoundException, ChecksumException, FormatException {
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        return new QRCodeReader().decode(bitmap).getText();
    }

    private static BufferedImage resizeToFixedSize(BufferedImage image, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = resized.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }

    public static BufferedImage mergeSymbolAndOriginal(
            int index,
            BufferedImage originalImage,
            BufferedImage denoisedImage) throws Exception {

        List<int[]> symbolPixels = getSymbolPixels(index);

        BufferedImage result = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);

        // originalImageで初期化
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                int value = originalImage.getRaster().getSample(x, y, 0);
                result.getRaster().setSample(x, y, 0, value);
            }
        }

        // シンボル領域だけdenoisedImageで上書き
        for (int[] coord : symbolPixels) {
            int x = coord[0], y = coord[1];
            if (x >= 0 && x < denoisedImage.getWidth() && y >= 0 && y < denoisedImage.getHeight()) {
                int value = denoisedImage.getRaster().getSample(x, y, 0);
                result.getRaster().setSample(x, y, 0, value);
            }
        }
        return result;
    }

    private static List<int[]> getSymbolPixels(int index) throws Exception {
        List<int[]> symbolPixels = new ArrayList<>();
        for (int[][] symbol : com.es3.libs.SymbolList.getSymbolList(index)) {
            for (int[] coord : symbol) {
                symbolPixels.add(coord);
            }
        }
        return symbolPixels;
    }
}