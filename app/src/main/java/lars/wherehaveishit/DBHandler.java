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
    static String KEY_SHITNAME = "Shitname";
    static String KEY_DATE = "ShitDate";
    static String KEY_LONGITUDE = "ShitLongitude";
    static String KEY_LATITUDE = "ShitLatitude";
    static String KEY_CLEANNESS = "ShitRatingCleanness";
    static String KEY_PRIVACY = "ShitRatingPrivacy";
    static int DATABASE_VERSION = 1;
    static String DATABASE_NAME = "ShitsInApp";


    public DBHandler( Context context )
    {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate( SQLiteDatabase studentDB )
    {

        String MAKE_TABLE = "CREATE TABLE " + TABLE_SHITS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SHITNAME + " TEXT," + KEY_DATE + " TEXT," + ")";
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
                shit.setShitDate(cursor.getString(1));
                shit.setShitLongitude(cursor.getString(2));
                shit.setShitLatitude(cursor.getString(3));
                shit.setShitRatingCleanness(cursor.getString(4));
                shit.setShitRatingPrivacy(cursor.getString(5));
                shitsArrayList.add(shit); ;
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return shitsArrayList;
    }


}
