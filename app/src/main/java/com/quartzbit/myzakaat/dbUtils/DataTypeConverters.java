package com.quartzbit.myzakaat.dbUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.room.TypeConverter;


/**
 * Created by Jemsheer K D on 02 January, 2018.
 * Package in.techware.afcetcbus.database
 * Project AFS_Bus_App
 */

public class DataTypeConverters {

    @TypeConverter
    public static ArrayList<String> fromJSONToArrayList(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayListToJSON(ArrayList<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
