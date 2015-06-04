package com.example.alexanderwu.wherediditgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayMessageActivity extends ActionBarActivity {
    public static final String PREF = "Prefs";

    public static final String CURRENT_POS = "CurrentPosition";
    public static final String NOTE_KEY= "NoteKey";
    public static final String MESSAGE_KEY = "MessageKey";

    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        TextView titleView = (TextView) findViewById(R.id.note_title);
        TextView textView = (TextView) findViewById(R.id.display_message);

        mSharedPreferences = getSharedPreferences(PREF, MODE_PRIVATE);

        int currentPosition = mSharedPreferences.getInt(CURRENT_POS,-1);
        if(currentPosition == -1)
            Toast.makeText(getApplicationContext(), "CURRENT_POS not found!", Toast.LENGTH_SHORT).show();

        //Set Title
        String title = mSharedPreferences.getString(NOTE_KEY + currentPosition,"Title");
        titleView.setText(title);

        // Load my message
        String message = mSharedPreferences.getString(MESSAGE_KEY + currentPosition,"");
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
