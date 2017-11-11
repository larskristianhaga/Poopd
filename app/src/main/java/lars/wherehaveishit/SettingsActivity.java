package lars.wherehaveishit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatPreferenceActivity
{

    private static final String TAG = SettingsActivity.class.getSimpleName();
    private final String versionName = BuildConfig.VERSION_NAME;
    private final int versionCode = BuildConfig.VERSION_CODE;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();

        addPreferencesFromResource(R.xml.pref_main);

        Preference versionName = findPreference("versionName");
        try
        {
            versionName.setSummary(appVersionName());
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static class MainPreferenceFragment extends PreferenceFragment
    {

        @Override
        public void onCreate( final Bundle savedInstanceState )
        {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);


            // feedback preference click listener
            Preference myPref = findPreference(getString(R.string.key_send_feedback));
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

    public static void sendFeedback( Context context )
    {

        String body = null;
        try
        {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
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
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }

    public String appVersionName( ) throws PackageManager.NameNotFoundException
    {

        return BuildConfig.VERSION_NAME;
    }


}