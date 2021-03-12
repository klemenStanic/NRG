package com.company;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

public class OutputProcessing {
    public static void main(String[] args) {
        String inputFileName = args[0];
        byte[] fileContents = readFile(inputFileName);
        float[] floats = new float[fileContents.length / 4];

        for (int i = 0; i < fileContents.length; i+=4){
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.put(0, fileContents[i]);
            bb.put(1, fileContents[i+1]);
            bb.put(2, fileContents[i+2]);
            bb.put(3, fileContents[i+3]);
            floats[i / 4] = bb.getFloat();
            bb.clear();
        }


        float[] out = remap(floats);
        for (int i = 0; i < out.length; i++){
            byte b = (byte) (int) out[i];
            System.out.write(b);
        }



    }

    public static float[] remap(float[] floats){
        float[] out = new float[floats.length];
        float a = 0;
        float b = 1;
        int c = 0;
        int d = 255;

        for (int i = 0; i < floats.length; i++){
            if (floats[i] <= 0){
                out[i] = 0;
            } else if (floats[i] >= 1){
                out[i] = 255;
            } else {
                out[i] = (floats[i] - a) * ((d - c) / (b - a)) + c;
            }
        }
        return out;

    }

    // Method for getting the maximum value
    public static float getMax(float[] inputArray){
        float maxValue = inputArray[0];
        for(int i=1;i < inputArray.length;i++){
            if(inputArray[i] > maxValue){
                maxValue = inputArray[i];
            }
        }
        return maxValue;
    }

    // Method for getting the minimum value
    public static float getMin(float[] inputArray){
        float minValue = inputArray[0];
        for(int i=1;i<inputArray.length;i++){
            if(inputArray[i] < minValue){
                minValue = inputArray[i];
            }
        }
        return minValue;
    }

    static byte[] readFile(String filename){
        Path path = Path.of(filename);
        try {
            byte[] content = Files.readAllBytes(path);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
