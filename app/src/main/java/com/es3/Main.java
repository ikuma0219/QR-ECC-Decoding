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
	private static final String DENOISED_IMAGE_PATH = "app/data/resourse/denoised/9.5/";

	public static void main(String[] args) {
		int successfulDecodes = 0;
		for (int i = 0; i < 200; i++) {
			try {
				File originalImageFile = new File(ORIGINAL_IMAGE_PATH + i + ".png");
				File denoisedImageFile= new File(DENOISED_IMAGE_PATH + i + ".png");

				BufferedImage originalImage = ImageIO.read(originalImageFile);
				BufferedImage denoisedImage = ImageIO.read(denoisedImageFile);

				// txt に i 行の消失位置を保存
				GetErrorSymbol.getErrorSymbol(i);

				String originalData = Decode.decodeQRCode(originalImage);
				String denoisedData = Decode.decodeQRCode(denoisedImage);

				System.out.println(i + ".png " + originalData + " " + denoisedData);

				if (denoisedData != null && denoisedData.equals(originalData)) {
					successfulDecodes++;
				}

			} catch (IOException e) {
				System.err.println("Error reading image: " + e.getMessage());
			} catch (NotFoundException | ChecksumException | FormatException e) {
				System.err.println(i + ".png " + e.getMessage());
			}
		}
		System.out.println("Total successful decodes: " + successfulDecodes);
	}
}