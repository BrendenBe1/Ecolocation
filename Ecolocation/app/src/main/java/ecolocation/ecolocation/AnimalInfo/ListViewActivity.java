package ecolocation.ecolocation.AnimalInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import ecolocation.ecolocation.R;

public class ListViewActivity extends AppCompatActivity {
    //Widgets
    ListView listView;

    //variables for creating the list
    private ArrayList<Animal> animalList;
    private AnimalAdapter adapter;
    private static Ecosystem sEcosystem;
    LatLng chosenLocation;

    //constants
    static final String SELECTED_ANIMAL = "selected animal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

//        chosenLocation = getIntent().getExtras().getParcelable("COORDS"); // get coordinates
//        Log.d("LATITUDE: ", String.valueOf(chosenLocation.latitude));
//        Log.d("LONGITUDE: ", String.valueOf(chosenLocation.longitude));


        //----------- toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_up_navigation);

        //get animal list
        animalList = Ecosystem.get(this).getAnimalList();

        //-------- Implementing Widgets
        listView = (ListView) findViewById(R.id.layout_list);

        // ----------- Adapter Stuff
        // setting up the individual list items with the adapter
        adapter = new AnimalAdapter(this, animalList);
        //the adapater fills the list with the array list
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal currAnimal = animalList.get(position);
                Intent intent = DetailActivity.newIntent(ListViewActivity.this,
                        currAnimal.getBinomial());
                startActivity(intent);
            }
        });

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
            case R.id.recalc:
                return true;
            case R.id.reset:
                //TODO: replace this with restoring sliders to original values
                Intent intent = new Intent( ListViewActivity.this, ListViewActivity.class );
                finish();
                startActivity(intent);
                return true;

            case R.id.alph_ascending:
                sorter.sort(animalList, SORT_TYPE.BINOMIAL, 0);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.alph_descending:
                sorter.sort(animalList, SORT_TYPE.BINOMIAL, 1);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.pop_ascending:
                sorter.sort(animalList, SORT_TYPE.POPULATION, 0);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.pop_descending:
                sorter.sort(animalList, SORT_TYPE.POPULATION, 1);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.mass_ascending:
                sorter.sort(animalList, SORT_TYPE.MASS, 0);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.mass_descending:
                sorter.sort(animalList, SORT_TYPE.MASS, 1);
                adapter.notifyDataSetChanged();
                return true;

            case  R.id.endang_ascending:
                sorter.sort(animalList, SORT_TYPE.THREAT_LEVEL, 1);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.endang_descending:
                sorter.sort(animalList, SORT_TYPE.THREAT_LEVEL, 0);
                adapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
