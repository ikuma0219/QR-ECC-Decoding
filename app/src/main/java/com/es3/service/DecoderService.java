package com.es3.service;

import com.es3.libs.*;
import com.es3.utils.PathManager;
import com.google.zxing.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public class DecoderService {
    private static final int MAX_RETRY = 5;

    public boolean tryDecoding(int index) throws Exception {
        for (int attempt = 0; attempt <= MAX_RETRY; attempt++) {
            System.out.println("try" + attempt);
            try {
                String originalData = QRCodeDecoderUtil.decodeImage(PathManager.getOriginalImagePath(index));

                List<Integer> eraseSymbols = EraseCandidateSelector.getEraseSymbol(index, PathManager.NOISE_LEVEL);
                List<Integer> trimmed = ErasePositionProcessor.trimErasePositions(eraseSymbols, attempt);
                System.out.println(trimmed);
                ErasePositionHolder.setErasePositions(trimmed);

                BufferedImage originalImage = loadAndResize(PathManager.getOriginalImagePath(index));
                BufferedImage denoisedImage = loadAndResize(PathManager.getDenoisedImagePath(index));
                BufferedImage mergedImage = ImageMerger.mergeSymbolAndOriginal(index, originalImage, denoisedImage);

                BufferedImage resized = ImageUtil.resizeToFixedSize(mergedImage, 232, 232);
                String mergedData = QRCodeDecoderUtil.decodeBufferedImage(resized);

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
}