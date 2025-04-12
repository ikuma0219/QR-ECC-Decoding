package com.es3.service;

import com.es3.libs.*;
import com.es3.utils.PathManager;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public class DecoderService {
    private static final int MAX_RETRY = 5;

    public boolean tryDecoding(int index) {

        for (int attempt = 0; attempt <= MAX_RETRY; attempt++) {
            System.out.println("try" + attempt);
            try {
                String originalData = decodeImage(PathManager.getOriginalImagePath(index));

                List<Integer> eraseSymbols = EraseCandidateSelector.getEraseSymbol(index,
                        PathManager.NOISE_LEVEL);
                List<Integer> trimmed = ErasePositionProcessor.trimErasePositions(eraseSymbols, attempt);
                System.out.println(trimmed.toString());
                ErasePositionHolder.setErasePositions(trimmed);

                String denoisedData = decodeImage(PathManager.getDenoisedImagePath(index));
                if (denoisedData != null && denoisedData.equals(originalData)) {
                    System.out.println(index + ".png: " + denoisedData + " 復号成功！！！");
                    return true;
                }

            } catch (IOException | NotFoundException | ChecksumException | FormatException e) {
            }
        }
        System.out.println(index + ".png: デコード失敗");
        return false;
    }

    private static String decodeImage(String filePath)
            throws NotFoundException, ChecksumException, FormatException, IOException {
        BufferedImage image = ImageIO.read(new File(filePath));
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        return new QRCodeReader().decode(bitmap).getText();
    }
}
