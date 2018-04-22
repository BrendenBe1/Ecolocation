package ecolocation.ecolocation;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Chandler on 3/6/2018.
 *
 * For tab layout:
 *      https://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout#create-fragment
 */

public class ListViewFragment extends Fragment {
    //widgets
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    // variables for displaying the list
    private AnimalAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Animal> animalList;

    private static final String ARG_PAGE = "ARG_PAGE";
    /**
     * 1 = current mammals
     * 2 = historic mammals
     */
    private int pageType;
    private AnimalType animalType;

    public static ListViewFragment newInstance(int page){
        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        pageType = getArguments().getInt(ARG_PAGE);

        // -------- Retrieving correct list
        if(pageType == 0){
            //use current mammals list
            animalList = Ecosystem.get(getContext()).getCurrentList();
            animalType = AnimalType.CURRENT_MAMMAL;
        }
        else{
            animalList = Ecosystem.get(getContext()).getHistoricList();
            animalType = AnimalType.HISTORIC_MAMMAL;
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        AnimalType[] type = {animalType};
        new Task ().execute(type);

        // Setting Up RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AnimalAdapter(animalList);
        recyclerView.setAdapter(adapter);
        Ecosystem ecosystem = Ecosystem.get(getContext());
        ecosystem.setAdapter(adapter);

        return view;
    }

    public void updateListView(){
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>{
        private ArrayList<Animal> list;

        public AnimalAdapter(ArrayList<Animal> animalList){
            list = animalList;
        }

        @Override
        public int getItemCount(){
            return list.size();
        }

        @Override
        public AnimalViewHolder  onCreateViewHolder(ViewGroup parent, int i){
            final View rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            //enable selecting an item
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = recyclerView.getChildAdapterPosition(rowView);
                    if(pos >= 0 && pos < getItemCount()){
                        Animal currentAnimal = list.get(pos);
                        Intent intent = DetailActivity.newIntent(getActivity(),
                                currentAnimal.getBinomial(), animalType);
                        startActivity(intent);
                    }
                }
            });

            AnimalViewHolder holder =  new AnimalViewHolder(rowView);

            return holder;
        }

        @Override
        public void onBindViewHolder(AnimalViewHolder viewHolder, int pos){
            Animal animal = list.get(pos);
            viewHolder.commonNameTxt.setText(animal.getName());
            viewHolder.binomialTxt.setText(animal.getBinomial());
            viewHolder.animalPic.setImageDrawable(animal.getPicture());

            //------- Color-Code rows
            Resources res = getContext().getResources();    //allows access to the color files
            switch (animal.getThreatLevel().getName()){
                case "Least Concern":
                    viewHolder.commonNameTxt.setTextColor(res.getColor(R.color.leastConcern));
                    break;
                case "Near Threatened":
                    viewHolder.commonNameTxt.setTextColor(res.getColor(R.color.nearThreatened));
                    break;
                case "Vulnerable":
                    viewHolder.commonNameTxt.setTextColor(res.getColor(R.color.vulnerable));
                    break;
                case "Endangered":
                    viewHolder.commonNameTxt.setTextColor(res.getColor(R.color.endagered));
                    break;
                case "Critically Endangered":
                    viewHolder.commonNameTxt.setTextColor(res.getColor(R.color.criticallyEndangered));
                    break;
                case "Extinct in the Wild":
                    viewHolder.commonNameTxt.setTextColor(res.getColor(R.color.extinctInTheWild));
                    break;
                case "Extinct":
                    viewHolder.commonNameTxt.setTextColor(res.getColor(R.color.extinct));
                    break;
                case "Not Evaluated":
                    viewHolder.commonNameTxt.setTextColor(res.getColor(R.color.notEvaluated));
                    break;
                case "Data Deficient":
                    viewHolder.commonNameTxt.setTextColor(res.getColor(R.color.dataDeficient));
                    break;
                default:
                    viewHolder.commonNameTxt.setTextColor(res.getColor(R.color.notEvaluated));
            }
        }

        public final void notifyImageChange(AnimalType type){
                if(animalType == AnimalType.CURRENT_MAMMAL && animalType == type){
                    list.clear();
                    list.addAll(Ecosystem.get(getContext()).getCurrentList());
                    notifyDataSetChanged();
                }
                else if(animalType == AnimalType.HISTORIC_MAMMAL && animalType == type){
                    list.clear();
                    list.addAll(Ecosystem.get(getContext()).getHistoricList());
                    notifyDataSetChanged();
                }
        }

        public class AnimalViewHolder extends RecyclerView.ViewHolder {
            // widgets
            ImageView animalPic;
            TextView commonNameTxt;
            TextView binomialTxt;

