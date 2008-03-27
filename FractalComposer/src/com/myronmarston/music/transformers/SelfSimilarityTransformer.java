package com.myronmarston.music.transformers;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;

/**
 *
 * @author Myron
 */
public class SelfSimilarityTransformer implements Transformer {   
    private boolean applyToRhythm;        

    public boolean doApplyToRhythm() {
        return applyToRhythm;
    }
    
    public SelfSimilarityTransformer(boolean applyToRhythm) {
        this.applyToRhythm = applyToRhythm;
    }         
    
    public NoteList transform(NoteList input) {                             
        Note firstNote = input.getFirstAudibleNote(); // the note we will compare against for the self-similarity
        NoteList transformedList; // used to store the temporary results of the transformations
        OctaveTransformer octaveTransformer; // used to transpose the input to the correct octave...
        TransposeTransformer transposer; // used to transpose the input to a new pitch level
        RhythmicDurationTransformer rhythmScaler; // used to scale the rhythm of the transposedList        
        NoteList output = new NoteList(input.size() * input.size()); // the final output
        
        for (Note inputNote : input) {
            // first, transpose the input to the correct octave...
            octaveTransformer = new OctaveTransformer(inputNote.getOctave() - firstNote.getOctave());
            transformedList = octaveTransformer.transform(input);
            
            // second, transpose the input to the correct pitch level...
            transposer = new TransposeTransformer(inputNote.getScaleStep() - firstNote.getScaleStep());
            transformedList = transposer.transform(transformedList);
            
            // third, scale the rhythm if we're applying self-similarity to it...
            if (this.doApplyToRhythm()) {
                assert (firstNote.getDuration() > 0) : firstNote.getDuration();
                rhythmScaler = new RhythmicDurationTransformer(inputNote.getDuration() / firstNote.getDuration());
                transformedList = rhythmScaler.transform(transformedList);
            }            

            output.addAll(transformedList);
        }
        
        return output;
    }
}
