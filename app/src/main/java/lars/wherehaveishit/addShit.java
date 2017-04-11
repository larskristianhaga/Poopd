package lars.wherehaveishit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

import static lars.wherehaveishit.R.id.ratingBar;
import static lars.wherehaveishit.R.id.txt_ShitName;

public class addShit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shit);
    }


    protected void savingFile()
    {
        //TODO: Save name
        //TODO: Save rating
        //TODO: Save cordinates

        // feks:
        // Lars Hjemme|5.5|15.35334,43.434434
    }


    protected void doneShitting(View view)
    {

        savingFile();




        // Goes back to maps view
        Intent Maps = new Intent(this,MapsActivity.class);
        Maps.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Maps);
    }

}
