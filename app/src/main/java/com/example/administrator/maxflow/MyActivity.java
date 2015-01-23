package com.example.administrator.maxflow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MyActivity extends Activity {
    public static final String KEY_NODES = "size";
    public static final String KEY_SOURCE = "source";
    public static final String KEY_SINK = "sink";
    public EditText nodes;
    public EditText source;
    public EditText sink;
    public Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        next = (Button) findViewById(R.id.next);
        nodes = (EditText) findViewById(R.id.nodes);
        source = (EditText) findViewById(R.id.source);
        sink = (EditText) findViewById(R.id.sink);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


    int n = Integer.parseInt(nodes.getText().toString());
    int so = Integer.parseInt(source.getText().toString());
    int si = Integer.parseInt(sink.getText().toString());
    if (n > 0 && so < n && so >= 0 && si < n && si >= 0 && so != si) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_NODES, "" + n);
        editor.putString(KEY_SOURCE, "" + so);
        editor.putString(KEY_SINK, "" + si);
        editor.commit();


        startActivity(new Intent(getBaseContext(), Input.class));
    } else {
        Toast.makeText(getBaseContext(), "Invalid entries", Toast.LENGTH_SHORT).show();
    }


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
