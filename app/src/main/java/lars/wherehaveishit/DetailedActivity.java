package lars.wherehaveishit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class DetailedActivity extends AppCompatActivity
{

    EditText poopNameLayout;
    RatingBar poopCleannessLayout;
    RatingBar poopPrivacyLayout;
    RatingBar poopOverallLayout;
    EditText poopNoteLayout;
    EditText poopDateLayout;
    TextView poopLocationLatLayout;
    TextView poopLocationLonLayout;
    Marker marker;
    MenuItem editMenuIcon;
    MenuItem doneMenuIcon;
    MenuItem deleteMenuIcon;

    DBHandler db;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        db = new DBHandler(this);

        poopNameLayout = (EditText) findViewById(R.id.editPoopName);
        poopCleannessLayout = (RatingBar) findViewById(R.id.editPoopCleanness);
        poopPrivacyLayout = (RatingBar) findViewById(R.id.editPoopPrivacy);
        poopOverallLayout = (RatingBar) findViewById(R.id.editPoopOverall);
        poopNoteLayout = (EditText) findViewById(R.id.editPoopNote);
        poopLocationLatLayout = (TextView) findViewById(R.id.poopLocationLat);
        poopLocationLonLayout = (TextView) findViewById(R.id.poopLocationLon);
        poopDateLayout = (EditText) findViewById(R.id.editPoopDate);

        Intent markerFromMapsActivity = getIntent();
        Bundle bundle = markerFromMapsActivity.getExtras();
        String markerName = bundle.getString("markerName");
        Log.i("markerName", markerName);

        final List<Shit> poops = db.findAPoop(markerName);

        for (Shit poop : poops)
        {
            poopNameLayout.setText(poop.getShitName());
            poopCleannessLayout.setRating((float) poop.getShitRatingCleanness());
            poopPrivacyLayout.setRating((float) poop.getShitRatingPrivacy());
            poopOverallLayout.setRating((float) poop.getShitRatingOverall());
            poopNoteLayout.setText(poop.getShitNote());
            poopLocationLatLayout.setText(poop.getShitLatitude());
            poopLocationLonLayout.setText(poop.getShitLongitude());
            poopDateLayout.setText(poop.getShitDate());
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        editMenuIcon = menu.findItem(R.id.editPoop);
        doneMenuIcon = menu.findItem(R.id.editPoopDone);
        deleteMenuIcon = menu.findItem(R.id.deletePoop);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {

        switch (item.getItemId())
        {
            case R.id.deletePoop:
                //db.slettKontaktString(detailsAboutStudentFirstName, detailsAboutStudentLastName);
                //Toast.makeText(DetailedActivity.this, "You deleted: " + detailedStudentName, Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.editPoop:

                poopNameLayout.setEnabled(true);
                poopNoteLayout.setEnabled(true);

                editMenuIcon.setVisible(false);
                deleteMenuIcon.setVisible(false);
                doneMenuIcon.setVisible(true);

                Toast.makeText(DetailedActivity.this, "You can now edit your poop!", Toast.LENGTH_LONG).show();
                break;
            case R.id.editPoopDone:
                //oppdater();

                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
