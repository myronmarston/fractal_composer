package com.myronmarston.music.transformers;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;

/**
 * Transformer that transposes the notes of the NoteList to a different pitch
 * level.  This does not imply a key change as the term "transpose" is normally
 * used in music theory.  Rather, each note in the given NoteList is moved the
 * same number of scale steps to a different pitch level.  
 * Example: G5 A5 B5 G5 -> A5 B5 C6 A5
 * 
 * @author Myron
 */
public class TransposeTransformer implements Transformer {   
    private int transposeSteps;
    
    /**
     * Gets the number of scale steps to transpose the given NoteList.
     * 
     * @return the transpose steps
     */
    public int getTransposeSteps() {
        return transposeSteps;
    }
    
    /**
     * Constructor.
     * 
     * @param transposeSteps the number of scale steps to transpose the given NoteList
     */
    public TransposeTransformer(int transposeSteps) {
        this.transposeSteps = transposeSteps;
    }
    
    public NoteList transform(NoteList input) {
        Note newNote;
        NoteList output = new NoteList(input.size());
        
        for (Note inputNote : input) {
            newNote = new Note(inputNote);
            newNote.setScaleStep(newNote.getScaleStep() + this.getTransposeSteps());
            
            output.add(newNote);
        }
        
        return output;
    }
}
