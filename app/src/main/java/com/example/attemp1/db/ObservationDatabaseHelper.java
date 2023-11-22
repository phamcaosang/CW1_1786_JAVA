package com.example.attemp1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.attemp1.model.Observation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ObservationDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "observations.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_OBSERVATIONS = "observations";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HIKE_ID = "hike_id";
    private static final String COLUMN_OBSERVATION = "observation";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_ADDITIONAL_COMMENTS = "additional_comments";

    public ObservationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_OBSERVATIONS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_HIKE_ID + " INTEGER, "
                + COLUMN_OBSERVATION + " TEXT, "
                + COLUMN_TIME + " TEXT, "
                + COLUMN_ADDITIONAL_COMMENTS + " TEXT, "
                + "FOREIGN KEY (" + COLUMN_HIKE_ID + ") REFERENCES " + HikeDatabaseHelper.TABLE_HIKES + "(" + HikeDatabaseHelper.COLUMN_ID + "))";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);
        onCreate(db);
    }

    public long addObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_HIKE_ID, observation.getHikeID());
        values.put(COLUMN_OBSERVATION, observation.getObservation());
        values.put(COLUMN_TIME, new SimpleDateFormat("yyyy-MM-dd").format(observation.getTime()));
        values.put(COLUMN_ADDITIONAL_COMMENTS, observation.getAdditionalComments());

        long newRowId = db.insert(TABLE_OBSERVATIONS, null, values);
        db.close();
        return newRowId;
    }

    public Observation getObservation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Observation observation = null;

        Cursor cursor = db.query(TABLE_OBSERVATIONS, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                observation = cursorToObservation(cursor);
            }
            cursor.close();
        }

        db.close();
        return observation;
    }

    public int updateObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_HIKE_ID, observation.getHikeID());
        values.put(COLUMN_OBSERVATION, observation.getObservation());
        values.put(COLUMN_TIME, new SimpleDateFormat("yyyy-MM-dd").format(observation.getTime()));
        values.put(COLUMN_ADDITIONAL_COMMENTS, observation.getAdditionalComments());

        return db.update(TABLE_OBSERVATIONS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(observation.getId())});
    }

    public void deleteObservation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBSERVATIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteObservationsByHike(int hikeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBSERVATIONS, COLUMN_HIKE_ID + " = ?", new String[]{String.valueOf(hikeID)});
        db.close();
    }

    private Observation cursorToObservation(Cursor cursor) {
        Observation observation = new Observation();

        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        if (idIndex >= 0) {
            observation.setId(cursor.getInt(idIndex));
        }

        int hikeIDIndex = cursor.getColumnIndex(COLUMN_HIKE_ID);
        if (hikeIDIndex >= 0) {
            observation.setHikeID(cursor.getInt(hikeIDIndex));
        }

        int observationIndex = cursor.getColumnIndex(COLUMN_OBSERVATION);
        if (observationIndex >= 0) {
            observation.setObservation(cursor.getString(observationIndex));
        }

        int timeIndex = cursor.getColumnIndex(COLUMN_TIME);
        if (timeIndex >= 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date time = dateFormat.parse(cursor.getString(timeIndex));
                observation.setTime(time);
            } catch (Exception e) {
                observation.setTime(new Date());
            }
        }

        int additionalCommentsIndex = cursor.getColumnIndex(COLUMN_ADDITIONAL_COMMENTS);
        if (additionalCommentsIndex >= 0) {
            observation.setAdditionalComments(cursor.getString(additionalCommentsIndex));
        }

        return observation;
    }

    public List<Observation> getObservationsByHike(int hikeID) {
        List<Observation> observations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_HIKE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(hikeID) };

        Cursor cursor = db.query(TABLE_OBSERVATIONS, null, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Observation observation = cursorToObservation(cursor);
                    observations.add(observation);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return observations;
    }


}
