package com.myronmarston.music.transformers;

import com.myronmarston.music.MidiNote;
import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;
import com.myronmarston.music.settings.SegmentSettings;
import com.myronmarston.music.settings.SelfSimilaritySettings;

/**
 * Transformer that applies the self-similarity algorithm to the given NoteList.
 * This can apply to the pitch, rhythm and/or volume, depending on the 
 * SelfSimilaritySettings.  
 * Example: G5 A5 B5 G5 -> G5 A5 B5 G5, A5 B5 C6 A5, B5 C6 D6 B5, G5 A5 B5 G5
 * 
 * @author Myron
 */
public class SelfSimilarityTransformer implements Transformer {   
    private SelfSimilaritySettings settings;        

    /**
     * Gets the self-similarity settings to be used by this transformer.  
     * Guarenteed to never be null.  The settings determine whether or not 
     * self-similarity is applied to the pitch, rhythm and/or volume.
     * 
     * @return the self-similarity settings
     */
    public SelfSimilaritySettings getSettings() {
        if (settings == null) settings = new SelfSimilaritySettings(true, true, true);
        return this.settings;
    }
    
    /**
     * Constructor.
     * 
     * @param settings the self-similarity settings to use
     */
    public SelfSimilarityTransformer(SelfSimilaritySettings settings) {
        this.settings = settings;
    }         
    
    /**
     * Constructor.  Use this constructor to provide values for the 
     * self-similarity settings.
     * 
     * @param applyToPitch true to apply self-similarity to the pitch
     * @param applyToRhythm true to apply self-similarity to the rhythm
     * @param applyToVolume true to apply self-similarity to the volume
     */
    public SelfSimilarityTransformer(boolean applyToPitch, boolean applyToRhythm, boolean applyToVolume) {
        this(new SelfSimilaritySettings(applyToPitch, applyToRhythm, applyToVolume));
    }
    
    /**
     * Default Constructor.
     */
    public SelfSimilarityTransformer() {};
    
    public NoteList transform(NoteList input) {        
        if (!this.getSettings().getApplyToPitch() &&
            !this.getSettings().getApplyToRhythm() &&
            !this.getSettings().getApplyToVolume()) {
            // there is no self-similarity, so just return a copy of the input
            
            CopyTransformer ct = new CopyTransformer();
            return ct.transform(input);
        }            
        
        Note firstNote = input.getFirstAudibleNote(); // the note we will compare against for the self-similarity
        NoteList transformedList; // used to store the temporary results of the transformations                
        NoteList output = new NoteList(input.size() * input.size()); // the final output
                
        for (Note inputNote : input) {                  
            if (inputNote.isRest()) {
                transformedList = new NoteList();
                transformedList.add(Note.createRest(input.getDuration()));                
                transformedList = transform_rhythm(transformedList, firstNote, inputNote);
            } else {
                transformedList = transform_pitch(input, firstNote, inputNote);
                transformedList = transform_rhythm(transformedList, firstNote, inputNote);
                transformedList = transform_volume(transformedList, firstNote, inputNote);
            }
            output.addAll(transformedList);            
        }
        
        return output;
    }

    private NoteList transform_pitch(NoteList input, Note firstNote, Note inputNote) {
        if (!this.getSettings().getApplyToPitch()) return input;
                                                   
        // transpose the input to the correct octave...
        OctaveTransformer octaveTransformer = new OctaveTransformer(inputNote.getOctave() - firstNote.getOctave());
        NoteList octaveTransformedList = octaveTransformer.transform(input);

        // transpose the input to the correct pitch level...        
        TransposeTransformer transposer = new TransposeTransformer(inputNote.getScaleStep() - firstNote.getScaleStep());
        NoteList transposedList = transposer.transform(octaveTransformedList);

        // set the chromatic adjustment on this note segment as necessary...
        SegmentSettings segmentSettings = new SegmentSettings(inputNote.getChromaticAdjustment() - firstNote.getChromaticAdjustment());
        for (Note n : transposedList) n.setSegmentSettings(segmentSettings);
        
        return transposedList;
    }

    private NoteList transform_rhythm(NoteList input, Note firstNote, Note inputNote) {
        if (!this.getSettings().getApplyToRhythm()) return input;                
        
        // scale the rhythm...
        assert (inputNote.getDuration().compareTo(0) > 0) : inputNote.getDuration(); // we would get div-by-zero below if the duration is zero, and less than zero is nonsensical
        RhythmicDurationTransformer rhythmScaler = new RhythmicDurationTransformer(firstNote.getDuration().dividedBy(inputNote.getDuration()));
        return rhythmScaler.transform(input);
    }
    
    private NoteList transform_volume(NoteList input, Note firstNote, Note inputNote) {
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
