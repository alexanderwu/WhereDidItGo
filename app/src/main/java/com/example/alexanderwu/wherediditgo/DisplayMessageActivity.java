package com.example.alexanderwu.wherediditgo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class DisplayMessageActivity extends ActionBarActivity {
    public static final String PREF = "Prefs";

    public static final String CURRENT_POS = "CurrentPosition";
    public static final String NOTE_KEY= "NoteKey";
    public static final String MESSAGE_KEY = "MessageKey";
    public static final String IMAGE_PATH_KEY = "ImagePath";

    SharedPreferences mSharedPreferences;

    private File imageFile;
    ImageView viewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        TextView titleView = (TextView) findViewById(R.id.note_title);
        TextView textView = (TextView) findViewById(R.id.display_message);
        viewImage = (ImageView) findViewById(R.id.viewImage);

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

        loadImage();
    }

    public void loadImage() {
        int currentPosition = mSharedPreferences.getInt(CURRENT_POS,-1);
        String filePath = mSharedPreferences.getString(IMAGE_PATH_KEY + currentPosition,"invalid");
        if(!filePath.equals("invalid") ) {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, bitmapOptions);

            viewImage.setImageBitmap(bitmap);
        } else {

            Toast.makeText(this, "Error: " + filePath, Toast.LENGTH_LONG).show();
        }
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

    public void selectImage(View view) {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayMessageActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    int currentPosition = mSharedPreferences.getInt(CURRENT_POS,-1);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    imageFile = new File(
                            Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                            "temp" + currentPosition + ".jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) { // Take Photo
                if(imageFile.exists()) {
                    String picturePath = imageFile.getAbsolutePath();

                    int currentPosition = mSharedPreferences.getInt(CURRENT_POS,-1);
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(IMAGE_PATH_KEY + currentPosition, picturePath);
                    if(e.commit()) {
                        Toast.makeText(this, "This file was saved at "+ picturePath, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Image not saved!", Toast.LENGTH_SHORT).show();
                    }

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath, bitmapOptions);
                    viewImage.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(this, "There was an error saving the file", Toast.LENGTH_LONG).show();
                }


            } else if (requestCode == 2) { // Select Image
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();

                int currentPosition = mSharedPreferences.getInt(CURRENT_POS,-1);
                SharedPreferences.Editor e = mSharedPreferences.edit();
                e.putString(IMAGE_PATH_KEY + currentPosition, picturePath);
                if(e.commit()) {
                    Toast.makeText(this, "This file was saved at "+ picturePath, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Image not saved!", Toast.LENGTH_SHORT).show();
                }

                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath, bitmapOptions);
                viewImage.setImageBitmap(bitmap);
            }
        }
    }
}
