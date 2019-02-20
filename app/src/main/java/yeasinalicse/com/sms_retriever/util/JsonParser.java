package yeasinalicse.com.sms_retriever.util;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Yeasin on 13-Jun-18.
 */

public class JsonParser {
    public static String getString(JSONObject obj, String key){
        try {
            return obj.getString(key);
        } catch (Exception ex){
            Log.e("JSON ERROR", ex.getMessage());
        }
        return "";
    }

    public static int getInt(JSONObject obj, String key){
        try {
            return obj.getInt(key);
        } catch (Exception ex){
            Log.e("JSON ERROR", ex.getMessage());
        }
        return 0;
    }

    public static long getLong(JSONObject obj, String key){
        try {
            return obj.getLong(key);
        } catch (Exception ex){
            Log.e("JSON ERROR", ex.getMessage());
        }
        return 0L;
    }

    public static double getDouble(JSONObject obj, String key){
        try {
            return obj.getDouble(key);
        } catch (Exception ex){
            Log.e("JSON ERROR", ex.getMessage());
        }
        return 0f;
    }

    public static JSONObject getJSONObject(JSONObject obj, String key){
        try {
            return obj.getJSONObject(key);
        } catch (Exception ex){
            Log.e("JSON ERROR", ex.getMessage());
        }
        return new JSONObject();
    }

    public static JSONArray getJSONArray(JSONObject obj, String key){
        try {
            return obj.getJSONArray(key);
        } catch (Exception ex){
            Log.e("JSON ERROR", ex.getMessage());
        }
        return new JSONArray();
    }
}
