package ru.hotel72.utils;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 09.02.13
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class DataTransfer {
    private static HashMap<String, Object> data = new HashMap<String, Object>();

    public static Object get(String key){
        if(data.containsKey(key)){
            Object obj = data.get(key);
            data.remove(key);
            return obj;
        }
        return null;
    }

    public static boolean put(String key, Object obj){
        if(data.containsKey(key))
            return false;

        data.put(key, obj);
        return true;
    }
}
