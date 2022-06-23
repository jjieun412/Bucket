package com.example.bucket;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SharedPrefManager {


    //데이터를 내부 저장소에 저장하기
    public static void setPreference(Context context, String key, String value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setListPreference(Context context, String key, List<String> value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.apply();
    }

    public static void setArrayListPreference(Context context, String key, ArrayList<String> value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        JSONArray jsonArray = new JSONArray();
        for(int i=0; i<value.size(); i++){
            jsonArray.put(value.get(i));
        }
        if(!value.isEmpty()) {
            editor.putString(key, jsonArray.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }



    //내부저장소 저장된 데이터 가져오기
    public static String getPreferenceString(Context context, String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, "");
    }

    public static List<String> getPreferenceList(Context context, String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = pref.getString("key", null);
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> list = gson.fromJson(json, type);
        return list;
    }

    public static ArrayList<String> getPreferenceArrayList(Context context, String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String json = pref.getString("key", null);
        ArrayList<String> value = new ArrayList<String>();
        if(json != null) {
            try{
                JSONArray jsonArray = new JSONArray(json);
                for(int i=0; i<jsonArray.length(); i++) {
                    String data = jsonArray.optString(i);
                    value.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}