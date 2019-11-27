package com.example.vale_yeni;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


public class Kronometre extends AppCompatActivity {

    TextView textView ;

    Button start, pause, reset, lap ;

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;

    Handler handler;

    int Seconds, Minutes, MilliSeconds,saat_kayıt,dakika,saniye_kayıt;

    ListView listView ;

    String[] ListElements = new String[] {  };

    List<String> ListElementsArrayList ;

    ArrayAdapter<String> adapter ;

    long tStart,sonMilis,tDelta;
    double elapsedSeconds;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kronometre_deneme);

        textView = (TextView)findViewById(R.id.textView);
        start = (Button)findViewById(R.id.button);
        pause = (Button)findViewById(R.id.button2);
        reset = (Button)findViewById(R.id.button3);
        lap = (Button)findViewById(R.id.button4) ;
        listView = (ListView)findViewById(R.id.listview1);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);






        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





            }


        });




        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startClock();



            }


        });







    }


    private void startClock(){
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(60000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                              //  Toast.makeText(getApplicationContext(), String.valueOf(date), Toast.LENGTH_LONG).show();

                                long timeElapsed = SystemClock.elapsedRealtime(); // - chrono.getBase();

                               // long timeElapsed = 7564000; //For example
                                int hours = (int) (timeElapsed / 3600000);
                                int minutes = (int) (timeElapsed - hours * 3600000) / 60000;
                                int seconds = (int) (timeElapsed - hours * 3600000 - minutes * 60000) / 1000;







                                Toast.makeText(getApplicationContext(), "SAAT: "+String.valueOf(hours)+"DAKİKA: "+String.valueOf(minutes), Toast.LENGTH_LONG).show();




                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

}