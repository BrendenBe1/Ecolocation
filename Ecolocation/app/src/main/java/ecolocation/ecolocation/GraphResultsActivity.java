package ecolocation.ecolocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GraphResultsActivity extends AppCompatActivity {
    //widgets
    Button listViewButton;
    //TODO: delete these buttons after tech demo
    Button googleButton;
    Button dbButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_results);

        //-------- Implementing Widgets
        listViewButton = (Button) findViewById(R.id.bttn_list_view);

        //-------- Setting OnClickListeners
        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to next activity: ListViewActivity
                Intent intent = new Intent(GraphResultsActivity.this, ListViewActivity.class);
                startActivity(intent);
            }
        });

        //TODO: delete after tech demo
        googleButton = (Button) findViewById(R.id.bttn_google);
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphResultsActivity.this,
                        SpatialMapActivity.class);
                startActivity(intent);
            }
        });

        dbButton = (Button) findViewById(R.id.bttn_database);
        dbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphResultsActivity.this,
                        DatabaseDemoActivity.class);
                startActivity(intent);
            }
        });
    }
}
