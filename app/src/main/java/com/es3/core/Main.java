package com.es3.core;

import java.io.IOException;
import com.google.zxing.NotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            Processor processor = new Processor();
            processor.run();
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }
    }
}