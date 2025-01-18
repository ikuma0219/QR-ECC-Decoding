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
    public static void updateErasePositionFile(int targetRow, int attempt) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            int currentRow = 0;

            while ((line = reader.readLine()) != null) {
                if (currentRow == targetRow) {
                    String updatedLine = trimErasePositions(line, attempt);
                    saveToFile(updatedLine);
                    return;
                }
                currentRow++;
            }
        }
    }

    private static String trimErasePositions(String line, int attempt) {
        String[] elements = line.split(",");
        int newLength = Math.max(0, elements.length - (2 * attempt));
        String newLine = String.join(",", Arrays.copyOf(elements, newLength));
        System.out.println(newLine);
        return newLine;
    }

    private static void saveToFile(String content) throws IOException {
        try (FileWriter writer = new FileWriter(TXT_FILE_PATH)) {
            writer.write(content);
        }
    }

    // 追記(2024/3/19) 消失位置を得る
    public static int[] getErasePositionsFromFile() {
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