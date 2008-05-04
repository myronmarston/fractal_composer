package com.myronmarston.music.scales;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteName;
import com.myronmarston.util.ClassHelper;

import org.simpleframework.xml.*;

import java.util.*;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * Scales are used to convert a Note to a MidiNote and to provide the midi file
 * with a time signature.  Since each Note contains data on its identity 
 * relative to a given scale, the Scale must be used to convert it to a concrete 
 * MidiNote (e.g., pitch, etc).
 * 
 * @author Myron 
 */
@Root
public abstract class Scale {    
    @Element
    private KeySignature keySignature;
    private static List<Class> scaleTypes;
    private final static int MIDI_KEY_OFFSET = 12; //C0 is Midi pitch 12 (http://www.phys.unsw.edu.au/jw/notes.html)    
    
    /**
     * The number of chromatic pitches in one octave: 12.
     */
    public final static int NUM_CHROMATIC_PITCHES_PER_OCTAVE = 12;         
    
    /**
     * Constructor.
     * 
     * @param keySignature the key signature of the scale
     */
    protected Scale(KeySignature keySignature) {
        this.keySignature = keySignature;
    }

    /**
     * Gets the tonal center of the scale.
     * 
     * @return the key name
     */
    public NoteName getKeyName() {        
        return keySignature.getKeyName();
    }

    /**
     * Gets the key signature of the scale.
     * 
     * @return the key signature
     */
    public KeySignature getKeySignature() {
        return keySignature;
    }         
    
    /**
     * Gets the midi pitch number for the tonic of this scale at the given 
     * octave.
     * 
     * @param octave the octave
     * @return the midi pitch number
     */
    public int getMidiPitchNumberForTonicAtOctave(int octave) {
        return MIDI_KEY_OFFSET // C0, our base pitch...
            + this.getKeyName().getNoteNumber() // the number of half steps to the key            
            + (NUM_CHROMATIC_PITCHES_PER_OCTAVE * octave); // 12 half steps in an octave
    }          
       
    /**
     * Gets an array of integers representing the number of half steps above
     * the tonic for each scale step.
     * 
     * @return array of integers
     */
    abstract public int[] getScaleStepArray();
    
    /**
     * Gets an array of integers of letter numbers above the tonic letter number.
     * 
     * @return letter number array.
     */
    abstract public int[] getLetterNumberArray();
    
    /**
     * Normalizes a chromatic adjustment, putting it in the range -6 to 6.
     * 
     * @param chromaticAdjustment the chromatic adjustment to normalize
     * @return the normalized value
     */
    static private int getNormalizedChromaticAdjustment(int chromaticAdjustment) {
        // put it in the range -6..6 if it is outside of that...
        if (chromaticAdjustment > (Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE / 2)) {            
            return chromaticAdjustment - Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE;
        }
        
        if (chromaticAdjustment < -(Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE / 2)) {
            return chromaticAdjustment + Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE;
        }
        
        return chromaticAdjustment;
    }
    
