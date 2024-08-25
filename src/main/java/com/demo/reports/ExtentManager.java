package com.demo.reports;

import com.aventstack.extentreports.ExtentTest;

public final class ExtentManager {

    private static ThreadLocal<ExtentTest> exTest = new ThreadLocal<>();

    private ExtentManager(){}

    public static ExtentTest getTest(){
        return exTest.get();
    }

    static void setTest(ExtentTest test){
       exTest.set(test);
    }
}
