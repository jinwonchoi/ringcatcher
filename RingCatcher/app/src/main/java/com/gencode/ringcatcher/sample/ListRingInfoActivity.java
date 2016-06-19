package com.gencode.ringcatcher.sample;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gencode.ringcatcher.R;
import com.gencode.ringcatcher.common.QuickstartPreferences;
import com.gencode.ringcatcher.common.RingBearer;
import com.gencode.ringcatcher.http.JsonHttpCaller;
import com.gencode.ringcatcher.obj.JsonConstants;
import com.gencode.ringcatcher.obj.RingUpdateRequest;
import com.gencode.ringcatcher.obj.RingUpdateResult;
import com.gencode.ringcatcher.task.AsyncUpdateRing;
import com.gencode.ringcatcher.task.IUpdateRingTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListRingInfoActivity extends AppCompatActivity {
    private static final String TAG = ListRingInfoActivity.class.getSimpleName();
    boolean isUpdate = false;
    final ArrayList<String> list = new ArrayList<String>();
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ring_info);
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

        Intent intent = getIntent();
        String message = intent.getStringExtra(QuickstartPreferences.MENU_KEY);
        if (QuickstartPreferences.MENU_UPDATE_RING_INFO.equals(message)) {
            isUpdate = true;
            this.setTitle(R.string.title_activity_update);
        } else {
            this.setTitle(R.string.title_activity_checkout);
        }
        listview = (ListView) findViewById(R.id.list_ringinfo);
        listview = (ListView) findViewById(R.id.list_ringinfo);
        String[] values = new String[] {};
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                Toast.makeText(view.getContext(), String.format("이 음원들 등록합니다.[%s]", item), Toast.LENGTH_LONG).show();
            }

        });
        AsyncUpdateRing asyncUpdateRing =new AsyncUpdateRing(new IUpdateRingTask() {
            @Override
            public void OnTaskCompleted(RingUpdateResult result) {
                List<String> updateList =  result.getUpdateList();
                Log.d(TAG, "OnTaskCompleted result:"+result);
                try {
                    for (String item : updateList) {
                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(item);
                        String callingNum = jsonObject.optString(JsonConstants.callingNum);
                        String filePath  = jsonObject.optString(JsonConstants.filePath);
                        list.add(callingNum+":"+filePath);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        asyncUpdateRing.execute(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String tokenId = RingBearer.getInstance().getTokenId();
        String myPhoneNumer = RingBearer.getInstance().getMyPhoneNumber();
        String myPhoneNick = RingBearer.getInstance().getMyPhoneNick();

        list.clear();
        list.add("-");
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public void remove(String object) {
            super.remove(object);
            mIdMap.remove(object);
        }

        @Override
        public void clear() {
            super.clear();
            mIdMap.clear();
        }

        @Override
        public long getItemId(int position) {

            String item = getItem(position);
            Integer result = mIdMap.get(item);
            if (result==null) result = 0;
            return result;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
