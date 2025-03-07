package com.es3.core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import com.es3.libs.ErasePositionWriter;
import com.es3.libs.ErasePositionReader;
import com.es3.utils.ImageUtils;
import com.es3.utils.QRCodeDecoder;
import com.google.zxing.NotFoundException;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;

public class Processor {
    private static final String ORIGINAL_IMAGE_PATH = "app/data/resourse/original/";
    private static final String DENOISED_IMAGE_PATH = "app/data/resourse/denoised/";
    private static final String NOISE_LEVEL = "9.0";
    private static final int MAX_TRIES = 6;
    private static final int BRIGHTNESS_THRESHOLD = 160;
    private static final int MAX_IMAGES = 200;

    public void run() throws IOException, NotFoundException {
        ErasePositionWriter.clearCsvFile();
        ErasePositionWriter.eraseSymbolList(NOISE_LEVEL, BRIGHTNESS_THRESHOLD);
        int successfulDecodes = 0;

        for (int i = 0; i < MAX_IMAGES; i++) {
            try {
                BufferedImage originalImage = ImageUtils.loadImage(ORIGINAL_IMAGE_PATH + i + ".png");
                String originalData = QRCodeDecoder.decode(originalImage);

                if (processDenoisedImage(i, originalData)) {
                    successfulDecodes++;
                }
            } catch (IOException | NotFoundException | ChecksumException | FormatException e) {
                continue;
            }
            System.out.println("Total successful decodes: " + successfulDecodes);
        }
    }

    private boolean processDenoisedImage(int index, String originalData) {
        for (int j = 1; j <= MAX_TRIES; j++) {
            try {
                ErasePositionReader.processRowAndSaveToFile(index, j);
                BufferedImage denoisedImage = ImageUtils.loadImage(DENOISED_IMAGE_PATH + NOISE_LEVEL + "/" + index + ".png");
                String denoisedData = QRCodeDecoder.decode(denoisedImage);

                if (denoisedData != null && denoisedData.equals(originalData)) {
                    System.out.println(index + ".png: " + denoisedData + " 復号成功！！！");
                    return true;
                }
            } catch (IOException | NotFoundException | ChecksumException | FormatException e) {
            }

            if (j == MAX_TRIES) {
                System.out.println(index + ".png: jが6に達したためループを終了します。");
            }
        }
        return false;
    }
}
