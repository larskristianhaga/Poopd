package lars.wherehaveishit;

import java.util.Calendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class AddShitActivity extends AppCompatActivity {

    EditText shitName;

    EditText shitNote;

    RatingBar shitRatingCleanness;

    RatingBar shitRatingPrivary;

    RatingBar shitRatingOverall;

    DBHandler db;

    MenuItem adjustMenuIcon;

    static String locationLat;

    static String locationLon;

    static String savingFromMapIfBackLat;

    static String savingFromMapIfBackLon;

    static float locationFromMapAccuracy;

    boolean locationIsAdjusted = false;

    String shitCustom = "No";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shit);

        shitName = findViewById(R.id.edit_shitName);
        shitRatingCleanness = findViewById(R.id.ratingBarCleanness);
        shitRatingPrivary = findViewById(R.id.ratingBarPrivacy);
        shitRatingOverall = findViewById(R.id.ratingBarOverall);
        shitNote = findViewById(R.id.edit_shitNote);
        db = new DBHandler(this);

        try {
            Intent locationFromOtherActivity = getIntent();
            Bundle bundle = locationFromOtherActivity.getExtras();

            if (bundle != null) {
                locationLat = bundle.getString("LocationLatitude");
                locationLon = bundle.getString("LocationLongitude");
                locationFromMapAccuracy = bundle.getFloat("LocationAccuracy");
            }

            savingFromMapIfBackLat = locationLat;
            savingFromMapIfBackLon = locationLon;

        } catch (NullPointerException e) {
            Log.e("Location from bundle", "Location is null/or not reachable from bundle " + e.toString());
        }

        shitRatingCleanness.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                shitRatingOverall.setRating((shitRatingPrivary.getRating() + rating) / 2);

            }
        });

        shitRatingPrivary.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                shitRatingOverall.setRating((rating + shitRatingCleanness.getRating()) / 2);

            }
        });

    }


    private boolean savingData() {

        // If there is not a value in name you cannot add the shit and it will prompt the user saying something went wrong when saving the data
        if (shitName.getText().length() == 0) {
            Toast.makeText(this, getApplicationContext().getString(R.string.type_in_name), Toast.LENGTH_LONG).show();
            Log.i("returns", "name.getText().length() == 0");
            return false;
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

        if (locationLon == null || locationLat == null) {
            Log.i("LocationEmpty", "locationLon og locationLat is empty");
            shit = new Shit(shitNameFin, shitDateFin, savingFromMapIfBackLon, savingFromMapIfBackLat, shitRatingCleannessFin, shitRatingPrivacyFin,
                    shitRatingOverallFin, shitNoteFin, shitCustom);
        } else {
            shit = new Shit(shitNameFin, shitDateFin, locationLon, locationLat, shitRatingCleannessFin, shitRatingPrivacyFin, shitRatingOverallFin,
                    shitNoteFin, shitCustom);
        }

        db.addShit(shit);
        Log.i("AddingShit", String.valueOf(shit));

        return true;
    }

    // Prompt the users and ask if the don't want to save their shit
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(getApplicationContext().getString(R.string.sure_you_want_to_disregard))
                .setPositiveButton(getApplicationContext().getString(R.string.yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }

                })
                .setNegativeButton(getApplicationContext().getString(R.string.no), null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_poop_menu, menu);
        adjustMenuIcon = menu.findItem(R.id.editLocation);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.editLocation:
                // Handles the menu press
                Log.i("Menu", "editLocation pressed");

                Intent adjustLocation = new Intent(AddShitActivity.this, EditLocationActivity.class);
                adjustLocation.putExtra("LocationLatitude", locationLat);
                adjustLocation.putExtra("LocationLongitude", locationLon);
                adjustLocation.putExtra("LocationAccuracy", locationFromMapAccuracy);
                Log.i("Location", "Lat: " + locationLat + " Lon: " + locationLon);
                startActivityForResult(adjustLocation, 555);
                break;
            case R.id.done:
                if (savingData()) {
                    finish();

                } else {
                    // Displays a text saying that data was not saved properly, if dataSaved != true.
                    Toast.makeText(AddShitActivity.this, getApplicationContext().getString(R.string.not_saved_properly), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 555) {
            if (resultCode == RESULT_OK) {
                locationLat = data.getStringExtra("LocationLatitude");
                locationLon = data.getStringExtra("LocationLongitude");

                Toast.makeText(AddShitActivity.this, getApplicationContext().getString(R.string.location_adjusted), Toast.LENGTH_LONG).show();

                locationIsAdjusted = true;
            }
            if (resultCode == RESULT_CANCELED) {
                Log.i("resultCode", "RESULT_CANCELED");
                locationIsAdjusted = false;
            }
        }
    }
}

