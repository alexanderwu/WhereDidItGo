package com.example.alexanderwu.wherediditgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditMessageActivity extends ActionBarActivity {
    public static final String PREF = "Prefs";

    public static final String CURRENT_POS = "CurrentPosition";
    public static final String MESSAGE_KEY = "MessageKey";

    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);

        EditText editText = (EditText) findViewById(R.id.edit_message);

        mSharedPreferences = getSharedPreferences(PREF, MODE_PRIVATE);

        int currentPosition = mSharedPreferences.getInt(CURRENT_POS,-1);
        if(currentPosition == -1)
            Toast.makeText(getApplicationContext(), "CURRENT_POS not found!", Toast.LENGTH_SHORT).show();

        // Put saved message in editText
        String message = mSharedPreferences.getString(MESSAGE_KEY + currentPosition,"");
        editText.setText(message);
    }

    public void saveMessage(View view) {
        // Load message onto editText
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        int currentPosition = mSharedPreferences.getInt(CURRENT_POS,-1);
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putString(MESSAGE_KEY + currentPosition, message);
        e.commit();

        // Go back to DisplayMessageActivity
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
