package com.myronmarston.music.transformers;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;

/**
 *
 * @author Myron
 */
public class OctaveTransformer implements NoteListTransformer {
    private int octaveChange;
    
    public OctaveTransformer(int octaveChange) {
        this.octaveChange = octaveChange;
    }
    
    public int getOctaveChange() {
        return octaveChange;
    }
    
    public NoteList transform(NoteList input) {
        Note newNote;
        NoteList output = new NoteList(input.size());
        
        for (Note inputNote : input) {
            newNote = new Note(inputNote);
            newNote.setOctave(newNote.getOctave() + this.getOctaveChange());
            
            output.add(newNote);
        }
        
        return output;
    }
}
