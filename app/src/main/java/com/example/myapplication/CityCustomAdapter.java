package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CityCustomAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String countryName;
    private String username;
    private String password;
    private String vehicleType;
    private List<String> data;
    private ArrayList<String> arrayList;


    public CityCustomAdapter(Context context, List<String> itemList, String i_CountryName, String i_Username, String i_Password, String i_VehicleType) {
        this.context= context;
        this.data=itemList;
        countryName = i_CountryName;
        username = i_Username;
        password = i_Password;
        vehicleType = i_VehicleType;
        inflater = LayoutInflater.from(context);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(data);
    }

    @Override
    public int getCount(){
        return data.size();
    }

    @Override
    public Object getItem(int i){

        return data.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    static class DataHolder
    {
        TextView tvCityName;

    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DataHolder holder;

        if(convertView==null)
        {
            holder = new DataHolder();
            convertView = inflater.inflate(R.layout.item_row_city, null);
            holder.tvCityName = (TextView)convertView.findViewById(R.id.tvCity);

            convertView.setTag(holder);
        }
        else
        {
            holder = (DataHolder)convertView.getTag();
        }

        final String cityName = data.get(position);
        holder.tvCityName.setText(cityName);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("countryName", countryName);
                    intent.putExtra("cityName", cityName);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("type", vehicleType);
                    context.startActivity(intent);
            }
        });
        return  convertView;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if(charText.length() == 0){
            data.addAll(arrayList);
        }
        else{
            for(String city : arrayList){
                if(city.toLowerCase(Locale.getDefault()).contains(charText)){
                    data.add(city);
                }
            }
        }

        notifyDataSetChanged();
    }
}
