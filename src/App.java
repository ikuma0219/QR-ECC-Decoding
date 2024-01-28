import java.awt.Graphics2D;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import java.io.File;



import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.detector.Detector;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;


public class App 
{
    public static void main( String[] args ) throws IOException
    {

		// //// QRコード生成。基本使わないが必要な時があるかもなので一応置いておく
        // String contents = "http://www.eng.kobe-u.ac.jp/";
		// System.out.println(contents);
		// try {
		// 	Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
      	// 	hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 	hints.put(EncodeHintType.QR_VERSION, 2);
		// 	QRCodeWriter writer = new QRCodeWriter();
		// 	BitMatrix bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, 256, 256, hints);
		// 	BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
		// 	ImageIO.write(image, "png", new File("example.png"));
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }

		// ////普通のQRコードリーダー
		// // 環境ごとにresourseがあるパスが違うので適宜変更
		// String pathname = "C:\\code\\QRreader\\qrreder\\AI2data\\resource_original_7_9\\0.png"; 
		// File file = new File(pathname);
		// BufferedImage image = ImageIO.read(file);
		// LuminanceSource source = new BufferedImageLuminanceSource(image);
		// Binarizer binarizer = new HybridBinarizer(source);
		// BinaryBitmap bitmap = new BinaryBitmap(binarizer);
		// QRCodeReader reader = new QRCodeReader();
		// try {
		// 	Result result = reader.decode(bitmap);
		// 	System.out.println(result.getText());
		// } catch (NotFoundException | ChecksumException | FormatException e) {
		// 	// TODO Auto-generated catch block
		// 	e.printStackTrace();
		// }


		// ////QRコードをresourseから複数読み取り,originalとdenoiseを比較してdenoiseの復号できない数を調べる。
		// //正しいQRコードと誤ったQRコードのそれぞれについてファイル番号を格納するリストと総数を格納するint
		// ArrayList<Integer> correctnum = new ArrayList<Integer>();
		// ArrayList<Integer> errornum = new ArrayList<Integer>();
		// int correctcount = 0;
		// int errorcount = 0;

		// for(int i = 0; i <= 199; i++) {
		// 	System.out.println(i);
		// 	// 環境ごとにresourseがあるパスが違うので適宜変更
		// 	String pathname_original = "C:\\code\\QRreader\\qrreder\\resource_original_8_0\\"+ i +".png"; 
		// 	String pathname_denoise = "C:\\code\\QRreader\\qrreder\\Blur\\sigma4_0\\"+ i +".png"; 

		// 	File file_original = new File(pathname_original);
		// 	File file_denoise = new File(pathname_denoise);
		// 	try {
		// 		BufferedImage image_original = ImageIO.read(file_original);
		// 		BufferedImage image_denoise = ImageIO.read(file_denoise);
				
		// 		LuminanceSource source_original = new BufferedImageLuminanceSource(image_original);
		// 		Binarizer binarizer_original = new HybridBinarizer(source_original);
		// 		BinaryBitmap bitmap_original = new BinaryBitmap(binarizer_original);
		// 		QRCodeReader reader_original = new QRCodeReader();
		// 		Result result_original = reader_original.decode(bitmap_original);
		// 		System.out.println(result_original.getText());

		// 		LuminanceSource source_denoise = new BufferedImageLuminanceSource(image_denoise);
		// 		Binarizer binarizer_denoise = new HybridBinarizer(source_denoise);
		// 		BinaryBitmap bitmap_denoise = new BinaryBitmap(binarizer_denoise);
		// 		QRCodeReader reader_denoise = new QRCodeReader();
		// 		Result result_denoise = reader_denoise.decode(bitmap_denoise);
		// 		System.out.println(result_denoise.getText());

		// 		if(result_original.getText().equals(result_denoise.getText())){
		// 			correctnum.add(i);
		// 		}

		// 	} catch (Exception e) {
		// 	errornum.add(i);
		// 	e.printStackTrace();
		// 	}
		// }

		// correctcount = correctnum.size();
		// errorcount = errornum.size();

		// System.out.println("correctnum:" + correctnum);
		// System.out.println("correctcount:" + correctcount);
		// System.out.println("errornum:" + errornum);
		// System.out.println("errorcount:" + errorcount);
		
		

