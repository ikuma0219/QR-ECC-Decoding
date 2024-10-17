package com.es3.libs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetErasePosition {
    // 追記(2024/3/19) 消失位置を得る
    public static int[] getErasePosition() {
        List<Integer> rowData = new ArrayList<>();
        try {
            FileReader reader = new FileReader("app/temp/target_eraseposition.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                for (String value : values) {
                    rowData.add(Integer.parseInt(value.trim()));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rowData.stream().mapToInt(Integer::intValue).toArray();
    }
}