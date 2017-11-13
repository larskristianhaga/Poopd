package lars.wherehaveishit;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
    int mapType = 1;
    List<Shit> allShitsInDB;

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

                String currentLocationLonFin;
                String currentLocationLatFin;
                float currentLocationAccuracy;

                try
                {
                    Location getCurrentLocation = MapsActivity.mMap.getMyLocation();

                    currentLocationLatFin = String.valueOf(getCurrentLocation.getLatitude());
                    currentLocationLonFin = String.valueOf(getCurrentLocation.getLongitude());
                    currentLocationAccuracy = getCurrentLocation.getAccuracy();

                    Intent addShit = new Intent(MapsActivity.this, AddShitActivity.class);
                    addShit.putExtra("LocationLatitude", currentLocationLatFin);
                    addShit.putExtra("LocationLongitude", currentLocationLonFin);
                    addShit.putExtra("LocationAccuracy", currentLocationAccuracy);
                    startActivity(addShit);

                } catch (NullPointerException e)
                {
                    Log.e("Location", "Location is null/or not reachable: " + e.toString());
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_location), Toast.LENGTH_SHORT).show();
                    return;
                }
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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mapType = preferences.getInt("mapType", 1);

        mMap.setMapType(mapType);

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

        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener()
        {

            @Override
            public void onInfoWindowLongClick( final Marker marker )
            {

                new AlertDialog.Builder(MapsActivity.this)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage(getResources().getString(R.string.sure_you_want_to_delete))
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick( DialogInterface dialog, int which )
                            {

                                db.deleteAPoop((Long) marker.getTag());
                                Toast.makeText(MapsActivity.this, getResources().getString(R.string.poop_deleted), Toast.LENGTH_SHORT).show();
                                recreate();
                            }

                        })
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .show();
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {

            @Override
            public void onInfoWindowClick( Marker marker )
            {

                Log.i("markerTag", String.valueOf(marker.getTag()));

                Intent seeDetailedPoop = new Intent(MapsActivity.this, DetailedActivity.class);
                seeDetailedPoop.putExtra("markerTag", (Long) marker.getTag());
                startActivity(seeDetailedPoop);
            }
        });

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback()
        {

            @Override
            public void onMapLoaded( )
            {

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (Shit marker : allShitsInDB)
                {
                    double testLat = Double.parseDouble(marker.getShitLatitude());
                    double testLon = Double.parseDouble(marker.getShitLongitude());


                    LatLng test = new LatLng(testLat, testLon);
                    Log.i("LatLon", "LatLon: " + test);
                    builder.include(test);
                }

                LatLngBounds bounds = builder.build();
                Log.i("bounds", String.valueOf(bounds));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));

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
                Toast.makeText(this, getResources().getString(R.string.need_location), Toast.LENGTH_LONG).show();

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
                    Toast.makeText(this, getResources().getString(R.string.no_longer_able_to_add_poop), Toast.LENGTH_LONG).show();

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
            mMap.clear();
            readFileAndMarkOnMap();
            Log.i("readFileAndMarkOnMap", "readFileAndMarkOnMap");
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            mapType = preferences.getInt("mapType", 1);
            mMap.setMapType(mapType);
            Log.i("mapType", String.valueOf(mapType));
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

        allShitsInDB = db.findAllShits();

        numberOfTotalShits = 0;

        for (Shit shitInMap : allShitsInDB)
        {
            // Try catching here because if someone gets a Nullpointer as location as manages to save it, it beaks the app and you have to delete the entire database.
            try
            {
                createMarker(shitInMap.getShitName(), shitInMap.getShitLongitude(), shitInMap.getShitLatitude(), shitInMap.getShitRatingCleanness(), shitInMap.getShitRatingPrivacy(), shitInMap.getShitRatingOverall()).setTag(shitInMap.get_ID());
                numberOfTotalShits++;
            } catch (NullPointerException e)
            {
                Log.e("createMaker", e.toString());
            }

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
            {

                @Override
                public View getInfoWindow( Marker marker )
                {

                    return null;
                }

                @Override
                public View getInfoContents( Marker marker )
                {

                    LinearLayout info = new LinearLayout(getApplicationContext());
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(getApplicationContext());
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER_HORIZONTAL);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(getApplicationContext());
                    snippet.setTextColor(Color.GRAY);
                    snippet.setGravity(Gravity.CENTER_HORIZONTAL);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });
        }

    }

    private Marker createMarker( String shitName, String shitLongitude, String shitLatitude, double shitRatingCleanness, double shitRatingPrivacy, double shitRatingOverall )
    {

        double shitLongitudeFin = Double.parseDouble(shitLongitude);
        double shitLatitudeFin = Double.parseDouble(shitLatitude);
        String avgRating = String.valueOf(((shitRatingCleanness + shitRatingPrivacy + shitRatingOverall) / 3)).substring(0, 3);
        return mMap.addMarker(new MarkerOptions()
                                      .position(new LatLng(shitLatitudeFin, shitLongitudeFin))
                                      .title(shitName)
                                      .draggable(false)
                                      .snippet(getResources().getString(R.string.avg_rating) + " " + avgRating + "\n" + getResources().getString(R.string.click_to_see_more)));

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
                    .setMessage(getResources().getString(R.string.sure_you_want_to_exit))
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick( DialogInterface dialog, int which )
                        {

                            finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), null)
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
            case R.id.statsistics:
                Intent seeStatistics = new Intent(this, StatisticsActivity.class);
                seeStatistics.putExtra("TotalNumberOfShits", numberOfTotalShits);
                startActivity(seeStatistics);
                break;
            case R.id.settings:
                Intent seeSettings = new Intent(this, SettingsActivity.class);
                startActivity(seeSettings);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}