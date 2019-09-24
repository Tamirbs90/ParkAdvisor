package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String username;
    private String password;
    private String vehicleType;
    private List<PlaceItem> data;
    private ArrayList<PlaceItem> arrayList;


    public CustomAdapter(Context context, List<com.example.myapplication.PlaceItem> placeItemList, String i_Username, String i_Password, String i_VehicleType) {
        this.context= context;
        this.data=placeItemList;
        inflater = LayoutInflater.from(context);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(data);
        username = i_Username;
        password = i_Password;
        vehicleType = i_VehicleType;
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
        ImageView ivFlag;
        TextView tvCountryName;

    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DataHolder holder;

        if(convertView==null)
        {
            holder = new DataHolder();
            convertView = inflater.inflate(R.layout.item_row, null);
            holder.ivFlag = (ImageView)convertView.findViewById(R.id.ivCountry);
            holder.tvCountryName = (TextView)convertView.findViewById(R.id.tvCountry);

            convertView.setTag(holder);
        }
        else
        {
            holder = (DataHolder)convertView.getTag();
        }

        PlaceItem dataItem = data.get(position);
        holder.tvCountryName.setText(data.get(position).getCountryName());
        holder.ivFlag.setImageResource(data.get(position).getResIdThumbnail());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.tvCountryName.getText().toString().equals("Israel") || holder.tvCountryName.getText().toString().equals("Germany")) {
                    Intent intent = new Intent(context, CityActivity.class);
                    intent.putExtra("countryName", data.get(position).getCountryName());
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("type", vehicleType);
                    context.startActivity(intent);
                }
                else{
                    Toast myToast = new Toast(context);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Park Advisor didn't find cities in ");
                    sb.append(holder.tvCountryName.getText().toString());
                    myToast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
                }
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
            for(com.example.myapplication.PlaceItem placeItem : arrayList){
                if(placeItem.getCountryName().toLowerCase(Locale.getDefault()).contains(charText)){
                    data.add(placeItem);
                }
            }
        }

        notifyDataSetChanged();
    }
}

