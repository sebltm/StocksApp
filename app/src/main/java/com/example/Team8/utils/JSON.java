package com.example.Team8.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JSON {
    private final String str_data;

    private HashMap data_obj = null;
    private Object[] data_arr = null;

    private String type = null;

    public JSON(String s) {
        str_data = s;
        try {

            if (isObj(s)) {
                type = "object";
                data_obj = (HashMap) toMap(new JSONObject(s));
            } else if (isArray(s)) {
                type = "array";
                data_arr = toArray(new JSONArray(s));
            }
        } catch (Exception e) {
        }
    }


    private boolean isArray(String r) {
        try {
            new JSONArray(r);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isObj(String r) {
        try {
            new JSONObject(r);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Object[] toArray(JSONArray jsonArray) throws JSONException {
        Object[] array = new Object[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);

            if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            if (value instanceof JSONArray) {
                value = toArray((JSONArray) value);
            }

            array[i] = value;
        }

        return array;
    }

    private Map<String, Object> toMap(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> iterator = jsonObject.keys();

        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            if (value instanceof JSONArray) {
                value = toArray((JSONArray) value);
            }

            map.put(key, value);
        }

        return map;
    }

    public Object[] getDataArray() {
        return data_arr;
    }

    public HashMap getDataObj() {
        return data_obj;
    }

    public String getType() {
        return type;
    }

    public String getStr_data() {
        return str_data;
    }
}
