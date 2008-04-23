package com.myronmarston.music.scales;

import com.myronmarston.music.NoteName;

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
}
