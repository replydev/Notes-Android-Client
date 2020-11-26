package me.replydev.notes_android.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.replydev.notes_android.R;
import me.replydev.notes_android.json.Note;

import java.util.ArrayList;

public class NotesAdapter extends ArrayAdapter<Note> {
    private final Context context;
    private final ArrayList<Note> notes;
    public NotesAdapter(@NonNull Context context, int resource,ArrayList<Note> notes) {
        super(context, resource,notes);
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Note n = notes.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.note_list_item,parent,false);
        }

        TextView notesTitleText = convertView.findViewById(R.id.noteTitleTextView);
        //TextView notesBodyText = convertView.findViewById(R.id.notesBodyText);

        notesTitleText.setText(n.getTitle());
        //notesBodyText.setText(n.getBody());

        return convertView;
    }
}
