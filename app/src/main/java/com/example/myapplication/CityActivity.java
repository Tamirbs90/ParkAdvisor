package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class CityActivity extends AppCompatActivity {
    private CityCustomAdapter adapter;
    private ListView listView;
    private ArrayList<String> arrayCity;
    private String countryName = "";
    private String username = "";
    private String password = "";
    private String vehicleType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        StringBuilder title = new StringBuilder();
        listView = (ListView)findViewById(R.id.cityListView);
        ActionBar actionBar = getSupportActionBar();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            countryName = extras.getString("countryName");
            username = extras.getString("username");
            password = extras.getString("password");
            vehicleType = extras.getString("type");
            title.append(countryName);
        }
        title.append(" Cities");
        actionBar.setTitle(title.toString());
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorLightBlue)));
        setCities(countryName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.location_menu, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase(Locale.getDefault());
                adapter.filter(newText);
                return  false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setCities(String value) {
        arrayCity = new ArrayList<>();
        if(value.equals("Israel")){
            arrayCity.addAll(Arrays.asList(getResources().getStringArray(R.array.israeli_cities)));
        }
        else if(value.equals("Germany")) {
            arrayCity.addAll(Arrays.asList(getResources().getStringArray(R.array.germany_cities)));
        }
        else{

        }
        adapter = new CityCustomAdapter(CityActivity.this, arrayCity, countryName, username, password, vehicleType);
        listView.setAdapter(adapter);
    }
}
