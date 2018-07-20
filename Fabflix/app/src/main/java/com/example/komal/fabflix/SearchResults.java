package com.example.komal.fabflix;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.view.View;
import android.widget.*;
/**
 * Created by Komal on 3/6/2018.
 */

public class SearchResults extends AppCompatActivity {

    public SearchResults(){};
    private SearchTask mAuthTask = null;
    private Button prev;
    private Button next;
    private JSONArray jsonArray = null;
    int nextStart = 0;
    int nextEnd = 10;
    TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);

        Bundle extras = getIntent().getExtras();
        String newString;

        if (extras == null)
        {
            newString = null;
        }
        else
        {
            newString = extras.getString("searchQuery");
            mAuthTask = new SearchTask(newString);
            mAuthTask.execute((Void) null);
        }


        prev.setOnClickListener( new View.OnClickListener()
        {
            public void onClick (View v){
                prev_page(v);

            }
        });

        next.setOnClickListener( new View.OnClickListener()
        {
            public void onClick (View v){
                next_page(v);
            }
        });

    }

    protected void prev_page(View v)
    {
        try
        {
            if (nextStart == 0 || jsonArray.length() <= 10) //first page
                return;

            if (tl.getChildCount() == 1)
                return;

            tl.removeAllViews();
            TableRow[] tr_head = new TableRow[11];
            tl = (TableLayout) findViewById(R.id.main_table);

            tr_head[0] = new TableRow(this);
            tr_head[0].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            TextView col1 = new TextView(this);
            TextView col2 = new TextView(this);
            TextView col3 = new TextView(this);
            TextView col4 = new TextView(this);
            TextView col5 = new TextView(this);

            col1.setText("TITLE");
            col2.setText("YEAR");
            col3.setText("DIRECTOR");
            col4.setText("GENRES");
            col5.setText("STARS");

            col1.setTextColor(Color.BLACK);
            col2.setTextColor(Color.BLACK);
            col3.setTextColor(Color.BLACK);
            col4.setTextColor(Color.BLACK);
            col5.setTextColor(Color.BLACK);

            col1.setPadding(5, 5, 5, 5);
            col2.setPadding(5, 5, 5, 5);
            col3.setPadding(5, 5, 5, 5);
            col4.setPadding(5, 5, 5, 5);
            col5.setPadding(5, 5, 5, 5);

            tr_head[0].addView(col1);
            tr_head[0].addView(col2);
            tr_head[0].addView(col3);
            tr_head[0].addView(col4);
            tr_head[0].addView(col5);

            tl.addView(tr_head[0], new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            if (nextEnd == jsonArray.length())
            {
                int numForLast = jsonArray.length()%10;
                nextEnd = nextEnd - numForLast;

            }
            else
            {
                nextEnd = nextEnd - 10;
            }

            List<String> jsonObjectList = new ArrayList<String>();
            nextStart = nextStart - 10;
            for (int j = nextStart; j < nextEnd; j++)
            {
                jsonObjectList.add(jsonArray.getString(j));
            }

            for (int i = 1; i < 11; i++) {
                JSONObject j = new JSONObject(jsonObjectList.get(i - 1));
                String title = j.getString("title");
                String year = j.getString("year");
                String director = j.getString("director");
                String genres = j.getString("genres");
                String stars = j.getString("stars");

                tr_head[i] = new TableRow(this);
                tr_head[i].setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                TextView column1 = new TextView(this);
                TextView column2 = new TextView(this);
                TextView column3 = new TextView(this);
                TextView column4 = new TextView(this);
                TextView column5 = new TextView(this);

                column1.setText(title);
                column2.setText(year);
                column3.setText(director);
                column4.setText(genres);
                column5.setText(stars);

                column1.setTextColor(Color.BLACK);
                column2.setTextColor(Color.BLACK);
                column3.setTextColor(Color.BLACK);
                column4.setTextColor(Color.BLACK);
                column5.setTextColor(Color.BLACK);

                column1.setPadding(5, 5, 5, 5);
                column2.setPadding(5, 5, 5, 5);
                column3.setPadding(5, 5, 5, 5);
                column4.setPadding(5, 5, 5, 5);
                column5.setPadding(5, 5, 5, 5);

                tr_head[i].addView(column1);
                tr_head[i].addView(column2);
                tr_head[i].addView(column3);
                tr_head[i].addView(column4);
                tr_head[i].addView(column5);

                tl.addView(tr_head[i], new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));

            }


        }
        catch(Exception e)
        {
            System.out.println("ERROR");
            e.printStackTrace();
        }
    }

    protected void next_page(View v)
    {
        try
        {
            if (tl.getChildCount() == 1 || jsonArray.length() <= 10)
                return;

            nextStart = nextStart + 10;
            nextEnd = nextEnd + 10;

            if (nextStart >= jsonArray.length() && nextEnd >= jsonArray.length())
                return;

            tl.removeAllViews();
            TableRow[] tr_head = new TableRow[11];
            tl = (TableLayout) findViewById(R.id.main_table);

            tr_head[0] = new TableRow(this);
            tr_head[0].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            TextView col1 = new TextView(this);
            TextView col2 = new TextView(this);
            TextView col3 = new TextView(this);
            TextView col4 = new TextView(this);
            TextView col5 = new TextView(this);

            col1.setText("TITLE");
            col2.setText("YEAR");
            col3.setText("DIRECTOR");
            col4.setText("GENRES");
            col5.setText("STARS");

            col1.setTextColor(Color.BLACK);
            col2.setTextColor(Color.BLACK);
            col3.setTextColor(Color.BLACK);
            col4.setTextColor(Color.BLACK);
            col5.setTextColor(Color.BLACK);

            col1.setPadding(5, 5, 5, 5);
            col2.setPadding(5, 5, 5, 5);
            col3.setPadding(5, 5, 5, 5);
            col4.setPadding(5, 5, 5, 5);
            col5.setPadding(5, 5, 5, 5);

            tr_head[0].addView(col1);
            tr_head[0].addView(col2);
            tr_head[0].addView(col3);
            tr_head[0].addView(col4);
            tr_head[0].addView(col5);

            tl.addView(tr_head[0], new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            if (nextEnd >= jsonArray.length() && nextStart < jsonArray.length())
            {
                int numForLast = jsonArray.length()%10;
                nextEnd = jsonArray.length();
                List<String> jsonObjectList = new ArrayList<String>();
                for (int j = nextStart; j < nextEnd; j++)
                {
                    jsonObjectList.add(jsonArray.getString(j));
                }

                for (int i = 1; i <= numForLast; i++)
                {
                    JSONObject j = new JSONObject(jsonObjectList.get(i - 1));
                    String title = j.getString("title");
                    String year = j.getString("year");
                    String director = j.getString("director");
                    String genres = j.getString("genres");
                    String stars = j.getString("stars");

                    tr_head[i] = new TableRow(this);
                    tr_head[i].setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    TextView column1 = new TextView(this);
                    TextView column2 = new TextView(this);
                    TextView column3 = new TextView(this);
                    TextView column4 = new TextView(this);
                    TextView column5 = new TextView(this);

                    column1.setText(title);
                    column2.setText(year);
                    column3.setText(director);
                    column4.setText(genres);
                    column5.setText(stars);

                    column1.setTextColor(Color.BLACK);
                    column2.setTextColor(Color.BLACK);
                    column3.setTextColor(Color.BLACK);
                    column4.setTextColor(Color.BLACK);
                    column5.setTextColor(Color.BLACK);

                    column1.setPadding(5, 5, 5, 5);
                    column2.setPadding(5, 5, 5, 5);
                    column3.setPadding(5, 5, 5, 5);
                    column4.setPadding(5, 5, 5, 5);
                    column5.setPadding(5, 5, 5, 5);

                    tr_head[i].addView(column1);
                    tr_head[i].addView(column2);
                    tr_head[i].addView(column3);
                    tr_head[i].addView(column4);
                    tr_head[i].addView(column5);

                    tl.addView(tr_head[i], new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    if (i == numForLast)
                        return;
                }
            }

            List<String> jsonObjectList = new ArrayList<String>();
            for (int j = nextStart; j < nextEnd; j++)
            {
                jsonObjectList.add(jsonArray.getString(j));
            }

            for (int i = 1; i < 11; i++) {
                JSONObject j = new JSONObject(jsonObjectList.get(i - 1));
                String title = j.getString("title");
                String year = j.getString("year");
                String director = j.getString("director");
                String genres = j.getString("genres");
                String stars = j.getString("stars");

                tr_head[i] = new TableRow(this);
                tr_head[i].setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                TextView column1 = new TextView(this);
                TextView column2 = new TextView(this);
                TextView column3 = new TextView(this);
                TextView column4 = new TextView(this);
                TextView column5 = new TextView(this);

                column1.setText(title);
                column2.setText(year);
                column3.setText(director);
                column4.setText(genres);
                column5.setText(stars);

                column1.setTextColor(Color.BLACK);
                column2.setTextColor(Color.BLACK);
                column3.setTextColor(Color.BLACK);
                column4.setTextColor(Color.BLACK);
                column5.setTextColor(Color.BLACK);

                column1.setPadding(5, 5, 5, 5);
                column2.setPadding(5, 5, 5, 5);
                column3.setPadding(5, 5, 5, 5);
                column4.setPadding(5, 5, 5, 5);
                column5.setPadding(5, 5, 5, 5);

                tr_head[i].addView(column1);
                tr_head[i].addView(column2);
                tr_head[i].addView(column3);
                tr_head[i].addView(column4);
                tr_head[i].addView(column5);

                tl.addView(tr_head[i], new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));

            }
        }
        catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }

    }

    protected void addTenToTable()
    {
        if (jsonArray.length() <= 10)
        {
            TableRow[] tr_head = new TableRow[jsonArray.length() + 1];
            tl = (TableLayout) findViewById(R.id.main_table);

            tr_head[0] = new TableRow(this);
            tr_head[0].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            TextView col1 = new TextView(this);
            TextView col2 = new TextView(this);
            TextView col3 = new TextView(this);
            TextView col4 = new TextView(this);
            TextView col5 = new TextView(this);

            col1.setText("TITLE");
            col2.setText("YEAR");
            col3.setText("DIRECTOR");
            col4.setText("GENRES");
            col5.setText("STARS");

            col1.setTextColor(Color.BLACK);
            col2.setTextColor(Color.BLACK);
            col3.setTextColor(Color.BLACK);
            col4.setTextColor(Color.BLACK);
            col5.setTextColor(Color.BLACK);

            col1.setPadding(5, 5, 5, 5);
            col2.setPadding(5, 5, 5, 5);
            col3.setPadding(5, 5, 5, 5);
            col4.setPadding(5, 5, 5, 5);
            col5.setPadding(5, 5, 5, 5);

            tr_head[0].addView(col1);
            tr_head[0].addView(col2);
            tr_head[0].addView(col3);
            tr_head[0].addView(col4);
            tr_head[0].addView(col5);

            tl.addView(tr_head[0], new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            for (int i = 1; i < jsonArray.length() + 1; i++)
            {
                try
                {
                    JSONObject j = new JSONObject(jsonArray.getString(i - 1));
                    String title = j.getString("title");
                    String year = j.getString("year");
                    String director = j.getString("director");
                    String genres = j.getString("genres");
                    String stars = j.getString("stars");

                    tr_head[i] = new TableRow(this);
                    tr_head[i].setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    TextView column1 = new TextView(this);
                    TextView column2 = new TextView(this);
                    TextView column3 = new TextView(this);
                    TextView column4 = new TextView(this);
                    TextView column5 = new TextView(this);

                    column1.setText(title);
                    column2.setText(year);
                    column3.setText(director);
                    column4.setText(genres);
                    column5.setText(stars);

                    column1.setTextColor(Color.BLACK);
                    column2.setTextColor(Color.BLACK);
                    column3.setTextColor(Color.BLACK);
                    column4.setTextColor(Color.BLACK);
                    column5.setTextColor(Color.BLACK);

                    column1.setPadding(5, 5, 5, 5);
                    column2.setPadding(5, 5, 5, 5);
                    column3.setPadding(5, 5, 5, 5);
                    column4.setPadding(5, 5, 5, 5);
                    column5.setPadding(5, 5, 5, 5);

                    tr_head[i].addView(column1);
                    tr_head[i].addView(column2);
                    tr_head[i].addView(column3);
                    tr_head[i].addView(column4);
                    tr_head[i].addView(column5);

                    tl.addView(tr_head[i], new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
                }
                catch(Exception e)
                {
                    System.out.println(e.getStackTrace());
                }
            }
            return;
        }

        TableRow[] tr_head = new TableRow[11];
        tl = (TableLayout) findViewById(R.id.main_table);

        tr_head[0] = new TableRow(this);
        tr_head[0].setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        TextView col1 = new TextView(this);
        TextView col2 = new TextView(this);
        TextView col3 = new TextView(this);
        TextView col4 = new TextView(this);
        TextView col5 = new TextView(this);

        col1.setText("TITLE");
        col2.setText("YEAR");
        col3.setText("DIRECTOR");
        col4.setText("GENRES");
        col5.setText("STARS");

        col1.setTextColor(Color.BLACK);
        col2.setTextColor(Color.BLACK);
        col3.setTextColor(Color.BLACK);
        col4.setTextColor(Color.BLACK);
        col5.setTextColor(Color.BLACK);

        col1.setPadding(5, 5, 5, 5);
        col2.setPadding(5, 5, 5, 5);
        col3.setPadding(5, 5, 5, 5);
        col4.setPadding(5, 5, 5, 5);
        col5.setPadding(5, 5, 5, 5);

        tr_head[0].addView(col1);
        tr_head[0].addView(col2);
        tr_head[0].addView(col3);
        tr_head[0].addView(col4);
        tr_head[0].addView(col5);

        tl.addView(tr_head[0], new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        for (int i = 1; i < 11; i++) {
            try
            {
                JSONObject j = new JSONObject(jsonArray.getString(i - 1));
                String title = j.getString("title");
                String year = j.getString("year");
                String director = j.getString("director");
                String genres = j.getString("genres");
                String stars = j.getString("stars");

                tr_head[i] = new TableRow(this);
                tr_head[i].setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                TextView column1 = new TextView(this);
                TextView column2 = new TextView(this);
                TextView column3 = new TextView(this);
                TextView column4 = new TextView(this);
                TextView column5 = new TextView(this);

                column1.setText(title);
                column2.setText(year);
                column3.setText(director);
                column4.setText(genres);
                column5.setText(stars);

                column1.setTextColor(Color.BLACK);
                column2.setTextColor(Color.BLACK);
                column3.setTextColor(Color.BLACK);
                column4.setTextColor(Color.BLACK);
                column5.setTextColor(Color.BLACK);

                column1.setPadding(5, 5, 5, 5);
                column2.setPadding(5, 5, 5, 5);
                column3.setPadding(5, 5, 5, 5);
                column4.setPadding(5, 5, 5, 5);
                column5.setPadding(5, 5, 5, 5);

                tr_head[i].addView(column1);
                tr_head[i].addView(column2);
                tr_head[i].addView(column3);
                tr_head[i].addView(column4);
                tr_head[i].addView(column5);

                tl.addView(tr_head[i], new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
            }
            catch(Exception e)
            {
                System.out.println("ERROR");
                e.printStackTrace();
            }

        }
    }

    protected void displayNoResults()
    {
        TableRow[] tr_head = new TableRow[1];
        tl = (TableLayout) findViewById(R.id.main_table);

        tr_head[0] = new TableRow(this);
        tr_head[0].setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        TextView col1 = new TextView(this);

        col1.setText("NO RESULTS FOR SEARCH");
        col1.setTextColor(Color.BLACK);
        col1.setPadding(5, 5, 5, 5);
        tr_head[0].addView(col1);

        tl.addView(tr_head[0], new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
    }

    public class SearchTask extends AsyncTask<Void, Void, Boolean> {
        private final String query;

        SearchTask(String mquery) {
            query = mquery;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String url = "http://ec2-13-57-13-30.us-west-1.compute.amazonaws.com:8080/fabflixApp/NormalSearch";
                String[] qArray = query.trim().split("\\s+");
                DefaultHttpClient httpClient = new DefaultHttpClient();
                url += "?title=";
                for (int i = 0; i < qArray.length; i++) {
                    String add;
                    if (i == qArray.length - 1)
                        add = qArray[i];
                    else
                        add = qArray[i] + "+";

                    url += add;
                }
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream is = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                String js = sb.toString();
                if (js.equals("fail"))
                {
                    return false;
                }

                jsonArray = new JSONArray(js);
                Handler refresh = new Handler(Looper.getMainLooper());
                refresh.post(new Runnable() {
                    public void run()
                    {
                        addTenToTable();
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success)
        {
            if (!success)
            {
                System.out.println("no results");
                displayNoResults();
            }
        }
    }
}
