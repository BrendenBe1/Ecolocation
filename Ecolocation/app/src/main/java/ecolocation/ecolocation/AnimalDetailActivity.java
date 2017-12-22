package ecolocation.ecolocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AnimalDetailActivity extends AppCompatActivity {
    //widgets
    ImageView animalPic;
    TextView nameText;
    TextView descText;
    TextView wikiLink;
    TextView massText;
    TextView populationText;
    TextView dietText;
    TextView endangeredLevel;
    Button back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);

        //----------- toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);

        //implementing widgets
        animalPic = (ImageView) findViewById(R.id.pic_animal);
        nameText = (TextView) findViewById(R.id.txt_animal_name);
        descText = (TextView) findViewById(R.id.txt_desc);
        wikiLink = (TextView) findViewById(R.id.txt_wiki_link);
        massText = (TextView) findViewById(R.id.txt_mass);
        populationText = (TextView) findViewById(R.id.txt_population);
        dietText = (TextView) findViewById(R.id.txt_diet);
        endangeredLevel = (TextView) findViewById(R.id.txt_endangered_level);

        String url = "http://cefns.nau.edu/~mh973/images/elephant.jpg";


        Picasso.with(this).load(url).error(R.mipmap.ic_launcher).into(animalPic);
    }

    //used to go back to the ListView Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        //go to ListView Activity
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AnimalDetailActivity.this, ListViewActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
