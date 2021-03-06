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

    public static final int LIST_SIZE = 20;

    public static final String PREF = "Prefs";
    public static final String COUNT_KEY = "Count";

    public static final String CURRENT_POS = "CurrentPosition";
    public static final String MESSAGE_KEY = "MessageKey";
    public static final String NOTE_KEY= "NoteKey";
    public static final String IMAGE_PATH_KEY = "ImagePath";

    SharedPreferences mSharedPreferences;

    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        ListView mainListView = (ListView) findViewById(R.id.listView);

        // Create an ArrayAdapter for the ListView
        mArrayAdapter = new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1,
                        mNameList);
        mainListView.setAdapter(mArrayAdapter);

        mSharedPreferences = getSharedPreferences(PREF, MODE_PRIVATE);

        loadData();

        mainListView.setOnItemClickListener(this);
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int positionToRemove = position;
                String noteName = mNameList.get(position).toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
                builder.setTitle("Delete note").setMessage("Delete " + noteName + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                int count = mSharedPreferences.getInt(COUNT_KEY, 0);
                                SharedPreferences.Editor e = mSharedPreferences.edit();
                                for (int i = positionToRemove + 1; i < count; i++) {
                                    String note = mSharedPreferences.getString(NOTE_KEY + Integer.toString(i+1), "Title");
                                    String message = mSharedPreferences.getString(MESSAGE_KEY + Integer.toString(i+1), "");
                                    String image_path = mSharedPreferences.getString(IMAGE_PATH_KEY + Integer.toString(i+1), "invalid");
                                    e.putString(NOTE_KEY + i, note);
                                    e.putString(MESSAGE_KEY + i, message);
                                    e.putString(IMAGE_PATH_KEY + i, image_path);
                                }
                                e.putString(NOTE_KEY + count,"");
                                e.putString(MESSAGE_KEY + count,"");
                                e.putString(IMAGE_PATH_KEY + count,"invalid");
                                e.putInt(COUNT_KEY,count-1);
                                if (e.commit()) {
                                    mNameList.remove(positionToRemove);
                                    mArrayAdapter.notifyDataSetChanged();
                                } else {
                                    //Toast.makeText(getApplicationContext(), "Unable to remove", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
                return false;
            }
        });
     }

    public void loadData() { // from OnCreate
        int count = mSharedPreferences.getInt(COUNT_KEY,0);
        for(int i=1; i<=count; i++) {
            String note = mSharedPreferences.getString(NOTE_KEY + Integer.toString(i),"failed");
            mNameList.add(note);
        }
        mArrayAdapter.notifyDataSetChanged();
    }

    public void newMessage(View view) { // from pressing "New Note" Button
        int count = mSharedPreferences.getInt(COUNT_KEY,0);
        if(count >= LIST_SIZE) {
            Toast.makeText(getApplicationContext(), "List is full", Toast.LENGTH_SHORT).show();
            return;
        }
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
                if(!inputName.equals("")) { // Ignore if no input
                    int count = mSharedPreferences.getInt(COUNT_KEY,0);
                    count++;
                    // Puts it into memory
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putInt(COUNT_KEY,count);
                    e.putString(NOTE_KEY + count,inputName);
                    if(e.commit()) {
                        Toast.makeText(getApplicationContext(), count + ": " + inputName + " saved!", Toast.LENGTH_SHORT).show();
                        mNameList.add(inputName);
                        mArrayAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), inputName + " not saved!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });
        alert.show();
    }

    public void deleteNotes(View view) { // from pressing "Reset" button
        AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
        builder.setTitle("Reset").setMessage("Are you sure?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor e = mSharedPreferences.edit();
                e.clear();
                e.commit();
                mArrayAdapter.clear();
                mArrayAdapter.notifyDataSetChanged();
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String noteName = mNameList.get(position).toString();
        // Log the item's position and contents to the console in Debug
        Log.d("WhereIsIt ", position + ": " + noteName);

        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putInt(CURRENT_POS,position + 1); // + 1 because list position starts count at 0
        if(e.commit()) {
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),"Failure!!!", Toast.LENGTH_SHORT).show();
        }
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
