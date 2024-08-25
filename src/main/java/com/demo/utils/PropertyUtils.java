package com.demo.utils;

import com.demo.constants.FrameworkConstants;
import com.demo.enums.PropertiesType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class PropertyUtils {

    private PropertyUtils(){}

    private static Properties properties = new Properties();
    private static Map<String,String> propertyMap = new HashMap<>();

    static{
        try(FileInputStream inputStream = new FileInputStream(FrameworkConstants.getPropertyFilePath())){
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        properties.entrySet().forEach(e-> propertyMap.put(String.valueOf(e.getKey()), String.valueOf(e.getValue())));
    }

    public static String getvalue (PropertiesType key){
        return propertyMap.get(key.name().toLowerCase());
    }

}
