package com.example.Team8.utils;

import java.util.ArrayList;

public class ArrayUtils {
    public static Double[] toDoubleArr(Object[] arr) {
        ArrayList<Double> d = new ArrayList<>();
        for (Object o:arr) {
            d.add(Double.valueOf(String.valueOf(o)));
        }
        return d.toArray(new Double[0]);
    }

    public static String[] toStringArr(Object[] arr) {
        ArrayList<String> s = new ArrayList<>();
        for (Object o:arr) {
            s.add(String.valueOf(o));
        }
        return s.toArray(new String[0]);
    }

    public static Integer[] toIntegerArr(Object[] arr) {
        ArrayList<Integer> i = new ArrayList<>();
        for (Object o:arr) {
            i.add(Integer.valueOf(String.valueOf(o)));
        }
        return i.toArray(new Integer[0]);
    }
}
