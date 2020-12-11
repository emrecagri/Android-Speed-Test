package com.emrecagri.speedtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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


    //menu kodları baslangici
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        switch (id){

            case R.id.hakkinda:

                final AlertDialog.Builder hakkindapenceresi = new AlertDialog.Builder( MainActivity.this ); //hakkindapenceresi adında AlertDialog tanımladık
                hakkindapenceresi.setTitle("Hakkında"); //AlertDialog penceresi başlığı
                hakkindapenceresi.setMessage( "Bu uygulama \ngithub.com/emrecagri \ntarafından hazırlanmıştır." );         //AlertDialog penceresi açıklaması
                hakkindapenceresi.setCancelable( true ); //true ile bu AlertDialog penceresinden seçeneklere basılmadan çıkış yapılabileceğini belirttik
                hakkindapenceresi.setPositiveButton( "Geri", new DialogInterface.OnClickListener() { //AlertDialog Pozitif Geri seçeneği
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel(); // Geri seçeneği seçildiğinde bu kod çalışıp AlertDialog penceresini kapatacak
                    }
                });
                AlertDialog hakkindapenceresiAlertDialogu = hakkindapenceresi.create();
                hakkindapenceresiAlertDialogu.show(); // AlertDialog penceresini gösteren kod

                break;


            case R.id.cikis:

                final AlertDialog.Builder cikispenceresi = new AlertDialog.Builder( MainActivity.this ); //cikispenceresi adında AlertDialog tanımladık
                cikispenceresi.setMessage( "Uygulamadan çıkmak istediğinize emin misiniz?" );         //AlertDialog penceresi açıklaması
                cikispenceresi.setCancelable( true );  //true ile bu AlertDialog penceresinden seçeneklere basılmadan çıkış yapılabileceğini belirttik
                cikispenceresi.setNegativeButton( "Hayır",new DialogInterface.OnClickListener() { //AlertDialog Negatif Hayır seçeneği
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel(); // Hayır seçeneği seçildiğinde bu kod çalışıp AlertDialog penceresini kapatacak
                    }
                });
                cikispenceresi.setPositiveButton( "Evet", new DialogInterface.OnClickListener() { //AlertDialog Pozitif Evet seçeneği
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();   // Evet seçeneği seçildiğinde bu kod çalışıp uygulamayı kapatacak
                    }
                });
                AlertDialog cikispenceresiAlertDialogu = cikispenceresi.create();
                cikispenceresiAlertDialogu.show(); // AlertDialog penceresini gösteren kod

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //menu kodlari sonu

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
