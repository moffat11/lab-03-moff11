package com.example.listycitylab3;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AddCityFragment.AddCityDialogListener {

    private ArrayList<City> dataList;
    private ListView cityList;
    private CityArrayAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] cities = {
                "Edmonton", "Vancouver", "Toronto"
        };
        String[] provinces = {"AB", "BC", "ON"};

        dataList = new ArrayList<>();
        for(int i = 0; i< cities.length; i++){
            dataList.add(new City(cities[i], provinces[i]));
        }
        
        cityList = findViewById(R.id.city_list);
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        FloatingActionButton fab = findViewById(R.id.button_add_city);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                new AddCityFragment().show(getSupportFragmentManager(), "Add City");
            }
        });

        cityList.setOnItemClickListener((parent, view, position, id) ->{
            City tag = cityAdapter.getItem(position);
            if (tag == null) return;
            EditCityFragment
                    .newInstance(tag, position)
                    .show(getSupportFragmentManager(), "EditCity");
        });

        getSupportFragmentManager().setFragmentResultListener(
                EditCityFragment.REQ_KEY, this, (requestKey, bundle) -> {
                    City updated = (City) bundle.getSerializable(EditCityFragment.ARG_CITY);
                    int pos = bundle.getInt(EditCityFragment.ARG_POS, -1);
                    if (updated != null && pos >= 0 && pos < dataList.size()){
                        dataList.set(pos, updated);
                        cityAdapter.notifyDataSetChanged();
                    }
                }
        );
    }

    @Override
    public void addCity(City city) {
        cityAdapter.add(city);
        cityAdapter.notifyDataSetChanged();
    }


}