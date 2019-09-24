package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.Locale;

public class CounterActivity extends AppCompatActivity {

    private static final int HOURS_IN_DAY = 24;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MILLISECONDS_IN_SECONDS = 1000;
    private static final int SECONDS_IN_HOUR = 3600;
    private TextView timerTextShow;
    private TextView timerText;
    private AppLogic appLogic;
    private long timeLeftInMillis;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Parking Countdown");
        timerTextShow= (TextView)findViewById(R.id.timerTextShow);
        timerText=(TextView) findViewById(R.id.timerText);
        appLogic= AppLogic.getInstance(getApplicationContext());
        Intent intent = getIntent();
        long timeLeftInSeconds = intent.getLongExtra("counter",0);
        startTimer(timeLeftInSeconds);
    }

    public void startTimer(long seconds){
        timeLeftInMillis= seconds * MILLISECONDS_IN_SECONDS;
        countDownTimer = new CountDownTimer(timeLeftInMillis,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                StringBuilder timerText = new StringBuilder();
                timeLeftInMillis = millisUntilFinished;
                int days = (int) ((timeLeftInMillis / MILLISECONDS_IN_SECONDS) / SECONDS_IN_HOUR) / HOURS_IN_DAY;
                int hours = (int) (timeLeftInMillis / MILLISECONDS_IN_SECONDS) / SECONDS_IN_HOUR;
                int minutes = (int) ((timeLeftInMillis / MILLISECONDS_IN_SECONDS) % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE;
                int seconds = (int) (timeLeftInMillis / MILLISECONDS_IN_SECONDS) % SECONDS_IN_MINUTE;

                if(days > 0){
                    hours -= HOURS_IN_DAY;
                    timerText.append(days);
                    timerText.append(" Days and ");
                }
                timerText.append(String.format(Locale.getDefault(),
                        "%d:%02d:%02d", hours, minutes, seconds));
                if (hours > 0) {
                    timerText.append(" Hours");

                }
                else if(minutes > 0) {
                    timerText.append(" Minutes");
                }
                else{
                    timerText.append(" Seconds");
                }

                if(days == 0 && hours ==0 && minutes==15 && seconds==0){
                    showNotification("Your parking is over in 15 minutes");
                }
                timerTextShow.setText(timerText.toString());
            }

            @Override
            public void onFinish() {
                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                timerText.setText("Parking is over");
                timerText.setTypeface(boldTypeface);
                timerText.setTextSize(30);
                timerTextShow.setText("00:00:00");
                showNotification("Your parking is over");
            }
        }.start();
    }

    public void pause(View v){

        countDownTimer.cancel();
        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
        startActivity(intent);
    }

    public void showNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Parking Alert")
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("parkadvisor");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "parkadvisor",
                    "Park Advisor",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        notificationManager.notify(0, builder.build());
    }

    @Override
    public void onBackPressed(){
        return;
    }
}
