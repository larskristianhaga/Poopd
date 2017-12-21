package lars.wherehaveishit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DetailedActivity extends AppCompatActivity
{

    EditText poopNameLayout;
    RatingBar poopCleannessLayout;
    RatingBar poopPrivacyLayout;
    RatingBar poopOverallLayout;
    EditText poopNoteLayout;
    TextView poopDateLayout;
    TextView poopTimeLayout;
    TextView poopLocationLatLayout;
    TextView poopLocationLonLayout;
    String detailedPoopID = null;
    MenuItem editMenuIcon;
    MenuItem deleteMenuIcon;
    MenuItem doneMenuIcon;
    long markerTag;
    DBHandler db;
    boolean editMode = false;

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
        poopLocationLatLayout = (TextView) findViewById(R.id.poopLocationLatValue);
        poopLocationLonLayout = (TextView) findViewById(R.id.poopLocationLonValue);
        poopDateLayout = (TextView) findViewById(R.id.textPoopDate);
        poopTimeLayout = (TextView) findViewById(R.id.textPoopTime);

        Intent markerFromMapsActivity = getIntent();
        Bundle bundle = markerFromMapsActivity.getExtras();
        markerTag = bundle.getLong("markerTag");
        Log.i("markerTAG", String.valueOf(markerTag));

        final List<Shit> poops = db.findAPoop(markerTag);
        Log.i("poopsList", String.valueOf(poops));

        for (Shit poop : poops)
        {
            poopNameLayout.setText(poop.getShitName());
            poopCleannessLayout.setRating((float) poop.getShitRatingCleanness());
            poopPrivacyLayout.setRating((float) poop.getShitRatingPrivacy());
            poopOverallLayout.setRating((float) poop.getShitRatingOverall());
            poopNoteLayout.setText(poop.getShitNote());
            poopLocationLatLayout.setText(poop.getShitLatitude());
            poopLocationLonLayout.setText(poop.getShitLongitude());
            poopDateLayout.setText(poop.getShitDate().split(",")[0].trim());
            poopTimeLayout.setText(poop.getShitDate().split(",")[1].trim());
            detailedPoopID = String.valueOf(poop.get_ID());
        }

        poopNameLayout.setEnabled(false);
        poopCleannessLayout.setEnabled(false);
        poopPrivacyLayout.setEnabled(false);
        poopOverallLayout.setEnabled(false);
        poopNoteLayout.setEnabled(false);

        final View view = findViewById(R.id.detailed_activity_linearlayout);
        view.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick( View v )
            {

                if (!editMode)
                {
                    Animation animation = new AlphaAnimation(1, 0);
                    animation.setDuration(20);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setRepeatCount(2);
                    animation.setRepeatMode(Animation.REVERSE);

                    View editButtonMode = findViewById(R.id.editPoop);
                    editButtonMode.startAnimation(animation);
                }
            }
        });
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
                db.deleteAPoop(markerTag);
                Toast.makeText(DetailedActivity.this, getApplicationContext().getString(R.string.place_deleted), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.editPoop:

                poopNameLayout.setEnabled(true);
                poopCleannessLayout.setEnabled(true);
                poopPrivacyLayout.setEnabled(true);
                poopOverallLayout.setEnabled(true);
                poopNoteLayout.setEnabled(true);

                editMenuIcon.setVisible(false);
                doneMenuIcon.setVisible(true);
                deleteMenuIcon.setVisible(false);

                Toast.makeText(DetailedActivity.this, getApplicationContext().getString(R.string.now_edit_place), Toast.LENGTH_LONG).show();
                editMode = true;
                break;
            case R.id.editPoopDone:
                updatePoop();

                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void updatePoop( )
    {

        Shit updatePoop = new Shit();
        updatePoop.setShitName(poopNameLayout.getText().toString());
        updatePoop.setShitNote(poopNoteLayout.getText().toString());
        updatePoop.setShitRatingCleanness(poopCleannessLayout.getRating());
        updatePoop.setShitRatingPrivacy(poopPrivacyLayout.getRating());
        updatePoop.setShitRatingOverall(poopOverallLayout.getRating());
        updatePoop.set_ID(Long.parseLong(detailedPoopID));
        db.updateAPoop(updatePoop);
    }
}
