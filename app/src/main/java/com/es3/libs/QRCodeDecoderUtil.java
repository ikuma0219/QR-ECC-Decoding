package com.es3.libs;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class QRCodeDecoderUtil {

    public static String decodeImage(String filePath)
            throws NotFoundException, ChecksumException, FormatException, IOException {
        BufferedImage image = ImageIO.read(new File(filePath));
        return decodeBufferedImage(image);
    }

    public static String decodeBufferedImage(BufferedImage image)
            throws NotFoundException, ChecksumException, FormatException {
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(image)));
        return new QRCodeReader().decode(bitmap).getText();
    }
}