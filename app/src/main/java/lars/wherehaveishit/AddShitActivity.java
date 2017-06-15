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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
            return;
        }

        // Displays a text saying that data was not saved properly
        Toast.makeText(this, "The data was not saved properly", Toast.LENGTH_SHORT).show();
    }

    protected void savingData( )
    {
        // Saves the current location to a variable.
        // noinspection deprecation
        Location currentLocation = MainActivity.mMap.getMyLocation();
        // Cuts the string so only the cordinates is saved
        String currentLocationSubstring = currentLocation.toString().substring(15, 34);

        // Getting info from etxt_shitName and saves it to a String
        EditText name = (EditText) findViewById(R.id.etxt_ShitName);
        if (name.getText().length() == 0)
        {
            return;
        }
        Log.i("Saving File", String.valueOf(name.length()));
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
        String savingShitString = currentLocationSubstring + "|" + shitName + "|" + shitRating + "|" + shitDate + "/" + shitMonth + "/" + shitYear + "|" + shitHour + ":" + shitMinute;

        // Save file to internal storage
        FileOutputStream saveFile;
        try
        {
            saveFile = openFileOutput("savedShits", Context.MODE_PRIVATE);
            saveFile.write(savingShitString.getBytes());
            saveFile.close();
            dataSaved = true;

            Toast.makeText(getApplicationContext(), "File saved!", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {e.printStackTrace();} catch (IOException e)
        {
            e.printStackTrace();
        }

        /*
        // Read file
        StringBuffer stringBuffer = new StringBuffer();

        try
        {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput("savedShits")));
            String inputString;
            while ((inputString = inputReader.readLine()) != null)
            {
                stringBuffer.append(inputString + "\n");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        */


        //dataSaved = true;


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
