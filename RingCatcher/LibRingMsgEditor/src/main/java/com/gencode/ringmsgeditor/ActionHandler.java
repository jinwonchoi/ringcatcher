package com.gencode.ringmsgeditor;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-06-11.
 */
public class ActionHandler {
    private Context mContext;
    final String TAG = this.getClass().getName();

    private Map<Integer, View> mMsgMap = new HashMap<Integer, View>();
    private Map<String, String> mContactMap = new HashMap<String, String>();
    private int mLastViewId = -1;

    public ActionHandler(Context context) {
        mContext = context;
    }

    public Map<Integer, View> getMsgMap() {
        return mMsgMap;
    }

    public boolean isLayoutEmpty() {
        return (mMsgMap.size()==0);
    }

    public int getLastViewId() {
        return mLastViewId;
    }

    public Map<String, String> getMapForMessageSave() {
        Map<String, String> messsageMap = new HashMap<String, String>();

        for (Map.Entry<Integer, View> entry : mMsgMap.entrySet())
        {
            if (entry.getValue() instanceof PhotoRelativeLayout) {
                Log.d(TAG, "getMapForMessageSave:"+entry.getKey()+":"+((PhotoRelativeLayout) entry.getValue()).getUri());
            } else if (entry.getValue() instanceof EditText) {
                Log.d(TAG, "getMapForMessageSave:"+entry.getKey()+":"+((EditText) entry.getValue()).getText().toString());
            }
        }
        int i = 0;
        for (Map.Entry<Integer, View> entry : mMsgMap.entrySet())
        {
            if (entry.getValue() instanceof PhotoRelativeLayout) {
                messsageMap.put(String.format("%d:img",i),((PhotoRelativeLayout) entry.getValue()).getUri());
            } else if (entry.getValue() instanceof EditText) {
                messsageMap.put(String.format("%d:txt",i),((EditText) entry.getValue()).getText().toString());
            }
            i++;
        }
        return messsageMap;
    }

    public void clearMessagesOnEdit() {
        Log.d(TAG, "clearMessagesOnEdit");
        Map<Integer, View> copyMap = new HashMap<Integer, View>(mMsgMap);
        for (Map.Entry<Integer, View> entry : copyMap.entrySet()) {
            if (entry.getValue() instanceof TextView) {
                doDeleteTextView(R.id.content_msg_layout, entry.getKey());
            } else if (entry.getValue() instanceof EditText) {
                doDeleteEditText(R.id.content_msg_layout, entry.getKey());
            }else {
                doDeleteImageView(R.id.content_msg_layout, entry.getKey());
            }
        }
        mMsgMap.clear();
    }

    public void updateMsgMapOnAdd() {

    }

    public void updateMsgMapOnRemove(int comparedId) {
        if (this.mLastViewId != comparedId) return;

        for (Map.Entry<Integer, View> entry : mMsgMap.entrySet())
        {
            this.mLastViewId = entry.getKey();
        }
        Log.d(TAG, "updateLastViewId: old mLastViewId="+comparedId+"  new mLastViewId="+mLastViewId);
    }

    public boolean doDeleteImageView(int parentId, int childId) {
        try {
            RelativeLayout parent = (RelativeLayout) ((Activity) mContext).findViewById(parentId);
            PhotoRelativeLayout child = (PhotoRelativeLayout) parent.findViewById(childId);
            parent.removeView(child);

            mMsgMap.remove(childId);
            updateMsgMapOnRemove(childId);
        } catch (Exception e) {
            Log.e(TAG, " ", e);
            return false;
        }
        return true;
    }

    public boolean doDeleteTextView(int parentId, int childId) {
        try {
            RelativeLayout parent = (RelativeLayout) ((Activity) mContext).findViewById(parentId);
            TextView child = (TextView) parent.findViewById(childId);
            parent.removeView(child);
            mMsgMap.remove(childId);
            updateMsgMapOnRemove(childId);
        } catch (Exception e) {
            Log.e(TAG, " ", e);
            return false;
        }
        return true;
    }

    public boolean doDeleteEditText(int parentId, int childId) {
        try {
            RelativeLayout parent = (RelativeLayout) ((Activity) mContext).findViewById(parentId);
            EditText child = (EditText) parent.findViewById(childId);
            parent.removeView(child);
            mMsgMap.remove(childId);
            updateMsgMapOnRemove(childId);
        } catch (Exception e) {
            Log.e(TAG, " ", e);
            return false;
        }
        return true;
    }

    public boolean doDeleteContactButton(int parentId, int childId) {
        try {
            LinearLayout parent = (LinearLayout) ((Activity) mContext).findViewById(parentId);
            ContactButton child = (ContactButton) parent.findViewById(childId);
            parent.removeView(child);
        } catch (Exception e) {
            Log.e(TAG, " ", e);
            return false;
        }
        return true;
    }


    public View doAddContactButton(int parentId, final IContactButtonHandler handler, String contactNumber, String contactName) {
        final ContactButton theButton = new ContactButton(mContext, handler, contactNumber, contactName);
        LinearLayout contactLayout = (LinearLayout)((Activity)mContext).findViewById(parentId);
        contactLayout.addView(theButton);
        return theButton;
    }


    public View doAddImageView(int parentId, int imgId) {
        return doAddImageView(parentId, null, imgId, "");
    }

    public View doAddImageView(int parentId, Drawable drawable) {
        return doAddImageView(parentId, drawable, -1,"");
    }

    public View doAddImageView(int parentId, String imageUriStr) {
        return doAddImageView(parentId, null, -1, imageUriStr);
    }


