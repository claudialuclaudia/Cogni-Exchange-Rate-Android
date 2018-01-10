package cogninow.com.cogni;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView title;
    TextView text;
    GraphView graph;
    List<CurrencyModel> currencies = SampleCurrencies.currencies;
    List<String> spinnerArray;
    List<CurrencyModel> first;
    List<CurrencyModel> second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);
        graph = (GraphView) findViewById(R.id.graph);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        spinnerArray = new ArrayList<>();
        first = new ArrayList<>();
        second = new ArrayList<>();
        String addition, addition2;
        for (int i = 0; i<currencies.size()-1; i++){
            for (int j=1; j<currencies.size(); j++){
                addition = currencies.get(i).getAbbrev() + "/" + currencies.get(j).getAbbrev();
                addition2 =  currencies.get(j).getAbbrev() + "/" + currencies.get(i).getAbbrev();
                spinnerArray.add(addition);
                spinnerArray.add(addition2);
                first.add(currencies.get(i));
                first.add(currencies.get(j));
                second.add(currencies.get(j));
                second.add(currencies.get(i));
            }
        }
    }

    public String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(filename);
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
        spinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerArray));

//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.spinner_list_item_array, R.layout.spinner_item);
//        ArrayAdapter<CharSequence> adapter;
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                for (int i =0; i<spinnerArray.size();i++){
                    if (position == i){
                        title.setText(first.get(i).getFullName() + " to One " + second.get(i).getFullName());
                        String filename;
                        if (first.get(i).getAbbrev().charAt(0) < second.get(i).getAbbrev().charAt(0)){
                            filename = "EX" + first.get(i).getAbbrev() + second.get(i).getAbbrev();
                        } else {
                            filename = "EX" + second.get(i).getAbbrev() + first.get(i).getAbbrev();
                        }
                        try {
                            JSONObject obj = new JSONObject(loadJSONFromAsset(filename + ".json"));
                            JSONArray m_jArry = obj.getJSONArray(filename);
                            DataPoint[] dp = new DataPoint[m_jArry.length()];

                            double maxEx=0;
                            String maxDate="";
                            double minEx=Double.MAX_VALUE;
                            String minDate="";
//
                            for(int j = 0 ; j < m_jArry.length() ; j++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(j);
                                String DATE_value = jo_inside.getString("DATE");
                                String EXString = "EX" + first.get(i).getAbbrev() + second.get(i).getAbbrev();
                                String EX_value = jo_inside.getString(EXString);
                                double ex = Double.parseDouble(EX_value);
                                dp[j] = new DataPoint(j, ex);

                                if (ex > maxEx) {
                                    maxEx = ex;
                                    maxDate = DATE_value;
                                }
                                if (ex < minEx) {
                                    minEx = ex;
                                    minDate = DATE_value;
                                }

                                text.setText("The Current (" + DATE_value + ")" + spinnerArray.get(i)
                                        + " Exchange Rate is: " + EX_value
                                        + "\nThe Highest " + spinnerArray.get(i) +
                                        " Exchange Rate over the past years was: " + maxEx +
                                        " (" + maxDate + ") " +
                                        "\nThe Lowest " + spinnerArray.get(i) +
                                        " Exchange Rate over the past years was: " + minEx +
                                        " (" + minDate + ") ");
                            }

                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
                                graph.removeAllSeries();
                                graph.addSeries(series);

                                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
                                staticLabelsFormatter.setHorizontalLabels(new String[] {"1994", "2000", "2006", "2012", "2018"});
                                graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
//
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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