package ecolocation.ecolocation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LocationActivity extends AppCompatActivity {
    //widgets
    Button currentLocBttn;
    Button customLocBttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //-------- Initializing Widgets
        currentLocBttn = (Button) findViewById(R.id.bttn_curr_location);
        customLocBttn = (Button) findViewById(R.id.bttn_custom_location);

        //-------- Setting Up Event Listeners
        currentLocBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: select the users current location
            }
        });

        customLocBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: let the user pick a location from Google Maps
            }
        });
    }
}
