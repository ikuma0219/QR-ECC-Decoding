package com.es3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.es3.libs.QRCodeDecoder;
import com.es3.libs.ErasePositionWriter;
import com.es3.libs.ErasePositionReader;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;

public class Main {

	private static final String ORIGINAL_IMAGE_PATH = "app/data/resourse/original/";
	private static final String DENOISED_IMAGE_PATH = "app/data/resourse/denoised/";
	private static final String NOISE_LEVEL = "10.5";
	private static final int MAX_TRIES = 5;

	public static void main(String[] args) throws IOException, NotFoundException {
		int successfulDecodes = 0;

		initializeErasePosition();

		for (int i = 0; i < 200; i++) {
			String originalData = null;
			try {
				BufferedImage originalImage = loadImage(ORIGINAL_IMAGE_PATH + i + ".png");
				originalData = QRCodeDecoder.decode(originalImage);
			} catch (IOException | NotFoundException | ChecksumException | FormatException e) {
				continue; // エラーが発生した場合、次の画像へ
			}

			if (attemptDenoisedDecoding(i, originalData)) {
				successfulDecodes++;
			}
		}
		System.out.println("Total successful decodes: " + successfulDecodes);
	}

	private static void initializeErasePosition() throws NotFoundException, IOException {
		ErasePositionWriter.clearCsvFile(); // CSVファイルのクリア
		ErasePositionWriter.eraseSymbolList(NOISE_LEVEL); // 輝度値による消失シンボル推定
	}

	// デノイズされた画像を試行してデコード
	private static boolean attemptDenoisedDecoding(int index, String originalData) {
		for (int j = 0; j <= MAX_TRIES; j++) {
			try {
				ErasePositionReader.processRowAndSaveToFile(index, j); // 消失位置を取得

				BufferedImage denoisedImage = loadImage(DENOISED_IMAGE_PATH + NOISE_LEVEL + "/" + index + ".png");
				String denoisedData = QRCodeDecoder.decode(denoisedImage);

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
}