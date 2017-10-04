package lars.wherehaveishit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity
{
    //int totalNumberOfShits;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        TextView totalNumberOfShits = (TextView) findViewById(R.id.etxt_numberoftotalshits);
        TextView mostShitsInCountry = (TextView) findViewById(R.id.etxt_mostshitsincountry);
        TextView countryWithMostShits = (TextView) findViewById(R.id.etxt_country_with_most_shits);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Intent fromMain = getIntent();
        Bundle bundle = fromMain.getExtras();
        int numberOfShitsFromMain = bundle.getInt("TotalNumberOfShits");

        Log.i("shits", String.valueOf(numberOfShitsFromMain));
        Log.i("shits", String.valueOf(totalNumberOfShits));

    }
}
