package com.gencode.ringcatcher.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016-06-11.
 */
public class Utils {
    private static final String TAG = Utils.class.getName();

    public static JSONObject getJSONObjectFromBase64(String base64Str) throws JSONException {
        byte[] valueDecoded = Base64.decode(base64Str.getBytes(), Base64.DEFAULT);
        System.out.println("Decoded value: " + new String(valueDecoded));
        return new JSONObject(new String(valueDecoded));
    }

    public static String getBase64StringFromJSONObject(JSONObject jsonObject) {
        byte[] bytesEncoded = Base64.encode(jsonObject.toString().getBytes(),Base64.DEFAULT);
        System.out.println("Encoded value: "+new String(bytesEncoded));
        return new String(bytesEncoded);
    }
}
