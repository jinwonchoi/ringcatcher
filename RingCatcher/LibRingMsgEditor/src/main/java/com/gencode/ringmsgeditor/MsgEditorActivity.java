package com.gencode.ringmsgeditor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gencode.ringcatcher.common.RingBearer;
import com.gencode.ringcatcher.gcm.QuickstartPreferences;
import com.gencode.ringcatcher.http.HttpConstants;
import com.gencode.ringcatcher.http.HttpIntentService;
import com.gencode.ringcatcher.http.MessageWrapper;
import com.gencode.ringcatcher.http.ReturnCode;
import com.gencode.ringcatcher.obj.RegisterResult;
import com.gencode.ringmsgeditor.task.AsyncInsertMessage;
import com.gencode.ringmsgeditor.task.IInsertMessageTask;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class MsgEditorActivity extends AppCompatActivity implements OnModeSwitchListener, OnImageSizeChangeListener {
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
    //private Map<Integer, View> mMsgMap = new HashMap<Integer, View>();
    private int mSelectedViewId = -1;
    //private int mLastViewId = -1;

    //private EditText msgEdit;

    RelativeLayout mContentMainLayout;
    LinearLayout mUpperEditLayout;
    LinearLayout mBottomEditLayout;
    AppBarLayout mAppBar;
    Button mBtnContact;
    int idIntentID = 999;//앨범사진추가용
    EditText mEditTextContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_editor);

