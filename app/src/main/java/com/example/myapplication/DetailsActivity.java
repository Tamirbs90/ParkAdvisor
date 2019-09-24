package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {
    private TextView result;
    private TextView until;
    private TextView payment;
    private TextView remark;
    private TextView tvParkingResult;
    private TextView tvEndOfParking;
    private TextView tvRemarks;
    private TextView tvPayment;
    private Button startParkingBtn;
    private AppLogic appLogic;
    private ImageDetailsFinder imageDetailsFinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Parking Sign Result");

        result = (TextView) findViewById(R.id.resultText);
        until = (TextView) findViewById(R.id.untilText);
        payment = (TextView) findViewById(R.id.paymentText);
        remark = (TextView) findViewById(R.id.remarksText);
        tvParkingResult = (TextView) findViewById(R.id.resultView);
        tvEndOfParking = (TextView) findViewById(R.id.untilView);
        tvPayment = (TextView) findViewById(R.id.paymentView);
        tvRemarks = (TextView) findViewById(R.id.remarksView);
        startParkingBtn = (Button) findViewById(R.id.startParkingBtn);
        imageDetailsFinder = new ImageDetailsFinder();
        appLogic = AppLogic.getInstance(getApplicationContext());
        setDetails();
        if(!imageDetailsFinder.getMatchFound()){
            startParkingBtn.setText(getResources().getString(R.string.camera_return));
            tvParkingResult.setText("Parking sign match not found");
            until.setText("");
            remark.setText("");
            payment.setText("");
            result.setTextColor(getResources().getColor(R.color.colorBlack));
            tvParkingResult.setTextColor(getResources().getColor(R.color.colorBlack));
            Toast myToast = new Toast(this);
            myToast.makeText(getBaseContext(), "Try to stabilize the camera and crop properly", Toast.LENGTH_LONG).show();
        }
        startParkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imageDetailsFinder.getMatchFound() || startParkingBtn.getText().equals(getResources().getString(R.string.camera_return))) {
                    Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity.class);
                    startActivity(cameraIntent);
                } else {
                    Intent counterIntent = new Intent(getApplicationContext(), CounterActivity.class);
                    counterIntent.putExtra("counter", imageDetailsFinder.getTimer());
                    startActivity(counterIntent);
                }
            }
        });
    }

    public void setDetails() {
        int imageId = appLogic.getMatchImageId();
        VehicleType vehicleType = appLogic.getVehicleType();
        imageDetailsFinder.ShowDetails(imageId, vehicleType);
        setUI();
        appLogic.setMatchImageId(0);
    }

    private void setUI() {
        tvParkingResult.setText(imageDetailsFinder.getResultString());
        tvEndOfParking.setText(imageDetailsFinder.getEndOfTimeString());
        if (tvEndOfParking.getText().toString().contains(imageDetailsFinder.getUnlimited())) {
            startParkingBtn.setText(getResources().getString(R.string.camera_return));
        }
        if(tvParkingResult.getText().toString().contains(imageDetailsFinder.getCanNotPark())){
            result.setTextColor(getResources().getColor(R.color.light_red));
            remark.setTextColor(getResources().getColor(R.color.light_red));
            payment.setTextColor(getResources().getColor(R.color.light_red));
            until.setTextColor(getResources().getColor(R.color.light_red));
            tvEndOfParking.setTextColor(getResources().getColor(R.color.light_red));
            tvRemarks.setTextColor(getResources().getColor(R.color.light_red));
            tvPayment.setTextColor(getResources().getColor(R.color.light_red));
            tvParkingResult.setTextColor(getResources().getColor(R.color.light_red));
            startParkingBtn.setText(getResources().getString(R.string.camera_return));
        }

        tvPayment.setText(imageDetailsFinder.getPaymentString());
        tvRemarks.setText(imageDetailsFinder.getRemarksString());
    }
}


