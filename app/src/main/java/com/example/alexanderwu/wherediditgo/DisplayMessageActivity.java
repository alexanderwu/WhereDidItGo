package com.example.alexanderwu.wherediditgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayMessageActivity extends ActionBarActivity {
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_display_message);

        // Create LinearLayout
        LinearLayout linLayout = new LinearLayout(this);
        linLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setContentView(linLayout, linLayoutParam);

        // Get the message from the intent
        //Intent intent = getIntent();
        //String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

        // Create the text view
        TextView textView = new TextView(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String message = settings.getString("myNote","");

        textView.setTextSize(30);
        textView.setText(message);
        textView.setPadding(30,30,30,30);
        linLayout.addView(textView);

        //Create the editButton
        Button editButton = new Button(this);
        editButton.setText("Edit Message");
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditMessageActivity.class);
                startActivity(intent);
            }
        });
        linLayout.addView(editButton);

        // Set the linLayout as the activity layout
        setContentView(linLayout);
    }

    // Edit Message

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
