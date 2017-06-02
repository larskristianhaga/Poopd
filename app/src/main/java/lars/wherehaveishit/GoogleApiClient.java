package lars.wherehaveishit;

import android.location.Location;
import android.os.Bundle;

import java.util.Objects;

public abstract class GoogleApiClient extends Object
{

    protected void onCreate( Bundle savedInstanceState )
    {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                                  this /* OnConnectionFailedListener */)
                .addApi(LocationServices.API)
                .addScope(Location.SCOPE_FILE)
                .build();

    }
}
