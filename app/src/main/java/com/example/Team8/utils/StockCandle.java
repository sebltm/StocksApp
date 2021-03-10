package com.example.Team8.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.Team8.utils.ArrayUtils.toDoubleArr;
import static com.example.Team8.utils.ArrayUtils.toIntegerArr;
import static com.example.Team8.utils.ArrayUtils.toStringArr;

public class StockCandle {

    //    List of open prices for returned candles.
    private final List<Double> o;

    //    List of high prices for returned candles.
    private final List<Double> h;

    //    List of low prices for returned candles.
    private final List<Double> l;

    //    List of close prices for returned candles.
    private final List<Double> c;

    //    List of volume data for returned candles.
    private final List<Integer> v;

    //    List of timestamp for returned candles.
    private final List<String> t;

    public static ArrayList<StockCandle> stock_candles = new ArrayList<>();

    public StockCandle(HashMap<String, ?> data) {
        //TODO deal with all the possible null values here, e.g data.getOrDefault()
        o = Arrays.asList(toDoubleArr((Object[]) data.get("o")));
        h = Arrays.asList(toDoubleArr((Object[]) data.get("h")));
        l = Arrays.asList(toDoubleArr((Object[]) data.get("l")));
        c = Arrays.asList(toDoubleArr((Object[]) data.get("c")));
        v = Arrays.asList(toIntegerArr((Object[]) data.get("v")));
        t = Arrays.asList(toStringArr((Object[]) data.get("t")));
        stock_candles.add(this);
    }

    public static String[] getNames() {
        return new String[]{"o", "h", "l", "c", "v", "t"};
    }

    public List<Double> getO() {
        return o;
    }

    public List<Double> getH() {
        return h;
    }

    public List<Double> getL() {
        return l;
    }

    public List<Double> getC() {
        return c;
    }

    public List<Integer> getV() {
        return v;
    }

    public List<String> getT() {
        return t;
    }
}
