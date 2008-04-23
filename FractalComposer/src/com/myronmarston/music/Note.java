package com.myronmarston.music;

import com.myronmarston.music.settings.SegmentSettings;
import com.myronmarston.music.scales.Scale;

import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;

import java.util.Arrays;

/**
 * Represents a note, relative to a particular scale.  This note will need to be
 * converted to midi events using the MidiNote class before it can be used in
 * a Midi track.  
 * 
 * @author Myron
 */
public class Note {   
    private int scaleStep; // number of scale steps above the tonic; 0 = tonic, 7 = octave, 9 = third an octave above, etc.
    private int octave; // which octave the note should be in.  0 begins the first octave in Midi that contains the tonic.
    private int chromaticAdjustment; // the number of half steps to adjust from the diatonic note; used if this note is an accidental
    private Fraction duration; // how long the note should last, in whole notes.
    private int volume = MidiNote.DEFAULT_VELOCITY; // how loud the note should be on a scale from 0 to 127.    
    private SegmentSettings segmentSettings; // an object containing settings to apply to a segment of notes
    
    /**
     * Default constructor.
     */
    public Note() {}
                
    /**
     * Constructor.
     * 
     * @param scaleStep number of scale steps above the tonic; 0 = tonic, 
     *        7 = octave, 9 = third an octave above, etc.
     * @param octave which octave the note should be in.  0 begins the first 
     *        octave in Midi that contains the tonic.
     * @param chromaticAdjustment the number of half steps to adjust from the
     *        diatonic note; used if this note is an accidental
     * @param duration how long the note should last, in whole notes
     * @param volume how loud the note should be (0-127)
     */
    public Note(int scaleStep, int octave, int chromaticAdjustment, Fraction duration, int volume) {
        this.setScaleStep(scaleStep);
        this.setOctave(octave);
        this.setChromaticAdjustment(chromaticAdjustment);
        this.setDuration(duration);
        this.setVolume(volume);        
    }
    
    /**
     * Constructor.
     * 
     * @param scaleStep number of scale steps above the tonic; 0 = tonic, 
     *        7 = octave, 9 = third an octave above, etc.
     * @param octave which octave the note should be in.  0 begins the first 
     *        octave in Midi that contains the tonic.
     * @param chromaticAdjustment the number of half steps to adjust from the
     *        diatonic note; used if this note is an accidental
     * @param duration how long the note should last, in whole notes
     * @param volume how loud the note should be (0-127)
     * @param segmentSettings additional settings for a group of notes this note
     *        is a part of
     */
    public Note(int scaleStep, int octave, int chromaticAdjustment, Fraction duration, int volume, SegmentSettings segmentSettings) {
        this(scaleStep, octave, chromaticAdjustment, duration, volume);
        this.setSegmentSettings(segmentSettings);               
    }        
    
    /**
     * Copy Constructor.
     * 
     * @param inputNote note to copy     
     */
    public Note(Note inputNote) {
        this(inputNote.getScaleStep(), inputNote.getOctave(), inputNote.getChromaticAdjustment(), inputNote.getDuration(), inputNote.getVolume(), inputNote.getSegmentSettings());
    }
        
    /**
     * Creates a note that is a rest.
     * 
     * @param duration how long the rest should last, in whole notes
     * @return the rest     
     */
    static public Note createRest(Fraction duration) {
        Note rest = new Note();
                
        rest.setDuration(duration); // use the setter so we get an exception if the duration is 0
        rest.setVolume(MidiNote.MIN_VELOCITY);              
        
        assert rest.isRest() : rest;
        return rest;
    }   

    /**
     * Gets the number of scale steps above the tonic; In an 7-note scale, 
     * 0 = tonic, 7 = octave, 9 = third an octave above, etc.
     * 
     * @return the scale step
     */
    public int getScaleStep() {
        return scaleStep;
    }

    /**
     * Sets the number of scale steps above the tonic; In an 7-note scale, 
     * 0 = tonic, 7 = octave, 9 = third an octave above, etc.
     * 
     * @param scaleStep the scale step
     * @throws UnsupportedOperationException if the note is a rest
     */
    public void setScaleStep(int scaleStep) {
        throwUnsupportedOperationExceptionIfRest("scaleStep", scaleStep);
        this.scaleStep = scaleStep;        
    }

    /**
     * Gets which octave the note should be in.  0 begins the first octave in 
     * Midi that contains the tonic.
     * 
     * @return the octave
     */
    public int getOctave() {
        return octave;
    }

    /**
     * Sets which octave the note should be in.  0 begins the first octave in 
     * Midi that contains the tonic.
     * 
     * @param octave the octave
     */
    public void setOctave(int octave) {
        throwUnsupportedOperationExceptionIfRest("octave", octave);
        this.octave = octave;        
    }

    /**
     * Gets the number of half steps to adjust from the diatonic note; used if 
     * this note is an accidental.
     * 
     * @return the chromatic adjustment
     */
    public int getChromaticAdjustment() {
        return chromaticAdjustment;
    }

