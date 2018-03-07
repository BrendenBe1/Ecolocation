package ecolocation.ecolocation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    //widgets
    private ViewPager viewPager;
    private ArrayList<Animal> animalList;

    //class variables
    private AnimalType animalType;

    //constants
    private static final String EXTRA_ANIMAL_NAME = "ANIMAL_NAME_EXTRA";
    private static final String EXTRA_ANIMAL_TYPE = "ANIMAL_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //----------- toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);


        //---------- Get the Animal the User Selected
        animalType = (AnimalType) getIntent().getSerializableExtra(EXTRA_ANIMAL_TYPE);

        //determine which
        if(animalType.equals(AnimalType.CURRENT_MAMMAL)){
            animalList = Ecosystem.get(this).getCurrentList();
        }
        else if(animalType.equals(AnimalType.HISTORIC_MAMMAL)){
            animalList = Ecosystem.get(this).getHistoricList();
        }
        final String animalBinomial = getIntent().getStringExtra(EXTRA_ANIMAL_NAME);


        //------ Set Up ViewPager
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                Animal animal = animalList.get(position);
                String binomial = animal.getBinomial();
                return DetailFragment.newInstance(binomial, animalType);
            }

            @Override
            public int getCount() {
                return animalList.size();
            }
        });

        viewPager.setCurrentItem(findSelectedAnimal(animalBinomial));
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

    /**
     *
     *
     *
     * @param activity      the activity requesting this page
     * @param animalName    the name of the animal to display
     * @param type    the type of animal to display (0 = current, 1 = historic)
     * @return              returns the intent with the extras in it
     */
    public static Intent newIntent(Context activity, String animalName, AnimalType type){
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(EXTRA_ANIMAL_NAME, animalName);
        intent.putExtra(EXTRA_ANIMAL_TYPE, type);
        return intent;
    }
}
