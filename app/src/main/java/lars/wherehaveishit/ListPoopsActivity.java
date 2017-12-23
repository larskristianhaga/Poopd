package lars.wherehaveishit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListPoopsActivity extends AppCompatActivity
{

    DBHandler db;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        db = new DBHandler(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_poop);
    }

    @Override
    public void onResume( )
    {

        super.onResume();

        final ListView listAllPoops = (ListView) findViewById(R.id.pooplistview);
        listAllPoops.invalidateViews();

        final List<Shit> allShitsInDB = db.findAllShits();
        final ArrayList<String> allShitInList = new ArrayList<>(allShitsInDB.size());

        int i = 0;
        for (Shit shitInfo : allShitsInDB)
        {
            allShitInList.add(i, shitInfo.getShitName());
            ++i;
        }

        if (i == 0)
        {
            Snackbar snack = Snackbar.make(listAllPoops, getApplicationContext().getString(R.string.no_places), Snackbar.LENGTH_INDEFINITE);
            View view = snack.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.TOP;
            params.gravity = Gravity.CENTER_HORIZONTAL;
            view.setLayoutParams(params);
            snack.show();
            return;
        }

        adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, allShitInList);
        listAllPoops.setAdapter(adapter);

        listAllPoops.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick( AdapterView<?> parent, View view, int position, long id )
            {

                Intent seeDetailedPoop = new Intent(ListPoopsActivity.this, DetailedActivity.class);
                // Adds +1 to id because markerTag starts at 1 when adding markers to the map. The list view starts at the first value with 0. So there is a 1 difference.
                seeDetailedPoop.putExtra("markerTag", id + 1);
                startActivity(seeDetailedPoop);
            }
        });

        listAllPoops.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick( AdapterView<?> parent, View view, int position, final long id )
            {

                new AlertDialog.Builder(ListPoopsActivity.this)
                        .setMessage(getResources().getString(R.string.sure_you_want_to_delete))
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick( DialogInterface dialog, int which )
                            {

                                db.deleteAPoop(id + 1);
                                Toast.makeText(ListPoopsActivity.this, getResources().getString(R.string.poop_deleted), Toast.LENGTH_SHORT).show();
                                recreate();
                            }

                        })
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .show();

                return true;
            }
        });
    }


}
