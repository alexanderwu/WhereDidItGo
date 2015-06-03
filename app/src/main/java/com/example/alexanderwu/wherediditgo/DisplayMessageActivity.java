package com.example.alexanderwu.wherediditgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayMessageActivity extends ActionBarActivity {
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get Handle to UI elements
        TextView title = (TextView) findViewById(R.id.note_title);
        TextView textView = (TextView) findViewById(R.id.display_message);
        Button editButton = (Button) findViewById(R.id.edit_button);

        // Load title
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("title");
            title.setText(value);
        }

        // Load my message
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String message = settings.getString("myNote","");
        textView.setText(message);

        //Create the editButton
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditMessageActivity.class);
                startActivity(intent);
            }
        });
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
