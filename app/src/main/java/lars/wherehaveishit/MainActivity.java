package lars.wherehaveishit;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import static lars.wherehaveishit.R.id.map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 99;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        mMap.setMapType(googleMap.MAP_TYPE_NORMAL);

        // Enables controls on the Map
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Checks the SDK build version, and decides what to do with it(location wise).
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
            //Not in api-23, no need to prompt
            mMap.setMyLocationEnabled(true);
        }
    }


    public boolean checkLocationPermission( )
    {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //TODO: Show an explanation why the app needs the spesific permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(this, "The app need to know your location, or some functionality will be disabled", Toast.LENGTH_LONG).show();

                //Prompt the user once explanation has been shown
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

                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        // Enables your position in the map
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    // permission denied, shit! Disable the functionality that depends on this permission.

                    // Makes the button invisible
                    View make_btn_addShit_invisible = findViewById(R.id.btn_addShit);
                    make_btn_addShit_invisible.setVisibility(View.INVISIBLE);

                    // Prompts a text explaining that some functionality will be disabled
                    Toast.makeText(this, "Some functionality will now be disabled :(", Toast.LENGTH_LONG).show();


                }
                return;
            }

        }
    }

    // Code that will run if the user minimize the app and opens it again
    @Override
    public void onResume( )
    {

        super.onResume(); // Always call the superclass method first


        Log.i("resume", "The onResume method ran");
    }


    // Creates a new Intent and changes over to it
    protected void addShitActivity( View view )
    {

        Intent addShit = new Intent(this, AddShitActivity.class);
        startActivity(addShit);
    }

    /*
     Prompt the users and ask if the really want to exit the application.
     */
    @Override
    public void onBackPressed( )
    {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit the app?")
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