package com.example.attemp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.attemp1.adapter.HikeListAdapter;
import com.example.attemp1.db.HikeDatabaseHelper;
import com.example.attemp1.model.Hike;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements HikeListAdapter.OnItemClickListener{
    private RecyclerView recyclerView;
    private HikeListAdapter hikeListAdapter;
    private List<Hike> hikes;

    private FloatingActionButton floatingActionButton;

    private HikeDatabaseHelper hikeDatabaseHelper;

    private EditText searchEditText;

    private String searchTextValue;

    @Override
    protected void onRestart() {
        super.onRestart();
        if(searchTextValue.isEmpty()){
            loadHikeData();
        }else{
            searchHikeByName(searchTextValue);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.addHikeButton);
        hikeDatabaseHelper = new HikeDatabaseHelper(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        searchEditText = findViewById(R.id.searchEditText);
        searchTextValue = String.valueOf(searchEditText.getText());
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchTextValue = s.toString();
                searchHikeByName(searchTextValue);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        floatingActionButton.setOnClickListener(v -> navigateToCreateHike());
        loadHikeData();
    }

    private void searchHikeByName(String nameSearch){
        hikes = hikeDatabaseHelper.searchHikesByName(nameSearch);
        loadHikeWithData(hikes);
    }

    private void loadHikeData(){
        // Set up the RecyclerView and adapter
        hikes = hikeDatabaseHelper.getAllHikes();
        loadHikeWithData(hikes);
    }

    private void loadHikeWithData(List<Hike> hikes){
        hikeListAdapter = new HikeListAdapter(hikes);
        hikeListAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(hikeListAdapter);
    }
    @Override
    public void onItemClick(int hikeId) {
        Intent intent = new Intent(MainActivity.this, HikeDetailActivity.class);
        intent.putExtra("hikeId", hikeId);
        startActivity(intent);
    }

    private void navigateToCreateHike(){
        Intent intent = new Intent(MainActivity.this, HikeCreationActivity.class);
        startActivity(intent);
    }
}