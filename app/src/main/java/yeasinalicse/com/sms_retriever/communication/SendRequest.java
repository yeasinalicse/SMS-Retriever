package yeasinalicse.com.sms_retriever.communication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Yeasin on 7/5/2016.
 */
public class SendRequest {

    public static void postJSONData(final Context context) {


        JSONObject jsonObject = new JSONObject();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, "URL HERE", jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject responseJson) {
                Log.e("success", "" + responseJson.toString());
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "" + error);

            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json;charset=UTF-8";
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjReq);

    }

    public void postStringData(final Context context) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "URL HERE", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("success", "" + response);

                try{
                    Toast.makeText(context,"Notification Sent Successfully", Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "" + error);
                try{
                    Toast.makeText(context,"Unable to Send Notification... Try again?", Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
