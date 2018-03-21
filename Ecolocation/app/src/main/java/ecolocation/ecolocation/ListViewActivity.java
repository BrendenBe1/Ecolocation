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
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {
    //Widgets
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

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

            case  R.id.endang_ascending:
                sorter.sort(getDisplayedList(), SORT_TYPE.THREAT_LEVEL, 0);
                updateFragment();
                return true;

            case R.id.endang_descending:
                sorter.sort(getDisplayedList(), SORT_TYPE.THREAT_LEVEL, 1);
                updateFragment();
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