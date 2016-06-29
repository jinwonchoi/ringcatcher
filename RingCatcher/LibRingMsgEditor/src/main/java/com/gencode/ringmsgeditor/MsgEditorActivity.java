package com.gencode.ringmsgeditor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gencode.ringcatcher.common.RingBearer;
import com.gencode.ringcatcher.gcm.QuickstartPreferences;
import com.gencode.ringcatcher.http.HttpConstants;
import com.gencode.ringcatcher.http.HttpIntentService;
import com.gencode.ringcatcher.http.MessageWrapper;
import com.gencode.ringmsgeditor.task.AsyncInsertMessage;
import com.gencode.ringmsgeditor.task.IInsertMessageTask;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class MsgEditorActivity extends AppCompatActivity implements OnModeSwitchListener {
    final String TAG = this.getClass().getName();

    // we can be in one of these 3 states
    /*
    display mode:
    ACTION_NONE;        //초기화. 입력가능상태 메뉴: preview,
    ACTION_IMAGE_EDIT
    | ACTION_TEXT_EDIT;  //입력가능상태 메뉴: delete
    ACTION_PREVIEW;    //입력창 안보임 메뉴: back|send
     */
    private static final int ACTION_NONE = 0;
    private static final int ACTION_IMAGE_EDIT = 1;
    private static final int ACTION_TEXT_EDIT = 2;
    private static final int ACTION_PREVIEW = 3;
    private static final int ACTION_ONLEY_PREVIEW = 4;
    private int actionMode = ACTION_NONE;

    private ActionHandler actionHandler;
    private ContentResolverHelper contentResolverHelper;
    private BroadcastReceiver httpResponseReceiver;
    private Map<Integer, View> mMsgMap = new HashMap<Integer, View>();
    private int mSelectedViewId = -1;
    private int mLastViewId = -1;

    private EditText msgEdit;
    private Button sendBtn;
    private Button photoBtn;
    private String profileId;
    private String profileName;
    private String profileEmail;

    LinearLayout mUpperEditLayout;
    LinearLayout mBottomEditLayout;
    AppBarLayout mAppBar;

    int idIntentID = 999;//앨범사진추가용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_editor);

        profileId = getIntent().getStringExtra(Common.PROFILE_ID);
        msgEdit = (EditText) findViewById(R.id.msg_edit);
        sendBtn = (Button) findViewById(R.id.add_btn);
        photoBtn = (Button) findViewById(R.id.photo_btn);

        mUpperEditLayout = (LinearLayout)findViewById(R.id.layout_upper_edit);
        mBottomEditLayout = (LinearLayout)findViewById(R.id.layout_bottom_edit);
        mAppBar = (AppBarLayout)findViewById(R.id.appbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

/* todo
                intent.putExtra(JsonConstants.callingNum,jsonRootObject.optString(JsonConstants.callingNum));
                intent.putExtra(JsonConstants.callingName,jsonRootObject.optString(JsonConstants.callingName));
                intent.putExtra(JsonConstants.jsonMessage,jsonRootObject.optString(JsonConstants.jsonMessage));
*/
        actionHandler  = new ActionHandler(this);
        contentResolverHelper = new ContentResolverHelper(this);

//        displayOnlyPreviewMode();
//        testLoadJsonMessageOnPreview();

        Intent intent = getIntent();
        if (QuickstartPreferences.ACTION_MESSAGE_VIEW.equals(intent.getAction())) {
            displayOnlyPreviewMode();
            loadJsonMessageOnPreview();
        } else {
            displayEditMode();
            //일반 edit뷰로 로딩
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            EditText et = (EditText) findViewById(R.id.msg_edit);
            et.setWidth(width);
        }
        registerReceiver();
        //contentResolverHelper.checkQuery();
        Log.d(TAG, "mMsgMap.size:"+mMsgMap.size());
    }

    private void displayOnlyPreviewMode() {
        Log.d(TAG, "displayOnlyPreviewMode");
        mUpperEditLayout.setVisibility(View.GONE);
        mBottomEditLayout.setVisibility(View.GONE);
        mAppBar.setVisibility(View.GONE);
        //mAppBar.setExpanded(false, true);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)mAppBar.getLayoutParams();
        lp.height = 0;
        mAppBar.setLayoutParams(lp);
        actionMode = ACTION_ONLEY_PREVIEW;
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        //invalidateOptionsMenu();
    }

    private void displayPreviewModeOnEdit() {
        Log.d(TAG, "displayPreviewModeOnEdit");
        mUpperEditLayout.setVisibility(View.GONE);
        mBottomEditLayout.setVisibility(View.GONE);

        actionMode = ACTION_PREVIEW;
        invalidateOptionsMenu();
    }

    private void displayEditMode() {
        Log.d(TAG, "displayEditMode");
        mUpperEditLayout.setVisibility(View.VISIBLE);
        mBottomEditLayout.setVisibility(View.VISIBLE);
        actionMode = ACTION_NONE;
        invalidateOptionsMenu();
    }

    private void testLoadJsonMessageOnPreview() {
        //메시지보기 뷰로 로딩

        try {
            String jsonMessage = "{\"message_to\":\"01055557777\",\"ring_message\":{\"1:img\":\"\\/ringmedia\\/20160626\\/0244445555_01055557777.jpg;1.000000;1.000000\",\"0:txt\":\"ㅎㅇㅌ퍼럿ㅎㄹ\"},\"message_from\":\"0244445555\"}";
            MessageWrapper messageWrapper = new MessageWrapper(jsonMessage, false);
            Log.d(TAG,"testLoad...:"+messageWrapper.toString());
            Map<String, String> messageMap = messageWrapper.getMessageList();
            for (Map.Entry<String, String> entry : messageMap.entrySet()) {
                Log.d(TAG, "add key" +entry.getKey());
                if (entry.getKey().endsWith("img")) {
                    Log.d(TAG, "add img" +entry.getValue());
                    addPhotoViewWithImageUri(entry.getValue());
                } else {
                    Log.d(TAG, "add txt" +entry.getValue());
                    addTextViewWithString(entry.getValue());
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "View mode jsonMessage error",e);
        }
    }

    private void loadJsonMessageOnPreview() {
        //메시지보기 뷰로 로딩
        Map<String, String> messageMap = new HashMap<String,String>();
        Intent intent = getIntent();
        String callingNum = intent.getStringExtra(JsonConstants.callingNum);
        String callingName = intent.getStringExtra(JsonConstants.callingName);
        String jsonMessage = intent.getStringExtra(JsonConstants.jsonMessage);

        Log.d(TAG,"loadJsonMessageOnPreview: "+ String.format("callingNum[%s]callingName[%s]jsonMessage[%s]",callingNum, callingName, jsonMessage));
        try {
            if (jsonMessage == null || jsonMessage.trim().equals("")) {
                messageMap = contentResolverHelper.doQuery(callingNum, RingBearer.getInstance().getMyPhoneNumber());
            } else {
                MessageWrapper messageWrapper = new MessageWrapper(jsonMessage, true);
                messageMap = messageWrapper.getMessageList();
            }
            SortedSet<String> keys = new TreeSet<String>(messageMap.keySet());
            for (String key : keys) {
                String value = messageMap.get(key);
                Log.d(TAG, "loadJsonMessageOnPreview adding:"+key+":"+value);
                if (key.endsWith("img")) {
                    addPhotoViewWithImageUri(value);
                } else {
                    addTextViewWithString(value);
                }
            }
        } catch (JSONException je) {
            Log.e(TAG, "View mode jsonMessage error",je);
        } catch (Exception e) {
            Log.e(TAG, "failed to get data", e);
        }
    }

    private void clearMessagesOnEdit() {
        Log.d(TAG, "clearMessagesOnEdit");
        for (Map.Entry<Integer, View> entry : mMsgMap.entrySet())
        {
            if (entry.getValue() instanceof TextView) {
                actionHandler.doDeleteTextView(R.id.content_msg_layout, entry.getKey());
            } else {
                actionHandler.doDeleteImageView(R.id.content_msg_layout, entry.getKey());
            }
        }
        mMsgMap.clear();
    }

    private void registerReceiver() {
        // The filter's action is BROADCAST_ACTION
        IntentFilter mStatusIntentFilter = new IntentFilter(
                HttpConstants.ACTION_BROADCAST_IMAGE_UPLOAD);
        mStatusIntentFilter.addAction(HttpConstants.ACTION_BROADCAST_MESSAGE_REGISTER);
        // Instantiates a new DownloadStateReceiver
        httpResponseReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean result = getIntent().getBooleanExtra("result", false);
                Toast.makeText(MsgEditorActivity.this, "message delivered! result:"+result, Toast.LENGTH_LONG);
                Log.d(TAG, "message delivered! result:"+result);
            }
        };
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                httpResponseReceiver,
                mStatusIntentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(httpResponseReceiver);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_msg_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            if (actionMode == ACTION_TEXT_EDIT) {
                if (actionHandler.doDeleteTextView(R.id.content_msg_layout, mSelectedViewId))
                    mMsgMap.remove(mSelectedViewId);
            } else if (actionMode == ACTION_IMAGE_EDIT) {
                if (actionHandler.doDeleteImageView(R.id.content_msg_layout, mSelectedViewId))
                    mMsgMap.remove(mSelectedViewId);
            }
            actionMode = ACTION_NONE;
            invalidateOptionsMenu();
        } else if (item.getItemId() == R.id.action_preview) {
            if (mMsgMap.size() == 0)
                Toast.makeText(this,R.string.message_empty, Toast.LENGTH_LONG );
            else {
                actionMode = ACTION_PREVIEW;
                displayPreviewModeOnEdit();
            }
        } else if (item.getItemId() == R.id.action_send) {
            if (mMsgMap.size() == 0)
                Toast.makeText(this,R.string.message_empty, Toast.LENGTH_LONG );
            else {
                sendMessage();
                actionMode = ACTION_NONE;
                clearMessagesOnEdit();
                displayEditMode();
            }
        } else if (item.getItemId() == R.id.action_back_from_preview || item.getItemId() == R.id.action_back_from_send) {
            actionMode = ACTION_NONE;
            displayEditMode();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if(actionMode == ACTION_NONE) {
            //if (mMsgMap.size() > 0) menu.setGroupVisible(R.id.menu_message,true);
            menu.setGroupVisible(R.id.menu_init,true);
            menu.setGroupVisible(R.id.menu_edit,false);
            menu.setGroupVisible(R.id.menu_preview,false);
        } else if (actionMode == ACTION_IMAGE_EDIT || actionMode == ACTION_TEXT_EDIT) {
            menu.setGroupVisible(R.id.menu_init,false);
            menu.setGroupVisible(R.id.menu_edit,true);
            menu.setGroupVisible(R.id.menu_preview,false);
        } else if (actionMode == ACTION_PREVIEW) {
            menu.setGroupVisible(R.id.menu_init,false);
            menu.setGroupVisible(R.id.menu_edit,false);
            menu.setGroupVisible(R.id.menu_preview,true);
        } else if (actionMode == ACTION_ONLEY_PREVIEW) {
            menu.setGroupVisible(R.id.menu_init,false);
            menu.setGroupVisible(R.id.menu_edit,false);
            menu.setGroupVisible(R.id.menu_preview,false);
        }
        return true;
    }

    @Override
    public void OnModeSwitchChange(View view, boolean isOnImage) {
        if (isOnImage) {
            actionMode = ACTION_IMAGE_EDIT;
            mSelectedViewId = view.getId();
            Log.d("CHOI_DEBUG","OnModeSwitchChange image id"+view.getId());
        } else {
            actionMode = ACTION_NONE;
            mSelectedViewId = -1;
            Log.d("CHOI_DEBUG","OnModeSwitchChange none");
        }
        invalidateOptionsMenu();
    }

    @Override
    public void OnModeSwitchChange(View view) {
        for (Map.Entry<Integer, View> entry : mMsgMap.entrySet())
        {
            if (entry.getValue() instanceof TextView && !entry.getValue().equals(view)) {
                ((TextView)entry.getValue()).setBackground(null);
            }
        }

        TextView tView = (TextView) view;
        if (tView.getBackground() ==null) {
            actionMode = ACTION_TEXT_EDIT;
            mSelectedViewId = view.getId();
            Log.d("CHOI_DEBUG","OnModeSwitchChange text id"+view.getId());
            tView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            actionMode = ACTION_NONE;
            mSelectedViewId = -1;
            Log.d("CHOI_DEBUG","OnModeSwitchChange none");
            tView.setBackground(null);
        }

        invalidateOptionsMenu();
    }


    private static final int CONTACT_PICKER_RESULT = 1001;

    public void doLaunchContactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.add_btn){
            addTextViewWithString(msgEdit.getText().toString());
        } else if (v.getId() == R.id.photo_btn) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, idIntentID);
        }
    }

    public void addTextViewWithString(String message) {
        TextView textView = (TextView)actionHandler.doAddTextView(R.id.content_msg_layout, mLastViewId
                ,message, this);
        Log.d(TAG, "Add textView id:"+textView.getId());
        mMsgMap.put(textView.getId(), textView);
        mLastViewId = textView.getId();//textView.getId();//
    }
    /**
     * 메시지 작성완료시 전송 버튼을 누르면 저장하고 이미지파일, JSON파일을 전송한다.
     */
    private void sendMessage() {
        //final String myPhoneNumber = Utils.getMyPhoneNumber(MsgEditorActivity.this);
        final String myPhoneNumber = RingBearer.getInstance().getMyPhoneNumber();
        final String myPhoneNick = RingBearer.getInstance().getMyPhoneNick();
        EditText editTextRecipientNumber = (EditText)MsgEditorActivity.this.findViewById(R.id.edit_contact);
        final String recipientNumber = editTextRecipientNumber.getText().toString();
        final Map<String, String> messageMap = getMapForMessageSave();
        if (recipientNumber == null || recipientNumber.trim().equals("")) {
            Toast.makeText(this,R.string.phone_number_not_selected, Toast.LENGTH_LONG);
            return;
        }

        //저장후에 서버로 전송한다.httpResponseReceiver를 통해 결과를 받음
        HttpIntentService.startActionMessageRegister(MsgEditorActivity.this,recipientNumber, RingBearer.getInstance().getTokenId()
                , myPhoneNumber,myPhoneNick, (HashMap<String, String>)messageMap);
//
//        new AsyncInsertMessage(new IInsertMessageTask() {
//            @Override
//            public void onTaskCompleted(boolean result) {
//                if (result) {
//                    //저장후에 서버로 전송한다.httpResponseReceiver를 통해 결과를 받음
//                    HttpIntentService.startActionMessageRegister(MsgEditorActivity.this,recipientNumber, RingBearer.getInstance().getTokenId()
//                            , myPhoneNumber,myPhoneNick, (HashMap<String, String>)messageMap);
//                }
//            }
//        }, contentResolverHelper, myPhoneNumber, recipientNumber).execute(messageMap);
    }

    private void receiveMessage() {

    }

    public void addPhotoViewWithImageUri(String imageUriStr ) {
        Log.d(TAG, "addPhotoViewWithImageUri:"+imageUriStr);
        PhotoRelativeLayout photoRelativeLayout = (PhotoRelativeLayout) actionHandler.doAddImageView(R.id.content_msg_layout, mLastViewId
                , imageUriStr);
        mMsgMap.put(photoRelativeLayout.getId(), photoRelativeLayout);
        mLastViewId = photoRelativeLayout.getId();//textView.getId();//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == idIntentID) {
            if (intent != null) {
                Log.d(TAG, "Photopicker: " + intent.getDataString());
//                Cursor cursor = getContentResolver().query(intent.getData(), null, null, null, null);
//                cursor.moveToFirst();  //if not doing this, 01-22 19:17:04.564: ERROR/AndroidRuntime(26264): Caused by: android.database.CursorIndexOutOfBoundsException: Index -1 requested, with a size of 1
//                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                String fileSrc = cursor.getString(idx);
//                Log.d(TAG, "Picture:" + fileSrc);
                addPhotoViewWithImageUri(intent.getDataString());
            }
            else {
                Log.d(TAG, "Photopicker canceled");
                Toast.makeText(this,R.string.message_photo_select_canceled,Toast.LENGTH_LONG);
                //msgEdit.setText("Image selection canceled!");
            }
        } else if (requestCode == CONTACT_PICKER_RESULT) {
            if (resultCode == RESULT_OK) {
                Cursor cursor = null;
                String phoneNumber = "";
                try {
                    Uri result = intent.getData();
                    Log.v(TAG, "Got a contact result: "
                            + result.toString());

                    // get the contact id from the Uri
                    String id = result.getLastPathSegment();

                    // query for everything email
                    cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[] { id },
                            null);

                    int phoneIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA);

                    // let's just get the first email
                    if (cursor.moveToFirst()) {
                        phoneNumber = cursor.getString(phoneIdx);
                        Log.v(TAG, "Got phone number: " + phoneNumber);
                    } else {
                        Log.w(TAG, "No results");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to get phone data", e);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                    EditText editTextContact = (EditText) findViewById(R.id.edit_contact);
                    editTextContact.setText(phoneNumber);
                    if (phoneNumber.length() == 0) {
                        Toast.makeText(this, R.string.message_no_phone_number_avail,
                                Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Log.d(TAG, "Contactpicker canceled");
                Toast.makeText(this,R.string.message_recepient_select_canceled,Toast.LENGTH_LONG);
            }
        }
    }

    private Map<String, String> getMapForMessageSave() {
        Map<String, String> messsageMap = new HashMap<String, String>();

        for (Map.Entry<Integer, View> entry : mMsgMap.entrySet())
        {
            if (entry.getValue() instanceof PhotoRelativeLayout) {
                Log.d(TAG, "getMapForMessageSave:"+entry.getKey()+":"+((PhotoRelativeLayout) entry.getValue()).getUri());
            } else if (entry.getValue() instanceof TextView) {
                Log.d(TAG, "getMapForMessageSave:"+entry.getKey()+":"+((TextView) entry.getValue()).getText().toString());
            }
        }
        int i = 0;
        for (Map.Entry<Integer, View> entry : mMsgMap.entrySet())
        {
            if (entry.getValue() instanceof PhotoRelativeLayout) {
                messsageMap.put(String.format("%d:img",i),((PhotoRelativeLayout) entry.getValue()).getUri());
            } else if (entry.getValue() instanceof TextView) {
                messsageMap.put(String.format("%d:txt",i),((TextView) entry.getValue()).getText().toString());
            }
            i++;
        }
        return messsageMap;
    }

}
