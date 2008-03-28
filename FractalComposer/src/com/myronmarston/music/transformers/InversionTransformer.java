package com.myronmarston.music.transformers;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;

/**
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
