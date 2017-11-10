package lars.wherehaveishit;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        TextView totalNumberOfShits = (TextView) findViewById(R.id.etxt_numberoftotalshits);

        int total = 0;

        Intent fromMain = getIntent();
        Bundle bundle = fromMain.getExtras();
        total = bundle.getInt("TotalNumberOfShits");

        Log.i("TotalNumberOfShits", String.valueOf(total));
        if (String.valueOf(total) == String.valueOf(0))
        {
            totalNumberOfShits.setText(R.string.no_poop);
        }
        else
        {
            totalNumberOfShits.setText(String.valueOf(total));
        }


        TextView sendFeedbackToDev = (TextView) findViewById(R.id.mailFeedbackToDeveloper);

        sendFeedbackToDev.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick( View v )
            {


                String body = null;
                try
                {
                    body = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
                    body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                            Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                            "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
                } catch (PackageManager.NameNotFoundException e)
                {
                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"LarsKHaga@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback about Poopd!");
                intent.putExtra(Intent.EXTRA_TEXT, body);
                getApplicationContext().startActivity(Intent.createChooser(intent, getApplicationContext().getString(R.string.choose_email_client)));
            }
        });

    }

}
