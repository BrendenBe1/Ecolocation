package ecolocation.ecolocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LocationActivity extends AppCompatActivity {
    //widgets
    Button currentLocBttn;
    Button customLocBttn;
    //TODO: delete these buttons after tech demo
    Button graphsButton;

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

        //TODO: delete below after tech demo
        graphsButton = (Button) findViewById(R.id.bttn_graphs);
        graphsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationActivity.this,
                        GraphResultsActivity.class);
                startActivity(intent);
            }
        });
    }
}
