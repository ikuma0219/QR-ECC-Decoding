package com;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

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

public class App {
	private static final String CSV_FILE = "temp/error_symbols.csv";
	private static final String SAVE_FILE = "temp/save_eraseposition.txt";
	private static final String ORIGINAL_IMAGE_PATH = "data/resourse/original/";
	private static final String DENOISED_IMAGE_PATH = "data/resourse/denoised/10.5/";

	public static void main(String[] args) {
		int successfulDecodes = 0;
		for (int i = 0; i < 200; i++) {
			try {
				File originalImageFile = new File(ORIGINAL_IMAGE_PATH + i + ".png");
				File denoisedImageFile= new File(DENOISED_IMAGE_PATH + i + ".png");

				BufferedImage originalImage = ImageIO.read(originalImageFile);
				BufferedImage denoisedImage = ImageIO.read(denoisedImageFile);

				String errorSymbol = getErrorSymbol(i);

				String originalData = decodeQRCode(originalImage, errorSymbol, i);
				String denoisedData = decodeQRCode(denoisedImage, errorSymbol, i);

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

	private static String getErrorSymbol(int targetRow) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
			String line;
			int currentRow = 0;
			while ((line = reader.readLine()) != null) {
				if (currentRow == targetRow) {
					try (FileWriter writer = new FileWriter(SAVE_FILE)) {
						writer.write(line);
					}
					return line;
				}
				currentRow++;
			}
		}
		return null;
	}

	private static String decodeQRCode(BufferedImage image, String errorSymbol, int i)
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