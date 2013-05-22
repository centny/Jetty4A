package com.centny.jetty4a;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.centny.jetty4a.server.JettyServer;

public class JettyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jetty);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.jetty, menu);
        return true;
    }

    JettyServer js = null;

    public void onClk(View v) {
        if (js != null) {
            try {
                js.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            js = null;
            Log.d("Server", "stopped");
        } else {
            File wdir = new File(Environment.getExternalStorageDirectory(),
                    "webapp");
            if (!wdir.exists()) {
                wdir.mkdirs();
            }
            js = new JettyServer(wdir, 8080);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Log.d("Server", "starting...");
                        js.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
