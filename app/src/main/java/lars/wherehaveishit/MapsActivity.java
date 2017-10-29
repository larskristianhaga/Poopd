package lars.wherehaveishit;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener
{

    static GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 99;
    FloatingActionButton btn_addPoop;
    int numberOfTotalShits = 0;
    DBHandler db;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        db = new DBHandler(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btn_addPoop = (FloatingActionButton) findViewById(R.id.btn_addShit);
        btn_addPoop.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick( View view )
            {

                Intent addShit = new Intent(MapsActivity.this, AddShitActivity.class);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onMapReady( GoogleMap googleMap )
    {

        mMap = googleMap;
        // Sets the map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Enables controls on the Map
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
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
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(16)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick( Marker marker )
            {


                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {

            @Override
            public void onInfoWindowClick( Marker marker )
            {
                Log.i("markerName",marker.getTitle());

                Intent seeDetailedPoop = new Intent(MapsActivity.this, DetailedActivity.class);
                seeDetailedPoop.putExtra("markerName", marker.getTitle());
                startActivity(seeDetailedPoop);
            }
        });
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

                    // Makes the button invisible, now only MapsActivity is accessible.
                    btn_addPoop.setVisibility(View.INVISIBLE);

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

        // Reads file and marks on the map if mMap is not null
        if (mMap != null)
        {
            readFileAndMarkOnMap();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if (mMap != null)
            {
                mMap.setMyLocationEnabled(true);
            }
            btn_addPoop.setVisibility(View.VISIBLE);


        }
    }


    public void readFileAndMarkOnMap( )
    {

        numberOfTotalShits = 0;
        final List<Shit> allShitsInDB = db.findAllShits();

        for (Shit shitInMap : allShitsInDB)
        {
            createMarker(shitInMap.getShitName(), shitInMap.getShitDate(), shitInMap.getShitLongitude(), shitInMap.getShitLatitude(), shitInMap.getShitRatingCleanness(), shitInMap.getShitRatingPrivacy(), shitInMap.getShitRatingOverall(), shitInMap.getShitNote());
            numberOfTotalShits++;
        }

    }

    private Marker createMarker( String shitName, String shitDate, String shitLongitude, String shitLatitude, double shitRatingCleanness, double shitRatingPrivacy, double shitRatingOverall, String shitNote )
    {

        double shitLongitudeFin = Double.parseDouble(shitLongitude);
        double shitLatitudeFin = Double.parseDouble(shitLatitude);
        return mMap.addMarker(new MarkerOptions()
                                      .position(new LatLng(shitLatitudeFin, shitLongitudeFin))
                                      .title(shitName)
                                      .draggable(false)
                                      .snippet("Cleanness: " + shitRatingCleanness + " Privacy : " + shitRatingPrivacy + " Overall " + shitRatingOverall + " Note: " + shitNote + " Date: " + shitDate));
    }


    // Prompt the users and ask if the really want to exit the application.
    @Override
    public void onBackPressed( )
    {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
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

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected( MenuItem item )
    {

        switch (item.getItemId())
        {
            case R.id.howtouse:
                Intent seeHowToUse = new Intent(this, HowToUseActivity.class);
                startActivity(seeHowToUse);
                break;
            case R.id.about:
                Intent seeAbout = new Intent(this, AboutActivity.class);
                startActivity(seeAbout);
                break;
            case R.id.statsistics:
                Intent seeStatistics = new Intent(this, StatisticsActivity.class);
                seeStatistics.putExtra("TotalNumberOfShits", numberOfTotalShits);
                startActivity(seeStatistics);
                break;
            case R.id.settings:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_LONG).show();
                break;
            case R.id.applicationsettingspage:
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}