            public AnimalViewHolder(View view){
                super(view);

                // initialize widgets
                animalPic = (ImageView) view.findViewById(R.id.pic_animal);
                commonNameTxt = (TextView) view.findViewById(R.id.txt_animal_name);
                binomialTxt = (TextView) view.findViewById(R.id.txt_binomial_title);
            }
        }
    }

    private void loadImageFromURL(final Animal animal)
    {
        // create an imageView to hold the picture
        final ImageView imageView = new ImageView(getContext());

        String fileName = "";
        if(animal.getType().equals(AnimalType.CURRENT_MAMMAL)){
            fileName = animal.getBinomial().replace(" ", "-").toLowerCase();
        }
        else{
            fileName = animal.getBinomial().replace(" ", "_").toLowerCase();
        }

        String url = "https://www.cefns.nau.edu/capstone/projects/CS/2018/Ecolocation/images/";

        url += "current/" + fileName + ".jpg";

        // call to get picture
        Picasso.with(getContext()).load(url).error(R.mipmap.ic_launcher).into(imageView,
                new com.squareup.picasso.Callback(){

                    /*
                     *  because the image doesn't load all at once you have to set the image for the animal
                     *  when it is successful
                     */
                    @Override
                    public void onSuccess()
                    {
                        Drawable d = imageView.getDrawable();
                        animal.setImage(d);
                        if(adapter != null){
                            adapter.notifyImageChange(animal.getType());
                        }
                    }
                    @Override
                    public void onError(){}
                });
    }

    private void loadImageRangeMap(final Animal animal){
        // create an imageView to hold the picture
        final ImageView imageView = new ImageView(getContext());

        String fileName = animal.getBinomial().toLowerCase().replace(" ", "-");
        String url = "https://www.cefns.nau.edu/capstone/projects/CS/2018/Ecolocation/images/" +
                "historic_range/" + fileName + ".png";

        // call to get picture
        Picasso.with(getContext()).load(url).error(R.mipmap.ic_launcher).into(imageView,
                new com.squareup.picasso.Callback(){

                    /*
                     *  because the image doesn't load all at once you have to set the image for the animal
                     *  when it is successful
                     */
                    @Override
                    public void onSuccess()
                    {
                        Drawable d = imageView.getDrawable();
                        animal.setRangeMap(d);
                        if(adapter != null){
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onError(){}
                });
    }

    // ------- loading database
    class Task extends AsyncTask<AnimalType, Void, Void> {
        final ArrayList<Animal> list = new ArrayList<>();
        Ecosystem ecosystem = Ecosystem.get(getContext());
        AnimalType type;
        LatLng chosenLocation;
        final Drawable pic = getContext().getResources().getDrawable(R.drawable
                .ic_launcher_background);

        @Override
        protected void onPreExecute() {
            chosenLocation = ecosystem.getChosenLocation();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(AnimalType... params) {
            type = params[0];

            OkHttpClient client = new OkHttpClient();
            RequestBody arguments = new FormBody.Builder()
                    .add("latitude", String.valueOf(chosenLocation.latitude))
                    .add("longitude", String.valueOf(chosenLocation.longitude))
                    .build();
            Log.d("latitude:::::::::::", String.valueOf(chosenLocation.latitude));
            Log.d("longitude:::::::::::", String.valueOf(chosenLocation.longitude));

            // determine the correct request object to make depending on animal type
            Request request;
            if(type == AnimalType.CURRENT_MAMMAL){
                request = new Request.Builder()
                        .url("http://18.222.2.88/get_data.php?")
                        .post(arguments)
                        .build();
            }
            else{
                request = new Request.Builder()
                        .url("http://18.222.2.88/get_historic_data.php?")
                        .post(arguments)
                        .build();
            }

            try {
                Response response = client.newCall(request).execute();

                JSONArray array = new JSONArray(response.body().string());

                for (int i = 0; i < array.length(); i++) {

                    JSONObject object = array.getJSONObject(i);

                    String binomial = object.getString("binomial");
                    String commonName = object.getString("common_name");
                    String description = object.getString("description");
                    String wikiLink = object.getString("wiki_link");
                    int mass = object.getInt("mass")/1000;  //convert it to kg

                    Animal animal;
                    if(type == AnimalType.CURRENT_MAMMAL){
                        animal = new Animal(binomial, commonName,
                                pic, description, wikiLink, object.getString("code"), mass,
                                AnimalType.CURRENT_MAMMAL);
                    }
                    else{
                        animal = new Animal(binomial, commonName, pic, description, wikiLink,
                                "EX", mass, AnimalType.HISTORIC_MAMMAL);
                    }

                    // make sure there are no repeats
                    if(!list.contains(animal)){
                        list.add(animal);
                    }
                    Log.d("return", animal.getBinomial());
                }
                if(type == AnimalType.CURRENT_MAMMAL){
                    ecosystem.setCurrentList(list);
                }
                else if(type == AnimalType.HISTORIC_MAMMAL){
                    ecosystem.setHistoricList(list);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            if(type == AnimalType.CURRENT_MAMMAL) {
                ArrayList<Animal> currentList = ecosystem.getCurrentList();

                for(int i = 0; i< currentList.size(); i++) {
                    Animal currAnimal = currentList.get(i);
                    loadImageFromURL(currAnimal);

                    Log.d("currAnimal", currAnimal.getBinomial());
                }
            }
            else{
                ArrayList<Animal> historicList = ecosystem.getHistoricList();

                for(int i = 0; i< historicList.size(); i++) {
                    Animal currAnimal = historicList.get(i);
                    loadImageFromURL(currAnimal);
                    loadImageRangeMap(currAnimal);

                    Log.d("currAnimal", currAnimal.getBinomial());
                }
            }

        }
    }
}