package ecolocation.ecolocation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ArrayList<Animal> animalList;

    //constants
    private static final String ANIMAL_NAME_EXTRA = "ANIMAL_NAME_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //get the animal the user selected
        animalList = Ecosystem.get(this).getAnimalList();
        final String animalBinomial = getIntent().getStringExtra(ANIMAL_NAME_EXTRA);

        //------ Set Up ViewPager
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                Animal animal = animalList.get(position);
                String binomial = animal.getBinomial();
                //TODO: create the appropriate fragment
                return AnimalDetailActivity.newInstance(binomial);
            }

            @Override
            public int getCount() {
                return animalList.size();
            }
        });

        viewPager.setCurrentItem(findSelectedAnimal(animalBinomial));
    }

    //encapsulates calling an intent for the activity that starts this activity
    public static Intent newIntent(Context packagePackage, String animalBinomial){
        Intent intent = new Intent(packagePackage, DetailActivity.class);
        intent.putExtra(ANIMAL_NAME_EXTRA, animalBinomial);
        return intent;
    }

    private  int findSelectedAnimal(String animalBinomial){
        for(int i=0; i<animalList.size(); i++){
            Animal currAnimal = animalList.get(i);
            if(currAnimal.getBinomial().equals(animalBinomial)){
                return i;
            }
        }
        return 0;
    }


}
