package com.es3.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtils {

    /**
     * 指定したパスから画像を読み込む
     *
     * @param filePath 読み込む画像のパス
     * @return BufferedImage 画像オブジェクト
     * @throws IOException ファイル読み込みエラー
     */
    public static BufferedImage loadImage(String filePath) throws IOException {
        File imageFile = new File(filePath);
        return ImageIO.read(imageFile);
    }

    /**
     * 画像を指定されたサイズにリサイズする
     *
     * @param image 変換する画像
     * @param newWidth 新しい幅
     * @param newHeight 新しい高さ
     * @return BufferedImage リサイズ後の画像
     */
    public static BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resizedImage;
    }
}
