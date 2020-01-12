package ru.meow.uksap.Java;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;

import ru.meow.uksap.R;

/*

    на 100 миллисекунд быстрее
    трассировка ошибок

*/

public class MainActivity extends AppCompatActivity implements Constant {
    public Context context;
    private Data data;
    private Preferences preferences;
    private TableUpdater tableUpdater;
    private Requests requests;


    private TableLayout table;
    private Button buttonParse;
    private Spinner spinnerGroupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();


        buttonParse = findViewById(R.id.button_parse);
        spinnerGroupList = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, new String[]{""});
        spinnerGroupList.setAdapter(adapter);
        table = findViewById(R.id.tablelayout);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        data = new Data();
        preferences = new Preferences(context, data);
        tableUpdater = new TableUpdater(context, data, preferences, table, spinnerGroupList, displayMetrics);
        requests = new Requests(context, data, tableUpdater, preferences);


        listeners();
        //requests.jsonParse();
        if (preferences.loadData()) tableUpdater.updateSpinner();
        else requests.jsonParse();
    }

    public void listeners() {
        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableUpdater.clearTable(true);
                preferences.load();
                requests.jsonParse();
            }
        });

        spinnerGroupList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (data.getCells() != null) {
                    tableUpdater.clearTable(false);
                    tableUpdater.updateTable((Integer) data.getMapGroup().keySet().toArray()[position]);
                    preferences.save(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
