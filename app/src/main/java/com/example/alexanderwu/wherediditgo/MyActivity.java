package com.example.alexanderwu.wherediditgo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MyActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    // ### Do I need these?
    //public final static String PREFS_NAME = "MyPrefsFile";
    //public final static String EXTRA_MESSAGE = "com.example.alexanderwu.wherediditgo.MESSAGE";
    public static final String PREF = "Prefs";

    public static final String NOTE_KEY = "NoteKey";
    public static final String COUNT_KEY = "Count";
    SharedPreferences mSharedPreferences;

    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // Get Handle to UI elements
        ListView mainListView = (ListView) findViewById(R.id.listView);

        // ### Working on this part:
        // Create an ArrayAdapter for the ListView
        mArrayAdapter = new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1,
                        mNameList);
        mainListView.setAdapter(mArrayAdapter);

        mSharedPreferences = getSharedPreferences(PREF, MODE_PRIVATE);

        loadData();

        mainListView.setOnItemClickListener(this);
    }
    // ###
    public void loadData() {
        String note = mSharedPreferences.getString(NOTE_KEY, "Bike");
        int count = mSharedPreferences.getInt(COUNT_KEY,0);
        mNameList.add(note);
        mArrayAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), count + " notes saved!", Toast.LENGTH_SHORT).show();
    }
    // ###

    //public void addNote() {}

    public void newMessage(View view) {
        // Dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Hello");
        alert.setMessage("Where is ...");
        // Create EditText for entry
        final EditText input = new EditText(this);
        alert.setView(input);
        // Make an "OK" button to save the name
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Grab the EditText's input
                String inputName = input.getText().toString();
                // Put it into memory (don't forget to commit!)
                SharedPreferences.Editor e = mSharedPreferences.edit();
                e.putString(NOTE_KEY,inputName);
                if(e.commit()) {
                    Toast.makeText(getApplicationContext(), inputName + " saved!", Toast.LENGTH_SHORT).show();
                    mNameList.add(inputName);
                    mArrayAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), inputName + " not saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Make a "Cancel" button that simply dismisses the alert
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });
        alert.show();

        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        //startActivity(intent);
    }

    public void deleteNotes(View view) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.clear();
        e.commit();
        mArrayAdapter.clear();
        mArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String noteName = mNameList.get(position).toString();
        // Log the item's position and contents to the console in Debug
        Log.d("omg android", position + ": " + noteName);
        Toast.makeText(getApplicationContext(), noteName + " clicked!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra("title",noteName);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
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
