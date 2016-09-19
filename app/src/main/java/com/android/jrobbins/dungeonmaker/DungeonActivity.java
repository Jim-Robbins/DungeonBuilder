package com.android.jrobbins.dungeonmaker;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.android.jrobbins.dungeonmaker.R;

public class DungeonActivity extends AppCompatActivity {

    String mapData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dungeon);

        //the detail Activity called via intent. Inspect the intent for parcelable movie data
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mapData = intent.getStringExtra(Intent.EXTRA_TEXT);

            TextView mapView = (TextView) findViewById(R.id.view_dungeon);
            mapView.setText(mapData);
        }
    }


}
