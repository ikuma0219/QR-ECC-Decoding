import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.BitMatrixParser;
import com.google.zxing.qrcode.detector.Detector;

public class Analysis {

    // originalqrと比較してdenoiseqrのモジュールがいくつ間違えているかを数える
    public int error_module_count(BufferedImage originalqr, BufferedImage denoiseqr) {
        int errorcount = 0;

        LuminanceSource source_original = new BufferedImageLuminanceSource(originalqr);
        Binarizer binarizer_original = new HybridBinarizer(source_original);
        BinaryBitmap bitmap_original = new BinaryBitmap(binarizer_original);

        LuminanceSource source_denoise = new BufferedImageLuminanceSource(denoiseqr);
        Binarizer binarizer_denoise = new HybridBinarizer(source_denoise);
        BinaryBitmap bitmap_denoise = new BinaryBitmap(binarizer_denoise);
        try {
            DetectorResult detectorResult_original = new Detector(bitmap_original.getBlackMatrix()).detect();
            BitMatrix bitmatrix_original = detectorResult_original.getBits();
            DetectorResult detectorResult_denoise = new Detector(bitmap_denoise.getBlackMatrix()).detect();
            BitMatrix bitmatrix_denoise = detectorResult_denoise.getBits();
            for (int i = 0; i < bitmatrix_denoise.getWidth(); i++) {
                for (int j = 0; j < bitmatrix_denoise.getHeight(); j++) {
                    if (bitmatrix_denoise.get(i, j) != bitmatrix_original.get(i, j)) {
                        errorcount++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorcount;
    }

    // originalqrと比較してdenoiseqrの間違えたモジュールを抽出する
    public BufferedImage error_module_pos(BufferedImage originalqr, BufferedImage denoiseqr)
            throws NotFoundException, FormatException {

        int box_size = 8;
        int margin = 2;

        LuminanceSource source_original = new BufferedImageLuminanceSource(originalqr);
        Binarizer binarizer_original = new HybridBinarizer(source_original);
        BinaryBitmap bitmap_original = new BinaryBitmap(binarizer_original);

        LuminanceSource source_denoise = new BufferedImageLuminanceSource(denoiseqr);
        Binarizer binarizer_denoise = new HybridBinarizer(source_denoise);
        BinaryBitmap bitmap_denoise = new BinaryBitmap(binarizer_denoise);
        DetectorResult detectorResult_original = new Detector(bitmap_original.getBlackMatrix()).detect();
        BitMatrix bitmatrix_original = detectorResult_original.getBits();
        DetectorResult detectorResult_denoise = new Detector(bitmap_denoise.getBlackMatrix()).detect();
        BitMatrix bitmatrix_denoise = detectorResult_denoise.getBits();
        BitMatrix bitmatrix_error = new BitMatrix(
                ((bitmatrix_original.getHeight())) * box_size + 2 * margin * box_size);
        for (int i = 0; i < bitmatrix_denoise.getWidth(); i++) {
            for (int j = 0; j < bitmatrix_denoise.getHeight(); j++) {
                if (bitmatrix_denoise.get(i, j) != bitmatrix_original.get(i, j)) {
                    for (int i1 = i * box_size + margin * box_size; i1 < i * box_size + margin * box_size
                            + box_size; i1++) {
                        for (int j1 = j * box_size + margin * box_size; j1 < j * box_size + margin * box_size
                                + box_size; j1++) {
                            bitmatrix_error.set(i1, j1);
                        }
                    }
                }
            }
        }
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitmatrix_error);
        return image;
    }

    // originalqrと比較してdenoiseqrのシンボルがいくつ間違えているかを数える
    public int error_symbol_count(BufferedImage originalqr, BufferedImage denoiseqr)
            throws NotFoundException, FormatException {
        int errorcount = 0;
        boolean errorSymbol = false;

        LuminanceSource source_original = new BufferedImageLuminanceSource(originalqr);
        Binarizer binarizer_original = new HybridBinarizer(source_original);
        BinaryBitmap bitmap_original = new BinaryBitmap(binarizer_original);

        LuminanceSource source_denoise = new BufferedImageLuminanceSource(denoiseqr);
        Binarizer binarizer_denoise = new HybridBinarizer(source_denoise);
        BinaryBitmap bitmap_denoise = new BinaryBitmap(binarizer_denoise);

        DetectorResult detectorResult_original = new Detector(bitmap_original.getBlackMatrix()).detect();
        BitMatrix bitmatrix_original = detectorResult_original.getBits();
        DetectorResult detectorResult_denoise = new Detector(bitmap_denoise.getBlackMatrix()).detect();
        BitMatrix bitmatrix_denoise = detectorResult_denoise.getBits();

        BitMatrixParser parser_original = new BitMatrixParser(bitmatrix_original);

        int[][][] Symbol = parser_original.ModuleToSymbol();

        for (int i = 0; i < Symbol.length; i++) {
            for (int j = 0; j < Symbol[i].length; j++) {
                if (bitmatrix_denoise.get(Symbol[i][j][0], Symbol[i][j][1]) != bitmatrix_original.get(Symbol[i][j][0],
                        Symbol[i][j][1])) {
                    errorSymbol = true;
                }
            }
            if (errorSymbol) {
                errorcount++;
                errorSymbol = false;
            }
        }
        return errorcount;
    }

    // originalqrと比較してdenoiseqrの間違えたシンボルを抽出する
    public BufferedImage error_symbol_pos(BufferedImage originalqr, BufferedImage denoiseqr)
            throws NotFoundException, FormatException {

        int box_size = 8;
        int margin = 2;

        LuminanceSource source_original = new BufferedImageLuminanceSource(originalqr);
        Binarizer binarizer_original = new HybridBinarizer(source_original);
        BinaryBitmap bitmap_original = new BinaryBitmap(binarizer_original);

        LuminanceSource source_denoise = new BufferedImageLuminanceSource(denoiseqr);
        Binarizer binarizer_denoise = new HybridBinarizer(source_denoise);
        BinaryBitmap bitmap_denoise = new BinaryBitmap(binarizer_denoise);

        DetectorResult detectorResult_original = new Detector(bitmap_original.getBlackMatrix()).detect();
        BitMatrix bitmatrix_original = detectorResult_original.getBits();
        DetectorResult detectorResult_denoise = new Detector(bitmap_denoise.getBlackMatrix()).detect();
        BitMatrix bitmatrix_denoise = detectorResult_denoise.getBits();

        BitMatrixParser parser_original = new BitMatrixParser(bitmatrix_original);

        int[][][] Symbol = parser_original.ModuleToSymbol();

        BitMatrix bitmatrix_error_min = new BitMatrix(bitmatrix_original.getHeight());
        BitMatrix bitmatrix_error = new BitMatrix((bitmatrix_original.getHeight()) * box_size + 2 * margin * box_size);

        for (int i = 0; i < Symbol.length; i++) {
            for (int j = 0; j < Symbol[i].length; j++) {
                if (bitmatrix_denoise.get(Symbol[i][j][0], Symbol[i][j][1]) != bitmatrix_original.get(Symbol[i][j][0],
                        Symbol[i][j][1])) {
                    for (int k = 0; k < Symbol[i].length; k++) {
                        bitmatrix_error_min.set(Symbol[i][k][0], Symbol[i][k][1]);
                    }
                }
            }
        }

        for (int i = 0; i < bitmatrix_error_min.getWidth(); i++) {
            for (int j = 0; j < bitmatrix_error_min.getHeight(); j++) {
                if (bitmatrix_error_min.get(i, j)) {
                    for (int i1 = i * box_size + margin * box_size; i1 < i * box_size + margin * box_size
                            + box_size; i1++) {
                        for (int j1 = j * box_size + margin * box_size; j1 < j * box_size + margin * box_size
                                + box_size; j1++) {
                            bitmatrix_error.set(i1, j1);
                        }
                    }
                }
            }
        }
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitmatrix_error);
        return image;
    }

    // 数字の配列をcsvファイルで出力
    public void exportCsv(String filename, int[] number) throws IOException {

        // 出力ファイルの作成
        String file = filename + ".csv";
        FileWriter fw = new FileWriter(file, false);
        // PrintWriterクラスのオブジェクトを生成
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

        // データを書き込む
        for (int i = 0; i < number.length; i++) {
            pw.print(number[i]);
            if (i < number.length - 1) {
                pw.print(",");
            }
        }
        // ファイルを閉じる
        pw.close();
    }
}