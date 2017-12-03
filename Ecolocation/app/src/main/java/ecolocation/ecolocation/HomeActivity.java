package ecolocation.ecolocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    //widgets
    Button runButton;
    Button infoButton;
    Button aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //----------- Implementing Widgets -------------
        runButton = (Button) findViewById(R.id.bttn_run);
        infoButton = (Button) findViewById(R.id.bttn_info);
        aboutButton = (Button) findViewById(R.id.bttn_about);

        //-----------Implementing Event Listeners for When the Buttons Are Clicked
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This tells it what activity to go to & what activity we're on
                Intent intent = new Intent(HomeActivity.this, LocationActivity.class);

                startActivity(intent);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }


}
