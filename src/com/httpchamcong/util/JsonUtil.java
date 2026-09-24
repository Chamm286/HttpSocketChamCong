package com.httpchamcong.util;

public class JsonUtil {

    public static String getString(String json, String key) {
        if (json == null || key == null) return null;

        String pattern = "\"" + key + "\"";
        int i = json.indexOf(pattern);
        if (i < 0) return null;

        int colon = json.indexOf(':', i + pattern.length());
        if (colon < 0) return null;

        int start = json.indexOf('"', colon + 1);
        if (start < 0) return null;

        int end = json.indexOf('"', start + 1);
        if (end < 0) return null;

        return json.substring(start + 1, end);
    }

    public static void main(String[] args) {
        String json = "{\"username\":\"chamnee\",\"password\":\"Chamnee@2025\"}";
        System.out.println("username = " + getString(json, "username"));
        System.out.println("password = " + getString(json, "password"));
    }
} 
