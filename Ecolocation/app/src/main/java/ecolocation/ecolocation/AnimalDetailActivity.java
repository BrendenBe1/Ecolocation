package ecolocation.ecolocation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);

        //implementing widgets
        animalPic = (ImageView) findViewById(R.id.pic_animal);
        nameText = (TextView) findViewById(R.id.txt_animal_name);
        descText = (TextView) findViewById(R.id.txt_desc);
        wikiLink = (TextView) findViewById(R.id.txt_wiki_link);
        massText = (TextView) findViewById(R.id.txt_mass);
        populationText = (TextView) findViewById(R.id.txt_population);
        dietText = (TextView) findViewById(R.id.txt_diet);
        endangeredLevel = (TextView) findViewById(R.id.txt_endangered_level);
    }
}
