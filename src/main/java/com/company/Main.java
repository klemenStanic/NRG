package com.company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.vecmath.Point3d;

public class Main {

    static String method;
    static float p;
    static float r;
    static float min_x;
    static float min_y;
    static float min_z;
    static float max_x;
    static float max_y;
    static float max_z;
    static int res_x;
    static int res_y;
    static int res_z;

    static ArrayList<Point3d> points = new ArrayList<>();
    static ArrayList<Float> pointsFuncValues = new ArrayList<Float>();


    public static void main(String[] args) {
        CommandOptions cmdOpt = new CommandOptions(args);

        // Get command line arguments and parse them into variables
        setParams(cmdOpt);

        // Read points data from std. in and store them in 2 arrayLists, one for points, another for f. vals
        readFromStdIn();

        process();

    }

    static float basicShepardsMethod(Point3d x, float p) {
        double divident = 0;
        double devisor = 0;

        for (int i = 0; i < points.size(); i++){
            Point3d xk = points.get(i);
            float yk = pointsFuncValues.get(i);
            if (xk.equals(x)){
                return yk;
            }

            float wk = basicShepardsMethodWeight(x, xk, p);
            divident += wk * yk;
            devisor += wk;
        }

        return (float) (divident / devisor);
    }

    static float basicShepardsMethodWeight(Point3d x, Point3d xk, float p){
        return (float) (1 / Math.pow(x.distance(xk), p));
    }

    static void process(){
        if (method.equals("basic")) {
            int i = 0;
            for (double z = min_z; z < max_z; z += Math.abs(min_z - max_z) / res_z){
                for (double y = min_y; y < max_y; y += Math.abs(min_y - max_y) / res_y){
                    for (double x = min_x; x < max_x; x += Math.abs(min_x - max_x) / res_x){
                        System.out.write((byte) basicShepardsMethod(new Point3d(x, y, z), p));
                    }
                }
            }
        }
    }

    static void setParams(CommandOptions cmdOpt){
        method = cmdOpt.getMethod();
        p = cmdOpt.getP();
        r = cmdOpt.getR();

        min_x = cmdOpt.getMin_x();
        min_y = cmdOpt.getMin_y();
        min_z = cmdOpt.getMin_z();

        max_x = cmdOpt.getMax_x();
        max_y = cmdOpt.getMax_y();
        max_z = cmdOpt.getMax_z();

        res_x = cmdOpt.getRes_x();
        res_y = cmdOpt.getRes_y();
        res_z = cmdOpt.getRes_z();
    }

    static void readFromStdIn(){
        Scanner inputScanner = new Scanner(System.in);
        while(inputScanner.hasNext()){
            String line = inputScanner.nextLine();

            String[] splitted = line.split(" ");
            double x = Double.parseDouble(splitted[0]);
            double y = Double.parseDouble(splitted[1]);
            double z = Double.parseDouble(splitted[2]);
            float functionVal = Float.parseFloat(splitted[3]);
            points.add(new Point3d(x, y, z));
            pointsFuncValues.add(functionVal);
        }
    }
}



