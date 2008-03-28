package com.myronmarston.music.transformers;

import com.myronmarston.music.MidiNote;
import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;
import com.myronmarston.music.settings.SelfSimilaritySettings;

/**
 *
 * @author Myron
 */
public class SelfSimilarityTransformer implements Transformer {   
    private SelfSimilaritySettings settings;        

    public SelfSimilaritySettings getSettings() {
        if (settings == null) settings = new SelfSimilaritySettings(true, true, true);
        return this.settings;
    }
    
    public SelfSimilarityTransformer(SelfSimilaritySettings settings) {
        this.settings = settings;
    }         
    
    public SelfSimilarityTransformer(boolean applyToPitch, boolean applyToRhythm, boolean applyToVolume) {
        this(new SelfSimilaritySettings(applyToPitch, applyToRhythm, applyToVolume));
    }
    
    public SelfSimilarityTransformer() {};
    
    public NoteList transform(NoteList input) {                             
        Note firstNote = input.getFirstAudibleNote(); // the note we will compare against for the self-similarity
        NoteList transformedList; // used to store the temporary results of the transformations                
        NoteList output = new NoteList(input.size() * input.size()); // the final output
        
        for (Note inputNote : input) {                        
            transformedList = transform_pitch(input, firstNote, inputNote);
            transformedList = transform_rhythm(transformedList, firstNote, inputNote);
            transformedList = transform_volume(transformedList, firstNote, inputNote);
                       
            output.addAll(transformedList);
        }
        
        return output;
    }

    protected NoteList transform_pitch(NoteList input, Note firstNote, Note inputNote) {
        if (!this.getSettings().getApplyToPitch()) return input;
                                   
        // transpose the input to the correct octave...
        OctaveTransformer octaveTransformer = new OctaveTransformer(inputNote.getOctave() - firstNote.getOctave());
        NoteList octaveTransformedList = octaveTransformer.transform(input);

        // transpose the input to the correct pitch level...
        TransposeTransformer transposer = new TransposeTransformer(inputNote.getScaleStep() - firstNote.getScaleStep());
        return transposer.transform(octaveTransformedList);
    }

    protected NoteList transform_rhythm(NoteList input, Note firstNote, Note inputNote) {
        if (!this.getSettings().getApplyToRhythm()) return input;                
        
        // scale the rhythm...
        assert (firstNote.getDuration() > 0) : firstNote.getDuration(); // we would get div-by-zero below if the duration is zero, and less than zero is nonsensical
        RhythmicDurationTransformer rhythmScaler = new RhythmicDurationTransformer(inputNote.getDuration() / firstNote.getDuration());
        return rhythmScaler.transform(input);
    }
    
    protected NoteList transform_volume(NoteList input, Note firstNote, Note inputNote) {
        if (!this.getSettings().getApplyToVolume()) return input;
                        
        int remainingVolumeRange = // get the above or below volume range based on the volume of the current note relative to the first note
            (inputNote.getVolume() > firstNote.getVolume()) ? 
            MidiNote.MAX_VELOCITY - firstNote.getVolume() :
            firstNote.getVolume() - MidiNote.MIN_VELOCITY;
        
        // if there's no volume range left to use, we have no way to scale it...
        if (remainingVolumeRange == 0) return input;
        
        double scaleFactor = (inputNote.getVolume() - firstNote.getVolume()) / (double) remainingVolumeRange;
        VolumeTransformer volumeScaler = new VolumeTransformer(scaleFactor);
        return volumeScaler.transform(input);        
    }
}