    /**
     * Sets the number of half steps to adjust from the diatonic note; used if 
     * this note is an accidental.
     * 
     * @param chromaticAdjustment the chromatic adjustment
     */
    public void setChromaticAdjustment(int chromaticAdjustment) {
        throwUnsupportedOperationExceptionIfRest("chromaticAdjustment", chromaticAdjustment);
        this.chromaticAdjustment = chromaticAdjustment;
    }

    /**
     * Gets the settings for the segment this note is a part of.
     * 
     * @return the segment settings
     */
    public SegmentSettings getSegmentSettings() {
        return segmentSettings;
    }

    /**
     * Sets the settings for the segment this note is a part of.
     * 
     * @param segmentSettings the segment settings
     */
    public void setSegmentSettings(SegmentSettings segmentSettings) {
        this.segmentSettings = segmentSettings;
    }    
    
    /**
     * Gets the chromatic adjustment on the segment settings, or the default 
     * chromatic adjustment if there are no settings.
     * 
     * @return the chromatic adjustment on the segment settings
     */
    private int getSegmentSettingsChromaticAdjustment() {
        if (this.getSegmentSettings() == null) return 0;
        return this.getSegmentSettings().getChromaticAdjustment();
    }
    
    /**
     * Gets how long the note should last, in whole notes.
     * 
     * @return the duration
     */
    public Fraction getDuration() {
        return duration;
    }       

    /**
     * Sets how long the note should last, in whole notes.
     * 
     * @param duration the duration
     */
    public void setDuration(Fraction duration) {
        if (duration.asDouble() <= 0) throw new IllegalArgumentException("The duration must be greater than zero.");
        this.duration = duration;
    }

    /**
     * Gets how loud the note should be (0-127).  0 makes it a rest.
     * 
     * @return the volume
     */
    public int getVolume() {
        return volume;
    }
    
    /**
     * Sets how loud the note should be (0-127).  0 makes it a rest.
     * 
     * @param volume the volume
     */
    public void setVolume(int volume) {
        if (volume < MidiNote.MIN_VELOCITY || volume > MidiNote.MAX_VELOCITY) {
            throw new IllegalArgumentException(String.format("The volume must be between %d and %d.  The passed volume was %d.", MidiNote.MIN_VELOCITY, MidiNote.MAX_VELOCITY, volume));
        }
        
        this.volume = volume;
        
        // set the pitch fields to default values. We do this so that our
        // hashCode and equals work properly.  All rests of the same length
        // should be equal.
        if (this.isRest()) { 
            this.setScaleStep(0);
            this.setOctave(0);
            this.setChromaticAdjustment(0);
        }
    }
    
    /**
     * Gets whether or not this note is a rest (i.e., has a volume of 0).
     * 
     * @return true if the volume is 0
     */
    public boolean isRest() {
        return (this.getVolume() == 0);
    }    
    
    /**
     * Throws an exception if the changing field is not being changed to zero
     * and the note is a rest.  Called from the setters.
     * 
     * @param changingField the field being changed
     * @param value the value the field is being changed to
     */
    protected void throwUnsupportedOperationExceptionIfRest(String changingField, int value) {
        if (this.isRest() && value != 0) { // each of the fields can be zero...
            throw new UnsupportedOperationException(String.format("The Note is a rest.  The %s field cannot be changed on a rest.", changingField));
        }
    }
    
    /**
     * Sometimes after transformations are applied to a note, the scale step is
     * less than 0 or greater than the number of scale steps in an octave.  
     * This method "normalizes" the note--adjusts its scaleStep and octave so 
     * that it is the same note, but the scaleStep is between 0 and the number 
     * of scale steps.
     * 
     * @param note the note to normalize
     * @return the normalized note
     */
    private Note getNormalizedNote(Scale scale) {
        Note tempNote = new Note(this);
        int numScaleStepsInOctave = scale.getScaleStepArray().length; // cache it
        
        // put the note's scaleStep into the normal range (0..numScaleSteps - 1), adjusting the octaves.
        while(tempNote.getScaleStep() < 0) {
            tempNote.setScaleStep(tempNote.getScaleStep() + numScaleStepsInOctave);
            tempNote.setOctave(tempNote.getOctave() - 1);
        }                
        
        while(tempNote.getScaleStep() > numScaleStepsInOctave - 1) {
            tempNote.setScaleStep(tempNote.getScaleStep() - numScaleStepsInOctave);
            tempNote.setOctave(tempNote.getOctave() + 1);
        }
        
        assert tempNote.getScaleStep() >= 0 && tempNote.getScaleStep() < numScaleStepsInOctave : tempNote.getScaleStep();
        
        return tempNote;
    }
    
