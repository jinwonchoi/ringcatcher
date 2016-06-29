package com.gencode.ringmsgeditor;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016-06-11.
 */
public class Utils {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    private static final String TAG = Utils.class.getName();
    /**
     * Generate a value suitable for use
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    /**
     * uriStr : uri;xscale;yscale  ex: android.resource://my_app_package/drawable/drawable_name;.2;0,45
     * @param context
     * @param uriStr
     * @return
     */
    public static Drawable drawableFromUri(Context context, String uriStr) {
        try {
            Uri uri = Uri.parse(uriStr);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            return Drawable.createFromStream(inputStream, uriStr.toString() );
        } catch (Exception e) {
            Log.e(TAG, "wrong URI",e);
            return null;
        }
    }

    public static Drawable drawableFromUrl(Context context, String url) {
        try {
            Bitmap x;
            Log.d(TAG, "drawableFromUrl:"+url);
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();

            x = BitmapFactory.decodeStream(input);

            return new BitmapDrawable(context.getResources(), x);
        } catch (Exception e) {
            Log.e(TAG, "wrong URL",e);
            return null;
        }
    }

    public static String getMyPhoneNumber(Context context) {
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String myNumber = tMgr.getLine1Number();
        if (myNumber == null || myNumber.trim().equals("")) {
            myNumber = context.getResources().getString(R.string.test_my_phone_number);
        }
        return myNumber;
    }

    public static JSONObject getJSONObjectFromBase64(String base64Str) throws JSONException {
        byte[] valueDecoded = Base64.decode(base64Str.getBytes(), Base64.DEFAULT);
        System.out.println("Decoded value: " + new String(valueDecoded));
        return new JSONObject(new String(valueDecoded));
    }

    public static String getBase64StringFromJSONObject(JSONObject jsonObject) {
        byte[] bytesEncoded = Base64.encode(jsonObject.toString().getBytes(),Base64.DEFAULT);
        String encodedStr = new String(bytesEncoded).replaceAll("(?:\\r\\n|\\n\\r|\\n|\\r)", "");
        System.out.println("Encoded value: "+encodedStr);
        return encodedStr;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
