package com.example.smartbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class splashscreen extends AppCompatActivity {
    private int sleep_timer=2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();
        logolauncher logolauncher = new logolauncher();
        logolauncher.start();
    }
    private class logolauncher extends Thread
    {


        public void run() {
            try{
                sleep(1000 * sleep_timer);

            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }

            Intent intent =new Intent(splashscreen.this,MainActivity.class);
            startActivity(intent);
            splashscreen.this.finish();
        }
    }
}


