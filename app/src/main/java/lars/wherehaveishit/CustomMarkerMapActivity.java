package lars.wherehaveishit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class CustomMarkerMapActivity extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_marker_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady( GoogleMap googleMap )
    {

        mMap = googleMap;

        // TODO: Make the user able to hold and drag a marker on the map to set the cordinates


    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_menu, menu);
        MenuItem done = menu.findItem(R.id.done);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {

        if (item.getItemId() == R.id.done)
        {

                finish();

        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
