package com.es3;

import com.es3.service.DecoderService;

public class Main {
    public static void main(String[] args) throws Exception {
        DecoderService decoder = new DecoderService();
        int successfulDecodes = 0;

        for (int index = 0; index < 200; index++) {
            if (decoder.tryDecoding(index)) {
                successfulDecodes++;
            }
        }

        System.out.println("Total successful decodes: " + successfulDecodes);
    }
}