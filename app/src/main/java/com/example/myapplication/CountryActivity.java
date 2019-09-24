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

public class CountryActivity extends AppCompatActivity {
    private CustomAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String username = "";
        String password = "";
        String vehicleType = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            password = extras.getString("password");
            vehicleType = extras.getString("type");

        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Choose Country");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorLightBlue)));
        listView = (ListView)findViewById(R.id.countryListView);
        ArrayList<PlaceItem> arrayCountry = new ArrayList<>();
        CountryListCreator.CreateCountryList(arrayCountry);
        adapter = new CustomAdapter(CountryActivity.this, arrayCountry, username, password, vehicleType);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.location_menu, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
