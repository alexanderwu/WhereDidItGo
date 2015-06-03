package com.example.alexanderwu.wherediditgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DisplayMessageActivity extends ActionBarActivity {
    public static final String PREF = "Prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        TextView title = (TextView) findViewById(R.id.note_title);
        TextView textView = (TextView) findViewById(R.id.display_message);

        // Load title
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("title");
            title.setText(value);
        }

        // Load my message
        SharedPreferences settings = getSharedPreferences(PREF, MODE_PRIVATE);
        String message = settings.getString("myNote","");
        textView.setText(message);

    }

    public void editMessage(View view) { // Clicking the "Edit Message" button
        Intent intent = new Intent(view.getContext(), EditMessageActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