    private View doAddImageView(int parentId, Drawable drawable, int imgId, String imageUriStr) {
        PhotoRelativeLayout relativeLayout;
        if (imgId > 0)
            relativeLayout = new PhotoRelativeLayout(mContext, (OnModeSwitchListener) mContext, (OnImageSizeChangeListener) mContext, imgId);
        else if (drawable != null)
            relativeLayout = new PhotoRelativeLayout(mContext, (OnModeSwitchListener) mContext, (OnImageSizeChangeListener) mContext, drawable);
        else //if (!imageUriStr.equals(""))
            relativeLayout = new PhotoRelativeLayout(mContext, (OnModeSwitchListener) mContext, (OnImageSizeChangeListener) mContext, imageUriStr);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout.setId(Utils.generateViewId());
        rlp.addRule(RelativeLayout.BELOW, mLastViewId);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        relativeLayout.setLayoutParams(rlp);
        // Adding the TextView to the RelativeLayout as a child
        //relativeLayout.addView(imgView);

        RelativeLayout contentMainLayout = (RelativeLayout)((Activity)mContext).findViewById(parentId);
        contentMainLayout.addView(relativeLayout);

        mMsgMap.put(relativeLayout.getId(), relativeLayout);
        mLastViewId = relativeLayout.getId();//textView.getId();//

        return relativeLayout;
    }

    public View doAddTextViewOnPreview(int parentId, String text, final OnModeSwitchListener listener) {
        return doAddTextView(parentId, text, listener, true);
    }

    public View doAddTextView(int parentId, String text, final OnModeSwitchListener listener) {
        return doAddTextView(parentId, text, listener, false);
    }

    public View doAddTextView(int parentId,  String text, final OnModeSwitchListener listener, boolean isPreview) {
        final TextView textView = new TextView(mContext);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        textView.setId(Utils.generateViewId());
        textView.setText(text);
        rlp.addRule(RelativeLayout.BELOW, mLastViewId);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        textView.setLayoutParams(rlp);
        //textView.setBackgroundResource(R.drawable.image_border);
        // Adding the TextView to the RelativeLayout as a child
        //relativeLayout.addView(imgView);
//        if (!isPreview) {
//            textView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    TextView tView = (TextView) v;
//                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                        case MotionEvent.ACTION_UP:
//                        case MotionEvent.ACTION_POINTER_UP:
//                            listener.OnModeSwitchChange(v);
//                            break;
//                    }
//                    return true;
//                }
//            });
//        }
        RelativeLayout contentMainLayout = (RelativeLayout)((Activity)mContext).findViewById(parentId);
        contentMainLayout.addView(textView);

        mMsgMap.put(textView.getId(), textView);
        mLastViewId = textView.getId();

        return textView;
    }

    public View doAddEditTextOnPreview(int parentId, final OnModeSwitchListener listener) {
        return doAddEditText(parentId, listener, true);
    }

    public View doAddEditText(int parentId, final OnModeSwitchListener listener) {
        return doAddEditText(parentId, listener, false);
    }

    public View doAddEditText(int parentId, final OnModeSwitchListener listener, boolean isPreview) {
        final EditText editText = new EditText(mContext);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        editText.setId(Utils.generateViewId());
        editText.setHint(R.string.edit_text_intro_hint);

        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setSingleLine(false);
        editText.setMaxLines(20);
        int maxLengthofEditText = 2000;
        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLengthofEditText)});
        editText.setBackgroundResource(android.R.color.transparent);
        rlp.addRule(RelativeLayout.BELOW, mLastViewId);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        editText.setLayoutParams(rlp);

/*
    <EditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:hint="새 메시지 작성하기"
        android:inputType="textCapSentences|textMultiLine"
        android:maxLength="2000"
        android:maxLines="4"
        android:id="@+id/editText"
        android:background="@android:color/transparent"
        android:layout_below="@+id/textView2"/>
 */

//        if (!isPreview) {
//            editText.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    EditText tView = (EditText) v;
//                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                        case MotionEvent.ACTION_UP:
//                        case MotionEvent.ACTION_POINTER_UP:
//                            listener.OnModeSwitchChange(v);
//                            break;
//                        case MotionEvent.ACTION_:
//                            listener.OnModeSwitchChange(v);
//                            break;
//                    }
//                    return true;
//                }
//            });
//        }
        RelativeLayout contentMainLayout = (RelativeLayout)((Activity)mContext).findViewById(parentId);
        contentMainLayout.addView(editText);

        mMsgMap.put(editText.getId(), editText);
        mLastViewId = editText.getId();

        return editText;
    }

    public void addContactToList(EditText editText, String contactNum, String contactName) {
        if (editText.getText().toString().trim().equals("")) {
            editText.setText(String.format("%s(%s)",contactName,contactNum));
        } else {
            editText.setText(editText.getText().toString().trim()
                    +","+String.format("%s(%s)",contactName,contactNum));
        }
        mContactMap.put(contactNum, contactName);
    }

    public String getContactNumberList() {
        StringBuffer sb = new StringBuffer();

        for (Map.Entry<String, String> entry : mContactMap.entrySet())
        {
            if (sb.toString().length() > 0) sb.append(",");
            sb.append(entry.getKey());
        }
        return sb.toString();
    }

    public String toNeatContactString(String editStr) {
        StringBuffer sb = new StringBuffer();
        Map<String, String> tempMap = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : mContactMap.entrySet())
        {
            if (!entry.getKey().contains(editStr)) continue;
            if (sb.length() > 0) sb.append(",");
            sb.append(String.format("%s(%s)", entry.getValue(),entry.getKey()));
            tempMap.put(entry.getValue(),entry.getKey());
        }
        mContactMap.clear();
        mContactMap.putAll(tempMap);
        return sb.toString();
    }
}
