package com.company;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class CommandOptions {

    ArrayList arguments;

    String method = "";
    private float p = 0;
    private float r = 0;
    private float min_x = 0;
    private float min_y = 0;
    private float min_z = 0;

    private float max_x = 0;
    private float max_y = 0;
    private float max_z = 0;

    private int res_x = 0;
    private int res_y = 0;
    private int res_z = 0;

    public CommandOptions(String[] args) {
        parse(args);

        this.method = valueOf("--method");

        this.p = Float.parseFloat(valueOf("--p"));
        this.r = Float.parseFloat(valueOf("--r"));

        this.min_x = Float.parseFloat(valueOf("--min-x"));
        this.min_y = Float.parseFloat(valueOf("--min-y"));
        this.min_z = Float.parseFloat(valueOf("--min-z"));

        this.max_x = Float.parseFloat(valueOf("--max-x"));
        this.max_y = Float.parseFloat(valueOf("--max-y"));
        this.max_z = Float.parseFloat(valueOf("--max-z"));

        this.res_x = Integer.parseInt(valueOf("--res-z"));
        this.res_y = Integer.parseInt(valueOf("--res-y"));
        this.res_z = Integer.parseInt(valueOf("--res-z"));

    }

    public void parse(String[] args)
    {
        arguments = new ArrayList();
        for ( int i = 0; i < args.length; i++ ) {
            arguments.add(args[i]);
        }
    }

    public String getMethod() {
        return method;
    }

    public float getP() {
        return p;
    }

    public float getR() {
        return r;
    }

    public float getMin_x() {
        return min_x;
    }

    public float getMin_y() {
        return min_y;
    }

    public float getMin_z() {
        return min_z;
    }

    public float getMax_x() {
        return max_x;
    }

    public float getMax_y() {
        return max_y;
    }

    public float getMax_z() {
        return max_z;
    }

    public int getRes_x() {
        return res_x;
    }

    public int getRes_y() {
        return res_y;
    }

    public int getRes_z() {
        return res_z;
    }

    public String valueOf(String option) {
        String value = null;
        String str;

        for ( int i = 0; i < arguments.size(); i++ ) {
            str = (String)arguments.get(i);
            if (str.equalsIgnoreCase(option)) {
                value = (String)arguments.get(i+1);
                break;
            }
        }

        return value;
    }
}
