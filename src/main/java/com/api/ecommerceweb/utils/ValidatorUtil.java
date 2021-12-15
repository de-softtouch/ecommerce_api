package com.api.ecommerceweb.utils;

public class ValidatorUtil {

    public static boolean isBlankOrNull(String s) {
        if (s == null || s.trim().equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isNumeric(String s) {
        try {
            if (!isBlankOrNull(s)) {
                Double.parseDouble(s);
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return  false;
    }
}
