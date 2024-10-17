package com.es3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.es3.libs.Decode;
import com.es3.libs.SaveErasePosition_to_txt;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;

public class Main {

	private static final String ORIGINAL_IMAGE_PATH = "app/data/resourse/original/";
	private static final String DENOISED_IMAGE_PATH = "app/data/resourse/denoised/9.8/";

	public static void main(String[] args) {
		int successfulDecodes = 0;
		for (int i = 0; i < 200; i++) {
			int j = 0;
			String denoisedData = null;
			String originalData = null;
			while (true) {
				try {
					File originalImageFile = new File(ORIGINAL_IMAGE_PATH + i + ".png");
					File denoisedImageFile = new File(DENOISED_IMAGE_PATH + i + ".png");

					BufferedImage originalImage = ImageIO.read(originalImageFile);
					BufferedImage denoisedImage = ImageIO.read(denoisedImageFile);

					// originalDataは固定値なので、最初に取得しておく
					originalData = Decode.decodeQRCode(originalImage);

					// denoisedDataがoriginalDataと一致するまでループ

					// 現在のjに基づいて消失位置を取得
					SaveErasePosition_to_txt.getErrorSymbol(i, j);

					// denoisedDataをデコード
					denoisedData = Decode.decodeQRCode(denoisedImage);
					System.out.println(denoisedData);

					// denoisedDataがnullでない、かつoriginalDataと一致すればループを抜ける
					if (denoisedData != null && denoisedData.equals(originalData)) {
						// デコード成功した場合の処理
						System.out.println(i + ".png " + originalData + " " + denoisedData + " 復号成功！！！");
						successfulDecodes++;
						break;
					}
				} catch (IOException e) {
					System.err.println("Error reading image: " + e.getMessage());
				} catch (NotFoundException e) {
					System.err
							.println("NotFoundException " + e.getMessage());
				} catch (ChecksumException e) {
					System.err
							.println("ChecksumException " + e.getMessage());
				} catch (FormatException e) {
					System.err.println("FormatException " + e.getMessage());
				}
				if (j == 5) {
					System.out.println(i + ".png  " + "jが5に達したためループを終了します。");
					break;
				}
				j++;
			}

		}
		System.out.println("Total successful decodes: " + successfulDecodes);
	}
}