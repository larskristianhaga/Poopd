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
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;

public class AddShitActivity extends AppCompatActivity
{

    // Declaring what information i need
    protected String shitName;
    protected String shitRating;
    protected String shitTime;
    protected String lastLocation;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shit);


    }


    protected void doneShitting( View view )
    {

        savingData();


        // Goes back to maps view
        Intent Maps = new Intent(this, MainActivity.class);
        Maps.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Maps);
    }

    protected void savingData( )
    {
        // Saves the current location to a variable.
        // noinspection deprecation
        Location currentLocation = MainActivity.mMap.getMyLocation();

        // To be removed, just for dev
        Toast.makeText(this, "Location:" + currentLocation, Toast.LENGTH_LONG).show();



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
