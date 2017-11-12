package lars.wherehaveishit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatPreferenceActivity
{

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

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
            addPreferencesFromResource(R.xml.pref_main);


            Preference versionName = findPreference("versionName");
            versionName.setSummary(BuildConfig.VERSION_NAME);

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

            /*Preference connectToGoogle = findPreference("connectToGoogle");
            connectToGoogle.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {

                @Override
                public boolean onPreferenceClick( Preference preference )
                {

                    return true;
                }
            });*/

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

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {

        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private static void sendFeedback( Context context )
    {

        String body = null;

        try
        {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
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
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }

}