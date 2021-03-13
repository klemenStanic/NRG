package com.company;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream("output/output_uint8.raw"));
            for (int i = 0; i < out.length; i++){
                int integer = (int) out[i];
                dos.writeByte(integer);

                //System.out.println(long_);
                //System.out.write( (byte) (long_ & 0xFF));
                //System.out.write(((byte) integer) & 0xFF);
                //System.out.println((int) out[i]);
                //byte b = (byte) (int) out[i];
                //System.out.write((byte) integer & 0xFF);
            }
            dos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static float[] remap(float[] floats){
        float[] out = new float[floats.length];
        float a = getMin(floats);
        float b = getMax(floats);
        int c = 0;
        int d = 255;

        for (int i = 0; i < floats.length; i++){
            if (floats[i] <= 0){
                out[i] = 0;
            } else if (floats[i] >= 2){
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
