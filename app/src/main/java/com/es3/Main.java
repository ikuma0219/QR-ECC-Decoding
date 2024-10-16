package com.es3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.es3.libs.Decode;
import com.es3.libs.GetErrorSymbol;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;

public class Main {

	private static final String ORIGINAL_IMAGE_PATH = "app/data/resourse/original/";
	private static final String DENOISED_IMAGE_PATH = "app/data/resourse/denoised/9.0/";

	public static void main(String[] args) {
		int successfulDecodes = 0;
		for (int i = 0; i < 200; i++) {
			int j = 0; // jを初期化
			String denoisedData = null;
			String originalData = null;

			try {
				File originalImageFile = new File(ORIGINAL_IMAGE_PATH + i + ".png");
				File denoisedImageFile = new File(DENOISED_IMAGE_PATH + i + ".png");

				BufferedImage originalImage = ImageIO.read(originalImageFile);
				BufferedImage denoisedImage = ImageIO.read(denoisedImageFile);

				// originalDataは固定値なので、最初に取得しておく
				originalData = Decode.decodeQRCode(originalImage);

				// denoisedDataがoriginalDataと一致するまでループ
				while (true) {
					// 現在のjに基づいて消失位置を取得
					GetErrorSymbol.getErrorSymbol(i, j);

					// denoisedDataをデコード
					denoisedData = Decode.decodeQRCode(denoisedImage);

					// denoisedDataがnullでない、かつoriginalDataと一致すればループを抜ける
					if (denoisedData != null && denoisedData.equals(originalData)) {
						break;
					}

					// 一致しない場合、jをインクリメントして再試行
					j++;
				}

				// デコード成功した場合の処理
				System.out.println(i + ".png " + originalData + " " + denoisedData);

				if (denoisedData != null && denoisedData.equals(originalData)) {
					successfulDecodes++;
				}

			} catch (IOException e) {
				System.err.println("Error reading image: " + e.getMessage());
			} catch (NotFoundException e) {
				System.err.println(i + ".png - NotFoundException: QR code not found in the image. " + e.getMessage());
			} catch (ChecksumException e) {
				System.err.println(i + ".png - ChecksumException: Data corrupted in the QR code. " + e.getMessage());
			} catch (FormatException e) {
				System.err.println(i + ".png - FormatException: QR code format invalid. " + e.getMessage());
			}
		}
		System.out.println("Total successful decodes: " + successfulDecodes);
	}
}