		////QRコードをresourseから複数読み取り,originalとdenoiseを比較していろいろする。
		//正しいQRコードと誤ったQRコードのそれぞれについてファイル番号を格納するリストと総数を格納するint

		Analysis analysis = new Analysis();	
		
		ArrayList<Integer> correctnum = new ArrayList<Integer>();
		ArrayList<Integer> errornum = new ArrayList<Integer>();
		int correctcount = 0;
		int errorcount = 0;

		//各QRコードについて誤ったモジュールとシンボルの総数
		int errormodulenum = 0;
		int errorsymbolnum = 0;

		//各QRコードの誤ったモジュールとシンボルを片っ端から突っ込む
		ArrayList<Integer> errormodulenum_list = new ArrayList<Integer>();
		ArrayList<Integer> errorsymbolnum_list = new ArrayList<Integer>();

		//保存するサンプルの数をカウントする
		int samplecount = 0;

		for(int i = 0; i <= 199; i++) {
			System.out.println(i);
			// 環境ごとにresourseがあるパスが違うので適宜変更
			String pathname_original = "C:\\code\\QRreader\\qrreder\\AI2data\\resource8_0\\qrtest\\"+ i +".png";
			String pathname_noise = "C:\\code\\QRreader\\qrreder\\AI2data\\resource8_0\\noise\\"+ i +".png";
			String pathname_denoise = "C:\\code\\QRreader\\qrreder\\AI2data\\resource8_0\\denoise\\"+ i +".png"; 

			File file_original = new File(pathname_original);
			File file_noise = new File(pathname_noise);
			File file_denoise = new File(pathname_denoise);
			
			try {
				BufferedImage image_original = ImageIO.read(file_original);
				BufferedImage image_noise = ImageIO.read(file_noise);
				BufferedImage image_denoise = ImageIO.read(file_denoise);

				errormodulenum = analysis.error_module_count(image_original, image_denoise);
				errorsymbolnum = analysis.error_symbol_count(image_original, image_denoise);
				
				if(errorsymbolnum > 5){
					////誤りシンボルが6以上のQRコードとその誤りシンボルと誤りモジュールの位置を画像として保存

					//ノイズ補正したQRコードを二値化したものも欲しいのでここで用意
					LuminanceSource source_denoise = new BufferedImageLuminanceSource(image_denoise);
					Binarizer binarizer_denoise = new HybridBinarizer(source_denoise);
					BinaryBitmap bitmap_denoise = new BinaryBitmap(binarizer_denoise);
					BitMatrix bitmatrix_denoise = bitmap_denoise.getBlackMatrix();
					BufferedImage image_denoise_bin = MatrixToImageWriter.toBufferedImage(bitmatrix_denoise);

					BufferedImage errormodule_place = analysis.error_module_pos(image_original, image_denoise);
					BufferedImage errorsymbol_place = analysis.error_symbol_pos(image_original, image_denoise);

					//適宜パスを変更
					String pathname_denoiseqr = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\errorsample\\denoiseqr\\" + i + ".png";
					String pathname_denoisebinqr = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\errorsample\\denoisebinqr\\" + i + ".png";
					String pathname_originalqr = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\errorsample\\originalqr\\" + i + ".png";
					String pathname_noiseqr = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\errorsample\\noiseqr\\" + i + ".png";
					String pathname_errormodule_place = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\errorsample\\errormodplace\\" + i + ".png";
					String pathname_errorsymbol_place = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\errorsample\\errorsymplace\\" + i + ".png";
					ImageIO.write(image_denoise, "png", new File(pathname_denoiseqr));
					ImageIO.write(image_denoise_bin, "png", new File(pathname_denoisebinqr));
					ImageIO.write(image_original, "png", new File(pathname_originalqr));
					ImageIO.write(image_noise, "png", new File(pathname_noiseqr));
					ImageIO.write(errormodule_place, "png", new File(pathname_errormodule_place));
					ImageIO.write(errorsymbol_place, "png", new File(pathname_errorsymbol_place));
				}else if((errorsymbolnum <= 5)&&(samplecount < 10)){
					////正しく読み取れたQRコードについてもサンプルをとる

					//ノイズ補正したQRコードを二値化したものも欲しいのでここで用意
					LuminanceSource source_denoise = new BufferedImageLuminanceSource(image_denoise);
					Binarizer binarizer_denoise = new HybridBinarizer(source_denoise);
					BinaryBitmap bitmap_denoise = new BinaryBitmap(binarizer_denoise);
					BitMatrix bitmatrix_denoise = bitmap_denoise.getBlackMatrix();
					BufferedImage image_denoise_bin = MatrixToImageWriter.toBufferedImage(bitmatrix_denoise);

					BufferedImage errormodule_place = analysis.error_module_pos(image_original, image_denoise);
					BufferedImage errorsymbol_place = analysis.error_symbol_pos(image_original, image_denoise);

					//適宜パスを変更
					String pathname_denoiseqr = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\correctsample\\denoiseqr\\" + i + ".png";
					String pathname_denoisebinqr = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\correctsample\\denoisebinqr\\" + i + ".png";
					String pathname_originalqr = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\correctsample\\originalqr\\" + i + ".png";
					String pathname_noiseqr = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\correctsample\\noiseqr\\" + i + ".png";
					String pathname_errormodule_place = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\correctsample\\errormodplace\\" + i + ".png";
					String pathname_errorsymbol_place = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\correctsample\\errorsymplace\\" + i + ".png";
					ImageIO.write(image_denoise, "png", new File(pathname_denoiseqr));
					ImageIO.write(image_denoise_bin, "png", new File(pathname_denoisebinqr));
					ImageIO.write(image_original, "png", new File(pathname_originalqr));
					ImageIO.write(image_noise, "png", new File(pathname_noiseqr));
					ImageIO.write(errormodule_place, "png", new File(pathname_errormodule_place));
					ImageIO.write(errorsymbol_place, "png", new File(pathname_errorsymbol_place));

					samplecount++;

				}
	
				System.out.println("errormodulenum:" + errormodulenum);
				System.out.println("errorsymbolnum:" + errorsymbolnum);

				errormodulenum_list.add(errormodulenum);
				errorsymbolnum_list.add(errorsymbolnum);
				
				LuminanceSource source_original = new BufferedImageLuminanceSource(image_original);
				Binarizer binarizer_original = new HybridBinarizer(source_original);
				BinaryBitmap bitmap_original = new BinaryBitmap(binarizer_original);
				QRCodeReader reader_original = new QRCodeReader();
				Result result_original = reader_original.decode(bitmap_original);
				System.out.println(result_original.getText());

				LuminanceSource source_denoise = new BufferedImageLuminanceSource(image_denoise);
				Binarizer binarizer_denoise = new HybridBinarizer(source_denoise);
				BinaryBitmap bitmap_denoise = new BinaryBitmap(binarizer_denoise);
				QRCodeReader reader_denoise = new QRCodeReader();
				Result result_denoise = reader_denoise.decode(bitmap_denoise);
				System.out.println(result_denoise.getText());

				if(result_original.getText().equals(result_denoise.getText())){
					correctnum.add(i);
				}

			} catch (Exception e) {
			errornum.add(i);
			e.printStackTrace();
			}
		}

		int[] errormodulenum_data = new int[40];
		int[] errorsymbolnum_data = new int[40];

		for(int i = 0; i < errormodulenum_data.length; i++){
			for(int j = 0; j < errormodulenum_list.size(); j++){
				if(errormodulenum_list.get(j) == i){
					errormodulenum_data[i]++;
				}
			}
			System.out.print(errormodulenum_data[i] + ",");
		}
		System.out.println();



		for(int i = 0; i < errorsymbolnum_data.length; i++){
			for(int j = 0; j < errorsymbolnum_list.size(); j++){
				if(errorsymbolnum_list.get(j) == i){
					errorsymbolnum_data[i]++;
				}
			}
			System.out.print(errorsymbolnum_data[i] + ",");
		}
		System.out.println();

		analysis.exportCsv("error_module_data", errormodulenum_data);
		analysis.exportCsv("error_symbol_data", errorsymbolnum_data);

		correctcount = correctnum.size();
		errorcount = errornum.size();

		System.out.println("correctnum:" + correctnum);
		System.out.println("correctcount:" + correctcount);
		System.out.println("errornum:" + errornum);
		System.out.println("errorcount:" + errorcount);
		

    }
}