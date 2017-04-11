package lars.wherehaveishit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static lars.wherehaveishit.R.id.addShit;
import static lars.wherehaveishit.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setMapType(googleMap.MAP_TYPE_NORMAL);

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        int MyVersion = Build.VERSION.SDK_INT;

        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            if (!checkIfAlreadyhavePermission())
            {
                requestForSpecificPermission();
            }
            else
            {
                manipulateMap();
            }
        }
    }


    private boolean checkIfAlreadyhavePermission()
    {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (result == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    private void requestForSpecificPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    manipulateMap();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void manipulateMap()
    {
            mMap.setMyLocationEnabled(true);

            Location myLocation = mMap.getMyLocation();

        if (myLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(myLocation.getLatitude(), myLocation
                            .getLongitude()), 18));
        }


    }

    protected void btn_addShit(View view)
    {
        Intent addShit = new Intent(this,addShit.class);
        startActivity(addShit);

    }

    //TODO: Make map zoom in on location when onCreate()
    //TODO: Make a screen that only will apprear on first time app open and ask for loc permission
}