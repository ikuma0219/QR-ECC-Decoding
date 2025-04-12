package com.es3.libs;

import java.util.ArrayList;
import java.util.List;

public class ErasePositionProcessor {

    // 試行回数に応じて消失位置リストを削る（2 * attempt）
    public static List<Integer> trimErasePositions(List<Integer> originalList, int attempt) {
        int newLength = Math.max(0, originalList.size() - (2 * attempt));
        return new ArrayList<>(originalList.subList(0, newLength));
    }

    // 文字列のCSV形式の行をList<Integer>に変換（例："1,2,3" → [1,2,3]）
    public static List<Integer> parseErasePositionLine(String line) {
        List<Integer> result = new ArrayList<>();
        for (String s : line.split(",")) {
            try {
                result.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException ignored) {
            }
        }
        return result;
    }

    // リストをint配列に変換（必要なら）
    public static int[] convertToArray(List<Integer> eraseList) {
        return eraseList.stream().mapToInt(Integer::intValue).toArray();
    }
}
