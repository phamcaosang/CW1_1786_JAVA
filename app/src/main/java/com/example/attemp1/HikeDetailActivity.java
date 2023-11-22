package com.example.attemp1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.attemp1.adapter.ObservationListAdapter;
import com.example.attemp1.db.HikeDatabaseHelper;
import com.example.attemp1.db.ObservationDatabaseHelper;
import com.example.attemp1.dialog.ObservationDialog;
import com.example.attemp1.model.Hike;
import com.example.attemp1.model.Observation;
import com.example.attemp1.utils.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HikeDetailActivity extends AppCompatActivity implements ObservationListAdapter.OnItemClickListener {
    private int hikeId;
    private ImageView backButton;
    private Button editButton;
    private Button deleteButton;
    private List<Observation> observations;
    private RecyclerView recyclerView;

    private ObservationListAdapter observationListAdapter;

    private HikeDatabaseHelper hikeDatabaseHelper;
    private ObservationDatabaseHelper observationDatabaseHelper;

    private EditText nameEditText;
    private EditText locationEditText;
    private EditText dateEditText;
    private RadioGroup parkingRadioGroup;

    private RadioButton yesRadioButton;
    private RadioButton noRadioButton;
    private EditText lengthEditText;
    private EditText difficultyEditText;
    private EditText descriptionEditText;

    private TextView hikeIdTextView;

    private Button addObservationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        // Retrieve the hikeId from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("hikeId")) {
            hikeId = intent.getIntExtra("hikeId", -1);
        }

        // Set up the views
        backButton = findViewById(R.id.backButton);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        addObservationButton = findViewById(R.id.addObservation);
        observationDatabaseHelper = new ObservationDatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // Set click listeners
        backButton.setOnClickListener(v -> onBackPressed());

        editButton.setOnClickListener(v -> {
            // Handle edit button click
            showUpdateConfirmationDialog();
        });

        deleteButton.setOnClickListener(v -> {
            // Handle delete button click
            showDeleteConfirmationDialog();
        });

        addObservationButton.setOnClickListener(v -> {
            // Handle delete button click
           handleAddObservation();
        });

        //Form Hke
        hikeDatabaseHelper = new HikeDatabaseHelper(this);
        hikeIdTextView = findViewById(R.id.hikeId);
        nameEditText = findViewById(R.id.nameEditText);
        locationEditText = findViewById(R.id.locationEditText);
        dateEditText = findViewById(R.id.dateEditText);
        parkingRadioGroup = findViewById(R.id.parkingRadioGroup);
        yesRadioButton = findViewById(R.id.yesRadioButton);
        noRadioButton = findViewById(R.id.noRadioButton);
        lengthEditText = findViewById(R.id.lengthEditText);
        difficultyEditText = findViewById(R.id.difficultyEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);

        // After variable init
        hikeIdTextView.setText(String.valueOf(hikeId));
        loadDetailHike();
        loadObservations();

    }

    private void loadObservations(){
        //Observation
        observations = observationDatabaseHelper.getObservationsByHike(hikeId);
        Log.println(Log.ASSERT, String.valueOf(Log.INFO),  observations.toString());
        observationListAdapter = new ObservationListAdapter(observations);
        observationListAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(observationListAdapter);
    }


    private void handleAddObservation(){
        ObservationDialog createObservationDialog = new ObservationDialog(this, hikeId, null);
        createObservationDialog.setOnDismissListener(dialog -> loadObservations());
        createObservationDialog.show();
    }

    @Override
    public void onItemClick(int observationId) {
        //HandleUpdateObservation
        Observation observation = observationDatabaseHelper.getObservation(observationId);
        ObservationDialog createObservationDialog = new ObservationDialog(this, hikeId, observation);
        createObservationDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                loadObservations();
            }
        });
        createObservationDialog.show();
    }

    private void loadDetailHike() {
        Hike currentHike = hikeDatabaseHelper.getHike(hikeId);
        if (currentHike != null) {
            // Populate the EditText fields
            nameEditText.setText(currentHike.getName());
            locationEditText.setText(currentHike.getLocation());
            dateEditText.setText(formatDate(currentHike.getDate())); // You can create a formatDate method
            if (currentHike.isParkingAvailability()) {
                yesRadioButton.setChecked(true);
            } else {
                noRadioButton.setChecked(true);
            }
            lengthEditText.setText(String.valueOf(currentHike.getLength()));
            difficultyEditText.setText(currentHike.getDifficulty());
            descriptionEditText.setText(currentHike.getDescription());
        } else {
            // Handle the case when the hike is not found
            ToastUtils.showToast(this, "Hike not found");
        }
    }

    // A method to format a Date object as a string in "yyyy-MM-dd" format
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this item?");

        // Add a positive button (Delete)
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteHikeHandler();
            }
        });

        // Add a negative button (Cancel)
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showUpdateConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Update");
        builder.setMessage("Are you sure you want to update this item?");

        // Add a positive button (Delete)
        builder.setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateHikeValidation();
            }
        });

        // Add a negative button (Cancel)
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateHikeValidation(){
        String name = nameEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String dateStr = dateEditText.getText().toString();
        boolean parkingAvailability = parkingRadioGroup.getCheckedRadioButtonId() == R.id.yesRadioButton;
        String lengthStr = lengthEditText.getText().toString(); // Read length as a string
        String difficulty = difficultyEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        // Validation
        if (name.isEmpty()) {
            ToastUtils.showToast(this, "Please enter a name");
            return;
        }

        if (location.isEmpty()) {
            ToastUtils.showToast(this, "Please enter a location");
            return;
        }

        if (dateStr.isEmpty()) {
            ToastUtils.showToast(this, "Please enter a date");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            ToastUtils.showToast(this, "Please enter a valid date (yyyy-MM-dd)");
            return;
        }

        if (lengthStr.isEmpty()) {
            ToastUtils.showToast(this, "Please enter a length");
            return;
        }

        double length;
        try {
            length = Double.parseDouble(lengthStr);
        } catch (NumberFormatException e) {
            ToastUtils.showToast(this, "Please enter a valid length");
            return;
        }

        // Create a new Hike object with the user input
        Hike currentHike = new Hike();
        currentHike.setId(hikeId);
        currentHike.setName(name);
        currentHike.setLocation(location);
        currentHike.setDate(date);
        currentHike.setParkingAvailability(parkingAvailability);
        currentHike.setLength(length);
        currentHike.setDifficulty(difficulty);
        currentHike.setDescription(description);

        // If all data is valid, create the new hike
        updateHikeHandler(currentHike);
    }

    private void updateHikeHandler(Hike currentHike){
        hikeDatabaseHelper.updateHike(currentHike);
        ToastUtils.showToast(this, "Hike with ID: " + String.valueOf(hikeId) + " updated");
    }

    private void deleteHikeHandler(){
        hikeDatabaseHelper.deleteHike(hikeId);
        observationDatabaseHelper.deleteObservationsByHike(hikeId);
        ToastUtils.showToast(this, "Hike with ID: " + String.valueOf(hikeId) + " deleted");
        onBackPressed();
    }
}