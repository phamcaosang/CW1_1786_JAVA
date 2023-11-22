package com.example.attemp1.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.attemp1.R;
import com.example.attemp1.db.ObservationDatabaseHelper;
import com.example.attemp1.model.Observation;
import com.example.attemp1.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ObservationDialog extends Dialog {

    private EditText observationEditText;
    private EditText commentsEditText;
    private EditText dateEditText;

    private ObservationDatabaseHelper observationDatabaseHelper;
    private Button addObservationButton;
    private Button updateObservationButton;
    private Button deleteObservationButton;
    private Button cancelButtonInDialog;
    private int currentHike;

    private Observation observationToUpdate; // Observation to update, null for create mode

    public ObservationDialog(Context context, int currentHike, Observation observationToUpdate) {
        super(context);
        this.observationToUpdate = observationToUpdate;
        this.currentHike = currentHike;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(observationToUpdate != null ? R.layout.form_edit_observation : R.layout.form_create_observation);
        commentsEditText = findViewById(R.id.commentsEditText);
        observationEditText = findViewById(R.id.observationEditText);
        observationDatabaseHelper= new ObservationDatabaseHelper(getContext());
        cancelButtonInDialog= findViewById(R.id.cancelButtonInDialog);
        cancelButtonInDialog.setOnClickListener(v -> {
            dismiss();
        });
        if (observationToUpdate != null){
            dateEditText = findViewById(R.id.dateEditText);
            dateEditText.setText(formatDateToString(observationToUpdate.getTime()));
            dateEditText.setClickable(false);
            dateEditText.setFocusable(false);
            dateEditText.setFreezesText(true);
            observationEditText.setText(observationToUpdate.getObservation());
            commentsEditText.setText(observationToUpdate.getAdditionalComments());
            updateObservationButton = findViewById(R.id.updateObservationButtonInDialog);
            deleteObservationButton = findViewById(R.id.deleteObservationButtonInDialog);

            updateObservationButton.setOnClickListener(v -> {
                handleUpdateObservation();
                dismiss();
            });

            deleteObservationButton.setOnClickListener(v -> {
                handleDeleteObservation();
                dismiss();
            });

        }else {
            addObservationButton = findViewById(R.id.addObservationButtonInDialog);
            addObservationButton.setOnClickListener(v -> {
                handleAddOversion();
                dismiss();
            });

        }
    }

    private String formatDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    private void handleAddOversion(){
        String observation = observationEditText.getText().toString();
        String comments = commentsEditText.getText().toString();

        if (observation.isEmpty()) {
            ToastUtils.showToast(getContext(), "Please enter observation");
            return;
        }
        Observation newObservation = new Observation(observation, comments, currentHike);
        observationDatabaseHelper.addObservation(newObservation);
        ToastUtils.showToast(getContext(), "New observation added");
    }


    private void handleUpdateObservation(){
        String observation = observationEditText.getText().toString();
        String comments = commentsEditText.getText().toString();

        if (observation.isEmpty()) {
            ToastUtils.showToast(getContext(), "Please enter observation to update");
            return;
        }
        observationToUpdate.setObservation(observation);
        observationToUpdate.setAdditionalComments(comments);
        observationToUpdate.setTime(new Date());
        observationDatabaseHelper.updateObservation(observationToUpdate);
        ToastUtils.showToast(getContext(), "Observation updated");
    }
    private void handleDeleteObservation(){
        observationDatabaseHelper.deleteObservation(observationToUpdate.getId());
        ToastUtils.showToast(getContext(), "Observation deleted");
    }
}
