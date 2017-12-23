package ecolocation.ecolocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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

    //constants
    static final String SELECTED_ANIMAL = "selected animal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);

        //----------- toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);

        //------------- implementing widgets
        animalPic = (ImageView) findViewById(R.id.pic_animal);
        nameText = (TextView) findViewById(R.id.txt_animal_name);
        descText = (TextView) findViewById(R.id.txt_desc);
        wikiLink = (TextView) findViewById(R.id.txt_wiki_link);
        massText = (TextView) findViewById(R.id.txt_mass);
        populationText = (TextView) findViewById(R.id.txt_population);
        dietText = (TextView) findViewById(R.id.txt_diet);
        endangeredLevel = (TextView) findViewById(R.id.txt_endangered_level);

        //------------ Getting current animal & setting contents of widgets
        String animalName = getIntent().getExtras().getString(SELECTED_ANIMAL);
        //get animal from the list
        Ecosystem sEcosystem = Ecosystem.get(this);
        Animal animal = sEcosystem.getAnimal(animalName);

        //set contents of widgets
        //TODO: uncomment the below lines when the information is available
        animalPic.setImageDrawable(animal.getPicture());
        nameText.setText(animalName);
        descText.setText(animal.getDescription());
//        wikiLink.setText(animal.get);
        massText.setText(String.valueOf(animal.getMass()));
        populationText.setText(String.valueOf(animal.getPopulation()));
//       dietText.setText(animal.get);
        endangeredLevel.setText(animal.getEndangeredLevel());
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
