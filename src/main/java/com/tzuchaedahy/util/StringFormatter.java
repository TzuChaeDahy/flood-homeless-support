package com.tzuchaedahy.util;

public class StringFormatter {
    public static String capitalize(String text) {
        if (text != null) {
            var firstLetter = text.substring(0,1).toUpperCase();
            var rest = text.substring(1);

            return firstLetter + rest;
        }

        return text;
    }
}
