package lars.wherehaveishit;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class AddShitActivity extends AppCompatActivity
{

    // Used to determine if the data is saved properly
    private boolean dataSaved = false;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shit);


    }

    protected void onResume( )
    {

        super.onResume();
        // Automaticly sets the variable to false
        dataSaved = false;

    }


    public void doneShitting( View view)
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
            Toast.makeText(this, "The data was not saved properly", Toast.LENGTH_LONG).show();
        }


    }


    /*
    private void customFile( )
    {
        // Needs to be the right format, else you'll get an error. double, double, String, String, String, String
        // Type your custom string here:
        double customLat = 0.0;
        double customLon = 0.0;
        String customName = null;
        String customRating = null;
        String customDateMonthYear = null;
        String customHourMinute = null;

        String customShitString = customLat + (char) 182 + customLon + (char) 182 + customName + (char) 182 + customRating + (char) 182 + customDateMonthYear + (char) 182 + customHourMinute + "\n";

        try
        {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("savedShits", Context.MODE_APPEND));
            outputStreamWriter.write(customShitString);
            outputStreamWriter.close();
            dataSaved = true;
            Toast.makeText(getApplicationContext(), "Custom file saved!", Toast.LENGTH_SHORT).show();
        } catch (IOException e)
        {
            Log.e("Exception", "Custom file write failed: " + e.toString());
            e.printStackTrace();
        }
    }
    */


    private void savingData( )
    {
        // Used to save custom files
        //customFile();

        // Saves the current location to a variable
        // noinspection deprecation
        Location currentLocation = MainActivity.mMap.getMyLocation();

        // Saves the Latitude and Longitude to a string variable
        String currentLocationLatFin = String.valueOf(currentLocation.getLatitude());
        String currentLocationLonFin = String.valueOf(currentLocation.getLongitude());


        // Getting info from etxt_shitName and saves it to a String
        EditText name = (EditText) findViewById(R.id.etxt_ShitName);
        // If there is not a value in name you cannot add the shit and it will prompt the user saying something went wrong when saving the data
        if (name.getText().length() == 0)
        {
            return;
        }
        // Saves it to a string
        String shitName = name.getText().toString();


        // Getting info from ratingBar and saves it to a float
        RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
        String shitRating = String.valueOf(rating.getRating());

        // Getting todays date, month, year, hour and minute, Saves it to int values
        Calendar savingDate = Calendar.getInstance();
        int shitDate = savingDate.get(Calendar.DATE);
        int shitMonth = savingDate.get(Calendar.MONTH);
        int shitYear = savingDate.get(Calendar.YEAR);
        int shitHour = savingDate.get(Calendar.HOUR_OF_DAY);
        int shitMinute = savingDate.get(Calendar.MINUTE);

        // Merging date, month and year
        String shitDateMonthYear = shitDate + "/" + shitMonth + "/" + shitYear;

        // Merging hour and minute
        String shitHourMinute = shitHour + ":" + shitMinute;

        // Makes a string array of current location, name of place, rating, date and time
        //String[] savingShitString = {currentLocationLatFin, currentLocationLonFin, shitName, shitRating, shitDateMonthYear, shitHourMinute, ";"};
        String savingShitString = currentLocationLatFin + (char) 182 + currentLocationLonFin + (char) 182 + shitName + (char) 182 + String.valueOf(shitRating) + (char) 182 + shitDateMonthYear + (char) 182 + shitHourMinute + "\n";

        Log.i("Write data:", savingShitString);

        try
        {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("savedShits", Context.MODE_APPEND));
            outputStreamWriter.write(savingShitString);
            outputStreamWriter.close();
            dataSaved = true;
            Toast.makeText(getApplicationContext(), "File saved!", Toast.LENGTH_SHORT).show();
        } catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
            e.printStackTrace();
        }
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
