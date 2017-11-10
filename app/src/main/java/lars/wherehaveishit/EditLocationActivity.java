package lars.wherehaveishit;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), (accuracy / 10 + 16)));

        Log.i("Location", "Accuracy: " + accuracy);
        Log.i("Location", "Zoom: " + (accuracy / 10 + 17));

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

        Toast.makeText(EditLocationActivity.this,"You can now adjust poop location within the circle",Toast.LENGTH_LONG).show();

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
                Toast.makeText(getBaseContext(), "You cannot set the marker outside the circle", Toast.LENGTH_LONG).show();
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
