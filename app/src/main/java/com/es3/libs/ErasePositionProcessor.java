package com.es3.libs;

import java.util.ArrayList;
import java.util.List;

public class ErasePositionProcessor {

    // 試行回数に応じて消失位置リストを削る（2 * attempt）
    public static List<Integer> trimErasePositions(List<Integer> originalList, int attempt) {
        int newLength = Math.max(0, originalList.size() - (2 * attempt));
        return new ArrayList<>(originalList.subList(0, newLength));
    }
}