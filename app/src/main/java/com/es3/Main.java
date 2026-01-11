package com.es3;

import com.es3.service.DecoderService;
import com.es3.service.DecoderService.DecodeResult;

public class Main {
    public static void main(String[] args) throws Exception {
        DecoderService decoder = new DecoderService();

        int successfulDecodes = 0;
        int misCorrections = 0;
        int failureCount = 0;

        for (int index = 0; index < 200; index++) {
            DecodeResult result = decoder.tryDecoding(index);

            if (result == DecodeResult.SUCCESS) {
                successfulDecodes++;
            } else if (result == DecodeResult.MIS_CORRECTION) {
                misCorrections++;
            } else {
                failureCount++;
            }
        }

        System.out.println("Total successful decodes: " + successfulDecodes);
        System.out.println("Total mis-corrections: " + misCorrections);
        System.out.println("Total failures: " + failureCount);
    }
}