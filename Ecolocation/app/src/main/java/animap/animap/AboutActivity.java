package animap.animap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    // widgets
    private TextView iucnCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        iucnCredit = (TextView) findViewById(R.id.txt_iucn_credit);
        iucnCredit.setText("IUCN 2017. The IUCN Red List of Threatened " +
                "Species. Version 2017-3. \\<http://www.iucnredlist.org>. " +
                "Downloaded on 05 December 2017.");
    }
}
