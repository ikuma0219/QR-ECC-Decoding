package com.es3.libs;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

public class ErasePositionWriter {
    private static final String DENOISED_IMAGE_PATH = "app/data/resourse/denoised/";
    // private static final String INVERTED_IMAGE_PATH = "app/data/resourse/inverted/";
    private static final String CSV_FILE_PATH = "app/temp/list_eraseposition.csv";

    public static void eraseSymbolList(String noiseLevel, int brightnessThreshold)
            throws IOException, NotFoundException {

        List<int[][]> symbols = new ArrayList<>();
        symbols.add(new int[][] { { 26, 26 }, { 26, 25 }, { 25, 26 }, { 25, 25 }, { 24, 26 }, { 24, 25 }, { 23, 26 }, { 23, 25 } });
        symbols.add(new int[][] { { 22, 26 }, { 22, 25 }, { 21, 26 }, { 21, 25 }, { 20, 26 }, { 20, 25 }, { 19, 26 }, { 19, 25 } });
        symbols.add(new int[][] { { 18, 26 }, { 18, 25 }, { 17, 26 }, { 17, 25 }, { 16, 26 }, { 16, 25 }, { 15, 26 }, { 15, 25 } });
        symbols.add(new int[][] { { 14, 26 }, { 14, 25 }, { 13, 26 }, { 13, 25 }, { 12, 26 }, { 12, 25 }, { 11, 26 }, { 11, 25 } });
        symbols.add(new int[][] { { 11, 24 }, { 11, 23 }, { 12, 24 }, { 12, 23 }, { 13, 24 }, { 13, 23 }, { 14, 24 }, { 14, 23 } });
        symbols.add(new int[][] { { 15, 24 }, { 15, 23 }, { 16, 24 }, { 16, 23 }, { 17, 24 }, { 17, 23 }, { 18, 24 }, { 18, 23 } });
        symbols.add(new int[][] { { 19, 24 }, { 19, 23 }, { 20, 24 }, { 20, 23 }, { 21, 24 }, { 21, 23 }, { 22, 24 }, { 22, 23 } });
        symbols.add(new int[][] { { 23, 24 }, { 23, 23 }, { 24, 24 }, { 24, 23 }, { 25, 24 }, { 25, 23 }, { 26, 24 }, { 26, 23 } });
        symbols.add(new int[][] { { 26, 22 }, { 26, 21 }, { 25, 22 }, { 25, 21 }, { 24, 22 }, { 24, 21 }, { 23, 22 }, { 23, 21 } });
        symbols.add(new int[][] { { 17, 22 }, { 17, 21 }, { 16, 22 }, { 16, 21 }, { 15, 22 }, { 15, 21 }, { 14, 22 }, { 14, 21 } });
        symbols.add(new int[][] { { 13, 22 }, { 13, 21 }, { 12, 22 }, { 12, 21 }, { 11, 22 }, { 11, 21 }, { 11, 20 }, { 11, 19 } });
        symbols.add(new int[][] { { 12, 20 }, { 12, 19 }, { 13, 20 }, { 13, 19 }, { 14, 20 }, { 14, 19 }, { 15, 20 }, { 15, 19 } });
        symbols.add(new int[][] { { 16, 20 }, { 16, 19 }, { 17, 20 }, { 17, 19 }, { 23, 20 }, { 23, 19 }, { 24, 20 }, { 24, 19 } });
        symbols.add(new int[][] { { 25, 20 }, { 25, 19 }, { 26, 20 }, { 26, 19 }, { 26, 18 }, { 26, 17 }, { 25, 18 }, { 25, 17 } });
        symbols.add(new int[][] { { 24, 18 }, { 24, 17 }, { 23, 18 }, { 23, 17 }, { 22, 17 }, { 21, 17 }, { 20, 17 }, { 19, 17 } });
        symbols.add(new int[][] { { 18, 17 }, { 17, 18 }, { 17, 17 }, { 16, 18 }, { 16, 17 }, { 15, 18 }, { 15, 17 }, { 14, 18 } });
        symbols.add(new int[][] { { 14, 17 }, { 13, 18 }, { 13, 17 }, { 12, 18 }, { 12, 17 }, { 11, 18 }, { 11, 17 }, { 10, 18 } });
        symbols.add(new int[][] { { 10, 17 }, { 9, 18 }, { 9, 17 }, { 7, 18 }, { 7, 17 }, { 6, 18 }, { 6, 17 }, { 5, 18 } });
        symbols.add(new int[][] { { 5, 17 }, { 4, 18 }, { 4, 17 }, { 3, 18 }, { 3, 17 }, { 2, 18 }, { 2, 17 }, { 2, 16 } });
        symbols.add(new int[][] { { 2, 15 }, { 3, 16 }, { 3, 15 }, { 4, 16 }, { 4, 15 }, { 5, 16 }, { 5, 15 }, { 6, 16 } });
        symbols.add(new int[][] { { 6, 15 }, { 7, 16 }, { 7, 15 }, { 9, 16 }, { 9, 15 }, { 10, 16 }, { 10, 15 }, { 11, 16 } });
        symbols.add(new int[][] { { 11, 15 }, { 12, 16 }, { 12, 15 }, { 13, 16 }, { 13, 15 }, { 14, 16 }, { 14, 15 }, { 15, 16 } });
        symbols.add(new int[][] { { 15, 15 }, { 16, 16 }, { 16, 15 }, { 17, 16 }, { 17, 15 }, { 18, 16 }, { 18, 15 }, { 19, 16 } });
        symbols.add(new int[][] { { 19, 15 }, { 20, 16 }, { 20, 15 }, { 21, 16 }, { 21, 15 }, { 22, 16 }, { 22, 15 }, { 23, 16 } });
        symbols.add(new int[][] { { 23, 15 }, { 24, 16 }, { 24, 15 }, { 25, 16 }, { 25, 15 }, { 26, 16 }, { 26, 15 }, { 26, 14 } });
        symbols.add(new int[][] { { 26, 13 }, { 25, 14 }, { 25, 13 }, { 24, 14 }, { 24, 13 }, { 23, 14 }, { 23, 13 }, { 22, 14 } });
        symbols.add(new int[][] { { 22, 13 }, { 21, 14 }, { 21, 13 }, { 20, 14 }, { 20, 13 }, { 19, 14 }, { 19, 13 }, { 18, 14 } });
        symbols.add(new int[][] { { 18, 13 }, { 17, 14 }, { 17, 13 }, { 16, 14 }, { 16, 13 }, { 15, 14 }, { 15, 13 }, { 14, 14 } });
        symbols.add(new int[][] { { 14, 13 }, { 13, 14 }, { 13, 13 }, { 12, 14 }, { 12, 13 }, { 11, 14 }, { 11, 13 }, { 10, 14 } });
        symbols.add(new int[][] { { 10, 13 }, { 9, 14 }, { 9, 13 }, { 7, 14 }, { 7, 13 }, { 6, 14 }, { 6, 13 }, { 5, 14 } });
        symbols.add(new int[][] { { 5, 13 }, { 4, 14 }, { 4, 13 }, { 3, 14 }, { 3, 13 }, { 2, 14 }, { 2, 13 }, { 2, 12 } });
        symbols.add(new int[][] { { 2, 11 }, { 3, 12 }, { 3, 11 }, { 4, 12 }, { 4, 11 }, { 5, 12 }, { 5, 11 }, { 6, 12 } });
        symbols.add(new int[][] { { 6, 11 }, { 7, 12 }, { 7, 11 }, { 9, 12 }, { 9, 11 }, { 10, 12 }, { 10, 11 }, { 11, 12 } });
        symbols.add(new int[][] { { 11, 11 }, { 12, 12 }, { 12, 11 }, { 13, 12 }, { 13, 11 }, { 14, 12 }, { 14, 11 }, { 15, 12 } });
        symbols.add(new int[][] { { 15, 11 }, { 16, 12 }, { 16, 11 }, { 17, 12 }, { 17, 11 }, { 18, 12 }, { 18, 11 }, { 19, 12 } });
        symbols.add(new int[][] { { 19, 11 }, { 20, 12 }, { 20, 11 }, { 21, 12 }, { 21, 11 }, { 22, 12 }, { 22, 11 }, { 23, 12 } });
        symbols.add(new int[][] { { 23, 11 }, { 24, 12 }, { 24, 11 }, { 25, 12 }, { 25, 11 }, { 26, 12 }, { 26, 11 }, { 18, 10 } });
        symbols.add(new int[][] { { 18, 9 }, { 17, 10 }, { 17, 9 }, { 16, 10 }, { 16, 9 }, { 15, 10 }, { 15, 9 }, { 14, 10 } });
        symbols.add(new int[][] { { 14, 9 }, { 13, 10 }, { 13, 9 }, { 12, 10 }, { 12, 9 }, { 11, 10 }, { 11, 9 }, { 11, 7 } });
        symbols.add(new int[][] { { 11, 6 }, { 12, 7 }, { 12, 6 }, { 13, 7 }, { 13, 6 }, { 14, 7 }, { 14, 6 }, { 15, 7 } });
        symbols.add(new int[][] { { 15, 6 }, { 16, 7 }, { 16, 6 }, { 17, 7 }, { 17, 6 }, { 18, 7 }, { 18, 6 }, { 18, 5 } });
        symbols.add(new int[][] { { 18, 4 }, { 17, 5 }, { 17, 4 }, { 16, 5 }, { 16, 4 }, { 15, 5 }, { 15, 4 }, { 14, 5 } });
        symbols.add(new int[][] { { 14, 4 }, { 13, 5 }, { 13, 4 }, { 12, 5 }, { 12, 4 }, { 11, 5 }, { 11, 4 }, { 11, 3 } });
        symbols.add(new int[][] { { 11, 2 }, { 12, 3 }, { 12, 2 }, { 13, 3 }, { 13, 2 }, { 14, 3 }, { 14, 2 }, { 15, 3 } });

        for (int i = 0; i < 200; i++) {
            try {
                BufferedImage denoisedImage = ImageIO.read(new File(DENOISED_IMAGE_PATH + noiseLevel + "/" + i + ".png"));
                if (denoisedImage == null) continue;

                int[][] denoisedImageArray = resizeAndConvertToArray(denoisedImage);
                List<Integer> errorSymbols = calculateErases(denoisedImageArray, symbols, brightnessThreshold);
                saveErrorSymbolsToCsv(errorSymbols);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static int[][] resizeAndConvertToArray(BufferedImage image) {
        int newWidth = image.getWidth() / 8, newHeight = image.getHeight() / 8;
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);
        
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, newWidth, newHeight, null);
        g.dispose();
        
        LuminanceSource source = new BufferedImageLuminanceSource(resizedImage);
        int[][] array = new int[newHeight][newWidth];
        byte[] matrix = source.getMatrix();
        
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                array[y][x] = matrix[y * newWidth + x] & 0xFF;
            }
        }
        return array;
    }

    private static List<Integer> calculateErases(int[][] denoisedImage, List<int[][]> symbols, int brightnessThreshold) {
        Map<Integer, Double> symbolMinBrightness = new HashMap<>();

        for (int i = 0; i < symbols.size(); i++) {
            double minConfidence = symbols.get(i).length > 0 ? Double.MAX_VALUE : -1;
            
            for (int[] point : symbols.get(i)) {
                int x = point[0], y = point[1];
                if (x >= 0 && x < denoisedImage.length && y >= 0 && y < denoisedImage[0].length) {
                    int brightness = denoisedImage[x][y];
                    if (brightness >= 78 && brightness <= 198) {
                        minConfidence = Math.min(minConfidence, Math.abs(brightnessThreshold - brightness));
                    }
                }
            }
            if (minConfidence != -1) symbolMinBrightness.put(i, minConfidence);
        }
        List<Integer> outputSymbols = symbolMinBrightness.entrySet().stream()
        .sorted(Map.Entry.comparingByValue())
        .limit(10)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());

        // もし10個未満なら、残りのシンボルから補充（同じ処理を再利用）
        if (outputSymbols.size() < 10) {
            Set<Integer> selectedSymbols = new HashSet<>(outputSymbols);

            for (int i = 0; i < symbols.size(); i++) {
                if (!selectedSymbols.contains(i)) {
                    double minConfidence = symbols.get(i).length > 0 ? Double.MAX_VALUE : -1;

                    for (int[] point : symbols.get(i)) {
                        int x = point[0], y = point[1];
                        if (x >= 0 && x < denoisedImage.length && y >= 0 && y < denoisedImage[0].length) {
                            int brightness = denoisedImage[x][y];
                            double confidence = Math.abs(brightnessThreshold - brightness);
                            minConfidence = Math.min(minConfidence, confidence);
                        }
                    }
                    if (minConfidence != -1) {
                        outputSymbols.add(i);
                        selectedSymbols.add(i);
                    }

                    if (outputSymbols.size() >= 10) break;
                }
            }
        }
        return outputSymbols;
    }

    private static void saveErrorSymbolsToCsv(List<Integer> errorSymbols) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {
            writer.write(errorSymbols.stream().map(String::valueOf).collect(Collectors.joining(",")) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearCsvFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


// for (int i = 0; i < 200; i++) {
//             try {
//                 File denoisedImageFile = new File(DENOISED_IMAGE_PATH + noiseLevel + "/" + i + ".png");
//                 BufferedImage denoisedImage = ImageIO.read(denoisedImageFile);

//                 int newWidth = denoisedImage.getWidth() / 8;
//                 int newHeight = denoisedImage.getHeight() / 8;
//                 int[][] denoisedImageArray = new int[newHeight][newWidth]; // 2次元配列を作成

//                 BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);
//                 Graphics2D g = resizedImage.createGraphics();
//                 g.drawImage(denoisedImage, 0, 0, newWidth, newHeight, null); // 元の画像をリサイズしてコピー
//                 g.dispose();
//                 LuminanceSource source = new BufferedImageLuminanceSource(resizedImage); // リサイズ後の画像に対してLuminanceSourceを適用

//                 for (int y = 0; y < newHeight; y++) {
//                     for (int x = 0; x < newWidth; x++) {
//                         int luminance = source.getMatrix()[y * newWidth + x] & 0xFF; // ピクセルのグレースケール値を取得
//                         denoisedImageArray[y][x] = luminance; // 2次元配列に格納
//                     }
//                 }
//                 List<Integer> errorSymbols = calculateErases(denoisedImageArray, symbols, brightnessThreshold, noiseLevel, i);
//                 saveErrorSymbolsToCsv(errorSymbols);

//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
//         }
//     }

//     public static List<Integer> calculateErases(int[][] denoisedImage, List<int[][]> symbols, int brightnessThreshold, String noiseLevel, int i) {
//         // 各シンボルごとの信頼度
//         Map<Integer, Double> symbolMinBrightness = new HashMap<>();
//         for (int symbolIndex = 0; symbolIndex < symbols.size(); symbolIndex++) {
//             int[][] symbol = symbols.get(symbolIndex);
//             double minConfidence = Double.MAX_VALUE; // 信頼度が最小の brightness を追跡
//             boolean validSymbol = false;

//             for (int[] point : symbol) {
//                 int x = point[0];
//                 int y = point[1];
//                 // 範囲チェック
//                 if (x >= 0 && x < denoisedImage.length && y >= 0 && y < denoisedImage[0].length) {
//                     int brightness = denoisedImage[x][y];
//                     if (brightness >= 78 && brightness <= 198) {
//                         double confidence = Math.abs(brightnessThreshold - brightness);
//                         minConfidence = Math.min(minConfidence, confidence);
//                         validSymbol = true;
//                     }
//                 }
//             }
//             if (validSymbol) {
//                 symbolMinBrightness.put(symbolIndex, minConfidence);
//             }
//         }

//         // 輝度差合計が小さい順にソート
//         List<Map.Entry<Integer, Double>> sortedSymbols = new ArrayList<>(symbolMinBrightness.entrySet());
//         sortedSymbols.sort(Map.Entry.comparingByValue());

//         // 上位10個のシンボルインデックスを取得
//         List<Integer> outputSymbols = new ArrayList<>();
//         for (int j = 0; j < Math.min(10, sortedSymbols.size()); j++) {
//             outputSymbols.add(sortedSymbols.get(j).getKey());
//         }

//         // もし10未満なら、消失候補以外のシンボルから補充
//         if (outputSymbols.size() < 10) {
//             Set<Integer> selectedSymbols = new HashSet<>(outputSymbols);
//             for (int symbolIndex = 0; symbolIndex < symbols.size(); symbolIndex++) {
//                 if (!selectedSymbols.contains(symbolIndex)) {
//                     int[][] symbol = symbols.get(symbolIndex);
//                     double minConfidence = Double.MAX_VALUE;
//                     boolean validSymbol = false;

//                     for (int[] point : symbol) {
//                         int x = point[0];
//                         int y = point[1];
//                         if (x >= 0 && x < denoisedImage.length && y >= 0 && y < denoisedImage[0].length) {
//                             int brightness = denoisedImage[x][y];
//                             double confidence = Math.abs(brightnessThreshold - brightness);
//                             minConfidence = Math.min(minConfidence, confidence);
//                             validSymbol = true;
//                         }
//                     }

//                     if (validSymbol) {
//                         symbolMinBrightness.put(symbolIndex, minConfidence);
//                         outputSymbols.add(symbolIndex);
//                         selectedSymbols.add(symbolIndex);
//                         if (outputSymbols.size() >= 10)
//                             break;
//                     }
//                 }
//             }
//         }
//         System.out.println("Final outputSymbols: " + outputSymbols);

//         // // 復号処理: 140を閾値として白（255）または黒（0）に変換
//         // decodeSymbols(denoisedImage, symbols, outputSymbols, INVERTED_IMAGE_PATH +
//         //         noiseLevel + "/" + i + ".png");

//         return outputSymbols;
//     }

//     // 信頼度計算
//     public static double calculateTheta(int L, int brightnessThreshold) {
//         int s = brightnessThreshold;
//         double theta;
//         if (L < s) {
//             theta = (double) (s - L) / s;
//         } else {
//             theta = (double) (L - s) / (255 - s);
//         }
//         return theta;
//     }

//     // public static void decodeSymbols(int[][] denoisedImage, List<int[][]> symbols, List<Integer> erasedSymbols,
//     //         String outputFilePath) {
//     //     // 輝度を変換
//     //     for (int symbolIndex = 0; symbolIndex < symbols.size(); symbolIndex++) {
//     //         int[][] symbol = symbols.get(symbolIndex);

//     //         for (int[] point : symbol) {
//     //             int x = point[0];
//     //             int y = point[1];

//     //             // 範囲チェック
//     //             if (x >= 0 && x < denoisedImage.length && y >= 0 && y < denoisedImage[0].length) {
//     //                 int brightness = denoisedImage[x][y];

//     //                 // 消失候補かどうかを判定
//     //                 if (erasedSymbols.contains(symbolIndex)) {
//     //                     // 消失候補はそのまま維持
//     //                     continue;
//     //                 } else {
//     //                     // 140を基準に白（255）または黒（0）に変換
//     //                     if (brightness < 138) {
//     //                         denoisedImage[x][y] = 0; // 黒
//     //                     } else {
//     //                         denoisedImage[x][y] = 255; // 白
//     //                     }
//     //                 }
//     //             }
//     //         }
//     //     }

//     //     // 変換後の画像をファイルに保存
//     //     saveImageAsPNG(denoisedImage, outputFilePath, 232, 232);
//     // }

//     // // 画像をPNGとして保存するメソッド
//     // public static void saveImageAsPNG(int[][] image, String outputFilePath, int newWidth, int newHeight) {
//     //     int height = image.length;
//     //     int width = image[0].length;

//     //     // BufferedImage を作成
//     //     BufferedImage bufferedImage = new BufferedImage(width, height,
//     //             BufferedImage.TYPE_BYTE_GRAY);

//     //     for (int y = 0; y < height; y++) {
//     //         for (int x = 0; x < width; x++) {
//     //             int value = image[y][x];
//     //             int rgb = (value << 16) | (value << 8) | value; // グレースケール
//     //             bufferedImage.setRGB(x, y, rgb);
//     //         }
//     //     }
    
//     //     // ファイルに保存
//     //     try {
//     //         File outputFile = new File(outputFilePath);
//     //         ImageIO.write(bufferedImage, "png", outputFile);
//     //         System.out.println("Image saved to: " + outputFilePath);
//     //     } catch (IOException e) {
//     //         System.err.println("Error saving image: " + e.getMessage());
//     //     }
//     // }

//     private static void saveErrorSymbolsToCsv(List<Integer> errorSymbols) {
//         String csvFilePath = "app/temp/list_eraseposition.csv";
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {
//             String line = errorSymbols.stream()
//                     .map(String::valueOf)
//                     .collect(Collectors.joining(","));
//             writer.write(line + "\n");
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     public static void clearCsvFile() {
//         String csvFilePath = "app/temp/list_eraseposition.csv";
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
//             writer.write(""); // 空の内容で上書き
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }