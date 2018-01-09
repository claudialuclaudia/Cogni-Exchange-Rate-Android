package cogninow.com.cogni;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = (TextView) findViewById(R.id.text);

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("EXMXUS");

            GraphView graph = (GraphView) findViewById(R.id.graph);
            DataPoint[] dp = new DataPoint[m_jArry.length()];

            double maxEx=0;
            String maxDate="";
            double minEx=Double.MAX_VALUE;
            String minDate="";

            for(int i = 0 ; i < m_jArry.length() ; i++){
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String DATE_value = jo_inside.getString("DATE");
                String EX_value = jo_inside.getString("EXMXUS");
                double ex = Double.parseDouble(EX_value);
                dp[i]=new DataPoint(i, ex);

                if (ex > maxEx){
                    maxEx = ex;
                    maxDate = DATE_value;
                }
                if (ex < minEx) {
                    minEx = ex;
                    minDate = DATE_value;
                }

                text.setText("The Current (" + DATE_value + ") MX/US Exchange Rate is: " + EX_value
                + "\nThe Highest MX/US Exchange Rate over the past 24 years was: " + maxEx +
                        " (" + maxDate + ") " +
                        "\nThe Lowest MX/US Exchange Rate over the past 24 years was: " + minEx +
                " (" + minDate + ") ");
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
            graph.addSeries(series);

            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[] {"1994", "2000", "2006", "2012", "2018"});
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("EXMXUS.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                } else if (position == 1) {
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        return true;
    }
}