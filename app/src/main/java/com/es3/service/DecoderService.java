package com.es3.service;

import com.es3.libs.*;
import com.es3.utils.PathManager;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class DecoderService {
    private static final int MAX_RETRY = 5;

    public boolean tryDecoding(int index) throws Exception {

        for (int attempt = 0; attempt <= MAX_RETRY; attempt++) {
            System.out.println("try" + attempt);
            try {
                String originalData = decodeImage(PathManager.getOriginalImagePath(index));

                List<Integer> eraseSymbols = EraseCandidateSelector.getEraseSymbol(index,
                        PathManager.NOISE_LEVEL);
                List<Integer> trimmed = ErasePositionProcessor.trimErasePositions(eraseSymbols, attempt);
                System.out.println(trimmed.toString());
                ErasePositionHolder.setErasePositions(trimmed);

                // 画像を読み込み
                BufferedImage originalImage = ImageIO.read(new File(PathManager.getOriginalImagePath(index)));
                BufferedImage denoisedImage = ImageIO.read(new File(PathManager.getDenoisedImagePath(index)));
                BufferedImage resizedoriginalImage = ImageUtil.resizeImage(originalImage, 8);
                BufferedImage resizeddenoisedImage = ImageUtil.resizeImage(denoisedImage, 8);

                // シンボル領域はdenoisedImage、シンボル領域以外はoriginalImageの値で合成
                BufferedImage mergedImage = mergeSymbolAndOriginal(index, resizedoriginalImage, resizeddenoisedImage);

                // 合成画像でデコード
                BinaryBitmap bitmap = new BinaryBitmap(
                        new HybridBinarizer(new BufferedImageLuminanceSource(mergedImage)));
                String mergedData = new QRCodeReader().decode(bitmap).getText();

                if (mergedData != null && mergedData.equals(originalData)) {
                    System.out.println(index + ".png: " + mergedData + " 復号成功！！！");
                    return true;
                }

            } catch (ArrayIndexOutOfBoundsException | IOException | NotFoundException | ChecksumException | FormatException e) {
            }
        }
        System.out.println(index + ".png: デコード失敗");
        return false;
    }

    private static String decodeImage(String filePath)
            throws NotFoundException, ChecksumException, FormatException, IOException {
        BufferedImage image = ImageIO.read(new File(filePath));
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        return new QRCodeReader().decode(bitmap).getText();
    }

    /**
     * シンボル領域はdenoisedImage、シンボル領域以外はoriginalImageの値で合成
     * 
     * @param index         対象画像のインデックス
     * @param originalImage 元画像
     * @param denoisedImage ノイズ除去画像
     * @return 合成画像
     */
    public static BufferedImage mergeSymbolAndOriginal(
            int index,
            BufferedImage originalImage,
            BufferedImage denoisedImage) throws Exception {

        // シンボル領域座標リストを取得
        List<int[][]> symbolList = com.es3.libs.SymbolList.getSymbolList(index);
        List<int[]> symbolPixels = new ArrayList<>();
        for (int[][] symbol : symbolList) {
            for (int[] coord : symbol) {
                symbolPixels.add(coord); // coord[0]=x, coord[1]=y
            }
        }

        BufferedImage result = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);

        // 1. 全体をoriginalImageでコピー
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                int value = originalImage.getRaster().getSample(x, y, 0);
                result.getRaster().setSample(x, y, 0, value);
            }
        }

        // 2. シンボル領域だけdenoisedImageの値で上書き
        for (int[] coord : symbolPixels) {
            int x = coord[0];
            int y = coord[1];
            // 範囲外チェック
            if (x >= 0 && x < denoisedImage.getWidth() && y >= 0 && y < denoisedImage.getHeight()) {
                int value = denoisedImage.getRaster().getSample(x, y, 0);
                result.getRaster().setSample(x, y, 0, value);
            }
        }
        return result;
    }
}