package com.example.administrator.maxflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Add extends Activity {
    Button Add;
    EditText from;
    EditText to;
    EditText cap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add);
        Add = (Button) findViewById(R.id.Add);
        from = (EditText) findViewById(R.id.editText);
        to = (EditText) findViewById(R.id.editText2);
        cap = (EditText) findViewById(R.id.editText3);
        Add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                if (from.getText().toString().equals("") || to.getText().toString().equals("") || cap.getText().toString().equals("")) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
                String result = from.getText().toString() + " " + to.getText().toString() + " " + cap.getText().toString();
                Log.d("RESULT OF ADD",result);
                i.putExtra("result", result);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
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
