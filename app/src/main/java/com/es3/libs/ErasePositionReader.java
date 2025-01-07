package com.es3.libs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ErasePositionReader {
    private static final String CSV_FILE_PATH = "app/temp/list_eraseposition.csv";
    private static final String TXT_FILE_PATH = "app/temp/target_eraseposition.txt";

    // CSVファイルからデータを取得し、結果をテキストファイルに保存
    public static String processRowAndSaveToFile(int targetRow, int j) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            int currentRow = 0;

            while ((line = reader.readLine()) != null) {
                if (currentRow == targetRow) {
                    String[] elements = line.split(",");

                    int elementsToRemove = 2 * j;

                    int newLength = Math.max(0, elements.length - elementsToRemove);

                    String[] trimmedElements = Arrays.copyOf(elements, newLength);

                    String newLine = String.join(",", trimmedElements);

                    try (FileWriter writer = new FileWriter(TXT_FILE_PATH)) {
                        writer.write(newLine);
                    }
                    System.out.println("try " + j);
                    System.out.println(newLine);
                    return newLine;
                }
                currentRow++;
            }
        }
        return null;
    }

    // 追記(2024/3/19) 消失位置を得る
    public static int[] readErasePositionsFromTxtFile() {
        List<Integer> erasePositions = new ArrayList<>();
        try {
            FileReader reader = new FileReader(TXT_FILE_PATH);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                for (String value : values) {
                    erasePositions.add(Integer.parseInt(value.trim()));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return erasePositions.stream().mapToInt(Integer::intValue).toArray();
    }
}