package lars.wherehaveishit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class addShit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shit);
    }





    protected void doneShitting(View view)
    {
        Intent Maps = new Intent(this,MapsActivity.class);
        Maps.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Maps);
    }

}
