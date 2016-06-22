package com.gencode.ringmsgeditor;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016-06-11.
 */
public class ActionHandler {
    private Context mContext;
    final String TAG = this.getClass().getName();

    public ActionHandler(Context context) {
        mContext = context;
    }

    public boolean doDeleteImageView(int parentId, int childId) {
        try {
            RelativeLayout parent = (RelativeLayout) ((Activity) mContext).findViewById(parentId);
            PhotoRelativeLayout child = (PhotoRelativeLayout) parent.findViewById(childId);
            parent.removeView(child);
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
        } catch (Exception e) {
            Log.e(TAG, " ", e);
            return false;
        }
        return true;
    }

    public View doAddImageView(int parentId, int prevViewId, int imgId) {
        return doAddImageView(parentId, prevViewId, null, imgId, "");
    }

    public View doAddImageView(int parentId, int prevViewId, Drawable drawable) {
        return doAddImageView(parentId, prevViewId, drawable, -1,"");
    }

    public View doAddImageView(int parentId, int prevViewId, String imageUriStr) {
        return doAddImageView(parentId, prevViewId, null, -1, imageUriStr);
    }


    private View doAddImageView(int parentId, int prevViewId, Drawable drawable, int imgId, String imageUriStr) {
        PhotoRelativeLayout relativeLayout;
        if (imgId > 0)
            relativeLayout = new PhotoRelativeLayout(mContext, (OnModeSwitchListener) mContext, imgId);
        else if (drawable != null)
            relativeLayout = new PhotoRelativeLayout(mContext, (OnModeSwitchListener) mContext, drawable);
        else //if (!imageUriStr.equals(""))
            relativeLayout = new PhotoRelativeLayout(mContext, (OnModeSwitchListener) mContext, imageUriStr);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout.setId(Utils.generateViewId());
        rlp.addRule(RelativeLayout.BELOW, prevViewId);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        relativeLayout.setLayoutParams(rlp);
        // Adding the TextView to the RelativeLayout as a child
        //relativeLayout.addView(imgView);

        RelativeLayout contentMainLayout = (RelativeLayout)((Activity)mContext).findViewById(parentId);
        contentMainLayout.addView(relativeLayout);
        return relativeLayout;
    }
    public View doAddTextViewOnPreview(int parentId, int prevViewId, String text, final OnModeSwitchListener listener) {
        return doAddTextView(parentId, prevViewId, text, listener, true);
    }

    public View doAddTextView(int parentId, int prevViewId, String text, final OnModeSwitchListener listener) {
        return doAddTextView(parentId, prevViewId, text, listener, false);
    }

    public View doAddTextView(int parentId, int prevViewId, String text, final OnModeSwitchListener listener, boolean isPreview) {
        final TextView textView = new TextView(mContext);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        textView.setId(Utils.generateViewId());
        textView.setText(text);
        rlp.addRule(RelativeLayout.BELOW, prevViewId);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        textView.setLayoutParams(rlp);
        //textView.setBackgroundResource(R.drawable.image_border);
        // Adding the TextView to the RelativeLayout as a child
        //relativeLayout.addView(imgView);
        if (!isPreview) {
            textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    TextView tView = (TextView) v;
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:
                            listener.OnModeSwitchChange(v);
                            break;
                    }
                    return true;
                }
            });
        }
        RelativeLayout contentMainLayout = (RelativeLayout)((Activity)mContext).findViewById(parentId);
        contentMainLayout.addView(textView);
        return textView;
    }

}
