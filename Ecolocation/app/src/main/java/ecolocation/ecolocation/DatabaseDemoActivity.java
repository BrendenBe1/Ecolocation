package ecolocation.ecolocation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseDemoActivity extends AppCompatActivity {
    //Widgets
    ListView listView;
    Button resetButton;
    Button recalcButton;
    Button sortButton;

    //variables for creating the list
    private ArrayList<Animal> animalList;
    private AnimalAdapter adapter;

    static final String DB_URL = "jdbc:mysql://ecolocationdata.c8qsf4w8dkdu.us-east-2.rds.amazonaws.com:3306/animal_data";
    static final String DB_DRV = "com.mysql.jdbc.Driver";
    static final String DB_USER = "team_ecolocation";
    static final String DB_PASSWD = "Ecolocation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_drive);

        //TODO: initialize animalList with database/google drive stuff;
        animalList = fillList();

        //-------- Implementing Widgets
        listView = (ListView) findViewById(R.id.layout_list);
        resetButton = (Button) findViewById(R.id.bttn_reset);
        recalcButton = (Button) findViewById(R.id.bttn_recalc);
        sortButton = (Button) findViewById(R.id.bttn_sort);


        //setting up the individual list items with the adapter
        adapter = new AnimalAdapter(this, animalList);
        /*
        * This line is doing a lot, the listView will take in individual
        * list_item layouts from the adapter. The adapter is filling in those
        * list_item layouts with the data it gets (that work is done w/n it's
        * own class).
        */
        listView.setAdapter(adapter);

        //-------- Setting Up Event Listeners
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: reset to original values
            }
        });

        recalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: calculate new nutrient dispersal
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: show different ways of sorting
            }
        });

        connectDB();
    }

    public void connectDB(){
        String type = "select";
        BackgroundWork backgroundwork = new BackgroundWork(this);
        backgroundwork.execute(type, DB_USER, DB_PASSWD);



        /*Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            // System.out.println("Database connection success");

            String result = "Database connection success\n";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from iucn");
            ResultSetMetaData rsmd = rs.getMetaData();

            while(rs.next()) {
                result += rsmd.getColumnName(1) + ": " + rs.getInt(1) + "\n";
                result += rsmd.getColumnName(2) + ": " + rs.getString(2) + "\n";
                result += rsmd.getColumnName(3) + ": " + rs.getString(3) + "\n";
            }
            Log.e("db", result);

        }
        catch(Exception e) {
            e.printStackTrace();

        }*/

    }
    //this is just filling it in with dummy data
    private ArrayList<Animal> fillList(){
        Drawable pic = getResources().getDrawable(R.drawable.ic_launcher_background);

        Animal lion = new Animal("lion", pic,"A big cat in Africa", "carnivore",
                "vulnerable", 187.5, 20000);

        Animal elephant = new Animal("african elephant", pic, "The largest land mammal",
                "herbivore", "vulnerable", 3500, 415000);

        Animal giraffe = new Animal("giraffe", pic, "An animal with a long neck",
                "herbivore", "vulnerable", 1192, 97500);

        Animal cheetah = new Animal("cheetah", pic, "A very fast animal",
                "carnivore", "vulnerable", 50, 7100);

        Animal zebra = new Animal("zebra", pic, "A striped horse.", "herbivore",
                "near threatened", 250, 150000);

        ArrayList<Animal> list = new ArrayList<Animal>();
        list.add(lion);
        list.add(elephant);
        list.add(giraffe);
        list.add(cheetah);
        list.add(zebra);

        return list;
    }
}
