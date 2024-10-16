package com.es3.libs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GetErrorSymbol {
    // CSVファイルから消失シンボルのデータをとる
    private static final String CSV_FILE = "app/temp/error_symbols.csv";
    private static final String SAVE_FILE = "app/temp/save_eraseposition.txt";

    public static String getErrorSymbol(int targetRow) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            int currentRow = 0;
            while ((line = reader.readLine()) != null) {
                if (currentRow == targetRow) {
                    try (FileWriter writer = new FileWriter(SAVE_FILE)) {
                        writer.write(line);
                    }
                    return line;
                }
                currentRow++;
            }
        }
        return null;
    }

}
