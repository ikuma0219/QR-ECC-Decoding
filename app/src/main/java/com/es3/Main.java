package com.es3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.es3.libs.ErasePositionWriter;
import com.es3.libs.ErasePositionReader;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class Main {

	private static final String ORIGINAL_IMAGE_DIR = "app/data/resourse/original/";
	private static final String DENOISED_IMAGE_DIR = "app/data/resourse/denoised/";
	private static final String NOISE_LEVEL = "10.5";
	private static final int MAX_RETRY = 5;
	private static final int BRIGHTNESS_THRESHOLD = 128;

	public static void main(String[] args) throws IOException, NotFoundException {
		initializeErasePositionData();
		int successfulDecodes = 0;

		for (int index = 0; index < 200; index++) {
			try {
				String originalData = decodeImage(ORIGINAL_IMAGE_DIR + index + ".png");
				if (tryDenoisedDecoding(index, originalData)) {
					successfulDecodes++;
				}

			} catch (IOException | NotFoundException | ChecksumException | FormatException e) {
			}

			System.out.println("Total successful decodes: " + successfulDecodes);
		}
	}

	private static void initializeErasePositionData() throws NotFoundException, IOException {
		ErasePositionWriter.clearCsvFile();
		ErasePositionWriter.eraseSymbolList(NOISE_LEVEL, BRIGHTNESS_THRESHOLD);
	}

	// デコード比較
	private static boolean tryDenoisedDecoding(int index, String originalData) {
		for (int attempt = 0; attempt <= MAX_RETRY; attempt++) {
			System.out.println("try" + attempt);
			try {
				ErasePositionReader.updateErasePositionFile(index, attempt); // 消失位置を取得

				String denoisedData = decodeImage(DENOISED_IMAGE_DIR + NOISE_LEVEL + "/" + index + ".png");

				// デコード成功かチェック
				if (denoisedData != null && denoisedData.equals(originalData)) {
					System.out.println(index + ".png: " + denoisedData + " 復号成功！！！");
					return true;
				}
			} catch (IOException | NotFoundException | ChecksumException | FormatException e) {
			}

		}
		System.out.println(index + ".png: jが5に達したためループを終了します。");
		return false;
	}

	// デコードロジック
	private static String decodeImage(String filePath)
			throws NotFoundException, ChecksumException, FormatException, IOException {
		BufferedImage image = loadImage(filePath);
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		Binarizer binarizer = new HybridBinarizer(source);
		BinaryBitmap bitmap = new BinaryBitmap(binarizer);
		QRCodeReader reader = new QRCodeReader();
		Result result = reader.decode(bitmap);
		String data = result.getText();
		return data;
	}

	// 画像ファイルを読み込む
	private static BufferedImage loadImage(String filePath) throws IOException {
		File imageFile = new File(filePath);
		return ImageIO.read(imageFile);
	}
}