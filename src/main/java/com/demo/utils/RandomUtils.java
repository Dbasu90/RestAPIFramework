package com.demo.utils;

public final class RandomUtils {

    //business layer --> all the business level changes

    private RandomUtils(){}

    public static int getId(){
        return FakerUtils.getNumber(10,100);
    }

    public static String getFullName(){
        return FakerUtils.getFullName().toUpperCase();
    }

    public static String getFirstName(){
        return FakerUtils.getFirstName().toLowerCase();
    }


}
