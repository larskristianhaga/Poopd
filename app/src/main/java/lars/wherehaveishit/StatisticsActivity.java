package lars.wherehaveishit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity
{

    DBHandler db;
    List<Shit> allShitsInDB;
    double avgPoopRating;
    List<String> listAllShitCountries = new LinkedList<>();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        db = new DBHandler(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        TextView totalNumberOfShits = (TextView) findViewById(R.id.etxt_numberoftotalshits);
        TextView avgRatingOfShits = (TextView) findViewById(R.id.etxt_ageratingofshits);
        TextView mostShitCountry = (TextView) findViewById(R.id.etxt_mostshitsincountry);

        allShitsInDB = db.findAllShits();


        long numberOfPlaces = db.getProfilesCount();
        totalNumberOfShits.setText(String.valueOf(numberOfPlaces));

        for (Shit shitInMap : allShitsInDB)
        {
            avgPoopRating = ((shitInMap.getShitRatingOverall() + shitInMap.getShitRatingPrivacy() + shitInMap.getShitRatingCleanness()) / 3) + avgPoopRating;

            if (isNetworkAvailable())
            {
                double lat = Double.parseDouble(shitInMap.getShitLatitude());
                double lon = Double.parseDouble(shitInMap.getShitLongitude());
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> place = null;
                String countryName = null;
                try
                {
                    place = geocoder.getFromLocation(lat, lon, 1);
                    countryName = place.get(0).getCountryName();
                    if (!countryName.equals(null))
                    {
                        listAllShitCountries.add(countryName);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        avgRatingOfShits.setText(String.valueOf(avgPoopRating / numberOfPlaces).substring(0, 3));
        mostShitCountry.setText(getShitiestCountry());


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
                } catch (PackageManager.NameNotFoundException e)
                {
                    Log.e("NameNotFoundException", e.getMessage());
                }
                body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                        Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                        "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"LarsKHaga@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback about Poopd!");
                intent.putExtra(Intent.EXTRA_TEXT, body);
                getApplicationContext().startActivity(Intent.createChooser(intent, getApplicationContext().getString(R.string.choose_email_client)));
            }
        });

    }

    private boolean isNetworkAvailable( )
    {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getShitiestCountry( )
    {

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        String tempStr;
        for (int i = 0; i < listAllShitCountries.size(); i++)
        {
            tempStr = listAllShitCountries.get(i);
            if (map.containsKey(tempStr))
            {
                map.put(tempStr, map.get(tempStr) + 1);
            }
            else
            {
                map.put(tempStr, 1);
            }
        }
        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : map.entrySet())
        {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue())
            {
                maxEntry = entry;
            }
        }
        return maxEntry.getKey();
    }
}

