package lars.wherehaveishit;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class EditLocationActivity extends AppCompatActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    double latitude;
    double longitude;
    float accuracy;
    Circle circleAroundMarker;
    Marker poopMarker;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent markerFromMapsActivity = getIntent();
        Bundle bundle = markerFromMapsActivity.getExtras();
        latitude = Double.parseDouble(bundle.getString("LocationLatitude"));
        longitude = Double.parseDouble(bundle.getString("LocationLongitude"));
        accuracy = bundle.getFloat("LocationAccuracy");

        Log.i("Latitude: ", String.valueOf(latitude));
        Log.i("Longitude: ", String.valueOf(longitude));
        Log.i("Accuracy: ", String.valueOf(accuracy));


    }

    @Override
    public void onMapReady( GoogleMap googleMap )
    {

        mMap = googleMap;

        LatLng poopLocation = new LatLng(latitude, longitude);
        poopMarker = mMap.addMarker(new MarkerOptions()
                                            .position(poopLocation)
                                            .title("Adjust your poop")
                                            .flat(false)
                                            .draggable(true));
        Log.i("MarkerLoc", String.valueOf(poopMarker.getPosition()));

        circleAroundMarker = mMap.addCircle(new CircleOptions()
                                                    .center(new LatLng(latitude, longitude))
                                                    .radius(accuracy + accuracy / 2)
                                                    .strokeColor(Color.GRAY)
                                                    .clickable(false)
                                                    .strokeWidth(7));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(circleAroundMarker.getCenter(), getZoomLevel(circleAroundMarker)));

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener()
        {

            @Override
            public void onMarkerDragStart( Marker marker )
            {

            }

            @Override
            public void onMarkerDrag( Marker marker )
            {

            }

            @Override
            public void onMarkerDragEnd( Marker marker )
            {

            }
        });

        Snackbar snack = Snackbar.make(findViewById(R.id.map), getApplicationContext().getString(R.string.adjust_poop_on_map), Snackbar.LENGTH_INDEFINITE);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();

    }

    private int getZoomLevel( Circle circleAroundMarker )
    {

        int zoomLevel = 11;

        if (circleAroundMarker != null)
        {
            double radius = circleAroundMarker.getRadius() + circleAroundMarker.getRadius() / 2;
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_location_menu, menu);
        MenuItem adjustMenuIcon = menu.findItem(R.id.adjustLocationDone);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {

        if (item.getItemId() == R.id.adjustLocationDone)
        {
            float[] results = new float[1];
            Location.distanceBetween(poopMarker.getPosition().latitude, poopMarker.getPosition().longitude, circleAroundMarker.getCenter().latitude, circleAroundMarker.getCenter().longitude, results);

            if (results[0] > circleAroundMarker.getRadius())
            {
                Toast.makeText(getBaseContext(), getApplicationContext().getString(R.string.marker_outside_sircle), Toast.LENGTH_LONG).show();
            }
            else
            {
                Bundle bundle = new Bundle();

                String sendBackLocationLat = String.valueOf(poopMarker.getPosition().latitude);
                String sendBackLocationLon = String.valueOf(poopMarker.getPosition().longitude);

                bundle.putString("LocationLatitude", sendBackLocationLat);
                bundle.putString("LocationLongitude", sendBackLocationLon);

                Intent goBackToAddPoop = new Intent();
                goBackToAddPoop.putExtras(bundle);
                setResult(RESULT_OK, goBackToAddPoop);
                Log.i("NewLoc", "Lat: " + poopMarker.getPosition().latitude + " Lon: " + poopMarker.getPosition().longitude);
                finish();
            }
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
