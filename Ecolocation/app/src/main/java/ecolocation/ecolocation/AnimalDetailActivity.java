package ecolocation.ecolocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java.util.ArrayList;

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

        //ArrayList<Animal> animalList = (ArrayList<Animal>) getIntent().getSerializableExtra("animal_list");
        back_button = (Button) findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: reset to original values
                Intent intent = new Intent( AnimalDetailActivity.this, GoogleDriveDemoActivity.class );
                startActivity( intent );
                finish();
            }
        });

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
}
