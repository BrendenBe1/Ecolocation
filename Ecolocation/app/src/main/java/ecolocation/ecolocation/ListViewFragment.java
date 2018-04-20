package ecolocation.ecolocation;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chandler on 3/6/2018.
 *
 * For tab layout:
 *      https://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout#create-fragment
 */

public class ListViewFragment extends Fragment {
    //widgets
    private RecyclerView recyclerView;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

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
                    ArrayList<Animal> temp = (ArrayList<Animal>) list.clone();
                    list.clear();
                    list.addAll(temp);
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
}
