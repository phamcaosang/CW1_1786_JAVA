package com.example.attemp1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.attemp1.model.Hike;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HikeDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hike.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_HIKES = "hikes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_PARKING_AVAILABILITY = "parking_availability";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_DIFFICULTY = "difficulty";
    public static final String COLUMN_DESCRIPTION = "description";

    public HikeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_HIKES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_LOCATION + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_PARKING_AVAILABILITY + " INTEGER, "
                + COLUMN_LENGTH + " REAL, "
                + COLUMN_DIFFICULTY + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES);
        onCreate(db);
    }

    public long addHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, hike.getName());
        values.put(COLUMN_LOCATION, hike.getLocation());
        values.put(COLUMN_DATE, new SimpleDateFormat("yyyy-MM-dd").format(hike.getDate()));
        values.put(COLUMN_PARKING_AVAILABILITY, hike.isParkingAvailability() ? 1 : 0);
        values.put(COLUMN_LENGTH, hike.getLength());
        values.put(COLUMN_DIFFICULTY, hike.getDifficulty());
        values.put(COLUMN_DESCRIPTION, hike.getDescription());

        long newRowId = db.insert(TABLE_HIKES, null, values);
        db.close();
        return newRowId;
    }

    public Hike getHike(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Hike hike = null;

        Cursor cursor = db.query(TABLE_HIKES, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                hike = cursorToHike(cursor);
            }
            cursor.close();
        }

        db.close();
        return hike;
    }

    public List<Hike> getAllHikes() {
        List<Hike> hikeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HIKES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Hike hike = cursorToHike(cursor);
                hikeList.add(hike);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return hikeList;
    }

    public int updateHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, hike.getName());
        values.put(COLUMN_LOCATION, hike.getLocation());
        values.put(COLUMN_DATE, new SimpleDateFormat("yyyy-MM-dd").format(hike.getDate()));
        values.put(COLUMN_PARKING_AVAILABILITY, hike.isParkingAvailability() ? 1 : 0);
        values.put(COLUMN_LENGTH, hike.getLength());
        values.put(COLUMN_DIFFICULTY, hike.getDifficulty());
        values.put(COLUMN_DESCRIPTION, hike.getDescription());

        return db.update(TABLE_HIKES, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(hike.getId())});
    }

    public void deleteHike(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIKES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Hike> searchHikesByName(String searchName) {
        List<Hike> hikeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HIKES + " WHERE " + COLUMN_NAME + " LIKE ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + searchName + "%"});

        if (cursor.moveToFirst()) {
            do {
                Hike hike = cursorToHike(cursor);
                hikeList.add(hike);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return hikeList;
    }

    private Hike cursorToHike(Cursor cursor) {
        Hike hike = new Hike();

        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        if (idIndex >= 0) {
            hike.setId(cursor.getInt(idIndex));
        }

        int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
        if (nameIndex >= 0) {
            hike.setName(cursor.getString(nameIndex));
        }

        int locationIndex = cursor.getColumnIndex(COLUMN_LOCATION);
        if (locationIndex >= 0) {
            hike.setLocation(cursor.getString(locationIndex));
        }

        int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
        if (dateIndex >= 0) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(cursor.getString(dateIndex));
                hike.setDate(date);
            } catch (Exception e) {
                hike.setDate(new Date());
            }
        }

        int parkingAvailabilityIndex = cursor.getColumnIndex(COLUMN_PARKING_AVAILABILITY);
        if (parkingAvailabilityIndex >= 0) {
            hike.setParkingAvailability(cursor.getInt(parkingAvailabilityIndex) == 1);
        }

        int lengthIndex = cursor.getColumnIndex(COLUMN_LENGTH);
        if (lengthIndex >= 0) {
            hike.setLength(cursor.getDouble(lengthIndex));
        }

        int difficultyIndex = cursor.getColumnIndex(COLUMN_DIFFICULTY);
        if (difficultyIndex >= 0) {
            hike.setDifficulty(cursor.getString(difficultyIndex));
        }

        int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
        if (descriptionIndex >= 0) {
            hike.setDescription(cursor.getString(descriptionIndex));
        }

        return hike;
    }

}