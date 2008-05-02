package com.myronmarston.music.scales;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteName;

import com.myronmarston.util.ClassHelper;
import java.util.*;

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
    private static Map<String, Class> scaleTypes;
    private final static int MIDI_KEY_OFFSET = 12; //C0 is Midi pitch 12 (http://www.phys.unsw.edu.au/jw/notes.html)    
    
    /**
     * The number of chromatic pitches in one octave: 12.
     */
    public final static int NUM_CHROMATIC_PITCHES_PER_OCTAVE = 12;    
    
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
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException thrown 
     *         if the key has more than 7 flats or sharps
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
    public int getMidiPitchNumberForTonicAtOctave(int octave) {
        return MIDI_KEY_OFFSET // C0, our base pitch...
            + this.getKeyName().getNoteNumber() // the number of half steps to the key            
            + (NUM_CHROMATIC_PITCHES_PER_OCTAVE * octave); // 12 half steps in an octave
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
     * Gets a list of all the classes in this package that subClass this type.
     * 
     * @return list of scale types    
     */
    public static Map<String, Class> getScaleTypes() {
        if (scaleTypes == null) {
            List<Class> scaleTypeList;
            try {
                scaleTypeList = ClassHelper.getSubclassesInPackage(Scale.class.getPackage().getName(), Scale.class);
            } catch (ClassNotFoundException ex) {
                // our code above is guarenteed to pass a valid package name, so we 
                // should never get this exception; if we do, there is a bug in the
                // code somewhere, so throw an assertion error.
                throw new AssertionError("getScaleTypes() failed for some unknown reason.  The exception was: " + ex.getMessage());
            }

            scaleTypes = new HashMap<String, Class>(scaleTypeList.size());
            String humanReadableName;
            for (Class c : scaleTypeList) {
                // change the string from 'CamelCasing' to 'Human Readable Format'
                humanReadableName = c.getSimpleName().replaceAll("[A-Z]", " $0");
                
                // remove the redundant word 'Scale' from the string
                humanReadableName = humanReadableName.replaceAll(" Scale", "");                
                
                // remove the leading space
                humanReadableName = humanReadableName.replaceFirst("^ ", "");
                scaleTypes.put(humanReadableName, c);
            }
        }    
        
        return scaleTypes;
    }
}
