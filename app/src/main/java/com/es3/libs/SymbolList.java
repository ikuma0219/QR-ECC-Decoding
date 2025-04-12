package com.es3.libs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.es3.utils.PathManager;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.BitMatrixParser;
import com.google.zxing.qrcode.detector.Detector;

public class SymbolList {

    public static List<int[][]> getSymbolList(int index) throws IOException, NotFoundException, FormatException {
        String imagePath = PathManager.getDenoisedImagePath(index);
        BufferedImage original = ImageIO.read(new File(imagePath));
        BufferedImage resized = ImageUtil.resizeImage(original, 8);
        LuminanceSource source = ImageUtil.createLuminanceSource(resized);

        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        DetectorResult detection = new Detector(bitmap.getBlackMatrix()).detect();

        BitMatrixParser parser = new BitMatrixParser(detection.getBits());
        int[][][] symbols = parser.ModuleToSymbol();

        List<int[][]> symbolList = new ArrayList<>();
        for (int[][] symbol : symbols) {
            symbolList.add(symbol);
        }
        return symbolList;
    }
}
