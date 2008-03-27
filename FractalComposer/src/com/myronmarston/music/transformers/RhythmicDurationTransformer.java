package com.myronmarston.music.transformers;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;

/**
 *
 * @author Myron
 */
public class RhythmicDurationTransformer implements Transformer {  
    private double scaleFactor;

    public double getScaleFactor() {
        return scaleFactor;
    }
    
    public RhythmicDurationTransformer(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
    
     public NoteList transform(NoteList input) {
        Note newNote;
        NoteList output = new NoteList(input.size());
        
        for (Note inputNote : input) {
            newNote = new Note(inputNote);
            newNote.setDuration(newNote.getDuration() * this.getScaleFactor());            
            
            output.add(newNote);
        }
        
        return output;
    }
}
