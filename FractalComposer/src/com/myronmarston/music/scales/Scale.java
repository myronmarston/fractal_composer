package com.myronmarston.music.scales;

import com.myronmarston.music.MidiNote;
import com.myronmarston.music.Note;
import com.myronmarston.music.NoteName;

import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;

import java.util.Arrays;

/**
 * Scales are used to convert a Note to a MidiNote and to provide the midi file
 * with a time signature.  Since each Note contains data on its identity 
 * relative to a given scale, the Scale must be used to convert it to a concrete 
 * MidiNote (e.g., pitch, etc).
 * 
 * @author Myron 
 */
public abstract class Scale {  
    private NoteName keyName;    
    private final static int MIDI_KEY_OFFSET = 12; //C0 is Midi pitch 12 (http://www.phys.unsw.edu.au/jw/notes.html)    
    private final static int NUM_CHROMATIC_PITCHES_PER_OCTAVE = 12;
    
    /**
     * Constructor.
     * 
     * @param keyName the tonal center of the scale
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException thrown
     *         if the given key has more than 7 sharps or flats
     */
    public Scale(NoteName keyName) throws InvalidKeySignatureException {
        this.setKeyName(keyName); 
    }

    /**
     * Gets the tonal center of the scale.
     * 
     * @return the key name
     */
    public NoteName getKeyName() {
        return keyName;
    }

    /**
     * Sets the tonal center of the scale.
     * 
     * @param keyName the key name
     */
    public void setKeyName(NoteName keyName) throws InvalidKeySignatureException {
        if (this.isInvalidKeyName(keyName))
            throw new InvalidKeySignatureException(keyName.toString());
        this.keyName = keyName;
    }    
    
    /**
     * Gets the midi pitch number for the tonic of this scale at the given 
     * octave.
     * 
     * @param octave the octave
     * @return the midi pitch number
     */
    private int getMidiPitchNumberForTonicAtOctave(int octave) {
        return MIDI_KEY_OFFSET // C0, our base pitch...
            + this.getKeyName().getNoteNumber() // the number of half steps to the key
            + (12 * octave); // 12 half steps in an octave
    }          
       
    /**
     * Gets the key signature for this scale.
     * 
     * @return the key signature
     */
    abstract public KeySignature getKeySignature();
    
    /**
     * Gets whether or not the passed key name is valid.  For example, G# major
     * is invalid, because it requires 8 sharps (i.e. 6 sharps and 1 double 
     * sharp) and the midi specification does not allow that.  However, G# minor
     * is a valid key, so the scale must determine what is valid and invalid.
     * 
     * @param keyName the key name to test
     * @return true if the key is not allowed by the midi specification
     */
    abstract protected boolean isInvalidKeyName(NoteName keyName);
    
    /**
     * Gets an array of integers representing the number of half steps above
     * the tonic for each scale step.
     * 
     * @return array of integers
     */
    abstract protected int[] getScaleStepArray();
    
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
    private Note getNormalizedNote(Note note) {
        Note tempNote = new Note(note);
        int numScaleStepsInOctave = this.getScaleStepArray().length; // cache it
        
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
     * Converts from quarter notes to ticks, based on the midi tick resolution.
     * 
     * @param quarterNotes the number of quarter notes
     * @param midiTickResolution the number of ticks per quarter note set on the
     *        midi sequence
     * @return the number of ticks
     */
    private static long convertQuarterNotesToTicks(Fraction quarterNotes, int midiTickResolution) {
        Fraction converted = quarterNotes.times(midiTickResolution);
        
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
    private int getMidiPitchNumber(Note note) {
        int[] scaleSteps = this.getScaleStepArray(); // cache it
        Note tempNote = this.getNormalizedNote(note); 
        int chromaticAdjustment = tempNote.getChromaticAdjustment();
        int chromaticAdjustmentDecrementer = chromaticAdjustment > 0 ? 1 : -1;
        int testPitchNum;
        
        // Figure out the chromatic adjustment to use for the note.  We attempt to
        // use the given chromatic adjustment, but if it results in producing a pitch
        // that is already present in our scale, we reduce the chromatic adjustment
        // until we get a pitch not present in our scale or we reach 0.
        // We do this because of cases such as a phrase F# G in the key of C.
        // When our fractal algorithm transposes this and it becomes B# C, we
        // wind up with two of the same pitches in a row.  This loses the "shape"
        // that we are dealing with, so we adjust the accidental as necessary.        
        while (Math.abs(chromaticAdjustment) > 0) {
            testPitchNum = (scaleSteps[tempNote.getScaleStep()] // the raw (natural) pitch number
                    + chromaticAdjustment                       // the chromatic adjustment to test...                    
                    + NUM_CHROMATIC_PITCHES_PER_OCTAVE)         // necessary for notes like Cb, to prevent testPitchNum from being negative
                    % NUM_CHROMATIC_PITCHES_PER_OCTAVE;         // mod it, to put it in the range 0-12
            
            assert testPitchNum >= 0 && testPitchNum < 12 : testPitchNum;                        
            
            // stop decrementing our chromatic adjustment if our scale lacks this pitch...
            if (Arrays.binarySearch(scaleSteps, testPitchNum) < 0) break;
            
            // move the chromatic adjustment towards zero...
            chromaticAdjustment -= chromaticAdjustmentDecrementer;
        }
        
        return scaleSteps[tempNote.getScaleStep()] + chromaticAdjustment         
            + this.getMidiPitchNumberForTonicAtOctave(tempNote.getOctave());
    }

    /**
     * Converts the given note to a Midi Note, that can then be used to get the
     * actual Midi note on and note off events.
     * 
     * @param note the note to convert
     * @param startTime when the note should be sounded, in quarter notes
     * @param midiTickResolution the number of ticks per quarer note for the 
     *        midi sequence
     * @return the MidiNote
     */
    public MidiNote convertToMidiNote(Note note, Fraction startTime, int midiTickResolution) {        
        MidiNote midiNote = new MidiNote();
                        
        midiNote.setDuration(convertQuarterNotesToTicks(note.getDuration(), midiTickResolution));
        midiNote.setStartTime(convertQuarterNotesToTicks(startTime, midiTickResolution));
        midiNote.setVelocity(note.getVolume());        
        midiNote.setPitch(getMidiPitchNumber(note));
        
        return midiNote;
    }          
}
