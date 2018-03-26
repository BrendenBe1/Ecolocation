package ecolocation.ecolocation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 *  This activity displays the results of calculating the nutrient movement graphically. Displays
 *  the results through a bar chart and a spatial map.
 */
public class DataResultsActivity extends AppCompatActivity {
    // private variables
    Button listViewBttn;
    LatLng chosenLocation;

    //animal variables
    ArrayList<Animal> currentMammalList;
    ArrayList<Animal> historicMammalList;

    /**
     *  Sets up the view and tool bar of the activity
     *
     * @param savedInstanceState    if there is a previous state, data is stored in this variable
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_results);

        //---------- Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         //--------- Get Ecosystem Data
        //need to get coordinates and initialize Ecosystem
        Ecosystem ecosystem = Ecosystem.get(this);
        // if the intent has coordinates then we need to get them, otherwise we already have a
        // chosen location
        if(getIntent().hasExtra("COORDS")){
            //get the chosen location's coordinates
            chosenLocation = getIntent().getExtras().getParcelable("COORDS");
            Log.d("LATITUDE graph: ", String.valueOf(chosenLocation.latitude));

            //get Ecosystem instance and get database info & set coordinates for it
            currentMammalList = ecosystem.getCurrentList(chosenLocation);
            historicMammalList = ecosystem.getHistoricList(chosenLocation);
        }
        else{
            currentMammalList = ecosystem.getCurrentList();
            historicMammalList = ecosystem.getHistoricList();
        }

        //----------- Widgets
        // initializing button for going to the next page
        listViewBttn = (Button) findViewById(R.id.bttn_list);
        listViewBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataResultsActivity.this,
                        ListViewActivity.class);
                startActivity(intent);
            }
        });

        // setting up the view pager widget
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new ResultsAdapter(getSupportFragmentManager(), this));

        // setting up the tab layout widget
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}

/**
 *  This class makes the adapter for setting up the TabLayout using a ViewPager.
 */
class ResultsAdapter extends FragmentPagerAdapter{
    final int PAGE_COUNT = 2;
    private String[] tabTitles = new String[]{"Bar Chart", "Spatial Mapping"};

    // constructor
    public ResultsAdapter(FragmentManager fm, Context context){
        super(fm);
    }

    /**
     *  Returns the page title at the indicated position
     *
     * @param position  position of the page title to return
     * @return          CharSequence of the page title at the indicated position
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    /**
     *  Returns the item from the indicated position
     *
     * @param position  the position to get the item form
     * @return          the Fragment at the indicated position
     */
    @Override
    public Fragment getItem(int position) {
        // position 0 == bar chart, position 1 == spatial mapping
        if(position == 0){
            return BarChartFragment.newInstance();
        }
        else{
            //position 1
            return SpatialMapFragment.newInstance();
        }
    }

    /**
     *  Returns the number of tabs
     *
     * @return  the number of tabs
     */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
