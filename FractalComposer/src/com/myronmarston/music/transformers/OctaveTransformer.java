package com.myronmarston.music.transformers;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;

/**
 * Transforms the given NoteList by moving it up or down some number of octaves.
 * Example: C5 G5 D5 C5 -> C3 G3 D3 C3
 * 
 * @author Myron
 */
public class OctaveTransformer implements Transformer {
    private int octaveChange;
    
    /**
     * Constructor.
     * 
     * @param octaveChange the number of octaves to transform
     */
    public OctaveTransformer(int octaveChange) {
        this.octaveChange = octaveChange;
    }
    
    /**
     * Gets the number of octaves to move the given NoteList.  Positive values 
     * move it up; negative values more it down.
     * 
     * @return the number of octaves to transform
     */
    public int getOctaveChange() {
        return octaveChange;
    }
    
    public NoteList transform(NoteList input) {
        Note newNote;
        NoteList output = new NoteList(input.size());
        
        for (Note inputNote : input) {
            newNote = new Note(inputNote);
                        
            if (!newNote.isRest()) { // don't change the octave on a rest...
                newNote.setOctave(newNote.getOctave() + this.getOctaveChange());
            }                        
            
            output.add(newNote);
        }
        
        return output;
    }
}
