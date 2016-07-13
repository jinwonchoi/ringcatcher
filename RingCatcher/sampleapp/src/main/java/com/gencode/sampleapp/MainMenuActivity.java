package com.gencode.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gencode.ringcatcher.common.RingBearer;
import com.gencode.ringcatcher.http.ReturnCode;
import com.gencode.ringcatcher.obj.MessageResult;
import com.gencode.ringcatcher.task.AsyncGetMessage;
import com.gencode.ringcatcher.task.IGetMessageTask;
import com.gencode.ringmsgeditor.MsgEditorActivity;
import com.gencode.sampleapp.R;
import com.gencode.ringcatcher.gcm.GcmActivity;
import com.gencode.ringcatcher.common.QuickstartPreferences;
import com.gencode.ringcatcher.obj.JsonConstants;

//import com.gencode.verysimpleapp.gcm.GcmActivity;

public class MainMenuActivity extends GcmActivity /*AppCompatActivity*/ implements AdapterView.OnItemClickListener{
    private static final String TAG = MainMenuActivity.class.getSimpleName();

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
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

        listView = (ListView)findViewById(R.id.menulistview);
        listView.setOnItemClickListener(this);
    }

    /**
     * ring message 노티를 받았을때 처리사항 구현
     * 예> DB에 저장
     */
    protected void processNotificationMessage() {
        Intent receivingIntent = getIntent();
        String callingNum = receivingIntent.getStringExtra(JsonConstants.callingNum);
        String callingName = receivingIntent.getStringExtra(JsonConstants.callingName);
        String ringSrcUrl = receivingIntent.getStringExtra(JsonConstants.ringSrcUrl);
        String expiredDate = receivingIntent.getStringExtra(JsonConstants.expiredDate);
        String durationType = receivingIntent.getStringExtra(JsonConstants.durationType);

        Log.d(TAG,"processNotificationMessage callingNum="+callingNum );
        if (callingNum != null && !callingNum.equals("")) {
            Intent intent = new Intent(this, NotificationCheckActivity.class);
            intent.putExtra(JsonConstants.callingNum, callingNum);
            intent.putExtra(JsonConstants.callingName, callingName);
            intent.putExtra(JsonConstants.ringSrcUrl, ringSrcUrl);
            intent.putExtra(JsonConstants.expiredDate, expiredDate);
            intent.putExtra(JsonConstants.durationType, durationType);
            startActivity(intent);
        }
    }
    /*
     * Parameters:
        adapter - The AdapterView where the click happened.
        view - The view within the AdapterView that was clicked
        position - The position of the view in the adapter.
        id - The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                Toast.LENGTH_SHORT).show();
        if (id == 0) {//<item>Register My Number, Nick, tokenId </item>
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra(QuickstartPreferences.MENU_KEY, QuickstartPreferences.MENU_REGISTRATION);
            Log.d(TAG, "id="+id+" menu_key="+QuickstartPreferences.MENU_REGISTRATION);
            startActivity(intent);
        } else if (id == 1) {//<item>Send My ring to mates</item>
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra(QuickstartPreferences.MENU_KEY, QuickstartPreferences.MENU_INVITE_MATE);
            Log.d(TAG, "id="+id+" menu_key="+QuickstartPreferences.MENU_INVITE_MATE);
            startActivity(intent);
        } else if (id == 2) {//<item>Download mates' ring</item>
            Intent intent = new Intent(this, ListRingInfoActivity.class);
            intent.putExtra(QuickstartPreferences.MENU_KEY, QuickstartPreferences.MENU_UPDATE_RING_INFO);
            Log.d(TAG, "id="+id+" menu_key="+QuickstartPreferences.MENU_UPDATE_RING_INFO);
            startActivity(intent);
        } else if (id == 3) {//<item>Download all rings</item>
            String message = "{\"userNum\":\"01055557777\",\"callerNum\":\"0244445555\",\"ringSrcUrl\":\"\\/ringmedia\\/20160419\\/0244445555_01055557777.jpg\",\"callerName\":\"Mememe\"}";
            Intent intent = new Intent(this, ListRingInfoActivity.class);
            intent.putExtra(QuickstartPreferences.MENU_KEY, QuickstartPreferences.MENU_CHECKOUT_RING_INFO);
            Log.d(TAG, "id="+id+" menu_key="+QuickstartPreferences.MENU_CHECKOUT_RING_INFO);
            startActivity(intent);
        } else if (id == 4) {//메시지 에디터 열기 0244445555 => 나 01055557777=> 받는사람
            RingBearer.getInstance().setMyPhoneNumber(getResources().getString(R.string.test_my_phone_number));
            RingBearer.getInstance().setMyPhoneNick(getResources().getString(R.string.test_my_phone_nick));
            RingBearer.getInstance().setTokenId(getResources().getString(R.string.test_my_token_id));
            Intent intent = new Intent(this, MsgEditorActivity.class);
            //intent.putExtra(QuickstartPreferences.MENU_KEY, QuickstartPreferences.MENU_CHECKOUT_RING_INFO);
            Log.d(TAG, "id="+id+" menu_key="+QuickstartPreferences.MENU_MESSAGE_EDITOR);
            startActivity(intent);
        } else if (id == 5) {//메시지 띄우기 01055557777 => 나 0244445555=> 보낸사람
            RingBearer.getInstance().setMyPhoneNumber(getResources().getString(R.string.test_my_phone_number2));
            RingBearer.getInstance().setMyPhoneNick(getResources().getString(R.string.test_my_phone_nick2));
            //RingBearer.getInstance().setTokenId(getResources().getString(R.string.test_my_token_id));

            AsyncGetMessage  asyncGetMessage = new AsyncGetMessage(new IGetMessageTask() {
                @Override
                public void OnTaskCompleted(MessageResult messageResult) {
                    TextView textView = (TextView)findViewById(R.id.text_register_result);
                    String tokenId = RingBearer.getInstance().getTokenId();
                    String myPhoneNumer = RingBearer.getInstance().getMyPhoneNumber();
                    String myPhoneNick = RingBearer.getInstance().getMyPhoneNick();
                    String callingNum = MainMenuActivity.this.getResources().getString(R.string.test_my_phone_number);
                    Log.d(TAG, "AsyncGetMessage OnTaskCompleted:"+messageResult.toString());
                    if (messageResult != null && messageResult.getResultCode().equals(ReturnCode.SUCCESS.get())) {
                        //디폴트 메시지 보기
                        if ("".equals(messageResult.getDefaultJsonMessage())) {
                            String defaultExpiredDate = messageResult.getDefaultExpiredDate();
                            String defaultDurationType = messageResult.getDefaultDurationType();
                            String defaultJsonMessage = messageResult.getDefaultJsonMessage();
                            // defaultExpiredDate (yyyymmdd)를 확인
                            // defaultDurationType 을 확인 : A- 1회용, T-기간지정, P-계속사용
                            Intent intent = new Intent(MainMenuActivity.this, MsgEditorActivity.class);
                            intent.setAction(com.gencode.ringcatcher.gcm.QuickstartPreferences.ACTION_MESSAGE_VIEW);
                            intent.putExtra(com.gencode.ringmsgeditor.JsonConstants.callingNum, getResources().getString(R.string.test_my_phone_number));
                            intent.putExtra(com.gencode.ringmsgeditor.JsonConstants.jsonMessage, defaultJsonMessage);
                            startActivity(intent);
                        }
                        //지정 메시지 보기
                        if ("".equals(messageResult.getJsonMessage())) {
                            String expiredDate = messageResult.getExpiredDate();
                            String durationType = messageResult.getDurationType();
                            String jsonMessage = messageResult.getJsonMessage();
                            // expiredDate (yyyymmdd)를 확인
                            // durationType 을 확인 : A- 1회용, T-기간지정, P-계속사용
                            Intent intent = new Intent(MainMenuActivity.this, MsgEditorActivity.class);
                            intent.setAction(com.gencode.ringcatcher.gcm.QuickstartPreferences.ACTION_MESSAGE_VIEW);
                            intent.putExtra(com.gencode.ringmsgeditor.JsonConstants.callingNum, getResources().getString(R.string.test_my_phone_number));
                            intent.putExtra(com.gencode.ringmsgeditor.JsonConstants.jsonMessage, jsonMessage);
                            startActivity(intent);
                        }
                    } else {
                        //error
                    }
                };
            });
            asyncGetMessage.execute();
            Log.d(TAG, "id="+id+" menu_key="+QuickstartPreferences.MENU_MESSAGE_EDITOR);

        }
    }

}
