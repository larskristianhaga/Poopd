package lars.wherehaveishit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class CustomPoopActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{

    DBHandler db;
    EditText customPoopName;
    RatingBar customPoopRatingCleanness;
    RatingBar customPoopRatingPrivacy;
    RatingBar customPoopRatingOverall;
    EditText customPoopNote;
    TextView customPoopDate;
    TextView customPoopTime;
    TextView customPoopLocationLat;
    TextView customPoopLocationLon;
    int year;
    int month;
    int day;
    int hour;
    int minute;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        db = new DBHandler(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_poop);

        customPoopName = (EditText) findViewById(R.id.editPoopName);
        customPoopRatingCleanness = (RatingBar) findViewById(R.id.editPoopCleanness);
        customPoopRatingPrivacy = (RatingBar) findViewById(R.id.editPoopPrivacy);
        customPoopRatingOverall = (RatingBar) findViewById(R.id.editPoopOverall);
        customPoopNote = (EditText) findViewById(R.id.editPoopNote);
        customPoopDate = (TextView) findViewById(R.id.textPoopDate);
        customPoopTime = (TextView) findViewById(R.id.textPoopTime);
        customPoopLocationLat = (TextView) findViewById(R.id.textPoopLocationLatitude);
        customPoopLocationLon = (TextView) findViewById(R.id.textPoopLocationLongitude);
        Button addDate = (Button) findViewById(R.id.addDate);
        Button addTime = (Button) findViewById(R.id.addTime);
        Button addLocation = (Button) findViewById(R.id.addLocation);

        addDate.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick( View v )
            {

                getTime();
                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomPoopActivity.this, CustomPoopActivity.this, year, month, day);

                datePickerDialog.show();
            }
        });

        addTime.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick( View v )
            {

                getTime();
                TimePickerDialog timePickerDialog = new TimePickerDialog(CustomPoopActivity.this, CustomPoopActivity.this, hour, minute, true);

                timePickerDialog.show();
            }
        });

        addLocation.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick( View v )
            {

                Intent setCustomLocation = new Intent(CustomPoopActivity.this, CustomMarkerMapActivity.class);
                startActivityForResult(setCustomLocation, 555);
            }
        });

    }

    private void getTime( )
    {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
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

            if (customPoopName.getText().length() == 0)
            {
                Toast.makeText(this, getApplicationContext().getString(R.string.type_in_name), Toast.LENGTH_LONG).show();
            }
            else if (customPoopDate.getText().toString().equals(getApplicationContext().getString(R.string.none)))
            {
                Toast.makeText(this, getApplicationContext().getString(R.string.type_in_date), Toast.LENGTH_LONG).show();
            }
            else if (customPoopTime.getText().toString().equals(getApplicationContext().getString(R.string.none)))
            {
                Toast.makeText(this, getApplicationContext().getString(R.string.type_in_time), Toast.LENGTH_LONG).show();
            }
            else if (customPoopLocationLat.getText().equals(getApplicationContext().getString(R.string.none)) || customPoopLocationLon.getText().equals(getApplicationContext().getString(R.string.none)))
            {
                Toast.makeText(this, getApplicationContext().getString(R.string.type_in_loc), Toast.LENGTH_LONG).show();
            }
            else
            {
                Shit shit;
                String customPoopDateAndTime = customPoopDate.getText().toString() + " , " + customPoopTime.getText().toString();

                shit = new Shit(customPoopName.getText().toString(), customPoopDateAndTime, customPoopLocationLon.getText().toString(), customPoopLocationLat.getText().toString(), customPoopRatingCleanness.getRating(), customPoopRatingPrivacy.getRating(), customPoopRatingOverall.getRating(), customPoopNote.getText().toString());

                db.addShit(shit);
                finish();
            }
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onDateSet( DatePicker view, int year, int month, int dayOfMonth )
    {

        customPoopDate.setText(dayOfMonth + "/" + (month + 1) + "-" + year);

    }

    @Override
    public void onTimeSet( TimePicker view, int hourOfDay, int minute )
    {

        customPoopTime.setText(hourOfDay + ":" + minute);
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 555)
        {
            if (resultCode == RESULT_OK)
            {
                Log.i("resultCode", "RESULT_OK");

                customPoopLocationLat.setText(data.getStringExtra("CustomLocationLatitude"));
                customPoopLocationLon.setText(data.getStringExtra("CustomLocationLongitude"));

            }
            if (resultCode == RESULT_CANCELED)
            {
                Log.i("resultCode", "RESULT_CANCELED");
            }
        }
    }
}