    /**
     * Converts from whole notes to ticks, based on the midi tick resolution.
     * 
     * @param wholeNotes the number of whole notes
     * @param midiTickResolution the number of ticks per whole note
     * @return the number of ticks
     */
    private static long convertWholeNotesToTicks(Fraction wholeNotes, int midiTickResolution) {
        Fraction converted = wholeNotes.times(midiTickResolution);        
        
        // converting to midi ticks should result in an integral number of ticks
        // because our tick resolution should be chosen based on what will
        // produce this.  Test that this is in fact the case...
        assert converted.denominator() == 1 : converted;
     
        // since the denominator is 1, the converted value is equal to the numerator...
        return converted.numerator();
    }        
    
    /**
     * Gets the midi pitch number for this note, taking into account the scale
     * step, octave and chromatic adjustment.
     * 
     * @param note input note
     * @return the midi pitch number
     */
    private int getMidiPitchNumber(Scale scale) {
        int[] scaleSteps = scale.getScaleStepArray(); // cache it
        Note normalizedNote = this.getNormalizedNote(scale);        
        int chromaticAdj = normalizedNote.getChromaticAdjustment();
        int chromaticAdjDecrementer = chromaticAdj > 0 ? 1 : -1;
        int testPitchNum;
        
        // Figure out the chromatic adjustment to use for the note.  We attempt to
        // use the given chromatic adjustment, but if it results in producing a pitch
        // that is already present in our scale, we reduce the chromatic adjustment
        // until we get a pitch not present in our scale or we reach 0.
        // We do this because of cases such as a phrase F# G in the key of C.
        // When our fractal algorithm transposes this and it becomes B# C, we
        // wind up with two of the same pitches in a row.  This loses the "shape"
        // that we are dealing with, so we adjust the accidental as necessary.        
        while (Math.abs(chromaticAdj) > 0) {
            testPitchNum = (scaleSteps[normalizedNote.getScaleStep()] // the raw (natural) pitch number                    
                    + chromaticAdj                                    // the chromatic adjustment to test...                    
                    + Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE)         // necessary for notes like Cb, to prevent testPitchNum from being negative
                    % Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE;         // mod it, to put it in the range 0-12
            
            assert testPitchNum >= 0 && testPitchNum < Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE : testPitchNum;                        
            
            // stop decrementing our chromatic adjustment if our scale lacks this pitch...
            if (Arrays.binarySearch(scaleSteps, testPitchNum) < 0) break;
            
            // move the chromatic adjustment towards zero...
            chromaticAdj -= chromaticAdjDecrementer;
        }
        
        return scaleSteps[normalizedNote.getScaleStep()]       // half steps above tonic
                + chromaticAdj                                 // the note's chromatic adjustment
                + this.getSegmentSettingsChromaticAdjustment() // chromatic adjustment for the segment
                + scale.getMidiPitchNumberForTonicAtOctave(normalizedNote.getOctave()); // take into account the octave
    }
    
    /**
     * Converts the note to a Midi Note, that can then be used to get the
     * actual Midi note on and note off events.
     * 
     * @param scale the scale to use
     * @param startTime the time this note should be played
     * @param midiTickResolution the number of ticks per whole note for the
     *        midi sequence
     * @return the MidiNote
     */
    public MidiNote convertToMidiNote(Scale scale, Fraction startTime, int midiTickResolution) {        
        MidiNote midiNote = new MidiNote();       
            
        midiNote.setDuration(convertWholeNotesToTicks(this.getDuration(), midiTickResolution));
        midiNote.setStartTime(convertWholeNotesToTicks(startTime, midiTickResolution));
        midiNote.setVelocity(this.getVolume());        
        midiNote.setPitch(this.getMidiPitchNumber(scale));
        
        return midiNote;
    }   
    
    
    @Override
    public String toString() {
        return String.format("Note = scaleStep %d, octave %d, chromaticAdjustment %d, duration %f, volume %d", this.scaleStep, this.octave, this.chromaticAdjustment, this.duration.asDouble(), this.volume);
    }

    // equals and hashCode were auto-generated by the Netbeans IDE, and modified
    // by hand to deal with the segment settings chromatic adjustment
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Note other = (Note) obj;
        if (this.scaleStep != other.scaleStep) {
            return false;
        }
        if (this.octave != other.octave) {
            return false;
        }
        if (this.chromaticAdjustment != other.chromaticAdjustment) {
            return false;
        }
        if (this.duration != other.duration && (this.duration == null || !this.duration.equals(other.duration))) {
            return false;
        }
        if (this.volume != other.volume) {
            return false;
        }
        
        // this part was coded by hand; the rest was auto-generated
        if (!this.isRest()) {
            // we only care about comparing segment settings if the note is not a rest
            if (this.getSegmentSettingsChromaticAdjustment() != other.getSegmentSettingsChromaticAdjustment()) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.scaleStep;
        hash = 89 * hash + this.octave;
        hash = 89 * hash + this.chromaticAdjustment;
        hash = 89 * hash + (this.duration != null ? this.duration.hashCode() : 0);
        hash = 89 * hash + this.volume;
        
        // this part was coded by hand; the rest was auto-generated
        hash = 89 * hash + (this.getSegmentSettingsChromaticAdjustment());
        
        return hash;
    }                   
}
