package yeasinalicse.com.sms_retriever.communication;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Yeasin on 7/5/2016.
 */
public class VolleySingleton {

    // Singleton object...
    private static VolleySingleton instance;

    final private RequestQueue requestQueue;
    private static ImageLoader imageLoader;

    //Constructor...
    private VolleySingleton(Context context) {

        requestQueue = Volley.newRequestQueue(context);

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(100000000);


            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    // Singleton method...
    public static VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(Context context) {
        if (requestQueue != null) {
            return requestQueue;
        } else {
            getInstance(context);
            return requestQueue;
        }
    }

    private RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static ImageLoader getImageLoader(Context context) {
        if (imageLoader != null) {
            return imageLoader;
        } else {
            getInstance(context);
            return imageLoader;
        }
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag("App");
        getRequestQueue().add(req);
    }

}