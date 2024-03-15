import java.awt.image.BufferedImage;
import java.io.File;
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
	public static void main(String[] args) {
		int successfulDecodes = 0;
		for (int i = 0; i < 200; i++) {
			try {
				File path = new File("data/resourse/denoised/9.6/" + i + ".png");
				BufferedImage img = ImageIO.read(path);
				LuminanceSource source = new BufferedImageLuminanceSource(img);
				Binarizer bin = new HybridBinarizer(source);
				BinaryBitmap bitmap = new BinaryBitmap(bin);

				QRCodeReader reader = new QRCodeReader();
				Result result = reader.decode(bitmap);

				String data = result.getText();
				System.out.println(i + ".png " + data);
				successfulDecodes++;
			} catch (IOException | NotFoundException e) {
				System.err.println(i + ".png: " + e.getMessage());
			} catch (ChecksumException | FormatException e) {
				System.err.println(i + ".png: " + e.getMessage());
			}
		}
		System.out.println("Total successful decodes: " + successfulDecodes);
	}
}
