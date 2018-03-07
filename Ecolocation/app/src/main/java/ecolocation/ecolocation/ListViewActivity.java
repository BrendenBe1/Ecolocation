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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        AnimalSort sorter = new AnimalSort();
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.recalc:
//                return true;
//            case R.id.reset:
//                //TODO: replace this with restoring sliders to original values
//                Intent intent = new Intent( ListViewActivity.this, ListViewActivity.class );
//                finish();
//                startActivity(intent);
//                return true;
//
//            case R.id.alph_ascending:
//                sorter.sort(animalList, SORT_TYPE.BINOMIAL, 0);
//                adapter.notifyDataSetChanged();
//                return true;
//
//            case R.id.alph_descending:
//                sorter.sort(animalList, SORT_TYPE.BINOMIAL, 1);
//                adapter.notifyDataSetChanged();
//                return true;
//
//            case R.id.mass_ascending:
//                sorter.sort(animalList, SORT_TYPE.MASS, 0);
//                adapter.notifyDataSetChanged();
//                return true;
//
//            case R.id.mass_descending:
//                sorter.sort(animalList, SORT_TYPE.MASS, 1);
//                adapter.notifyDataSetChanged();
//                return true;
//
//            case  R.id.endang_ascending:
//                sorter.sort(animalList, SORT_TYPE.THREAT_LEVEL, 1);
//                adapter.notifyDataSetChanged();
//                return true;
//
//            case R.id.endang_descending:
//                sorter.sort(animalList, SORT_TYPE.THREAT_LEVEL, 0);
//                adapter.notifyDataSetChanged();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}

class TabLayoutAdapter extends FragmentPagerAdapter{
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Current", "Historic"};
    private Context context;

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
}