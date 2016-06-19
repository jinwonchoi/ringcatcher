package com.gencode.ringcatcher.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gencode.ringcatcher.R;
import com.gencode.ringcatcher.common.QuickstartPreferences;
import com.gencode.ringcatcher.common.RingBearer;
import com.gencode.ringcatcher.http.JsonHttpCaller;
import com.gencode.ringcatcher.obj.InviteResult;
import com.gencode.ringcatcher.obj.InviteUploadRequest;
import com.gencode.ringcatcher.obj.RegisterRequest;
import com.gencode.ringcatcher.obj.RegisterResult;
import com.gencode.ringcatcher.task.AsyncInviteMate;
import com.gencode.ringcatcher.task.AsyncRegisterUser;
import com.gencode.ringcatcher.task.IInvteMateTask;
import com.gencode.ringcatcher.task.IRegisterTask;

import java.io.File;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    boolean isRegistration = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        EditText eMyNumber = (EditText)findViewById(R.id.edit_my_number);
        EditText eMyPhoneNick = (EditText)findViewById(R.id.edit_my_phone_nick);


        Intent intent = getIntent();
        String message = intent.getStringExtra(QuickstartPreferences.MENU_KEY);
        Log.d(TAG, "message="+message);

        if (message == null || message.equals(QuickstartPreferences.MENU_REGISTRATION)) {
            isRegistration = true;
            this.setTitle(R.string.title_activity_register);
            LinearLayout layout = (LinearLayout) findViewById(R.id.layout_recient);
            layout.setVisibility(View.INVISIBLE);

            eMyNumber.setText("01055557777");
            eMyPhoneNick.setText("나야 나!");
        } else {//if (message.equals(QuickstartPreferences.MENU_INVITE_MATE)) {
            isRegistration = false;
            this.setTitle(R.string.title_activity_invite);
            EditText eMateNumber = (EditText)findViewById(R.id.edit_the_other_number);
            EditText eMateNick = (EditText)findViewById(R.id.edit_the_other_phone_nick);

            //test-token-id 0244445555 It's me!
            eMyNumber.setText("0244445555");
            eMyPhoneNick.setText("It's me, Dude!");
            eMateNumber.setText("01055557777");
            eMateNick.setText("나야 나!");
//            eMateNumber.setText(RingBearer.getInstance().getFriendPhoneNumber());
//            eMateNick.setText(RingBearer.getInstance().getFriendNick());
        }
    }

    public void sendMessage(View view) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean sentToken = sharedPreferences
                .getBoolean(com.gencode.gcm.QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
        if (sentToken) {
            String token = sharedPreferences.getString(com.gencode.gcm.QuickstartPreferences.TOKEN_ID_FROM_GCM,"");
            Log.d(TAG, "sharedPreferences.TokenId="+token);
            RingBearer.getInstance().setTokenId(token);
        }

        Intent intent = new Intent(this, NotificationCheckActivity.class);
        EditText eMyNumber = (EditText)findViewById(R.id.edit_my_number);
        EditText eMyPhoneNick = (EditText)findViewById(R.id.edit_my_phone_nick);

        RingBearer.getInstance().setMyPhoneNumber(eMyNumber.getText().toString());
        RingBearer.getInstance().setMyPhoneNick(eMyPhoneNick.getText().toString());
        if (isRegistration) {
            AsyncRegisterUser asyncRegisterUser = new AsyncRegisterUser(new IRegisterTask() {
                @Override
                public void OnTaskCompleted(RegisterResult registerResult) {
                    TextView textView = (TextView)findViewById(R.id.text_register_result);
                    String tokenId = RingBearer.getInstance().getTokenId();
                    String myPhoneNumer = RingBearer.getInstance().getMyPhoneNumber();
                    String myPhoneNick = RingBearer.getInstance().getMyPhoneNick();
                    if (registerResult != null && !registerResult.equals("")) {
                        //Toast.makeText(this, R.string.gcm_send_message, Toast.LENGTH_LONG).show();
                        textView.setText(String.format("Register: result[%s:%s]\n my tokenId[%s]\nmynumber[%s]\nmynick[%s]\noverwrite[%s]"
                                ,registerResult.getResultCode(),registerResult.getResultMsg()
                                ,tokenId.substring(0,20),myPhoneNumer,myPhoneNick, true));
                    } else {
                        Toast.makeText(RegisterActivity.this, R.string.token_error_message, Toast.LENGTH_LONG).show();
                        textView.setText(String.format("RegisterError:result[%s:%s]\n" +
                                        " my tokenId[%s]\nmynumber[%s]\nmynick[%s]\noverwrite[%s]"
                                ,registerResult.getResultCode(),registerResult.getResultMsg()
                                ,tokenId.substring(0,20),myPhoneNumer,myPhoneNick, true));
                    }
                };
            });
            asyncRegisterUser.execute();
        } else {
            EditText eMateNumber = (EditText) findViewById(R.id.edit_the_other_number);
            EditText eMateNick = (EditText) findViewById(R.id.edit_the_other_phone_nick);
            TextView textMedia = (TextView) findViewById(R.id.text_media_file);
            RingBearer.getInstance().setFriendPhoneNumber(eMateNumber.getText().toString());
            RingBearer.getInstance().setFriendNick(eMateNick.getText().toString());
            RingBearer.getInstance().setMediaUrl(textMedia.getText().toString());
            AsyncInviteMate asyncInviteMate = new AsyncInviteMate(new IInvteMateTask() {
                @Override
                public void OnTaskCompleted(InviteResult result) {
                    TextView textView = (TextView)findViewById(R.id.text_register_result);
                    String tokenId = RingBearer.getInstance().getTokenId();
                    String myPhoneNumer = RingBearer.getInstance().getMyPhoneNumber();
                    String myPhoneNick = RingBearer.getInstance().getMyPhoneNick();
                    String friendNumber = RingBearer.getInstance().getFriendPhoneNumber();
                    String fileUrl = RingBearer.getInstance().getMediaUrl();

                    if (result != null && !result.equals("")) {
                        //Toast.makeText(this, R.string.gcm_send_message, Toast.LENGTH_LONG).show();
                        textView.setText(String.format("Register: result[%s:%s]\n my tokenId[%s]\nmy number[%s]\nmy nick[%s]\nfriend number[%s]\n fileUrl[%s]"
                                ,result.getResultCode(),result.getResultMsg()
                                ,tokenId.substring(0,20),myPhoneNumer,myPhoneNick, friendNumber, fileUrl));
                    } else {
                        Toast.makeText(RegisterActivity.this, R.string.token_error_message, Toast.LENGTH_LONG).show();
                        textView.setText(String.format("RegisterError:result[%s:%s]\n" +
                                        " my tokenId[%s]\nmy number[%s]\nmy nick[%s]\n" +
                                        "friend number[%s]\n" +
                                        " fileUrl[%s]"
                                ,result.getResultCode(),result.getResultMsg()
                                ,tokenId.substring(0,20),myPhoneNumer,myPhoneNick, friendNumber, fileUrl));
                    }
                }
            });
            asyncInviteMate.execute();
        }
    }

    public void pickMedia(View view) {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,1);
    }

    /**
     * 음원선택에 대한 처리
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                //the selected audio.
                //Uri uri = data.getData();
                String filePath = null;
                Uri _uri = data.getData();
                Log.d("","URI = "+ _uri);
                if (_uri != null && "content".equals(_uri.getScheme())) {
                    Cursor cursor = this.getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
                    cursor.moveToFirst();
                    filePath = cursor.getString(0);
                    cursor.close();
                } else {
                    filePath = _uri.getPath();
                }
                File file = new File(""+_uri);
                Log.d(TAG,"Chosen path = "+ filePath+" filename="+file.getName());

                TextView mediaView =(TextView)findViewById(R.id.text_media_file);
                mediaView.setText(filePath);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    class AsyncRegisterUser extends AsyncTask<Void, Void, RegisterResult> {
//        @Override
//        protected RegisterResult doInBackground(Void... args) {
//            this.
//            String matePhoneNumer = RingBearer.getInstance().getFriendPhoneNumber();
//            String matePhoneNick = RingBearer.getInstance().getFriendNick();
//            String mediaUrl = RingBearer.getInstance().getMediaUrl();
//
//            String tokenId = RingBearer.getInstance().getTokenId();
//            RegisterRequest registerRequest = new RegisterRequest();
//            registerRequest.setTokenId(tokenId);
//            registerRequest.setUserPhoneNum(RingBearer.getInstance().getMyPhoneNumber());
//            registerRequest.setUserNick(RingBearer.getInstance().getMyPhoneNick());
//            registerRequest.setOverwrite(true);
//            RegisterResult registerResult = null;
//            if (tokenId == null || tokenId.equals("")) {
//                Log.e(TAG, "Calling Asynch RegisterUser tokenId=null");
//                return null;
//            }
//
//            try {
//                JsonHttpCaller caller = new JsonHttpCaller();
//                registerResult = caller.registerUser(registerRequest);
//            } catch (Exception e) {
//                Log.e(TAG, "Calling Asynch RegisterUser",e);
//            }
//            return registerResult;
//        }
//
//        @Override
//        protected void onPostExecute(RegisterResult strResult) {
//            super.onPostExecute(strResult);
//            TextView textView = (TextView)findViewById(R.id.text_register_result);
//            String tokenId = RingBearer.getInstance().getTokenId();
//            String myPhoneNumer = RingBearer.getInstance().getMyPhoneNumber();
//            String myPhoneNick = RingBearer.getInstance().getMyPhoneNick();
//            if (strResult != null && !strResult.equals("")) {
//                //Toast.makeText(this, R.string.gcm_send_message, Toast.LENGTH_LONG).show();
//                textView.setText(String.format("Register: result[%s:%s]\n my tokenId[%s]\nmynumber[%s]\nmynick[%s]\noverwrite[%s]"
//                        ,strResult.getResultCode(),strResult.getResultMsg()
//                        ,tokenId.substring(0,20),myPhoneNumer,myPhoneNick, true));
//            } else {
//                Toast.makeText(RegisterActivity.this, R.string.token_error_message, Toast.LENGTH_LONG).show();
//                textView.setText(String.format("RegisterError:result[%s:%s]\n" +
//                                " my tokenId[%s]\nmynumber[%s]\nmynick[%s]\noverwrite[%s]"
//                        ,strResult.getResultCode(),strResult.getResultMsg()
//                        ,tokenId.substring(0,20),myPhoneNumer,myPhoneNick, true));
//            }
//        }
//    }
//test-token-id 0244445555 It's me!

}
