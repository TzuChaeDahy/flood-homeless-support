package com.tzuchaedahy.util;

public class Regex {
    public static boolean isPhoneValid(String phone) {
        String regex = "^[0-9]{11}$";
        return phone.matches(regex);
    }

    public static boolean isEmailValid(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(regex);
    }
}
