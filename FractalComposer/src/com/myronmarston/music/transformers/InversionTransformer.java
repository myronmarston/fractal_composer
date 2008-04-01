package com.myronmarston.music.transformers;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;

/**
 * Transformer that applies inversion to the given note list.
 * Example: C5 G5 D5 C5 -> C5 F4 B4 C5
 * 
 * @author Myron
 */
public class InversionTransformer implements Transformer {
        
    public NoteList transform(NoteList input) {
        Note newNote;
        Note firstNote = input.getFirstAudibleNote();
        NoteList output = new NoteList(input.size());
        
        for (Note inputNote : input) {
            newNote = new Note(inputNote);
            newNote.setScaleStep(firstNote.getScaleStep() - newNote.getScaleStep());
            
            output.add(newNote);
        }
        
        return output;
    }
}
