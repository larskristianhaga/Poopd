package lars.wherehaveishit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatisticsActivity extends AppCompatActivity
{

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

        TextView sendFeedbackToDev = (TextView) findViewById(R.id.mailFeedbackToDeveloper);

        sendFeedbackToDev.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick( View v )
            {

                Intent sendFeedback = new Intent(Intent.ACTION_SEND);
                sendFeedback.setType("message/rfc822");
                sendFeedback.putExtra(Intent.EXTRA_EMAIL, new String[]{"LarsKHaga@gmail.com"});
                sendFeedback.putExtra(Intent.EXTRA_SUBJECT, "");
                sendFeedback.putExtra(Intent.EXTRA_TEXT, "");
                try
                {
                    startActivity(Intent.createChooser(sendFeedback, "Send mail to developer"));
                } catch (android.content.ActivityNotFoundException ex)
                {
                    Toast.makeText(StatisticsActivity.this, "There are no email clients installed.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
