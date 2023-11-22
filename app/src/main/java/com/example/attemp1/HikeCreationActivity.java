package com.example.attemp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.attemp1.db.HikeDatabaseHelper;
import com.example.attemp1.model.Hike;
import com.example.attemp1.utils.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HikeCreationActivity extends AppCompatActivity {

    private ImageView backButton;
    Button saveButton;
    private HikeDatabaseHelper hikeDatabaseHelper;

    private EditText nameEditText;
    private EditText locationEditText;
    private EditText dateEditText;
    private RadioGroup parkingRadioGroup;
    private EditText lengthEditText;
    private EditText difficultyEditText;
    private EditText descriptionEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_creation);

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> onBackPressed());

        //form
        hikeDatabaseHelper = new HikeDatabaseHelper(this);
        nameEditText = findViewById(R.id.nameEditText);
        locationEditText = findViewById(R.id.locationEditText);
        dateEditText = findViewById(R.id.dateEditText);
        parkingRadioGroup = findViewById(R.id.parkingRadioGroup);
        lengthEditText = findViewById(R.id.lengthEditText);
        difficultyEditText = findViewById(R.id.difficultyEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);

        saveButton = findViewById(R.id.submitButton);
        saveButton.setOnClickListener(v -> addHikeHanleClick());


    }
    private void addHikeHanleClick() {
        // Get user input from the form
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
        Hike newHike = new Hike();
        newHike.setName(name);
        newHike.setLocation(location);
        newHike.setDate(date);
        newHike.setParkingAvailability(parkingAvailability);
        newHike.setLength(length);
        newHike.setDifficulty(difficulty);
        newHike.setDescription(description);

        // If all data is valid, create the new hike
        createNewHke(newHike);
    }

    private void createNewHke(Hike newHike){
        // Insert the new hike into the database
        long hikeId = hikeDatabaseHelper.addHike(newHike);

        if (hikeId != -1) {
            // Insertion was successful
            Toast.makeText(this, "Hike created successfully", Toast.LENGTH_SHORT).show();
            clearForm();
        } else {
            // Insertion failed
            Toast.makeText(this, "Hike creation failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        nameEditText.setText("");
        locationEditText.setText("");
        dateEditText.setText("");
        parkingRadioGroup.clearCheck();
        lengthEditText.setText("");
        difficultyEditText.setText("");
        descriptionEditText.setText("");
    }
}