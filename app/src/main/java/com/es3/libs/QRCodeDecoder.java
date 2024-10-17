package com.es3.libs;

import java.awt.image.BufferedImage;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;


public class QRCodeDecoder {
    	public static String decode(BufferedImage image)
			throws NotFoundException, ChecksumException, FormatException {
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		Binarizer binarizer = new HybridBinarizer(source);
		BinaryBitmap bitmap = new BinaryBitmap(binarizer);
		QRCodeReader reader = new QRCodeReader();
		Result result = reader.decode(bitmap);
		String data = result.getText();
		return data;
	}
    
}
