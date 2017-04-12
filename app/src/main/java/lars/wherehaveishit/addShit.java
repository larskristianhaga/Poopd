package lars.wherehaveishit;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;

import static android.R.attr.tag;
import static android.R.id.message;
import static lars.wherehaveishit.R.id.ratingBar;
import static lars.wherehaveishit.R.id.txt_ShitName;

public class addShit extends AppCompatActivity {

    // Declaring what information i need
    protected String shitName;
    protected String shitRating;
    protected String lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shit);
    }

    protected void doneShitting(View view)
    {

        savingFile();



        // Goes back to maps view
        Intent Maps = new Intent(this,MapsActivity.class);
        Maps.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Maps);
    }

    protected void savingFile()
    {
        //TODO: Save name
        //TODO: Save rating
        //TODO: Save cordinates

        // feks:
        // Lars Hjemme|5.5|15.35334,43.434434
    }


/*
    protected Location getLastLocation (GoogleApiClient client)
    {


        return null;
    }
*/




}
