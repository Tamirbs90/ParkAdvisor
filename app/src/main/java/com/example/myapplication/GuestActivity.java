package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class GuestActivity extends AppCompatActivity {
    private static final int LAST_NAME_FIELD = 0;
    private static final int FIRST_NAME_FIELD = 1;
    private static final int USERNAME_FIELD = 2;
    private static final int PASSWORD_FIELD = 3;
    private static final int CONFIRM_PASSWORD_FIELD = 4;
    private static final int NUMBER_OF_DETAILS = 5;
    private EditText myTexts[] = new EditText[NUMBER_OF_DETAILS];
    private Spinner mySpinner;
    private TextView typeTextView;
    private AppLogic appLogic;
    private String vehicleType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        appLogic= AppLogic.getInstance(getApplicationContext());
        myTexts[USERNAME_FIELD] = (EditText)findViewById(R.id.userNameText);
        myTexts[PASSWORD_FIELD] = (EditText)findViewById(R.id.passwordText);
        myTexts[FIRST_NAME_FIELD] =  (EditText)findViewById(R.id.firstNameText);
        myTexts[LAST_NAME_FIELD] = (EditText)findViewById(R.id.lastNameText);
        typeTextView = (TextView) findViewById(R.id.tvVehicleType);
        mySpinner = (Spinner) findViewById(R.id.vehicleSpinner);
        myTexts[CONFIRM_PASSWORD_FIELD] = (EditText)findViewById(R.id.confirmPasswordText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String value = extras.getString("username");
            myTexts[USERNAME_FIELD].setText(value);
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(GuestActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.vehicle_types));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        mySpinner.setSelection(0);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    vehicleType = parent.getItemAtPosition(position).toString();
                    typeTextView.setText(vehicleType);
                }
                else{
                    typeTextView.setText(getResources().getString(R.string.choose_vehicle));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        getSupportActionBar().setTitle("Register Form");
    }

    private boolean checkIfLegalRow(String inputString, StringBuilder sb, boolean flagName, boolean flagUsername){
        if(flagName){
            if(!Pattern.matches("[a-zA-Z ]+", inputString) || inputString.startsWith(" ") || inputString.endsWith(" ")) {
               if(inputString.startsWith(" ")){
                    sb.append(" field can not start with space");
               }
               else if(inputString.endsWith(" ")){
                   sb.append(" field can not end with space");
               }
               else {
                   sb.append(" field should contain only letters");
               }
                return false;
            }
            else if(inputString.length() < 2){
                sb.append(" too short");
                return false;
            }
        }
        else{
            if(!Pattern.matches("[a-zA-Z0-9]+", inputString)) {
                sb.append(" field should contain only letters or numbers");
                return false;
            }
            else if((!Pattern.matches(".{4,10}", inputString))){
                sb.append(" should contain 4-10 characters");
                return false;
            }
            if(Character.isDigit(inputString.charAt(0)) && flagUsername) {
                sb.append(" field must start with a letter");
                return false;
            }

        }

        return true;
    }

    private boolean checkIfFilled(String inputString, StringBuilder sb){
        if(inputString.isEmpty()){
            sb.append(" field can't be empty");
            return  false;
        }

        return true;
    }

    private boolean appendStringBuilder(int i, StringBuilder myStringBuilder){
        switch (i) {
            case LAST_NAME_FIELD:
                myStringBuilder.append("Last Name");
                break;
            case FIRST_NAME_FIELD:
                myStringBuilder.append("First Name");
                break;
            case USERNAME_FIELD:
                myStringBuilder.append("User Name");
                break;
            case PASSWORD_FIELD:
                myStringBuilder.append("Password");
                break;
            case CONFIRM_PASSWORD_FIELD:
                myStringBuilder.append("Confirm Password");
                break;
        }

        return (i < USERNAME_FIELD);
    }

    private boolean checkIfLegalForm(String stringArr[]) {
        Toast myToast = new Toast(this);
        boolean isLegalForm = true;
        boolean flagName = false;
        boolean flagUsername = false;
        boolean firstToast = true;
        StringBuilder empty = new StringBuilder();

        for (int i = 0; i < stringArr.length; i++) {
            empty.delete(0, empty.length());
            flagName = appendStringBuilder(i, empty);
            if(i == USERNAME_FIELD){
                flagUsername = true;
            }
            else{
                flagUsername = false;
            }

            if (!checkIfFilled(stringArr[i], empty)) {
                myTexts[i].setError(empty.toString());
                if(firstToast == true) {
                    myToast.makeText(getBaseContext(), "All fields must be filled", Toast.LENGTH_LONG).show();
                    firstToast = false;
                }

                isLegalForm = false;
            }
            else if(!checkIfLegalRow(stringArr[i], empty, flagName, flagUsername)) {
                myTexts[i].setError(empty.toString());
                if(firstToast){
                    myToast.makeText(getBaseContext(), empty.toString(),Toast.LENGTH_LONG).show();
                    firstToast = false;
                }
                isLegalForm = false;
            }
        }

        if(typeTextView.getText().equals(getResources().getString(R.string.choose_vehicle))){
            String msgString = "You must choose a vehicle type";
            if(firstToast) {
                myToast.makeText(getBaseContext(), msgString, Toast.LENGTH_LONG).show();
                firstToast = false;
            }
            typeTextView.setError(msgString);
            isLegalForm = false;
        }

        if(!(stringArr[PASSWORD_FIELD].compareTo(stringArr[CONFIRM_PASSWORD_FIELD]) == 0) || !(stringArr[PASSWORD_FIELD].length() == stringArr[CONFIRM_PASSWORD_FIELD].length())) {
            String myString = "password and confirm password fields are not equals";
            myTexts[CONFIRM_PASSWORD_FIELD].setError(myString);
            if (firstToast == true) {
                Toast passwordToast = new Toast(this);
                passwordToast.makeText(getBaseContext(), myString, Toast.LENGTH_LONG).show();
                firstToast = false;
                isLegalForm = false;
            }
        }

        return isLegalForm;
    }

    public void Register(View v) {
        Toast myToast = new Toast(this);
        String stringArr[] = new String[myTexts.length];
        for (int i = 0; i < myTexts.length; i++) {
            stringArr[i] = myTexts[i].getText().toString();
        }

        if (checkIfLegalForm(stringArr)) {
            if (!appLogic.isUserExists(stringArr[USERNAME_FIELD])) {
                appLogic.addUserToDB(stringArr[USERNAME_FIELD], stringArr[FIRST_NAME_FIELD], stringArr[LAST_NAME_FIELD], stringArr[PASSWORD_FIELD], vehicleType);
                myToast.makeText(getBaseContext(), "You successfully signed in", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("username", stringArr[USERNAME_FIELD]);
                intent.putExtra("type", vehicleType);
                startActivity(intent);
            }
            else {
                myTexts[USERNAME_FIELD].setError("This user name is already occupied, please choose a new one");
                myToast.makeText(getBaseContext(), "This username is already occupied, please choose new one", Toast.LENGTH_LONG).show();
            }
        }
    }
}
