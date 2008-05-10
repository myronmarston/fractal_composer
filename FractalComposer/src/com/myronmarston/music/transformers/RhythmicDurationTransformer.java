package com.myronmarston.music.transformers;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;

import com.myronmarston.util.Fraction;

/**
 * Transformer that scales the the rhythmic duration of the notes by some scale
 * factor.
 * Example: 1/4 1/8 1/4 -> 1/2 1/4 1/2
 * 
 * @author Myron
 */
public class RhythmicDurationTransformer implements Transformer {  
    private Fraction scaleFactor;

    /**
     * Gets the scale factor.  Factors less than 1 will increase the speed of
     * the NoteList; factors greater than 1 will slow it down.
     * 
     * @return the scale factor
     */
    public Fraction getScaleFactor() {        
        return scaleFactor;
    }
    
    /**
     * Constructor.
     * 
     * @param scaleFactor the scale factor. Factors greater than 1 will increase 
     *        the speed of the NoteList; factors less than 1 will slow it 
     *        down.
     */
    public RhythmicDurationTransformer(Fraction scaleFactor) {        
        if (scaleFactor.compareTo(0) <= 0) throw new IllegalArgumentException("The scaleFactor must be greater than 0.");
        this.scaleFactor = scaleFactor;
    }
    
     public NoteList transform(NoteList input) {
        Note newNote;
        NoteList output = new NoteList(input.size());
        
        for (Note inputNote : input) {
            newNote = new Note(inputNote);
            
            // if the scale factor is zero, we'll get a div-by-zero exception.  
            // our code should prevent it from ever reaching here if it's zero...
            assert this.getScaleFactor().asDouble() != 0d : this.getScaleFactor();
            newNote.setDuration(newNote.getDuration().dividedBy(this.getScaleFactor()));            
            
            output.add(newNote);
        }
        
        return output;
    }
}
