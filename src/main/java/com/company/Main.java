package com.company;

import cn.jimmiez.pcu.common.graphics.Octree;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

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

    static double basicShepardsMethod(Point3d x, float p) {
        double divident = 0;
        double devisor = 0;

        for (int i = 0; i < points.size(); i++){
            Point3d xk = points.get(i);
            double yk = pointsFuncValues.get(i);
            if (xk.equals(x)){
                return yk;
            }

            double wk = basicShepardsMethodWeight(x, xk, p);
            divident += wk * yk;
            devisor += wk;
        }

        return (divident / devisor);
    }

    static double basicShepardsMethodWeight(Point3d x, Point3d xk, float p){
        return (1 / Math.pow(x.distance(xk), p));
    }

    static double modifiedShepardsMethodWeight(Point3d x, Point3d xk, float r){
        double distToPoint = x.distance(xk);
        return Math.pow((Math.max(0, r - distToPoint) / r * distToPoint), 2);
    }

    static double modifiedShepardsMethod(Point3d x, float r, List<Integer> neighbours){
        double divident = 0;
        double devisor = 0;

        if (neighbours.size() == 1){
            return pointsFuncValues.get(neighbours.get(0));
        }

        for (int i = 0; i < neighbours.size(); i++){
            int xk_index = neighbours.get(i);
            Point3d xk = points.get(xk_index);
            double yk = pointsFuncValues.get(xk_index);
            if (xk.equals(x)){
                return yk;
            }

            double wk = modifiedShepardsMethodWeight(x, xk, r);

            divident += wk * yk;
            devisor += wk;
        }
        return (divident / devisor);
    }


    static void process(){
        if (method.equals("basic")) {
            float step_x = Math.abs(min_x - max_x) / res_x;
            float step_y = Math.abs(min_y - max_y) / res_y;
            float step_z = Math.abs(min_z - max_z) / res_z;

            for (int k = 0; k < res_z; k++){
                for (int j = 0; j < res_y; j++){
                    for (int i = 0; i < res_x; i++){
                        float x = min_x + i * step_x;
                        float y = min_y + j * step_y;
                        float z = min_x + k * step_z;
                        float f = (float) basicShepardsMethod(new Point3d(x, y, z), p);
                        byte[] buff = ByteBuffer.allocate(4).putFloat(f).array();
                        try {
                            System.out.write(buff);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if(method.equals("modified")){
            // Construct the octtree
            Octree octree = new Octree();
            octree.buildIndex(points);

            float step_x = Math.abs(min_x - max_x) / res_x;
            float step_y = Math.abs(min_y - max_y) / res_y;
            float step_z = Math.abs(min_z - max_z) / res_z;

            for (int k = 0; k < res_z; k++){
                for (int j = 0; j < res_y; j++){
                    for (int i = 0; i < res_x; i++){
                        float x = min_x + i * step_x;
                        float y = min_y + j * step_y;
                        float z = min_x + k * step_z;
                        Point3d currentPoint = new Point3d(x, y, z);
                        List<Integer> neighbours = new ArrayList<Integer>();
                        try {
                            neighbours = octree.searchAllNeighborsWithinDistance(currentPoint, r);
                        } catch (Exception e){
                            neighbours.clear();
                            neighbours.add(0, octree.searchNearestNeighbors(1, currentPoint)[0]);
                        }
                        double f = modifiedShepardsMethod(currentPoint, r, neighbours);
                        byte[] buff = ByteBuffer.allocate(4).putFloat((float) f).array();
                        try {
                            System.out.write(buff);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
            float x = Float.parseFloat(splitted[0]);
            float y = Float.parseFloat(splitted[1]);
            float z = Float.parseFloat(splitted[2]);
            float functionVal = Float.parseFloat(splitted[3]);
            points.add(new Point3d(x, y, z));
            pointsFuncValues.add(functionVal);
        }
    }
}



