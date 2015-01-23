package com.example.administrator.maxflow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

public class Input extends Activity {
    ListView list;
    TextView title;
    TextView perc;
    SharedPreferences prefs;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> resultAdapter;
    ProgressDialog dialog;
    ArrayList<String> result;
    final int REQUEST_ADD = 98;
    int n;
    int source, sink;
    int[][] capacity;
    int iterations;
    int limit;
    double percentage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        n = Integer.parseInt(prefs.getString(MyActivity.KEY_NODES, "0"));
        source = Integer.parseInt(prefs.getString(MyActivity.KEY_SOURCE, "-1"));
        sink = Integer.parseInt(prefs.getString(MyActivity.KEY_SINK, "-1"));
        capacity = new int[n][n];
        for (int i = 0; i < n; i++)
            Arrays.fill(capacity[i], -1);
        list = (ListView) findViewById(R.id.list);
        title = (TextView) findViewById(R.id.TitleInput);
        perc = (TextView) findViewById(R.id.PercInput);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.input, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_ADD) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                Log.d("RECEIVED RESULT", result);
                String[] split = result.split(" ");
                int from = Integer.parseInt(split[0]);
                int to = Integer.parseInt(split[1]);
                int capa = Integer.parseInt(split[2]);
                if (to >= n || from >= n || capa <= 0) {
                    Toast.makeText(this, "Invalid Entry", Toast.LENGTH_SHORT).show();
                } else {
                    capacity[from][to] = capa;
                    capacity[to][from] = 0;
                    adapter.add(from + " --> " + to + " c= " + capa);
                    adapter.notifyDataSetChanged();
                }

            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.adder) {
            Intent i = new Intent(getBaseContext(), Add.class);
            startActivityForResult(i, REQUEST_ADD);
            return true;
        }
        if (id == R.id.Go) {

            maxFlow();


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int INF = Integer.MAX_VALUE;

    public void init() {
        n = 7;
        source = 0;
        sink = 3;
        capacity = new int[n][n];
        for (int i = 0; i < n; i++)
            Arrays.fill(capacity[i], -1);
        capacity[0][1] = 3;
        capacity[1][0] = 0;

        capacity[1][2] = 3;
        capacity[2][1] = 0;

        capacity[2][3] = 2;
        capacity[3][2] = 0;

        capacity[4][3] = 3;
        capacity[3][4] = 0;

        capacity[5][4] = 2;
        capacity[4][5] = 0;

        capacity[6][5] = 4;
        capacity[5][6] = 0;

        capacity[0][6] = 1;
        capacity[6][0] = 0;

        capacity[6][2] = 5;
        capacity[2][6] = 0;
    }


    public int findPath(int[][] residual) {
        int n = residual[0].length;
        LinkedList<Integer> q = new LinkedList<Integer>();
        boolean visited[] = new boolean[n];
        int[] from = new int[n];
        Arrays.fill(from, -1);
        q.add(source);
        visited[source] = true;
        int minCap = INF;
        int where = -1;
        outer:
        while (!q.isEmpty()) {
            where = q.pollFirst();
            for (int i = 0; i < n; i++) {
                if (!visited[i] && residual[where][i] > 0) {
                    q.add(i);
                    visited[i] = true;
                    from[i] = where;
                    if (i == sink)
                        break outer;
                }
            }
        }
        where = sink;
        int prev = -1;

        while (from[where] > -1) {
            prev = from[where];

            if (prev < 0)
                break;
            minCap = Math.min(minCap, residual[prev][where]);
            where = prev;
        }
        where = sink;
        prev = -1;
        Stack<Integer> path = new Stack<Integer>();
        path.add(where);
        if (minCap < INF) {
            while (from[where] >= 0) {
                prev = from[where];
                path.add(prev);
                residual[prev][where] -= minCap;
                residual[where][prev] += minCap;
                where = prev;
            }
            //----------------------------------------------------------------------
            String midRes = "";
            midRes += ("Augmented value : " + minCap + " on path: ");
            while (!path.isEmpty())
                midRes += (path.pop() + "" + (path.size() > 0 ? "-->" : ""));
            Toast.makeText(getBaseContext(), midRes, Toast.LENGTH_SHORT).show();
            //-----------------------------------------------------------------------

            return minCap;
        } else
            return -1;
    }

    public void maxFlow() {
        int n = capacity[0].length;
        int[][] residual = new int[n][n];

        for (int i = 0; i < n; i++) {
            if (capacity[source][i] > 0)
                limit += capacity[source][i];
        }

        result = new ArrayList<String>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                residual[i][j] = capacity[i][j];
            }
        }

        int maxer = 0;

        while (true) {
            int augment = findPath(residual);

            if (augment <= 0) {
                break;
            }

            maxer += augment;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (capacity[i][j] > 0) {
                    result.add("Flow from " + i + "->" + j + " = " + residual[j][i]);
                }
            }
        }
        title.setText("MaxFLow = " + maxer);
        perc.setText("Flow/Capacity = " + maxer + "/" + limit + " (" + (int) ((double) (100 * maxer) / (double) limit) + "%)");
        Log.d("MAXFLOW WORKING ", "= " + maxer);
        resultAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        Log.d("ADAPTER INIT", "true");
        list.setAdapter(resultAdapter);
        resultAdapter.addAll(result);
        resultAdapter.notifyDataSetChanged();


    }


}
