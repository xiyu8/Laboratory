package com.jason.crypt;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;


public final class Jsons {
    public static final Gson GSON_DIS_HTML = new GsonBuilder().disableHtmlEscaping().create();
    public static final Gson GSON_MAP = new GsonBuilder().enableComplexMapKeySerialization().create();
    public static final Gson GSON_Expose = new GsonBuilder().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();


    public static synchronized String toJson(Object bean) {
        return GSON_DIS_HTML.toJson(bean);
    }

    public static synchronized String mapToJson(Map map) {
        return GSON_MAP.toJson(map);
    }

    public static synchronized <T> T fromJson(String json, Class<T> clazz) {
        try{
            return GSON_DIS_HTML.fromJson(json, clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String toJsonExpose(Object obj) {
        if (obj == null) {
            return "";
        }
        try{
            return GSON_Expose.toJson(obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T parseJsonWithExpose(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return GSON_Expose.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static synchronized <T> T fromJson(String json, Type type) {
        try{
            return GSON_DIS_HTML.fromJson(json, type);
        }catch (Exception e){
        }
        return null;

    }

    public static synchronized <T> T mapFromJson(String json, Type type) {
        try{
            return GSON_MAP.fromJson(json, type);
        }catch (Exception e){
        }
        return null;

    }

    public static String toJson(Map<String, String> map) {
        if (map == null || map.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder(64 * map.size());
        sb.append('{');
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        if (it.hasNext()) {
            append(it.next(), sb);
        }
        while (it.hasNext()) {
            sb.append(',');
            append(it.next(), sb);
        }
        sb.append('}');
        return sb.toString();
    }

    private static void append(Map.Entry<String, String> entry, StringBuilder sb) {
        String key = entry.getKey(), value = entry.getValue();
        if (value == null) value = "";
        sb.append('"').append(key).append('"');
        sb.append(':');
        sb.append('"').append(value).append('"');
    }
}
