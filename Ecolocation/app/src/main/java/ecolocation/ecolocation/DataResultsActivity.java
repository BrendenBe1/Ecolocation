package ecolocation.ecolocation;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

/**
 *  This activity displays the results of calculating the nutrient movement graphically. Displays
 *  the results through a bar chart and a spatial map.
 */
public class DataResultsActivity extends AppCompatActivity {
    // private variables
//    Button listViewBttn;
    LatLng chosenLocation;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);

        //----------- Widgets
        // setting up the view pager widget
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new ResultsAdapter(getSupportFragmentManager(), this));

        // setting up the tab layout widget
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
