package com.company;

public class Point {
    private double x, y, z;
    private double functionVal;

    public Point(String line){
        getPointData(line);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getFunctionVal() {
        return functionVal;
    }

    private void getPointData(String line){
        String[] splitted = line.split(" ");
        this.x = Double.parseDouble(splitted[0]);
        this.y = Double.parseDouble(splitted[1]);
        this.z = Double.parseDouble(splitted[2]);
        this.functionVal = Double.parseDouble(splitted[3]);
    }
}
