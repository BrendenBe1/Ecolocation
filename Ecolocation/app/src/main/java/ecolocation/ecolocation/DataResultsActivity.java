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

public class DataResultsActivity extends AppCompatActivity {
    // private variables
    Button listViewBttn;
    LatLng chosenLocation;

    //animal variables
    ArrayList<Animal> currentMammalList;
    ArrayList<Animal> historicMammalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_results);

        //---------- Implementing menu button
        //toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //----------- Widgets
        listViewBttn = (Button) findViewById(R.id.bttn_list);
        listViewBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataResultsActivity.this,
                        ListViewActivity.class);
                startActivity(intent);
            }
        });

        //--------- Get Ecosystem Data
        //need to get coordinates and initialize Ecosystem
        Ecosystem ecosystem = Ecosystem.get(this);
        if(getIntent().hasExtra("COORDS")){
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

        // set up the viewpager & its adapter
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new ResultsAdapter(getSupportFragmentManager(), this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}

class ResultsAdapter extends FragmentPagerAdapter{
    final int PAGE_COUNT = 2;
    private String[] tabTitles = new String[]{"Bar Chart", "Spatial Mapping"};
    private Context context;

    public ResultsAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

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

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
