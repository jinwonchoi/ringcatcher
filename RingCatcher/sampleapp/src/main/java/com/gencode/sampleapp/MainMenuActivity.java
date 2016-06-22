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
        Log.d(TAG,"processNotificationMessage callingNum="+callingNum );
        if (callingNum != null && !callingNum.equals("")) {
            Intent intent = new Intent(this, NotificationCheckActivity.class);
            intent.putExtra(JsonConstants.callingNum, callingNum);
            intent.putExtra(JsonConstants.callingName, callingName);
            intent.putExtra(JsonConstants.ringSrcUrl, ringSrcUrl);
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
        } else if (id == 4) {//메시지 에디터 열기
            Intent intent = new Intent(this, MsgEditorActivity.class);
            //intent.putExtra(QuickstartPreferences.MENU_KEY, QuickstartPreferences.MENU_CHECKOUT_RING_INFO);
            Log.d(TAG, "id="+id+" menu_key="+QuickstartPreferences.MENU_MESSAGE_EDITOR);
            startActivity(intent);
        }
    }

}
