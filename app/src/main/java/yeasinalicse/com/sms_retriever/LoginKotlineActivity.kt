package yeasinalicse.com.sms_retriever

import android.app.Activity
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.auth.api.phone.SmsRetriever
import org.json.JSONObject
import yeasinalicse.com.sms_retriever.communication.VolleySingleton
import yeasinalicse.com.sms_retriever.util.API
import yeasinalicse.com.sms_retriever.util.AppResponse
import yeasinalicse.com.sms_retriever.util.AppUtil
import yeasinalicse.com.sms_retriever.util.KEY

class LoginKotlineActivity : AppCompatActivity() , SMSBroadcastReceiver.OTPReceiveListener {
    lateinit var otpTxtView: TextView

    val smsBroadcast = SMSBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sendVerificationCode(this@LoginKotlineActivity, "88017339284", "BD")
        otpTxtView = findViewById(R.id.otp_txt)
        val hemper = AppSignatureHelper(this)
        Log.e("AppApplication", "" + hemper.appSignatures)

        startSMSListener()

        smsBroadcast.initOTPListener(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        applicationContext.registerReceiver(smsBroadcast, intentFilter)
//        requestHint()


    }

    private fun startSMSListener() {

        val client = SmsRetriever.getClient(this /* context */)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener {
            // Successfully started retriever, expect broadcast intent
            // ...
            otpTxtView.text = "Waiting for the OTP"
            Toast.makeText(this, "SMS Retriever starts", Toast.LENGTH_LONG).show()
        }

        task.addOnFailureListener {
            otpTxtView.text = "Cannot Start SMS Retriever"
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
        }
    }

    override fun onOTPReceived(otp: String) {
        if (smsBroadcast != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsBroadcast)
        }

        Toast.makeText(this, otp, Toast.LENGTH_SHORT).show()
        otpTxtView.text = "Your OTP is: $otp"
        Log.e("OTP Received", otp)
    }

    override fun onOTPTimeOut() {
        otpTxtView.setText("Timeout")
        Toast.makeText(this, " SMS retriever API Timeout", Toast.LENGTH_SHORT).show()
    }


    private fun sendVerificationCode(ctx: Activity, number: String, code: String) {

        if (AppUtil.isValidMobileNo(number)) {
            App.showProgressBar(this)

            Log.e("LOGIN", "please wait... $number")

            val jsonObject = JSONObject()

            try {
                jsonObject.put(KEY.PARAM.MOBILE_NO, number)
                jsonObject.put(KEY.PARAM.COUNTRY_NAME_CODE, code)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            Log.e("API", API.URL.COMMON.SEND_VERIFY_SMS)
            Log.e("OBJ", jsonObject.toString())

            val jsonObjReq = object : JsonObjectRequest(Request.Method.PUT, API.URL.COMMON.SEND_VERIFY_SMS, jsonObject, Response.Listener { responseObj ->
                Log.e("responseObjCodes", "" + responseObj.toString())
                App.hideProgressBar()
                try {
                    val response = AppResponse.build(responseObj)
                    Log.e("code res", "" + responseObj.toString())

                    if (response.getCode().equals(AppResponse.SUCCESS)) {
                        // initDi()


                    } else {
                        Toast.makeText(ctx, response.getMessage(), Toast.LENGTH_SHORT).show()
                    }
                } catch (ec: Exception) {
                    Toast.makeText(ctx, "failed_data_parsing_error", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { error ->
                App.hideProgressBar()
                Log.e("login error", "" + error)
                Toast.makeText(ctx, "something_is_wrong_try_again", Toast.LENGTH_SHORT).show()
            }) {

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    return AppUtil.getHeader(ctx)
                }

                override fun getBodyContentType(): String {
                    return "application/json;charset=UTF-8"
                }

            }
            VolleySingleton.getInstance(this).addToRequestQueue(jsonObjReq)

        } else {
            Toast.makeText(ctx, "invalid_mobile_number", Toast.LENGTH_SHORT).show()
        }
    }

}