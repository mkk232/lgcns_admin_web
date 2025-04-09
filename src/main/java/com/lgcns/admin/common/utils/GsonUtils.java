package com.lgcns.admin.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    private static final Gson gson = gsonBuilder.setPrettyPrinting().create();

    public GsonUtils() {}

    public static String toJson(Object object) {
        return gson.toJson(object);
    }
}