//        profileId = getIntent().getStringExtra(Common.PROFILE_ID);
//        msgEdit = (EditText) findViewById(R.id.msg_edit);
//        sendBtn = (Button) findViewById(R.id.add_btn);
//        photoBtn = (Button) findViewById(R.id.photo_btn);

        mUpperEditLayout = (LinearLayout)findViewById(R.id.layout_upper_edit);
        mBottomEditLayout = (LinearLayout)findViewById(R.id.layout_bottom_edit);
        mAppBar = (AppBarLayout)findViewById(R.id.appbar);
        mBtnContact = (Button)findViewById(R.id.btn_add_contact);
        mEditTextContact = (EditText) findViewById(R.id.edit_contact);
        mContentMainLayout = (RelativeLayout)findViewById(R.id.content_msg_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEditTextContact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mEditTextContact.setText(actionHandler.toNeatContactString(mEditTextContact.getText().toString()));
                }
            }
        });
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
//            EditText et = (EditText) findViewById(R.id.msg_edit);
//            et.setWidth(width);
        }
        registerReceiver();
        //contentResolverHelper.checkQuery();
        Log.d(TAG, "mMsgMap.size:"+actionHandler.getMsgMap().size());
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
        completeEditText();
        invalidateOptionsMenu();
    }

    private void displayEditMode() {
        Log.d(TAG, "displayEditMode");
        mUpperEditLayout.setVisibility(View.VISIBLE);
        mBottomEditLayout.setVisibility(View.VISIBLE);
        actionMode = ACTION_NONE;
        prepareEditText();
        EditText editText = (EditText)findViewById(actionHandler.getLastViewId());
        if(editText.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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

    /**
     *
     */
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
            } else {//gcm noti를 받고 클릭했을때 처리. 당분간 gcm 통하는 처리는 없음
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

//    private void clearMessagesOnEdit() {
//        Log.d(TAG, "clearMessagesOnEdit");
//        for (Map.Entry<Integer, View> entry : mMsgMap.entrySet()) {
//            if (entry.getValue() instanceof TextView) {
//                actionHandler.doDeleteTextView(R.id.content_msg_layout, entry.getKey());
//            } else if (entry.getValue() instanceof EditText) {
//                actionHandler.doDeleteEditText(R.id.content_msg_layout, entry.getKey());
//            }else {
//                actionHandler.doDeleteImageView(R.id.content_msg_layout, entry.getKey());
//            }
//        }
//        mMsgMap.clear();
//    }

    private void registerReceiver() {
        // The filter's action is BROADCAST_ACTION
        IntentFilter mStatusIntentFilter = new IntentFilter(
                HttpConstants.ACTION_BROADCAST_IMAGE_UPLOAD);
        mStatusIntentFilter.addAction(HttpConstants.ACTION_BROADCAST_MESSAGE_REGISTER);
        // Instantiates a new DownloadStateReceiver
        httpResponseReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //RegisterResult registerResult = null;
                //intentResult.putExtra(com.gencode.ringcatcher.obj.JsonConstants.resultCode, registerResult.getResultCode());
                String resultCode = intent.getStringExtra(com.gencode.ringcatcher.obj.JsonConstants.resultCode);
                if (ReturnCode.SUCCESS.get().equals(resultCode)||ReturnCode.UPDATE_OK.get().equals(resultCode)) {
                    Utils.showWarningDialog(MsgEditorActivity.this, R.string.title_send_message_ok,R.string.send_message_ok);
                } else {
                    Utils.showWarningDialog(MsgEditorActivity.this, R.string.title_send_message_fail,R.string.send_message_fail);
                }
                Log.d(TAG, "message delivered! result:"+resultCode);
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
                actionHandler.doDeleteEditText(R.id.content_msg_layout, mSelectedViewId);
            } else if (actionMode == ACTION_IMAGE_EDIT) {
                actionHandler.doDeleteImageView(R.id.content_msg_layout, mSelectedViewId);
            }
            actionMode = ACTION_NONE;
            invalidateOptionsMenu();
        } else if (item.getItemId() == R.id.action_preview) {
            if (actionHandler.isLayoutEmpty()) {
                Utils.showWarningDialog(this, R.string.title_message_empty, R.string.message_empty);
                return false;
            } else {
                actionMode = ACTION_PREVIEW;
                displayPreviewModeOnEdit();
            }
        } else if (item.getItemId() == R.id.action_send) {
            if (actionHandler.isLayoutEmpty()) {
                Utils.showWarningDialog(this, R.string.title_message_empty, R.string.message_empty);
                return false;
            } else {
                if (!sendMessage()) return false;
                actionMode = ACTION_NONE;
                actionHandler.clearMessagesOnEdit();
                displayEditMode();
            }
        } else if (/*item.getItemId() == R.id.action_back_from_preview || */item.getItemId() == R.id.action_back_from_send) {
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
        for (Map.Entry<Integer, View> entry : actionHandler.getMsgMap().entrySet())
        {
            if (entry.getValue() instanceof EditText && !entry.getValue().equals(view)) {
                ((EditText)entry.getValue()).setBackgroundResource(android.R.color.transparent);
            }
        }

        EditText tView = (EditText) view;
        if (((ColorDrawable)tView.getBackground()).getColor() == getResources().getColor(android.R.color.transparent)) {
            actionMode = ACTION_TEXT_EDIT;
            mSelectedViewId = view.getId();
            Log.d("CHOI_DEBUG","OnModeSwitchChange text id"+view.getId());
            tView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            actionMode = ACTION_NONE;
            mSelectedViewId = -1;
            Log.d("CHOI_DEBUG","OnModeSwitchChange none");
            tView.setBackgroundResource(android.R.color.transparent);
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
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, idIntentID);
    }

    //preview 용
    public void addTextViewWithString(String message) {
        TextView textView = (TextView)actionHandler.doAddTextView(R.id.content_msg_layout
                ,message, this);
        Log.d(TAG, "Add textView id:"+textView.getId());
        Log.d(TAG, "Add textView lastId="+actionHandler.getLastViewId());
    }

    //최초 또는 사진첨부후 편집모드 클릭 후 수행
    public void prepareEditText() {
        if (actionMode == ACTION_ONLEY_PREVIEW) return;
        EditText editText= (EditText)actionHandler.doAddEditText(R.id.content_msg_layout,this);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        RelativeLayout mContentMessageLayout = (RelativeLayout)findViewById(R.id.content_msg_layout);
        ViewGroup.LayoutParams mainLayoutParams = mContentMessageLayout.getLayoutParams();
        mainLayoutParams.height = mContentMessageLayout.getMeasuredHeight() + editText.getHeight()*3;
        mContentMessageLayout.setLayoutParams(mainLayoutParams);

//        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.content_msg_nestedscrl_view);
//        nestedScrollView.scrollTo(0, 0);

        //editText.setSelection(0);
        Log.d(TAG, "prepareEditText Add editText id:"+editText.getId());
    }

    //사진첨부 또는 미리보기 클릭, 보내기 이전 수행
    public void completeEditText() {
        View lastView = actionHandler.getMsgMap().get(actionHandler.getLastViewId());
        if (lastView instanceof  EditText) {
            if (((EditText)lastView).getText().toString().trim().equals("")) {
                actionHandler.doDeleteEditText(R.id.content_msg_layout, actionHandler.getLastViewId());
            }
        }
    }

    /**
     * 메시지 작성완료시 전송 버튼을 누르면 저장하고 이미지파일, JSON파일을 전송한다.
     */
    private boolean sendMessage() {
        //final String myPhoneNumber = Utils.getMyPhoneNumber(MsgEditorActivity.this);
        final String myPhoneNumber = RingBearer.getInstance().getMyPhoneNumber();
        final String myPhoneNick = RingBearer.getInstance().getMyPhoneNick();
        //EditText editTextRecipientNumber = (EditText)MsgEditorActivity.this.findViewById(R.id.edit_contact);
        Log.d(TAG, "sendMessage recipientNumber="+actionHandler.getContactNumberList());
        //테스트용으로 대치함
        final String recipientNumber = "01055557777";//actionHandler.getContactNumberList();
                //editTextRecipientNumber.getText().toString();
        completeEditText();
        final Map<String, String> messageMap = actionHandler.getMapForMessageSave();
        if (recipientNumber == null || recipientNumber.trim().equals("")) {
            Utils.showWarningDialog(this, R.string.title_phone_number_not_selected,R.string.phone_number_not_selected);
            return false;
        }

        //저장후에 서버로 전송한다.httpResponseReceiver를 통해 결과를 받음
        HttpIntentService.startActionMessageRegister(MsgEditorActivity.this,recipientNumber, RingBearer.getInstance().getTokenId()
                , myPhoneNumber,myPhoneNick,"20161231","T", (HashMap<String, String>)messageMap);
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
        return true;
    }

    private void receiveMessage() {

    }

    public void addPhotoViewWithImageUri(String imageUriStr ) {
        Log.d(TAG, "Add Photo addPhotoViewWithImageUri:"+imageUriStr);
        Log.d(TAG, "Add Photo lastId="+actionHandler.getLastViewId());
        completeEditText();
        PhotoRelativeLayout photoRelativeLayout = (PhotoRelativeLayout) actionHandler.doAddImageView(R.id.content_msg_layout
                , imageUriStr);
        prepareEditText();
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
                String phoneName = "";
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

                    int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA);
                    int phoneNameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                    // let's just get the first email
                    if (cursor.moveToFirst()) {
                        phoneNumber = cursor.getString(phoneNumberIdx);
                        phoneName= cursor.getString(phoneNameIdx);
                        Log.v(TAG, "Got phone number: " + phoneNumber+" Name:"+phoneName);
                    } else {
                        Log.w(TAG, "No results");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to get phone data", e);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                    actionHandler.addContactToList(mEditTextContact, phoneNumber,phoneName);
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

    @Override
    public void OnImageSizeChanged(View v) {
        Log.d(TAG, "OnImageSizeChanged mContentMainLayout width="+mContentMainLayout.getWidth() +" height="+mContentMainLayout.getHeight());
        mContentMainLayout.invalidate();
        Log.d(TAG, "invalidate         mContentMainLayout width="+mContentMainLayout.getWidth() +" height="+mContentMainLayout.getHeight());
    }
}
