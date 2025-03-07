package com.es3.utils;

import java.awt.image.BufferedImage;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class QRCodeDecoder {

    /**
     * 画像からQRコードをデコードする
     *
     * @param image デコード対象の画像
     * @return QRコードのデータ文字列
     * @throws NotFoundException QRコードが見つからなかった場合
     * @throws ChecksumException データのチェックサムエラー
     * @throws FormatException QRコードのフォーマットエラー
     */
    public static String decode(BufferedImage image) 
            throws NotFoundException, ChecksumException, FormatException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap bitmap = new BinaryBitmap(binarizer);
        QRCodeReader reader = new QRCodeReader();
        Result result = reader.decode(bitmap);
        return result.getText();
    }
}
