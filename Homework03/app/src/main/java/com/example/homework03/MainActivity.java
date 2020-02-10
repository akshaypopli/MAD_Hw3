package com.example.homework03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    SeekBar seekbar;
    TextView tv_selectedComplexity;
    Button button;
    TextView tv_min2;
    TextView tv_max2;
    TextView tv_avg2;
    int progress;
    double[] values;
    ProgressBar progressBar;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Homework03");

        seekbar = findViewById(R.id.seekBar);
        tv_selectedComplexity = findViewById(R.id.tv_selectedComplexity);
        button = findViewById(R.id.btn_1);
        tv_min2 = findViewById(R.id.tv_min2);
        tv_max2 = findViewById(R.id.tv_max2);
        tv_avg2 = findViewById(R.id.tv_avg2);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_selectedComplexity.setText(String.valueOf(i) + " Times");
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new GetValues().execute(progress);
                Thread thread = new Thread(new GetValues());
                thread.start();
                progressBar.setVisibility(View.VISIBLE);


            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                double avg = 0;
                ArrayList<Double> finalValues = new ArrayList<Double>();
                finalValues = (ArrayList<Double>) message.obj;
                Log.d("finalValues: ", String.valueOf(finalValues));

                tv_min2.setText(String.valueOf(finalValues.get(0)));
                tv_max2.setText(String.valueOf(finalValues.get(finalValues.size()-1)));
                for(double val: finalValues){
                    avg += val;
                }

                avg = avg/finalValues.size();
                tv_avg2.setText(String.valueOf(avg));
                progressBar.setVisibility(View.INVISIBLE);
                return false;
            }
        });

    }

     class GetValues implements Runnable {

        @Override
        public void run() {
            ArrayList<Double> values= new ArrayList<Double>();
            values = HeavyWork.getArrayNumbers(progress);
            Collections.sort(values);

            //Created a message to send data to main thread
            Message msg = new Message();
            msg.obj = values;
            handler.sendMessage(msg);
        }
    }
}
