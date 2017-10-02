package lars.wherehaveishit;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

@SuppressWarnings("UnusedReturnValue")
public class MainActivity extends FragmentActivity implements OnMapReadyCallback
{

    static GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 99;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btnAddShit = (FloatingActionButton) findViewById(R.id.btn_addShit);
        btnAddShit.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick( View view )
            {

                Intent addShit = new Intent(MainActivity.this,AddShitActivity.class);
                startActivity(addShit);
            }
        });

        // Checks the SDK of the phone its running on
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady( GoogleMap googleMap )
    {

        mMap = googleMap;
        // Sets the map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Enables controls on the Map
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Checks the SDK build version, and decides from that what it will continue doing.
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                // Enables your position in the map
                mMap.setMyLocationEnabled(true);
                readFileAndMarkOnMap();
            }
        }
        else
        {
            //Not in api-23 and above, no need to prompt
            mMap.setMyLocationEnabled(true);
            readFileAndMarkOnMap();

        }

    }


    // Requests the location permission
    private boolean checkLocationPermission( )
    {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //Toast.makeText(this, "The application will need your location to allow you to track your shits, else will some of the functionality will be disabled", Toast.LENGTH_LONG).show();
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
    public void onRequestPermissionsResult( int requestCode, @NonNull String permissions[], @NonNull int[] grantResults )
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
            }

        }
    }

    // Code that will run if the user minimize the app and opens it again
    @Override
    public void onResume( )
    {

        super.onResume();

    }


    @Override
    public void onStart( )
    {

        super.onStart();
    }


    public void readFileAndMarkOnMap( )
    {

        ArrayList<String> shitReadFromFileAr = new ArrayList<>();

        try
        {
            InputStream inputStream = getApplicationContext().openFileInput("savedShits");

            if (inputStream != null)
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;

                while ((receiveString = bufferedReader.readLine()) != null)
                {
                    shitReadFromFileAr.add(receiveString);
                }

                // Closes inputStream
                inputStream.close();

                // Checks the size of the recently read file, if its 0 then return. Because you cant set markers when you have no info
                if (shitReadFromFileAr.isEmpty() || shitReadFromFileAr.size() == 0)
                {
                    Log.i("return", "Now returns");
                    return;
                }

            }

        } catch (FileNotFoundException e)
        {
            Log.e("Read file", "File not found: " + e.toString());
            e.printStackTrace();
            return;
        } catch (IOException e)
        {
            Log.e("Read file", "Can not read file: " + e.toString());
            e.printStackTrace();
            return;
        }

        //


        for (String s : shitReadFromFileAr)
        {
            String[] shitSplit = s.split(String.valueOf((char) 182));

            String Latitude = shitSplit[0];
            String Longitude = shitSplit[1];
            String Name = shitSplit[2];
            String Rating = shitSplit[3];
            String Date = shitSplit[4];
            String Time = shitSplit[5];

            createMarker(Latitude, Longitude, Name, Rating, Date, Time);

        }

    }

    private Marker createMarker( String Latitude, String Longitude, String Name, String Rating, String Date, String Time )
    {

        double LatitudeFin = Double.parseDouble(Latitude);
        double LongitudeFin = Double.parseDouble(Longitude);
        return mMap.addMarker(new MarkerOptions()
                                      .position(new LatLng(LatitudeFin, LongitudeFin))
                                      .title(Name)
                                      .draggable(false)
                                      .snippet(Rating + " Stars" + " , " + Date + " , " + Time)

                             );
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