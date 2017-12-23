package lars.wherehaveishit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatPreferenceActivity
{

    static int mapTypeInt;
    static boolean backupEnabled;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mapTypeInt = preferences.getInt("mapType", 1);

        Log.i("MapType", String.valueOf(mapTypeInt));
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();

    }

    public static class MainPreferenceFragment extends PreferenceFragment
    {

        @Override
        public void onCreate( final Bundle savedInstanceState )
        {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);


            Preference versionName = findPreference("versionName");
            versionName.setSummary(BuildConfig.VERSION_NAME);

            final Preference mapType = findPreference("maptype");
            mapType.setSummary(getMapTypeString(mapTypeInt));
            mapType.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
            {

                @Override
                public boolean onPreferenceChange( Preference preference, Object newValue )
                {

                    mapType.setSummary((CharSequence) newValue);
                    Log.i("testing", String.valueOf(preference));
                    mapTypeInt = setMapTypeInt(newValue);
                    MapsActivity.mMap.setMapType(mapTypeInt);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("mapType", mapTypeInt);
                    editor.apply();
                    return true;
                }
            });

            Preference appInfo = findPreference("appInfo");
            appInfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {

                @Override
                public boolean onPreferenceClick( Preference preference )
                {

                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + "lars.wherehaveishit"));
                    startActivity(intent);
                    return true;
                }
            });

            /*
            final SwitchPreference backupToGoogleCloud = (SwitchPreference) findPreference("backup_to_cloud");
            backupToGoogleCloud.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
            {

                @Override
                public boolean onPreferenceChange( Preference preference, Object newValue )
                {

                    backupEnabled = (boolean) newValue;
                    return true;
                }
            });
            */

            Preference myPref = findPreference(getString(R.string.send_feedback));
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {

                public boolean onPreferenceClick( Preference preference )
                {

                    sendFeedback(getActivity());
                    return true;
                }
            });
        }
    }

    public static int setMapTypeInt( Object i )
    {

        int mapTypeString = 0;

        switch (i.toString())
        {
            case "Normal":
                mapTypeString = 1;
                break;
            case "Satellite":
                mapTypeString = 2;
                break;
            case "Terrain":
                mapTypeString = 3;
                break;
            case "Hybrid33":
                mapTypeString = 4;
                break;
        }
        return mapTypeString;
    }

    public static String getMapTypeString( int i )
    {

        String mapTypeString = null;

        switch (i)
        {
            case 1:
                mapTypeString = "Normal";
                break;
            case 2:
                mapTypeString = "Satellite";
                break;
            case 3:
                mapTypeString = "Terrain";
                break;
            case 4:
                mapTypeString = "Hybrid33";
                break;
        }
        return mapTypeString;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {

        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected static void sendFeedback( Context context )
    {

        String appVersion = null;

        try
        {
            appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            Log.e("NameNotFoundException", e.getMessage());
        }

        String body = "App version: " + appVersion + "," + "OS version: " + Build.VERSION.RELEASE + "," + "Device brand: " + Build.BRAND + "," + "Device model: " + Build.MODEL + "," + "Device manufacturer: " + Build.MANUFACTURER;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.dev_mail)});
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.feedback_about_application));
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }

}