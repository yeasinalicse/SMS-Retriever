package yeasinalicse.com.sms_retriever;

import android.app.Activity;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import yeasinalicse.com.sms_retriever.communication.VolleySingleton;
import yeasinalicse.com.sms_retriever.util.API;
import yeasinalicse.com.sms_retriever.util.AppResponse;
import yeasinalicse.com.sms_retriever.util.AppUtil;
import yeasinalicse.com.sms_retriever.util.KEY;

import java.util.Map;

public class LoginJavaActivity extends AppCompatActivity implements SMSBroadcastReceiver.OTPReceiveListener{
    TextView otpTxtView;
    SMSBroadcastReceiver smsBroadcast = new SMSBroadcastReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendVerificationCode(this, "88017339284", "BD");
        otpTxtView = findViewById(R.id.otp_txt);
        AppSignatureHelper hemper = new AppSignatureHelper(this);
        Log.e("AppApplication",""+hemper.getAppSignatures());
        startSMSListener();
        smsBroadcast.initOTPListener(this);
        IntentFilter intentFilter =new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(smsBroadcast, intentFilter);


    }

    private void startSMSListener() {

        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);
        Task<Void> task = client.startSmsRetriever();

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                otpTxtView.setText("Waiting for the OTP");
                Toast.makeText(LoginJavaActivity.this, "SMS Retriever starts", Toast.LENGTH_SHORT).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                otpTxtView.setText("Cannot Start SMS Retriever");
                Toast.makeText(LoginJavaActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onOTPReceived(@NotNull String otp) {
        if (smsBroadcast != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsBroadcast);
        }
        Toast.makeText(this, otp, Toast.LENGTH_SHORT).show();
        otpTxtView.setText("Your OTP is: "+otp);
        Log.e("OTP Received", otp);
    }

    @Override
    public void onOTPTimeOut() {
        otpTxtView.setText("Timeout");
        Toast.makeText(this, " SMS retriever API Timeout", Toast.LENGTH_SHORT).show();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smsBroadcast);

    }
    private void sendVerificationCode(final Activity ctx,String _number,String _countryNameCode) {
        if (AppUtil.isValidMobileNo(_number)) {
            App.showProgressBar(this);

            Log.e("LOGIN", "please wait... " + _number);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(KEY.PARAM.MOBILE_NO, _number);
                jsonObject.put(KEY.PARAM.COUNTRY_NAME_CODE, _countryNameCode);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Log.e("API", API.URL.COMMON.SEND_VERIFY_SMS);
            Log.e("OBJ", jsonObject.toString());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT, API.URL.COMMON.SEND_VERIFY_SMS, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject responseObj) {
                    Log.e("responseObjCodes", "" + responseObj.toString());
                    App.hideProgressBar();
                    try {
                        AppResponse response = AppResponse.build(responseObj);
                        Log.e("code res", "" + responseObj.toString());

                        if (response.getCode().equals(AppResponse.SUCCESS)) {


                        } else {
                            Toast.makeText(ctx, response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ec) {
                        Toast.makeText(ctx, "failed_data_parsing_error", Toast.LENGTH_SHORT).show();
                    }

                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    App.hideProgressBar();
                    Log.e("login error", "" + error);
                    Toast.makeText(ctx, "something_is_wrong_try_again", Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Log.e("getHeader", String.valueOf(AppUtil.getHeader(ctx)));
                    return AppUtil.getHeader(ctx);
                }

                @Override
                public String getBodyContentType() {
                    return "application/json;charset=UTF-8";
                }

            };
            VolleySingleton.getInstance(this).addToRequestQueue(jsonObjReq);

        } else {
            Toast.makeText(ctx,"invalid_mobile_number", Toast.LENGTH_SHORT).show();
        }
    }


}
