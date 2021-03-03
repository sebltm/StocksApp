package com.example.Team8.utils;

import java.util.HashMap;

public class Resolution {
    public static final HashMap<String, String> types = new HashMap<String, String>() {{
        Object[] r = {1, 5, 15, 30, 60, "D", "W", "M"};
        for (Object o : r) {
            put(String.valueOf(o), String.valueOf(o));
        }
    }};
}
