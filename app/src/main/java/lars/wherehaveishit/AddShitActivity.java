package lars.wherehaveishit;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;

public class AddShitActivity extends AppCompatActivity
{

    // Declaring what information i need
    protected String shitName;
    protected String shitRating;
    protected String shitTime;
    protected String lastLocation;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shit);


    }

    protected void doneShitting( View view )
    {

        savingFile();


        // Goes back to maps view
        Intent Maps = new Intent(this, MainActivity.class);
        Maps.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Maps);
    }

    protected void savingFile( )
    {
        //TODO: Save name
        //TODO: Save rating
        //TODO: Save cordinates


        Log.i("SavingFile", "The file is now saved");
        // feks:
        // Lars Hjemme|5.5|15.35334,43.434434
    }


    public Location getLastLocation( GoogleApiClient client )
    {


        return null;
    }








    //Prompt the users and ask if the don't want to save their shit
    @Override
    public void onBackPressed( )
    {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you dont want to save your shit?")
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
