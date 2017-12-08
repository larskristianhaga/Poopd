package lars.wherehaveishit;

import android.content.Intent;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomMarkerMapActivity extends AppCompatActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    boolean customMarkerAdded = false;
    double customMarkerLocationLat;
    double customMarkerLocationLon;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_marker_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Snackbar snack = Snackbar.make(findViewById(R.id.map), getApplicationContext().getString(R.string.place_custom_marker_on_map), Snackbar.LENGTH_INDEFINITE);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.gravity = Gravity.CENTER_HORIZONTAL;
        view.setLayoutParams(params);
        snack.show();
    }

    @Override
    public void onMapReady( final GoogleMap googleMap )
    {

        mMap = googleMap;

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {

            @Override
            public void onMapClick( LatLng latLng )
            {

                googleMap.clear();

                googleMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .title(getApplicationContext().getString(R.string.custom_marker))
                                            .draggable(true));

                customMarkerLocationLat = latLng.latitude;
                customMarkerLocationLon = latLng.longitude;

                customMarkerAdded = true;

            }


        });

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

                customMarkerLocationLat = marker.getPosition().latitude;
                customMarkerLocationLon = marker.getPosition().longitude;
            }
        });

        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_menu, menu);
        MenuItem adjustMenuIcon = menu.findItem(R.id.done);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {

        if (item.getItemId() == R.id.done)
        {

            Bundle bundle = new Bundle();

            bundle.putString("CustomLocationLatitude", String.valueOf(customMarkerLocationLat));
            bundle.putString("CustomLocationLongitude", String.valueOf(customMarkerLocationLon));
            Log.i("CustomMarker", customMarkerLocationLat + " " + customMarkerLocationLon);

            Intent goBackToAddPoop = new Intent();
            goBackToAddPoop.putExtras(bundle);
            setResult(RESULT_OK, goBackToAddPoop);
            finish();

        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
