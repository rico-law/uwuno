package com.learning.uwuno.unit;

import java.util.ArrayList;

public final class testUtils {

    private testUtils() {}

    public static String createJSON(ArrayList<String> keys, ArrayList<String> values) {
        if (keys.size() != values.size())
            throw new IllegalArgumentException();
        String json = "{ ";
        for (int i = 0; i < keys.size(); i++) {
            json += "\"" + keys.get(i) + "\":" + " \"" + values.get(i) + "\"";
            if (i != keys.size() - 1)
                json += ",";
        }
        json += " }";
        return json;
    }

    public static String createJSON(String key, String value) {
        return"{ \"" + key + "\": \"" + value + "\" }";
//        return"{ " + key + ": " + value + " }";
    }
}
