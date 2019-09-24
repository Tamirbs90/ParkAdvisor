package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LoginActivity extends AppCompatActivity {
    private EditText userNameField;
    private EditText passwordField;
    private String username;
    private String password;
    private AppLogic appLogic;
    private TextView showLocation;
    private TextView tvVehicleType;
    private Spinner vehicleTypeSpinner;
    private String currentCity="";
    private String currentCountry="";
    private String updateCountry = null;
    private String updateCity = null;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameField = (EditText) findViewById(R.id.userNameText2);
        passwordField = (EditText) findViewById(R.id.passwordText2);
        showLocation = (TextView) findViewById(R.id.showLocation);
        tvVehicleType = (TextView) findViewById(R.id.tvVehicleType2);
        vehicleTypeSpinner = (Spinner) findViewById(R.id.vehicleSpinner2);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            updateCountry = extras.getString("countryName");
            updateCity = extras.getString("cityName");
            userNameField.setText(extras.getString("username"));
            passwordField.setText(extras.getString("password"));
            tvVehicleType.setText(extras.getString("type"));

        }

        if (updateCity != null) {
            currentCity = updateCity;
        }
        if (updateCountry != null) {
            currentCountry = updateCountry;
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(LoginActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.vehicle_types));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleTypeSpinner.setAdapter(myAdapter);
        vehicleTypeSpinner.setSelection(0);
        vehicleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    tvVehicleType.setText(parent.getItemAtPosition(position).toString());
                } else {
                    tvVehicleType.setText(getResources().getString(R.string.choose_vehicle));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        appLogic = AppLogic.getInstance(getApplicationContext());
        if(!appLogic.getUsername().isEmpty()) {
            userNameField.setText(appLogic.getUsername());
        }
        findLocation();
        getSupportActionBar().setTitle("Login Form");
    }

    public void Login(View v) {
        Toast myToast = new Toast(this);
        boolean firstToast = true;
        username = userNameField.getText().toString();
        password = passwordField.getText().toString();
        boolean isUserExists = appLogic.isUserExists(username);
        boolean isUserValid = appLogic.isUserValid(username, password);
        boolean isSignsInCountryExist = appLogic.isSignsInCountryExists(currentCountry);

        if (isUserExists) {
            if (isUserValid && isSignsInCountryExist && !tvVehicleType.getText().toString().equals(getResources().getString(R.string.choose_vehicle))) {
                appLogic.setCurrentCity(currentCity);
                appLogic.setCurrentCountry(currentCountry);
                VehicleType type= VehicleType.fromString(tvVehicleType.getText().toString());
                appLogic.setVehicleType(type);
                appLogic.setUsername(userNameField.getText().toString());
                Intent startIntent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(startIntent);
            }
            else {
                if (!isUserValid) {
                    String incorrectPassword = "The password is incorrect";
                    passwordField.setError(incorrectPassword);
                    if(firstToast){
                        myToast.makeText(getBaseContext(), incorrectPassword, Toast.LENGTH_LONG).show();
                        firstToast = false;
                    }
                }
                if((currentCountry.equals("") || currentCity.equals("")) && firstToast){
                    myToast.makeText(getBaseContext(), "You must choose your location", Toast.LENGTH_LONG).show();
                    firstToast = false;
                }
                if(tvVehicleType.getText().equals(getResources().getString(R.string.choose_vehicle))){
                    String chooseVehicleTypeString = "You must choose a vehicle type";
                    tvVehicleType.setError(chooseVehicleTypeString);
                    if(firstToast){
                        myToast.makeText(getBaseContext(), chooseVehicleTypeString, Toast.LENGTH_LONG).show();
                        firstToast = false;
                    }
                }
                else if (!isSignsInCountryExist && firstToast) {
                    StringBuilder signsExist = new StringBuilder();
                    signsExist.append("Could not find parking signs in ");
                    signsExist.append(currentCountry);
                    myToast.makeText(getBaseContext(), signsExist.toString(), Toast.LENGTH_LONG).show();
                    firstToast = false;
                }
            }
        }

        else {
            String notExistsString = "User Name not exists";
            userNameField.setError(notExistsString);
            myToast.makeText(getBaseContext(), notExistsString, Toast.LENGTH_LONG).show();
            firstToast = false;
        }
    }

    public void Location(View v){
        Intent locationIntent = new Intent(getApplicationContext(), CountryActivity.class);
        locationIntent.putExtra("username", userNameField.getText().toString());
        locationIntent.putExtra("password", passwordField.getText().toString());
        locationIntent.putExtra("type", tvVehicleType.getText().toString());
        startActivity(locationIntent);
    }

    public void Register(View v){
        Intent registetrIntent = new Intent(LoginActivity.this, GuestActivity.class);
        registetrIntent.putExtra("username", userNameField.getText().toString());
        startActivity(registetrIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(LoginActivity.this,
                            ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        findLocation();
                    }
                }
                else{
                }
            }

        }
    }

    public void  findLocation() {

        StringBuilder sb = new StringBuilder();
        String locationQuestion = "Are you at? ";
        sb.append(locationQuestion);
        if (ContextCompat.checkSelfPermission(LoginActivity.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{ACCESS_COARSE_LOCATION}, 1);
        } else {
            try {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                findCurrentLocation(location);
                sb.append(currentCity);
                sb.append(", ");
                sb.append(currentCountry);
            } catch (Exception e) {
                e.printStackTrace();
                if (updateCity != null && updateCountry!= null) {
                    sb.append(updateCity);
                    sb.append(", ");
                    sb.append(updateCountry);
                }
                else{
                    sb.append("(Choose your location)");
                    Toast.makeText(LoginActivity.this, "Location Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            showLocation.setText(sb.toString());
        }
    }

    private void findCurrentLocation(Location location) {
        try{
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if((currentCountry.equals("") && currentCity.equals(""))) {
                currentCity = addresses.get(0).getLocality();
                currentCountry = addresses.get(0).getCountryName();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
