package com.example.Team8.utils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ArrayUtils {
    public static Double[] toDoubleArr(Object[] arr) {
        ArrayList<Double> d = new ArrayList<>();
        for (Object o : arr) {
            d.add(Double.valueOf(String.valueOf(o)));
        }
        return d.toArray(new Double[0]);
    }

    public static String[] toStringArr(Object[] arr) {
        ArrayList<String> s = new ArrayList<>();
        for (Object o : arr) {
            s.add(String.valueOf(o));
        }
        return s.toArray(new String[0]);
    }

    public static Integer[] toIntegerArr(Object[] arr) {
        ArrayList<Integer> i = new ArrayList<>();
        for (Object o : arr) {
            i.add(Integer.valueOf(String.valueOf(o)));
        }
        return i.toArray(new Integer[0]);
    }

    public static DataPoint[] toDataPointArr(Object[] arr, String[] timestamps) {
        ArrayList<DataPoint> d_p_l = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            double price_val = Double.parseDouble(String.valueOf(arr[i]));
            d_p_l.add(new DataPoint(
                    new BigDecimal(price_val),
                    DateTimeHelper.toDateTime(timestamps[i])
            ));
        }
        return d_p_l.toArray(new DataPoint[0]);
    }

    public static double[] doubleArr(Object[] arr) {
        double[] d = new double[arr.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = (double) arr[i];
        }
        return d;
    }

    public static Object[] parseObjToArr(Object obj) {
        Object[] o = (Object[]) obj;
        return o != null ? o : new Object[0];
    }

    public static Object getArrItemOrDefault(Object[] arr, int index, Object value) {
        return arr != null && index < arr.length ? arr[index] : value;
    }
}
