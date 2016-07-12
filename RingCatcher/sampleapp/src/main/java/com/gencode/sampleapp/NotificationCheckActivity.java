package com.gencode.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gencode.sampleapp.R;
import com.gencode.ringcatcher.common.QuickstartPreferences;
import com.gencode.ringcatcher.http.HttpConstants;
import com.gencode.ringcatcher.obj.JsonConstants;

public class NotificationCheckActivity extends AppCompatActivity {
    private static final String TAG = NotificationCheckActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_check);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String callingNum = intent.getStringExtra(JsonConstants.callingNum);
        String callingName = intent.getStringExtra(JsonConstants.callingName);
        String ringSrcUrl = intent.getStringExtra(JsonConstants.ringSrcUrl);
        TextView tvCallerNum = (TextView) findViewById(R.id.text_caller_num);
        TextView tvCallerName = (TextView) findViewById(R.id.text_caller_name);
        TextView tvRingSrcUrl = (TextView) findViewById(R.id.text_ring_src_url);

        tvCallerNum.setText(callingNum);
        tvCallerName.setText(callingName);
        tvRingSrcUrl.setText(HttpConstants.RING_CATCHER_MEDIA_HOME+ringSrcUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String message = intent.getStringExtra(QuickstartPreferences.MENU_KEY);


        //show result
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       //Handle app bar item clicks here. The app bar
       int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
