package com.example.Team8.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.Team8.utils.ArrayUtils.getArrItemOrDefault;
import static com.example.Team8.utils.ArrayUtils.parseObjToArr;
import static com.example.Team8.utils.ArrayUtils.toStringArr;

public class PricePoint implements Serializable {

    //    List of open prices for returned candles.
    private final List<DataPoint> open = new ArrayList<>();

    //    List of high prices for returned candles.
    private final List<DataPoint> high = new ArrayList<>();

    //    List of low prices for returned candles.
    private final List<DataPoint> low = new ArrayList<>();

    //    List of close prices for returned candles.
    private final List<DataPoint> close = new ArrayList<>();

    //    List of timestamp for returned candles.
    private List<String> timestamps = new ArrayList<>();

    public PricePoint() {
    }

    public PricePoint(HashMap<String, Object> data) {
        this.setData(data);
    }

    public void setData(HashMap<String, Object> data) {
        Object[] open_arr = parseObjToArr(data.get("o"));
        Object[] high_arr = parseObjToArr(data.get("h"));
        Object[] low_arr = parseObjToArr(data.get("l"));
        Object[] close_arr = parseObjToArr(data.get("c"));
        String[] timestamps_arr = toStringArr(parseObjToArr(data.get("t")));
        timestamps = Arrays.asList(timestamps_arr);

        for (int i = 0; i < timestamps_arr.length; i++) {
            open.add(new DataPoint(
                    new BigDecimal(String.valueOf(getArrItemOrDefault(open_arr, i, 0.0))),
                    DateTimeHelper.toDateTime(timestamps_arr[i])
            ));
            high.add(new DataPoint(
                    new BigDecimal(String.valueOf(getArrItemOrDefault(high_arr, i, 0.0))),
                    DateTimeHelper.toDateTime(timestamps_arr[i])
            ));
            low.add(new DataPoint(
                    new BigDecimal(String.valueOf(getArrItemOrDefault(low_arr, i, 0.0))),
                    DateTimeHelper.toDateTime(timestamps_arr[i])
            ));
            close.add(new DataPoint(
                    new BigDecimal(String.valueOf(getArrItemOrDefault(close_arr, i, 0.0))),
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
