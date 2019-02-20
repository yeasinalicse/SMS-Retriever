package yeasinalicse.com.sms_retriever.util;

import org.json.JSONObject;

/**
 * Created by Yeasin on 8/20/17.
 */

public class AppResponse {
    private String code;
    private String message;
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private AppResponse(){}

    public static AppResponse build(JSONObject data){
        AppResponse ar = new AppResponse();
        try{
            ar.setCode(data.getString("code"));
            ar.setMessage(data.getString("message"));
            ar.setData(data.get("data"));

        } catch (Exception ex){}

        return ar;
    }

    public static String SUCCESS = "01";
    public static String ERROR = "00";
    public static String DUPLICATE = "02";
}
