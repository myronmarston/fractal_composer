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
            
            if (!newNote.isRest()) {
                // invert the scale step...
                newNote.setScaleStep(firstNote.getScaleStep() - newNote.getScaleStep());

                // invert the octave.  This sets the octave to be equally distant from 
                // the first note's octave, but in the opposite direction.
                newNote.setOctave(firstNote.getOctave() + (firstNote.getOctave() - newNote.getOctave()));

                // invert the chromatic adjustment...
                newNote.setChromaticAdjustment(newNote.getChromaticAdjustment() * -1);
            }
            
            output.add(newNote);
        }
        
        return output;
    }
}
