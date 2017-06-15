package lars.wherehaveishit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddShitActivity extends AppCompatActivity
{

    // Declaring what information i need
    protected boolean dataSaved = false;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shit);


    }

    protected void onResume( )
    {

        super.onResume();
        dataSaved = false;

    }


    protected void doneShitting( View view )
    {

        savingData();

        // Goes back to maps view if the data is saved properly
        if (dataSaved == true)
        {
            Intent Maps = new Intent(this, MainActivity.class);
            Maps.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Maps);
        }
        // Displays a text saying that data was not saved properly
        Toast.makeText(this, "The data was not saved properly", Toast.LENGTH_SHORT).show();
    }

    protected void savingData( )
    {
        // Saves the current location to a variable.
        // noinspection deprecation
        Location currentLocation = MainActivity.mMap.getMyLocation();

        // Getting info from etxt_shitName and saves it to a String
        EditText name = (EditText) findViewById(R.id.etxt_ShitName);
        String shitName = name.getText().toString();

        // Getting info from ratingBar and saves it to a float
        RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
        float shitRating = rating.getRating();

        // Getting todays date, month, year, hour and minute, Saves it to int values
        Calendar savingDate = Calendar.getInstance();
        int shitDate = savingDate.get(Calendar.DATE);
        int shitMonth = savingDate.get(Calendar.MONTH);
        int shitYear = savingDate.get(Calendar.YEAR);
        int shitHour = savingDate.get(Calendar.HOUR_OF_DAY);
        int shitMinute = savingDate.get(Calendar.MINUTE);

        // Making a string out of current location, name of place, rating, date and time
        String savingShitString = shitName + "|" + shitRating + "|" + shitDate + "/" + shitMonth + "/" + shitYear + "|" + shitHour + ":" + shitMinute;

        // Calling another method that saves the String "savingShitString" to internal storage
        try
        {
            saveFiletoInternalStorage();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        // To be removed, just for dev
        Toast.makeText(this, "Location:" + currentLocation, Toast.LENGTH_SHORT).show();
        Log.i("Saving", shitName);
        Log.i("Saving", String.valueOf(shitRating));
        Log.i("Saving", shitDate + "/" + shitMonth + "/" + shitYear + " At: " + shitHour + ":" + shitMinute);
        Log.i("Saving", savingShitString);


        //dataSaved = true;


    }


    private void saveFiletoInternalStorage( ) throws IOException
    {
        String str = "testing123";
        FileOutputStream saveFile = openFileOutput("allshits",Context.MODE_PRIVATE);
        saveFile.write(str.getBytes());
        saveFile.close();
        Log.i("Saving", "File is now saved");


    }


    //Prompt the users and ask if the don't want to save their shit
    @Override
    public void onBackPressed( )
    {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you dont want to save your shit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {

                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


}
