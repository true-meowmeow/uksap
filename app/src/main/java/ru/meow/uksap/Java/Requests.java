package ru.meow.uksap.Java;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


class Requests implements Constant {

    private TableUpdater tableUpdater;
    private Data data;
    private RequestQueue mQueue;
    private Preferences preferences;
    Requests(Context context, Data data, TableUpdater tableUpdater, Preferences preferences) {
        this.data = data;
        this.tableUpdater = tableUpdater;
        this.preferences = preferences;

        mQueue = Volley.newRequestQueue(context);
    }

     void jsonParse() {
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonCells = response.getJSONArray("user");
                            List<List<String>> cells = new ArrayList<>(jsonCells.length());
                            for (int i = 0; i < jsonCells.length(); i++) { //row
                                cells.add(new ArrayList<String>(jsonCells.getJSONArray(i).length()));
                                for (int j = 0; j < jsonCells.getJSONArray(i).length(); j++) { //column
                                    cells.get(i).add(String.valueOf(jsonCells.getJSONArray(i).getJSONArray(j)).replaceAll("[\\[\\]\"]", "").replaceAll("\\\\n", "\n"));
                                }
                            }
                            data.setCells(cells);
                            preferences.saveData(data.getCells());
                            tableUpdater.updateSpinner();
                        } catch (JSONException e) {
                            tableUpdater.requestFailed(String.valueOf(e));
                            e.printStackTrace();
                        } catch (Exception e) {
                            tableUpdater.requestFailed(String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tableUpdater.requestFailed(String.valueOf(error));
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}
