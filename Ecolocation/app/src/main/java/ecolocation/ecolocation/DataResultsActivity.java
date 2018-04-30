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
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 *  This activity displays the results of calculating the nutrient movement graphically. Displays
 *  the results through a bar chart and a spatial map.
 */
public class DataResultsActivity extends AppCompatActivity {
    private ViewPager viewPager;


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
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new ResultsAdapter(getSupportFragmentManager(), this));

        // setting up the tab layout widget
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
    // -------- Dialogs
    public void createHelpDialog() {
        String message;
        if(viewPager.getCurrentItem() == 0){
            message = "<b>" + "What Does this Represent?" + "</b> <br/>"+
                        "Animals play an important role in the transport of nutrients, but this " +
                        "role has diminished because many of the largest animals have gone extinct."
                        + " Here, we quantify the movement of nutrients by animals on land both " +
                        "now and prior to their widespread reductions. We show lateral nutrient " +
                        "distribution capacity (km2·y−1) by all mammals as it would have been " +
                        "without the end-Pleistocene and Holocene megafauna extinctions, as it is" +
                        " currently, and as the percentage of the original value." +
                        "<br/> <br/> <b>" + "Switching Eras" + "</b> <br/>" +
                        "The drop down menu can be used to switch from the current spatial map to" +
                        " the historic spatial map (during the Pleistocene Era).";
        }
        else{
            message = "<b>" + "What Does This Represent?" + "</b> <br/>" +
                    "The bar chart is displaying the nutrient movement within the selected " +
                    "ecosystem. It is comparing the nutrient movement between the Pleistocene" +
                    " Era (2,500,000 to 11,000 years ago) and the current nutrient movement." +
                    "Nutrient movement is an ecosystem service provided by an ecosystem's " +
                    "animals. The higher the nutrient dispersal is the more healthy and " +
                    "productive it is. <br> Animals during the Pleistocene Era are typically " +
                    "larger than currently existing animals and contribute more to their " +
                    "ecosystems because of this." + "<br/><br/>" +

                    "<b>" + "What If Scenarios" + "</b> <br/>" +
                    "The blue slider represents the weight threshold for mammals in that " +
                    "ecosystem. By moving the nodes, you can see the effect of species of certain" +
                    " weights being removed from that ecosystem." + "<br/> <br/>";

//            "<b>" + "What Does This Represent?:" + "</b> <br/>" +
//                    "This spatial map represents the animals ability to distribute nutrients" +
//                    " in an ecosystem. There are two spatial maps to view. The first one " +
//                    "represents the current nutrient dispersal throughout the planet. The second" +
//                    " spatial map represents the nutrient dispersal during the Pleistocene Era" +
//                    " (2,500,000 to 11,000 years ago). <br> Nutrient dispersal is an ecosystem" +
//                    " service provided by an ecosystem's animals. It is vital for maintaining an" +
//                    " ecosystem's health. The higher the nutrient dispersal is the more healthy " +
//                    "and productive it is. <br> Animals during the Pleistocene Era are typically " +
//                    "larger than currently existing animals and contribute more to their " +
//                    "ecosystems because of this."
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml(message)).setTitle("Graph Help")
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
        String message = "<b>" + "For more information" + "</b> <br/>" +
                this.getResources().getString(R.string.website1) + "<br/> <br/>" +
                this.getResources().getString(R.string.website2);
        builder.setMessage(Html.fromHtml(message))
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
