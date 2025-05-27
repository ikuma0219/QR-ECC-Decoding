package com.es3.libs;

import java.util.List;

public class ErasePositionHolder {
    private static List<Integer> currentErasePositions;

    public static void setErasePositions(List<Integer> positions) {
        currentErasePositions = positions;
    }

    public static List<Integer> getErasePositions() {
        return currentErasePositions != null ? currentErasePositions : List.of();
    }
}
