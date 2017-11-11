package lars.wherehaveishit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper
{

    static String TABLE_SHITS = "Shit";
    static String KEY_ID = "_ID";
    static String KEY_SHITNAME = "ShitName";
    static String KEY_DATE = "ShitDate";
    static String KEY_LONGITUDE = "ShitLongitude";
    static String KEY_LATITUDE = "ShitLatitude";
    static String KEY_CLEANNESS = "ShitRatingCleanness";
    static String KEY_PRIVACY = "ShitRatingPrivacy";
    static String KEY_OVERALL = "ShitRatingOverall";
    static String KEY_SHITNOTE = "ShitNote";
    static int DATABASE_VERSION = 3;
    static String DATABASE_NAME = "ShitsInApp";


    public DBHandler( Context context )
    {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate( SQLiteDatabase studentDB )
    {

        String MAKE_TABLE = "CREATE TABLE " + TABLE_SHITS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SHITNAME + " TEXT," + KEY_DATE + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_CLEANNESS + " TEXT," + KEY_PRIVACY + " TEXT," + KEY_OVERALL + " TEXT," + KEY_SHITNOTE + " TEXT" + ")";
        Log.d("SQL", MAKE_TABLE);
        studentDB.execSQL(MAKE_TABLE);
    }

    @Override
    public void onUpgrade( SQLiteDatabase ShitDB, int i, int i1 )
    {

        ShitDB.execSQL("DROP TABLE IF EXISTS " + TABLE_SHITS);
        onCreate(ShitDB);
    }

    public void addShit( Shit shit )
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SHITNAME, shit.getShitName());
        values.put(KEY_DATE, shit.getShitDate());
        values.put(KEY_LONGITUDE, shit.getShitLongitude());
        values.put(KEY_LATITUDE, shit.getShitLatitude());
        values.put(KEY_CLEANNESS, shit.getShitRatingCleanness());
        values.put(KEY_PRIVACY, shit.getShitRatingPrivacy());
        values.put(KEY_OVERALL, shit.getShitRatingOverall());
        values.put(KEY_SHITNOTE, shit.getShitNote());
        db.insert(TABLE_SHITS, null, values);
        db.close();
    }

    public List<Shit> findAllShits( )
    {

        List<Shit> shitsArrayList = new ArrayList<Shit>();
        String selectQuery = "SELECT * FROM " + TABLE_SHITS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                Shit shit = new Shit();
                shit.set_ID(cursor.getLong(0));
                shit.setShitName(cursor.getString(1));
                shit.setShitDate(cursor.getString(2));
                shit.setShitLongitude(cursor.getString(3));
                shit.setShitLatitude(cursor.getString(4));
                shit.setShitRatingCleanness(cursor.getDouble(5));
                shit.setShitRatingPrivacy(cursor.getDouble(6));
                shit.setShitRatingOverall(cursor.getDouble(7));
                shit.setShitNote(cursor.getString(8));
                shitsArrayList.add(shit);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        Log.i("shitsArrayList", String.valueOf(shitsArrayList));
        return shitsArrayList;
    }

    public List<Shit> findAPoop( long poopID )
    {

        List<Shit> shitsArrayList = new ArrayList<Shit>();
        String selectQuery = "SELECT * FROM " + TABLE_SHITS + " WHERE " + KEY_ID + "='" + poopID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                Shit shit = new Shit();
                shit.set_ID(cursor.getLong(0));
                shit.setShitName(cursor.getString(1));
                shit.setShitDate(cursor.getString(2));
                shit.setShitLongitude(cursor.getString(3));
                shit.setShitLatitude(cursor.getString(4));
                shit.setShitRatingCleanness(cursor.getDouble(5));
                shit.setShitRatingPrivacy(cursor.getDouble(6));
                shit.setShitRatingOverall(cursor.getDouble(7));
                shit.setShitNote(cursor.getString(8));
                shitsArrayList.add(shit);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        Log.i("findAnShit", String.valueOf(shitsArrayList));
        return shitsArrayList;
    }

    public void deleteAPoop( long poopID )
    {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SHITS, KEY_ID + "=" + poopID, new String[]{ });
        db.close();
    }

    public int updateAPoop( Shit change )
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SHITNAME, change.getShitName());
        values.put(KEY_CLEANNESS, change.getShitRatingCleanness());
        values.put(KEY_PRIVACY, change.getShitRatingPrivacy());
        values.put(KEY_OVERALL, change.getShitRatingOverall());
        values.put(KEY_SHITNOTE, change.getShitNote());

        int changedPoop = db.update(TABLE_SHITS, values, KEY_ID + "= ?", new String[]{String.valueOf(change.get_ID())});
        db.close();

        return changedPoop;
    }
}
