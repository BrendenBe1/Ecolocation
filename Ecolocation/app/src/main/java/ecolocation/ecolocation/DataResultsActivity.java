package ecolocation.ecolocation;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //----------- Widgets
        // setting up the view pager widget
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new ResultsAdapter(getSupportFragmentManager(), this));

        // setting up the tab layout widget
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    // -------- Dialogs
    public void createHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("What Does This Represent?:\n" +
                "Both the 'Bar Chart' and the 'Spatial Maps' represent the ability to distribute nutrients in an ecosystem both in the current era and in the Pleistocene Era (2,500,000 to 11,000 years ago). " +
                "This is a service provided by the animals in an ecosystem that is vital for maintaining its health.\n\n" +
                "What If Scenarios:\n" +
                "The blue slider represents the weight threshold for mammals in that ecosystem. By moving the nodes, you can see the effect of species of certain weights being removed from that ecosystem.\n\n" +
                "Switching Eras:\n" +
                "The drop down menu can be used to switch from the current spatial map to the Pleistocene Era spatial map").setTitle("Graph Help")
                .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createInfoDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Insert links to Chris's Website here")
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user closes dialog
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // -------- Menu
    // inflate menu custom icons
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_data_results, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id =item.getItemId();
        switch (id){
            case R.id.info:
                createInfoDialog();
                break;

            case R.id.help:
                createHelpDialog();
                break;

            case R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

/**
 *  This class makes the adapter for setting up the TabLayout using a ViewPager.
 */
class ResultsAdapter extends FragmentPagerAdapter{
    final int PAGE_COUNT = 2;
    private String[] tabTitles = new String[]{"Spatial Mapping", "Bar Chart"};

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
            return SpatialMapFragment.newInstance();
        }
        else{
            //position 1
            return BarChartFragment.newInstance();
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
