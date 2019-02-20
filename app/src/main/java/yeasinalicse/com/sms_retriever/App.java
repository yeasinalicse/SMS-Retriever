package yeasinalicse.com.sms_retriever;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import org.json.JSONArray;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Stack;

/**
 * Created by Yeasin on 1/23/18.
 */

public class App extends Application {
    private static App instence;

    private static String SVR_KEY_URL = "MYAP_HST_KE_AP_URL";

    public static String APP_PREF_KEY = "MYAP_KYE_APP";


    private static Dialog progressDialog;
    private static ProgressDialog pgDialog;


    // manage app shared preference data ---- START

    public static String getSharedPreference(Context _context, String key) {
        return getSharedPreference(_context, key, "");
    }


    public static String getSharedPreference(Context _context, String key, String _default) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(App.APP_PREF_KEY, Context.MODE_PRIVATE);

        try {
            return sharedPreferences.getString(key, _default).trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static void setSharedPreference(Context _context, String key, String value) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(App.APP_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);

        editor.commit();
    }


    public static void setVisited(Context c) {
        final SharedPreferences prefs = c.getSharedPreferences(App.APP_PREF_KEY,
                Context.MODE_PRIVATE);
        prefs.edit().putBoolean("VISITED", true).commit();
    }

    public static boolean isVisited(Context c) {
        final SharedPreferences prefs = c.getSharedPreferences(App.APP_PREF_KEY,
                Context.MODE_PRIVATE);
        return prefs.getBoolean("VISITED", false);
    }

    //Date format like: 7 Jul 2018
    public static String getDateFormat(String date) throws ParseException {
        Log.e("date", date);
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        Log.e("dates", d.toString());
        cal.setTime(d);

        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        String year = new SimpleDateFormat("yyyy").format(cal.getTime());
        String day = new SimpleDateFormat("dd").format(cal.getTime());
        return day + " " + monthName + " " + year;
    }


    public static Date getTimeFromTimeString(String time) {
        Date outPutDate = null;
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.US);
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        try {
            Date date = parseFormat.parse(time);
            String outPut = displayFormat.format(date);
            outPutDate = displayFormat.parse(outPut);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outPutDate;
    }

    public static String getTimeString(String time) {
        String outPut = null;
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.US);
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        try {
            Date date = parseFormat.parse(time);
            outPut = displayFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outPut;
    }

    public static String getTimeFormat(String time) throws ParseException {
        Log.e("times", time);
        String timess = "";
        SimpleDateFormat dateString3 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
        try {
            Date date3 = dateString3.parse(time);
           // Log.e("getHours", "" + date3.getHours());

            //new format
            timess = sdf2.format(date3);


            //formatting the given time to new format with AM/PM
            Log.e("Given", "" + timess);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//
//        String times = String.valueOf(d.getTime());
        return timess;
    }

    public static void destroySharedPreferenceData(Context _context) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(App.APP_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    // manage app shared preference data ---- END


    public static void showProgressBar(Activity _context) {

        progressDialog = new Dialog(_context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        progressDialog.setContentView(R.layout.full_screen_dialog);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void hideProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void showProgressDialog(Activity _context) {
        hideProgressDialog();
        pgDialog = new ProgressDialog(_context);
        pgDialog.setMessage("Waiting");
        pgDialog.setCancelable(false);
        pgDialog.show();
    }

    public static void hideProgressDialog() {
        if (pgDialog != null && (pgDialog.isShowing())) {
            pgDialog.dismiss();
        }
    }


    public void onCreate() {
        super.onCreate();
        AppSignatureHelper hemper = new AppSignatureHelper(this);
        Log.e("AppApplication",""+hemper.getAppSignatures());
    }

    private App() {
    }

    public static synchronized App getInstance() {
        App app;
        synchronized (App.class) {
            if (instence == null) {
                instence = new App();
            }
            app = instence;
        }
        return app;
    }


    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String file : children) {
                if (!deleteDir(new File(dir, file))) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


    public static void destroyApp() {
        instence = null;
    }


}
