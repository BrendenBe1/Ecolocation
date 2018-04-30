package ecolocation.ecolocation;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {
    //Widgets
    ViewPager viewPager;
    TabLayout tabLayout;
    Button nextButton;

    // constants
    private final static String EXTRA_COORDINATES = "coordinates";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        // --------- Get Data From Database
        //need to get coordinates and initialize Ecosystem
        Ecosystem ecosystem = Ecosystem.get(this);
        // if the intent has coordinates then we need to get them
        if(getIntent().hasExtra(EXTRA_COORDINATES)){
            //get the chosen location's coordinates & send it to Ecosystem instance
            LatLng coordinates = getIntent().getExtras().getParcelable(EXTRA_COORDINATES);
            Log.d("LATITUDE graph: ", String.valueOf(coordinates.latitude));
            ecosystem.setChosenLocation(coordinates);
        }

        //----------- toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);

        //------------ Widgets
        //view pager
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayoutAdapter adapter = new TabLayoutAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);

        //tab layout
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // button to go to the DataResultsActivity
        nextButton = (Button) findViewById(R.id.bttn_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListViewActivity.this, DataResultsActivity
                        .class));
            }
        });

    }


    // ---------- Dialogs
    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String message = "<b>" + "‘Current’ Tab" + "</b> <br/>" +
                "This tab has a list of mammals that currently live in the chosen ecosystem " +
                "(The location you selected). Only mammals over 1kg are shown." + "<br/><br/>" +

                "<b>" + "‘Historic’ Tab" + "</b> <br/>" +
                "This tab has a list of mammals that existed in the chosen ecosystem (The " +
                "location you selected) during the Pleistocene Era (2,500,000 to 11,000 years ago)"
                + ". <br/><br/>" +

                "<b>" + "Sorting" + "</b> <br/>" +
                "The icon with three lines can be used to sort the list by common name, " +
                "scientific name, mass, and threat level in ascending or descending order." +

                "<br/><br/><b>" + "Additional Information" + "</b> <br/>" +
                "Click on an animal to view a page of information on that animal, as well as a " +
                "link to its Wikipedia page";

        builder.setMessage(Html.fromHtml(message)).setTitle("List Help").setNegativeButton
                ("close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //------------- Menu
    // inflate menu custom icons
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AnimalSort sorter = new AnimalSort();
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.help:
                createDialog();
                return true;

            case R.id.comm_ascending:
                sorter.sort(getDisplayedList(), SORT_TYPE.COMMON_NAME, 0);
                updateFragment();
                return true;

            case R.id.comm_descending:
                sorter.sort(getDisplayedList(), SORT_TYPE.COMMON_NAME, 1);
                updateFragment();
                return true;

            case R.id.bin_ascending:
                sorter.sort(getDisplayedList(), SORT_TYPE.BINOMIAL, 0);
                updateFragment();
                return true;

            case R.id.bin_descending:
                sorter.sort(getDisplayedList(), SORT_TYPE.BINOMIAL, 1);
                updateFragment();
                return true;

            case R.id.mass_ascending:
                sorter.sort(getDisplayedList(), SORT_TYPE.MASS, 0);
                updateFragment();
                return true;

            case R.id.mass_descending:
                sorter.sort(getDisplayedList(), SORT_TYPE.MASS, 1);
                updateFragment();
                return true;

            case  R.id.threat_ascending:
                sorter.sort(getDisplayedList(), SORT_TYPE.THREAT_LEVEL, 0);
                updateFragment();
                return true;

            case R.id.threat_descending:
                sorter.sort(getDisplayedList(), SORT_TYPE.THREAT_LEVEL, 1);
                updateFragment();
                return true;

            case R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private ArrayList<Animal> getDisplayedList(){
        // position = 0 current list, position 1 = historic list
        if(viewPager.getCurrentItem() == 0){
            //return the current list
            return Ecosystem.get(this).getCurrentList();
        }
        else{
            // return the historic list
            return Ecosystem.get(this).getHistoricList();
        }
    }

    private void updateFragment(){
        TabLayoutAdapter adapter = (TabLayoutAdapter) viewPager.getAdapter();
        ListViewFragment frag = (ListViewFragment) adapter.getCurrentFragment();
        frag.updateListView();
    }


    public static Intent newIntent(Context context, LatLng coordinates){
        Intent intent = new Intent(context, ListViewActivity.class);
        intent.putExtra(EXTRA_COORDINATES, coordinates);
        return intent;
    }


}

class TabLayoutAdapter extends FragmentPagerAdapter{
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Current", "Historic"};
    private Context context;
    private Fragment currentFragment;

    public TabLayoutAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return ListViewFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    public Fragment getCurrentFragment(){
        return currentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object){
        if(getCurrentFragment() != object){
            currentFragment = (Fragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }
}
