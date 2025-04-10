package com.cis.palm360.database;

/**
 * Created by Majid Golshadi on 4/14/2014.
 */
public class SqlString {

    public static String Int(int number){
        return "'"+number+"'";
    }

    public static String String(String name){
        return "'"+name+"'";
    }

    public static String count = "count(*)";
}
