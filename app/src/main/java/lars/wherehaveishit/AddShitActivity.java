package lars.wherehaveishit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.Calendar;

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
    MenuItem adjustMenuIcon;
    String locationLat;
    String locationLon;
    float locationFromMapAccuracy;
    boolean locationIsAdjusted = false;


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

                if (savingData())
                {
                    finish();

                }
                else
                {
                    // Displays a text saying that data was not saved properly, if dataSaved != true.
                    Toast.makeText(AddShitActivity.this, getApplicationContext().getString(R.string.not_saved_properly), Toast.LENGTH_LONG).show();

                    return;
                }
            }
        });


        try
        {
            Intent locationFromOtherActivity = getIntent();
            Bundle bundle = locationFromOtherActivity.getExtras();

            locationLat = bundle.getString("LocationLatitude");
            locationLon = bundle.getString("LocationLongitude");
            locationFromMapAccuracy = bundle.getFloat("LocationAccuracy");

        } catch (NullPointerException e)
        {
            Log.e("Location from bundle", "Location is null/or not reachable from bundle " + e.toString());
        }

    }

    protected void onResume( )
    {

        super.onResume();
        // Automaticly sets the variable to false
        dataSaved = false;

    }


    private boolean savingData( )
    {

        // If there is not a value in name you cannot add the shit and it will prompt the user saying something went wrong when saving the data
        if (shitName.getText().length() == 0)
        {
            Toast.makeText(this, getApplicationContext().getString(R.string.type_in_name), Toast.LENGTH_LONG).show();
            Log.i("returns", "name.getText().length() == 0");
            return dataSaved = false;
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

        Shit shit;

        shit = new Shit(shitNameFin, shitDateFin, locationLon, locationLat, shitRatingCleannessFin, shitRatingPrivacyFin, shitRatingOverallFin, shitNoteFin);
        Log.i("OriginalLocation", String.valueOf(shit));
        db.addShit(shit);

        return dataSaved = true;
    }

    // Prompt the users and ask if the don't want to save their shit
    @Override
    public void onBackPressed( )
    {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(getApplicationContext().getString(R.string.sure_you_want_to_disregard))
                .setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {

                        finish();
                    }

                })
                .setNegativeButton(getApplicationContext().getString(R.string.no), null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_poop_menu, menu);
        adjustMenuIcon = menu.findItem(R.id.editLocation);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {

        if (item.getItemId() == R.id.editLocation)
        {
            // Handles the menu press
            Log.i("Menu", "editLocation pressed");


            Intent adjustLocation = new Intent(AddShitActivity.this, EditLocationActivity.class);
            adjustLocation.putExtra("LocationLatitude", locationLat);
            adjustLocation.putExtra("LocationLongitude", locationLon);
            adjustLocation.putExtra("LocationAccuracy", locationFromMapAccuracy);
            Log.i("Location", "Lat: " + locationLat + " Lon: " + locationLon);
            startActivityForResult(adjustLocation, 555);
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 555)
        {
            if (resultCode == RESULT_OK)
            {
                locationLat = data.getStringExtra("LocationLatitude");
                locationLon = data.getStringExtra("LocationLongitude");

                Toast.makeText(AddShitActivity.this, getApplicationContext().getString(R.string.location_adjusted), Toast.LENGTH_LONG).show();

                locationIsAdjusted = true;
            }
            if (resultCode == RESULT_CANCELED)
            {
                Log.i("resultCode", "RESULT_CANCELED");
                locationIsAdjusted = false;
            }
        }
    }
}

