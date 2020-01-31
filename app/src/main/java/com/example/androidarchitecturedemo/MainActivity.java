package com.example.androidarchitecturedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1; // Used to differ which request is going out
    public static final int EDIT_NOTE_REQUEST = 2;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FAB configuration
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        // Similar to React Native's FlatList. Simply used to render lists of data
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class); // Deprecated implementation
        /*
            Tutorial showed the way to use ViewModelProviders, that has since been deprecated.
            Found an example on how to implement using ViewModelProvider below. Solution found on SO
            https://stackoverflow.com/questions/54677426/unable-to-open-activity-cannot-create-instance-of-android-view-model
         */
        //noteViewModel = ViewModelProviders.of(this, new NoteViewModelFactory(this.getApplication())).get(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    // Handles the result from the add note layout.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int id;
        String title;
        String description;
        int priority;

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);
            
            if(id == -1){
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            Note note = new Note(title, description, priority);
            note.setId(id);
            noteViewModel.update(note);

        }
        else{
            Toast.makeText(this, "Note Not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    // App bar icon handlers
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;
             default:
                 return super.onOptionsItemSelected(item);
        }
    }
}
