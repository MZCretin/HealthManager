package com.roll.healthmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class HealthManagerSPUtils {
    public static final String NAME = "sdk_health_manager_config";
    private static HealthManagerSPUtils instance = new HealthManagerSPUtils();
    private static SharedPreferences mSp;

    //单例
    private HealthManagerSPUtils() {

    }

    //得到单例
    public static HealthManagerSPUtils getInstance(Context context) {
        if (instance == null) {
            instance = new HealthManagerSPUtils();

        }
        if(mSp == null){
            mSp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        return instance;
    }

    //保存
    public void save(String key, Object value) {
        if (value instanceof String) {
            mSp.edit().putString(key, (String) value).commit();
        } else if (value instanceof Boolean) {
            mSp.edit().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Integer) {
            mSp.edit().putInt(key, (Integer) value).commit();
        }
    }

    //获取String类型数据
    public String getString(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    //获取Boolean类型数据
    public Boolean getBoolean(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }//获取Int类型数据

    public int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }

}
