package com.example.Team8.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.Team8.utils.ArrayUtils.parseObjToArr;
import static com.example.Team8.utils.ArrayUtils.toDoubleArr;
import static com.example.Team8.utils.ArrayUtils.toIntegerArr;
import static com.example.Team8.utils.ArrayUtils.toStringArr;

public class StockCandle {

    public static final ArrayList<StockCandle> stock_candles = new ArrayList<>();
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

    public StockCandle(HashMap<String, Object> data) {
        o = Arrays.asList(toDoubleArr(parseObjToArr(data.get("o"))));
        h = Arrays.asList(toDoubleArr(parseObjToArr(data.get("h"))));
        l = Arrays.asList(toDoubleArr(parseObjToArr(data.get("l"))));
        c = Arrays.asList(toDoubleArr(parseObjToArr(data.get("c"))));
        v = Arrays.asList(toIntegerArr(parseObjToArr(data.get("v"))));
        t = Arrays.asList(toStringArr(parseObjToArr(data.get("t"))));
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
