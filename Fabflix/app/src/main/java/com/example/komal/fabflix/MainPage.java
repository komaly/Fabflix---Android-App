package com.example.komal.fabflix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.SearchView;

/**
 * Created by Komal on 3/4/2018.
 */

public class MainPage extends AppCompatActivity{

    public MainPage(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        Button button = findViewById(R.id.search_button);
        final SearchView search = findViewById(R.id.simpleSearchView);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainPage.this, SearchResults.class);
                myIntent.putExtra("searchQuery", search.getQuery().toString());
                MainPage.this.startActivity(myIntent);
            }
        });

    }
}
