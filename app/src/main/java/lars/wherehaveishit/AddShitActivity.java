package lars.wherehaveishit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.List;

public class AddShitActivity extends AppCompatActivity
{

    // Used to determine if the data is saved properly
    private boolean dataSaved = false;
    EditText shitName;
    EditText shitNote;
    RatingBar shitRatingCleanness;
    RatingBar shitRatingPrivary;
    RatingBar shitRatingOverall;
    Button saveShit;
    DBHandler db;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shit);

        shitName = (EditText) findViewById(R.id.edit_shitName);
        shitRatingCleanness = (RatingBar) findViewById(R.id.ratingBarCleanness);
        shitRatingPrivary = (RatingBar) findViewById(R.id.ratingBarPrivacy);
        shitRatingOverall = (RatingBar) findViewById(R.id.ratingBarOverall);
        shitNote = (EditText) findViewById(R.id.edit_shitNote);
        saveShit = (Button) findViewById(R.id.doneShitting);
        db = new DBHandler(this);

        saveShit.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick( View v )
            {

                savingData();

                // Goes back to maps view if the data is saved properly, dataSaved equals true
                if (dataSaved)
                {
                    finish();

                }
                else
                {
                    // Displays a text saying that data was not saved properly, if dataSaved != true.
                    Toast.makeText(AddShitActivity.this, "Data was not saved properly", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    protected void onResume( )
    {

        super.onResume();
        // Automaticly sets the variable to false
        dataSaved = false;

    }


    private void savingData( )
    {
        // Saves the current location to a variable
        Location currentLocation = MapsActivity.mMap.getMyLocation();

        String currentLocationLatFin;
        String currentLocationLonFin;

        try
        {
            // Tries to save the latitude and longitude inside a scope, if it fails it returns.
            currentLocationLatFin = String.valueOf(currentLocation.getLatitude());
            currentLocationLonFin = String.valueOf(currentLocation.getLongitude());
        } catch (NullPointerException e)
        {
            Log.e("Location", "Location is null: " + e.toString());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Cannot detect location", Toast.LENGTH_LONG).show();
            return;
        }

        // If there is not a value in name you cannot add the shit and it will prompt the user saying something went wrong when saving the data
        if (shitName.getText().length() == 0)
        {
            Log.i("returns", "name.getText().lenght() == 0");
            return;
        }


        // Getting todays date, month, year, hour and minute, Saves it to int values
        Calendar savingDate = Calendar.getInstance();
        int shitDate = savingDate.get(Calendar.DATE);
        int shitMonth = savingDate.get(Calendar.MONTH);
        int shitYear = savingDate.get(Calendar.YEAR);
        int shitHour = savingDate.get(Calendar.HOUR_OF_DAY);
        int shitMinute = savingDate.get(Calendar.MINUTE);

        String shitNameFin = shitName.getText().toString();
        String shitDateFin = shitDate + "/" + shitMonth + "-" + shitYear + ", " + shitHour + ":" + shitMinute;
        double shitRatingCleannessFin = shitRatingCleanness.getRating();
        double shitRatingPrivacyFin = shitRatingPrivary.getRating();
        double shitRatingOverallFin = shitRatingOverall.getRating();
        String shitNoteFin = shitNote.getText().toString();

        Shit shit = new Shit(shitNameFin, shitDateFin, currentLocationLonFin, currentLocationLatFin, shitRatingCleannessFin, shitRatingPrivacyFin, shitRatingOverallFin, shitNoteFin);
        db.addShit(shit);

        dataSaved = true;
    }


    // Prompt the users and ask if the don't want to save their shit
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
