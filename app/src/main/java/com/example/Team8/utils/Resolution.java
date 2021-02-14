package com.example.Team8.utils;

import java.util.HashMap;

public class Resolution {
    public static HashMap getAll(){
        Object[] r = {1, 5, 15, 30, 60, "D", "W", "M"};
        HashMap<String, String> h = new HashMap<>();
        for (Object o :r) {
            h.put(String.valueOf(o), String.valueOf(o));
        }
        return h;
    }
}
