package com.gencode.ringmsgeditor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.gencode.ringmsgeditor.MsgContentProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-06-12.
 */
public class ContentResolverHelper {
    final String TAG = this.getClass().getName();
    Context mContext;

    public ContentResolverHelper(Context context) {
        mContext = context;
    }

    public Map<String, String> doQuery(String messageFrom, String messageTo) {
        Map<String, String> resultMap = new HashMap<String, String>();

        Cursor cursor = null;
        String phoneNumber = "";
        try {
            cursor = _doQuery(messageFrom,messageTo);
            int idxMsgSeq = cursor.getColumnIndex(MsgContentProvider.COL_MSG_SEQ);
            int idxType   = cursor.getColumnIndex(MsgContentProvider.COL_TYPE);
            int idxMsg    = cursor.getColumnIndex(MsgContentProvider.COL_MSG);

            while (cursor.moveToNext()) {
                resultMap.put(String.format("%d:%s", cursor.getInt(idxMsgSeq),cursor.getString(idxType)), cursor.getString(idxMsg));
                Log.d(String.format("doQuery messageFrom[%s] messageTo[%s]:", messageFrom, messageTo)+String.format("%d:%s", cursor.getInt(idxMsgSeq),cursor.getString(idxType)), cursor.getString(idxMsg));
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to get phone data", e);
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return resultMap;
    }

    private Cursor _doQuery(String messageFrom, String messageTo) {
        Log.v(TAG, String.format("_doQuery messageFrom[%s] messageTo[%s] ", messageFrom, messageTo));

        String selection = MsgContentProvider.COL_FROM + " = ? AND "+MsgContentProvider.COL_TO + " = ?";
        String[] whereArgs = new String[] { messageFrom, messageTo };
        // query for everything email
        return  mContext.getContentResolver().query(MsgContentProvider.CONTENT_URI_MESSAGES,
                null, selection, whereArgs,
                null);
    }

    private boolean _doDelete(String messageFrom, String messageTo) {
        Log.v(TAG, String.format("_doQuery messageFrom[%s] messageTo[%s] ", messageFrom, messageTo));

        String selection = MsgContentProvider.COL_FROM + " = ? AND "+MsgContentProvider.COL_TO + " = ?";
        String[] whereArgs = new String[] { messageFrom, messageTo };
        // query for everything email
        if (mContext.getContentResolver().delete(MsgContentProvider.CONTENT_URI_MESSAGES, selection, whereArgs)>0)
            return true;
        else
            return false;
    }

    public boolean doInsert(String messageFrom, String messageTo, Map<String, String> messageMap) {
        Cursor cursor = null;
        try {
            int seq = 0;

            cursor = _doQuery(messageFrom,messageTo);
            if (cursor.getCount() >= 1) {
                if (!_doDelete(messageFrom, messageTo)) throw new Exception(String.format("delete in insert error messageFrom[%s] messageTo[%s] ", messageFrom, messageTo));
            }

            for (Map.Entry<String, String> entry : messageMap.entrySet())
            {
                ContentValues values = new ContentValues(5);
                //values.put(MsgContentProvider.COL_ID, String.format("%s_%s", myPhoneNumber, recipientNumber));
                values.put(MsgContentProvider.COL_FROM, messageFrom);
                values.put(MsgContentProvider.COL_TO, messageTo);
                values.put(MsgContentProvider.COL_MSG_SEQ, seq);
                if (entry.getKey().endsWith("img")) {
                    values.put(MsgContentProvider.COL_TYPE, "img");
                } else {
                    values.put(MsgContentProvider.COL_TYPE, "txt");
                }
                values.put(MsgContentProvider.COL_MSG, entry.getValue());

                mContext.getContentResolver().insert(MsgContentProvider.CONTENT_URI_MESSAGES, values);
                seq++;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to insert", e);
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return true;
    }

    public boolean doDelete(String messageFrom, String messageTo) {
        try {
            if (!_doDelete(messageFrom, messageTo))
                throw new Exception(String.format("delete in insert error messageFrom[%s] messageTo[%s] ", messageFrom, messageTo));
        } catch (Exception e) {
            Log.e(TAG, "Failed to delete", e);
            return false;
        }
        return true;
    }

    /**
     *
     * @param messageFrom
     * @param messageTo
     * @param messageMap
     * @return
     */
    public boolean doUpdate(String messageFrom, String messageTo, Map<String, String> messageMap) {

        Log.v(TAG, String.format("doUpdate messageFrom[%s] messageTo[%s] ", messageFrom, messageTo));
        //todo 현재로선 필요없으나 추후 구현
//        String selection = MsgContentProvider.COL_FROM + " = ? AND "+MsgContentProvider.COL_TO + " = ?";
//        String[] whereArgs = new String[] { messageFrom, messageTo };
//
//        for (Map.Entry<String, String> entry : messageMap.entrySet()) {
//            String[] keyArr = entry.getKey().split(":");
//            int seq = Integer.parseInt(keyArr[0]);
//        }
//            if (mContext.getContentResolver().delete(MsgContentProvider.CONTENT_URI_MESSAGES, selection, whereArgs)>0)
//            return true;
//        else
//            return false;
        return true;
    }
}
