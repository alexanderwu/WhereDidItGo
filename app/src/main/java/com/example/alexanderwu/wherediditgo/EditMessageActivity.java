package com.example.alexanderwu.wherediditgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditMessageActivity extends ActionBarActivity {
    public static final String PREF= "Prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);
        // Get your saved message
        SharedPreferences settings = getSharedPreferences(PREF, 0);
        String message = settings.getString("myNote","");
        // Put message in editText
        EditText editText = (EditText) findViewById(R.id.edit_message);
        editText.setText(message);
    }

    public void saveMessage(View view) {
        // Load message onto editText
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences myNotes = getSharedPreferences(PREF, 0);
        SharedPreferences.Editor editor = myNotes.edit();
        editor.putString("myNote",message);
        // Commit the edits!
        editor.commit();

        // Go back to DisplayMessageActivity
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);
    }

    public void viewMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
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
