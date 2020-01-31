package com.example.androidarchitecturedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    // These constants are used for passing data between activities via Intent
    public static final String EXTRA_ID = "com.example.androidarchitecturedemo.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.androidarchitecturedemo.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.androidarchitecturedemo.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.androidarchitecturedemo.EXTRA_PRIORITY";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_prioirty);

        // Cannot set min and max in XML
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)) { // Checks and handles if request is for EDIT
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        }
        else {
            setTitle("Add Note");
        }
    }

    private void saveNote() {
        int id;
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        id = getIntent().getIntExtra(EXTRA_ID, -1);

        if(id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
