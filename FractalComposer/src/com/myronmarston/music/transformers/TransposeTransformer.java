package com.myronmarston.music.transformers;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;

/**
 *
 * @author Myron
 */
public class TransposeTransformer implements Transformer {   
    private int transposeSteps;
    
    public int getTransposeSteps() {
        return transposeSteps;
    }
    
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
