package data_analysis_method;
////QRコードを読み取って二値化して01の数字で出力

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.detector.Detector;

public class bitmatrix01 {
     public static void main( String[] args ) throws IOException, NotFoundException, FormatException
    {
        String pathname_denoise = "C:\\code\\QRreader\\qrreder\\AI2data\\sampledata8_0\\errorsample\\denoiseqr\\13.png"; 


        File file_denoise = new File(pathname_denoise);

        BufferedImage denoiseqr = ImageIO.read(file_denoise);
        
        LuminanceSource source_denoise = new BufferedImageLuminanceSource(denoiseqr);
        Binarizer binarizer_denoise = new HybridBinarizer(source_denoise);
        BinaryBitmap bitmap_denoise = new BinaryBitmap(binarizer_denoise);
       
        DetectorResult detectorResult_denoise = new Detector(bitmap_denoise.getBlackMatrix()).detect();
        BitMatrix bitmatrix_denoise = detectorResult_denoise.getBits();

        for(int i = 0; i < bitmatrix_denoise.getWidth(); i++){
            for(int j = 0; j < bitmatrix_denoise.getHeight(); j++){
                if(bitmatrix_denoise.get(i,j)){
                    System.out.print("1");
                }else{
                    System.out.print("0");
                }
                
            }
            System.out.println();
        }
    }

}
