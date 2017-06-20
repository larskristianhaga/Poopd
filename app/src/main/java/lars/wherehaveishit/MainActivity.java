package lars.wherehaveishit;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static lars.wherehaveishit.R.id.map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback
{

    protected static GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 99;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Checks the SDK of the phone its running on
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady( GoogleMap googleMap )
    {

        mMap = googleMap;
        // Sets the map type
        mMap.setMapType(googleMap.MAP_TYPE_NORMAL);

        // Enables controls on the Map
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Checks the SDK build version, and decides from that what it will continue doing.
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                // Enables your position in the map
                mMap.setMyLocationEnabled(true);
            }
        }
        else
        {
            //Not in api-23 and above, no need to prompt
            mMap.setMyLocationEnabled(true);
        }
    }

    // Requests the location permission
    public boolean checkLocationPermission( )
    {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "The application will need your location to allow you to track your shits, else will some of the functionality will be disabled", Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {

                // Show an expanation to the user, this thread waiting for the user's response! After the user sees the explanation, try again to request the permission.
                Toast.makeText(this, "The app need to know your location, or some functionality will be disabled", Toast.LENGTH_LONG).show();

                // Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
            return false;
        }
        else
        {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult( int requestCode, String permissions[], int[] grantResults )
    {

        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    // Permission was granted, WHOOOP!
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        // Enables your position in the map
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    // permission denied, shit!
                    // Disabling the functionality that depends on this permission.

                    // Makes the button invisible, now only MainActivity is accessible.
                    View make_btn_addShit_invisible = findViewById(R.id.btn_addShit);
                    make_btn_addShit_invisible.setVisibility(View.INVISIBLE);

                    // Prompts a text explaining that some functionality will be disabled
                    Toast.makeText(this, "You will no longer be able to add new shits :(", Toast.LENGTH_LONG).show();

                }
                return;
            }

        }
    }

    // Code that will run if the user minimize the app and opens it again
    @Override
    public void onResume( )
    {

        super.onResume();


        //
        //Not here the read code is going to end up in the finished project, just need a way to test it whenever i reopen the application
        //TODO: Relocate code

        // Read file for array
        //StringBuffer[] savingShitString = new StringBuffer[4];


        // Read file normal string
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
        // Prints out the last saved string
        Log.i("Last saved cords:", stringBuffer.toString());

    }


    /*
    public void readFileAndMarkOnMap( )
    {

        for (int i = 0; i < ; i++)
    }

    protected Maker createMarker( double latitude, double longitude, String name, String rating, String date, String time )
    {

        return mMap.addMarker(new MarkerOptions()
                                      .position(new LatLng(Latitude, Longitude))
                                      .title(name)
                                      .snippet()

                             );
    }
    */


    // Creates a new Intent and changes over to it
    protected void addShitActivity( View view )
    {

        Intent addShit = new Intent(this, AddShitActivity.class);
        startActivity(addShit);
    }


    // Prompt the users and ask if the really want to exit the application.
    @Override
    public void onBackPressed( )
    {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit the application?")
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