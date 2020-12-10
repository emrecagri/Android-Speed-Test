package com.emrecagri.speedtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jignesh13.speedometer.SpeedoMeterView;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class MainActivity extends AppCompatActivity {

    SpeedoMeterView speedoMeterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speedoMeterView =findViewById(R.id.speedometerview);
        final Button testet = findViewById(R.id.test_et);


        testet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SpeedTestTask speedTestTask = new SpeedTestTask();
                speedTestTask.execute();

                testet.setText("Tekrar Test Et");

            }
        });


    }

    class SpeedTestTask extends AsyncTask<Void,Void,Void>
    {

        TextView hiz = findViewById(R.id.hiz);

        @Override
        protected Void doInBackground(Void... voids) {

            SpeedTestSocket speedTestSocket = new SpeedTestSocket();

            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onCompletion(SpeedTestReport report) {

                    Log.v("speedtest", "[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                    Log.v("speedtest", "[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());

                }

                @Override
                public void onProgress(float percent, SpeedTestReport report) {


                   final int deger = (int) report.getTransferRateBit().intValue()/1000000;

                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           speedoMeterView.setSpeed(deger,true);//speed set 0 to 140

                           String degeryazi = String.valueOf(deger);
                           hiz.setText("İnternet hızınız "+degeryazi+" MB");

                       }
                   });

                }

                @Override
                public void onError(SpeedTestError speedTestError, String errorMessage) {

                }

            });

            speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/1M.iso");

            return null;
        }
    }

}
