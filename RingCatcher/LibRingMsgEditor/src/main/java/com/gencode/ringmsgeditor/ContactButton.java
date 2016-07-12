package com.gencode.ringmsgeditor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016-07-02.
 */
public class ContactButton extends Button {
    private IContactButtonHandler delegate;
    private String mContactNumber;
    private String mContactName;

    public ContactButton(Context context) {
        super(context);
        /*
                        <Button
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content" android:text="최진원"
                    android:layout_margin="3dp"
                    android:padding="2dp"
                    android:textColor="#ffffff"
                    android:background="@drawable/button_contact"
                    />
                     LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 0);

            // Setting the parameters on the TextView
            imgView.setLayoutParams(lp);
         */
        setTextColor(Color.BLACK);
        setBackgroundResource(R.drawable.button_contact);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(3, 3, 3, 3);
        setPadding(2,2,2,2);
        this.setLayoutParams(lp);
        this.setId(Utils.generateViewId());
    }

    public ContactButton(Context context, IContactButtonHandler handler, String contactNumber, String contactName) {
        super(context);
        delegate = handler;
        mContactNumber = contactNumber;
        mContactName = contactName;
        setHint(String.format("%s(%s)", mContactName, mContactNumber));
        setText(contactName);
    }

    public String getContactNumber() {
        return mContactNumber;
    }

    public String getContactName() {
        return mContactName;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!isEnabled()) {
            return super.onKeyUp(keyCode, event);
        }

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (event.hasNoModifiers()) {
                    /*
                     * If there is a click listener, just call through to
                     * super, which will invoke it.
                     *
                     * If there isn't a click listener, try to show the soft
                     * input method.  (It will also
                     * call performClick(), but that won't do anything in
                     * this case.)
                     */
                    if (!hasOnClickListeners()) {
                        delegate.needButtonRemoved(this.getId());
                    }
                }
        }

        return super.onKeyUp(keyCode, event);
    }
}
