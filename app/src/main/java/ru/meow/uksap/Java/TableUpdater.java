package ru.meow.uksap.Java;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ru.meow.uksap.R;

class TableUpdater implements Constant {

    private Data data;
    private Preferences preferences;
    private Context context;

    private DisplayMetrics displayMetrics;
    private TableLayout table;
    private Spinner spinnerGroupList;

    TableUpdater(Context context, Data data, Preferences preferences, TableLayout table, Spinner spinnerGroupList, DisplayMetrics displayMetrics) {
        this.context = context;
        this.data = data;
        this.preferences = preferences;
        this.table = table;
        this.spinnerGroupList = spinnerGroupList;
        this.displayMetrics = displayMetrics;
    }


    private boolean newPairs;
    private TextView[] number, name, auditory, viewDate, viewTimes;
    private TableRow[] tableRows, tableRowDate, tableRowTimes;

    void updateTable(int column) {
        table.setStretchAllColumns(false);


        tableRowTimes = new TableRow[(int) (Math.ceil((float) (data.getCells().size() - rowPairsFirst) / counterPairsInDay))];
        viewTimes = new TextView[(int) (Math.ceil((float) (data.getCells().size() - rowPairsFirst) / counterPairsInDay))];
        tableRowDate = new TableRow[(int) (Math.ceil((float) (data.getCells().size() - rowPairsFirst) / counterPairsInDay))];
        viewDate = new TextView[(int) (Math.ceil((float) (data.getCells().size() - rowPairsFirst) / counterPairsInDay))];

        number = new TextView[data.getCells().size() - rowPairsFirst];
        name = new TextView[data.getCells().size() - rowPairsFirst];
        auditory = new TextView[data.getCells().size() - rowPairsFirst];
        tableRows = new TableRow[data.getCells().size() - rowPairsFirst];


        int ccc = 0; //number of iterations % 2 for color
        int dayNumber = 0; //number of days
        int pairNumber = counterPairsInDay; //number of pairs
        int j;
        for (int i = rowPairsFirst; i < data.getCells().size(); i++) {
            j = i - rowPairsFirst;
            pairNumber++;
            if (pairNumber == 7) {


                tableRowDate[dayNumber] = new TableRow(context);
                tableRowTimes[dayNumber] = new TableRow(context);
                viewDate[dayNumber] = styleTextView(data.getCells().get(i).get(dateCell), false, 0, false, 33);
                viewTimes[dayNumber] = styleTextView(whenPairs(i, column), false, 0, true, 31);


                TableRow.LayoutParams param = new TableRow.LayoutParams();
                param.span = 3;

                viewDate[dayNumber].setLayoutParams(param);
                viewTimes[dayNumber].setLayoutParams(param);

                tableRowDate[dayNumber].addView(viewDate[dayNumber]);
                tableRowDate[dayNumber].setPadding(15, 90, 0, 5);
                tableRowDate[dayNumber].setBackgroundColor(Color.parseColor("#303030"));

                tableRowTimes[dayNumber].addView(viewTimes[dayNumber]);
                tableRowTimes[dayNumber].setPadding(0, 0, 0, 15);
                tableRowTimes[dayNumber].setBackgroundColor(Color.parseColor("#303030"));

                pairNumber = 1;
                dayNumber++;
                newPairs = true;
            }



            number[j] = styleTextView(data.getCells().get(i).get(columnNumber), true, displayMetrics.widthPixels / 8.65f, true, 24);
            name[j] = styleTextView(data.getCells().get(i).get(column), true, displayMetrics.widthPixels - (displayMetrics.widthPixels / 8.65f) - (displayMetrics.widthPixels / 5.15f), false, 24);
            auditory[j] = styleTextView(data.getCells().get(i).get(column + 1), true, displayMetrics.widthPixels / 5.15f, true, 24);


            tableRows[j] = new TableRow(context);
            tableRows[j].addView(number[j]);
            tableRows[j].addView(name[j]);
            tableRows[j].addView(auditory[j]);


            if (pairNumber == 1) {
                table.addView(tableRowDate[dayNumber - 1]);
                table.addView(tableRowTimes[dayNumber - 1]);
            }
            if (data.getCells().get(i).get(column).equals("")) continue;
            ccc++;
            if (newPairs) {
                ccc = 0;
                newPairs = false;
            }
            if (ccc % 2 == 0) tableRows[j].setBackgroundColor(Color.parseColor("#3b3f42"));


            table.addView(tableRows[j]);
        }
    }


    private TextView styleTextView(String text, boolean needWidth, float width, boolean gravityCENTER, int textSize) {
        TextView textView = new TextView(context);

        textView.setText(text);
        if (needWidth) textView.setWidth((int) width);
        if (gravityCENTER) textView.setGravity(Gravity.CENTER);
        textView.setTextSize(textSize);
        textView.setTextColor(Color.parseColor("#bbbbbb"));

        return textView;
    }


    private StringBuilder text;
    private boolean bool;
    private int indexLastPair;

    private String whenPairs(int i, int column) {
        text = new StringBuilder("выходной");
        bool = true;

        for (int j = i; j < i + counterPairsInDay; j++) {
            if (data.getCells().size() <= j) break;

            if (bool && !data.getCells().get(j).get(column).equals("")) {
                text.replace(0, text.length(), startTime[j - i] + " || ");
                bool = false;
            }
            if (!bool && !data.getCells().get(j).get(column).equals(""))
                indexLastPair = j - i;
        }

        if (!bool) text.insert(text.length(), endTime[indexLastPair]);
        return text.toString();
    }

    void requestFailed(String s) {
        table.removeAllViews();
        TableRow tableRow = new TableRow(context);
        tableRow.addView(styleTextView("ошибка, проверьте ваш доступ к интернету\n\nили свяжитесь со мной\n" + mail +"\n" + telegram + "\n\nКод ошибки\n" + s,
                true, displayMetrics.widthPixels, false, 27));
        table.addView(tableRow);
    }


    void clearTable(boolean isClearGroupList) {
        table.removeAllViews();
        if (isClearGroupList) {
            data.setMapGroup(null);
            spinnerGroupList.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_item, 0));
        }
    }

    void updateSpinner() {
        spinnerGroupList.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_item, data.createGroup()));
        try {
            spinnerGroupList.setSelection(preferences.getSelectedGroutInt());
        } catch (Exception ignored) {}
    }
}
