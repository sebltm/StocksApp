package com.example.Team8.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.Team8.utils.ArrayUtils.toStringArr;

public class PricePoint {

    //    List of open prices for returned candles.
    private List<DataPoint> open = new ArrayList<>();

    //    List of high prices for returned candles.
    private List<DataPoint> high = new ArrayList<>();

    //    List of low prices for returned candles.
    private List<DataPoint> low = new ArrayList<>();

    //    List of close prices for returned candles.
    private List<DataPoint> close = new ArrayList<>();

    //    List of timestamp for returned candles.
    private List<String> timestamps = new ArrayList<>();

    public PricePoint() {
    }

    public PricePoint(HashMap data) {
        this.setData(data);
    }

    public void setData(HashMap data) {
        Object[] open_arr = (Object[]) data.get("o");
        Object[] high_arr = (Object[]) data.get("h");
        Object[] low_arr = (Object[]) data.get("l");
        Object[] close_arr = (Object[]) data.get("c");
        String[] timestamps_arr = toStringArr((Object[]) data.get("t"));
        timestamps = Arrays.asList(timestamps_arr);

        for (int i = 0; i < timestamps_arr.length; i++) {
            open.add(new DataPoint(
                    new BigDecimal(Double.valueOf(String.valueOf(open_arr[i]))),
                    DateTimeHelper.toDateTime(timestamps_arr[i])
            ));
            high.add(new DataPoint(
                    new BigDecimal(Double.valueOf(String.valueOf(high_arr[i]))),
                    DateTimeHelper.toDateTime(timestamps_arr[i])
            ));
            low.add(new DataPoint(
                    new BigDecimal(Double.valueOf(String.valueOf(low_arr[i]))),
                    DateTimeHelper.toDateTime(timestamps_arr[i])
            ));
            close.add(new DataPoint(
                    new BigDecimal(Double.valueOf(String.valueOf(close_arr[i]))),
                    DateTimeHelper.toDateTime(timestamps_arr[i])
            ));
        }
    }

    public List<DataPoint> getOpen() {
        return open;
    }

    public List<DataPoint> getHigh() {
        return high;
    }

    public List<DataPoint> getLow() {
        return low;
    }

    public List<DataPoint> getClose() {
        return close;
    }

    public List<String> getTimestamps() {
        return timestamps;
    }
}
