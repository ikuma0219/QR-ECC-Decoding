package com.es3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.es3.libs.ErasePositionWriter;
import com.es3.libs.ErasePositionReader;
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

public class Main {

	private static final String ORIGINAL_IMAGE_PATH = "app/data/resourse/original/";
	private static final String DENOISED_IMAGE_PATH = "app/data/resourse/denoised/";
	private static final String NOISE_LEVEL = "10.5";
	private static final int MAX_TRIES = 5;
	private static final int BRIGHTNESS_THRESHOLD = 128;

	public static void main(String[] args) throws IOException, NotFoundException {
		initializeErasePosition();
		int successfulDecodes = 0;

		for (int i = 0; i < 200; i++) {
			String originalData = null;
			try {
				BufferedImage originalImage = loadImage(ORIGINAL_IMAGE_PATH + i + ".png");
				originalData = decode(originalImage);
			} catch (IOException | NotFoundException | ChecksumException | FormatException e) {
				continue;
			}

			if (attemptDenoisedDecoding(i, originalData)) {
				successfulDecodes++;
			}
			System.out.println("Total successful decodes: " + successfulDecodes);
		}
	}

	private static void initializeErasePosition() throws NotFoundException, IOException {
		ErasePositionWriter.clearCsvFile();
		ErasePositionWriter.eraseSymbolList(NOISE_LEVEL, BRIGHTNESS_THRESHOLD);
	}

	// デコード比較
	private static boolean attemptDenoisedDecoding(int index, String originalData) {
		for (int j = 0; j <= MAX_TRIES; j++) {
			try {
				ErasePositionReader.processRowAndSaveToFile(index, j); // 消失位置を取得

				BufferedImage denoisedImage = loadImage(DENOISED_IMAGE_PATH + NOISE_LEVEL + "/" + index + ".png");
				String denoisedData = decode(denoisedImage);

				// デコード成功かチェック
				if (denoisedData != null && denoisedData.equals(originalData)) {
					System.out.println(index + ".png: " + denoisedData + " 復号成功！！！");
					return true;
				}
			} catch (IOException | NotFoundException | ChecksumException | FormatException e) {
			}

			if (j == MAX_TRIES) {
				System.out.println(index + ".png: jが5に達したためループを終了します。");
				break;
			}
		}
		return false;
	}

	// 画像ファイルを読み込む
	private static BufferedImage loadImage(String filePath) throws IOException {
		File imageFile = new File(filePath);
		return ImageIO.read(imageFile);
	}

	// デコードロジック
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