    /**
     * Sets the scaleStep and chromaticAdjustment on the given note, based on
     * the passed noteName.  
     * 
     * @param note the scaleStep and chromatic adjustment will be set on this 
     *        note
     * @param noteName the noteName of the pitch that should be used to
     *        determine the pitch values
     */
    public void setNotePitchValues(Note note, NoteName noteName) {
        int intervalAboveTonic = this.getKeyName().getPositiveIntervalSize(noteName);
        int scaleStepIndex = Arrays.binarySearch(this.getLetterNumberArray(), intervalAboveTonic);
        
        if (scaleStepIndex >= 0) { 
            // our scale contains this letter, so we can use it to determine our scale step
            // and then figure out our necessary chromaticAdjustment
            setNotePitchValues_Helper(note, noteName, scaleStepIndex);
        } else {
            // our scale does not contain this letter (example: F# for C major 
            // pentatonic-C D E G A).  We need to figure out which of our scale
            // steps this is closest to, and use that, modifying the chromatic
            // adjustment accordingly            
            
            // find the two closest note letters...
            int letterNumberArrayInsertionPt = -scaleStepIndex - 1;
            int nearestLowerScaleStepIndex = (letterNumberArrayInsertionPt == 0 ? this.getLetterNumberArray().length - 1 : letterNumberArrayInsertionPt - 1);
            int nearestHigherScaleStepIndex = (letterNumberArrayInsertionPt == this.getLetterNumberArray().length ?  0 : letterNumberArrayInsertionPt);
            
            // figure out which of these two is closest to our given note...
            int lowerScaleStepNumHalfStepsAboveTonic = this.getScaleStepArray()[nearestLowerScaleStepIndex];
            int higherScaleStepNumHalfStepsAboveTonic = this.getScaleStepArray()[nearestHigherScaleStepIndex];
            int givenNoteHalfStepsAboveTonic = this.getKeyName().getPositiveChromaticSteps(noteName);
                        
            int distanceFromLowerScaleStep = Math.abs(getNormalizedChromaticAdjustment(givenNoteHalfStepsAboveTonic - lowerScaleStepNumHalfStepsAboveTonic));
            int distanceFromHigherScaleStep = Math.abs(getNormalizedChromaticAdjustment(higherScaleStepNumHalfStepsAboveTonic - givenNoteHalfStepsAboveTonic));
                        
            if (distanceFromLowerScaleStep < distanceFromHigherScaleStep) {
                setNotePitchValues_Helper(note, noteName, nearestLowerScaleStepIndex);
            } else {
                setNotePitchValues_Helper(note, noteName, nearestHigherScaleStepIndex);
            }    
        }    
    }

    /**
     * Helper method for setNotePitchValues().  Sets the scaleStep and 
     * chromatic adjustment on the note, given a noteName and a scaleStepIndex.
     * 
     * @param note the scaleStep and chromatic adjustment will be set on this 
     *        note
     * @param noteNameForPitch the noteName of the pitch that should be used to
     *        determine the pitch values
     * @param scaleStepIndex the index of the scaleStep withing this scales's
     *        scaleStepArray that should be used
     */
    protected void setNotePitchValues_Helper(Note note, NoteName noteNameForPitch, int scaleStepIndex) {        
        note.setScaleStep(scaleStepIndex);

        int givenNoteHalfStepAboveTonic = this.getKeyName().getPositiveChromaticSteps(noteNameForPitch);
        int diatonicNoteHalfStepsAboveTonic = this.getScaleStepArray()[scaleStepIndex];
        int chromaticAdjustment = getNormalizedChromaticAdjustment(givenNoteHalfStepAboveTonic - diatonicNoteHalfStepsAboveTonic);
                
        note.setChromaticAdjustment(chromaticAdjustment);
    }     
    
    /**
     * Gets the scale that should be used by default before the user specifies
     * one.  
     * 
     * @return a chromatic scale
     */
    public static Scale getDefault() {
        try {
            return new ChromaticScale();
        } catch (InvalidKeySignatureException ex) {
            throw new UndeclaredThrowableException(ex, "An exception occurred while instantiating a ChromaticScale.  This indicates a programming error.");
        }
    }
    
    /**
     * Gets a list of all the classes in this package that subClass this type.
     * 
     * @return list of scale types    
     */
    public synchronized static List<Class> getScaleTypes() {
        if (scaleTypes == null) {            
            try {                
                return ClassHelper.getSubclassesInPackage(Scale.class.getPackage().getName(), Scale.class);
            } catch (ClassNotFoundException ex) {
                // our code above is guarenteed to pass a valid package name, so we 
                // should never get this exception; if we do, there is a bug in the
                // code somewhere, so throw an assertion error.
                throw new UndeclaredThrowableException(ex, "An exception occurred while getting the scale types.  This indicates a programming error.");
            }
        }
        
        return scaleTypes;
    }

    @Override
    public String toString() {
        return this.getKeyName().toString() + this.getClass().getSimpleName().replaceAll("([A-Z])", " $1");
    }        

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Scale other = (Scale) obj;
        if (this.keySignature != other.keySignature && (this.keySignature == null || !this.keySignature.equals(other.keySignature))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.keySignature != null ? this.keySignature.hashCode() : 0);
        return hash;
    }        
}
