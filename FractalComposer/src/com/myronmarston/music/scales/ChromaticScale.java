package com.myronmarston.music.scales;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteName;

import java.util.*;
import org.simpleframework.xml.*;

/**
 * Chromatic Scale--a scale containing all 12 pitches.  This scale does not have
 * a true tonal center; instead, the key signature is set to C major just so we
 * don't have any flats or sharps in our key signature and all notes will have
 * their own accidentals.
 * 
 * @author Myron
 */
@Root
public class ChromaticScale extends Scale {
    private final static int[] SCALE_STEPS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};        
    private final static List<NoteName> VALID_KEY_NAMES = Collections.unmodifiableList(new ArrayList<NoteName>());
    
    /**
     * Constructor.       
     *    
     * @throws InvalidKeySignatureException should never be thrown but must be
     *         declared because the super class constructor can throw it
     */
    public ChromaticScale() throws InvalidKeySignatureException {           
        super(new KeySignature(Tonality.Major, Tonality.Major.getDefaultKey()));                
    }
    
    /**
     * We have some code that uses reflection to instantiate scales, passing
     * the key name.  All other scales have this constructor, so to make this
     * one conform, we provide this one as well.  
     * 
     * @param keyName ignored
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException 
     *         should never be thrown
     */
    public ChromaticScale(NoteName keyName) throws InvalidKeySignatureException {
        this();
    }
        
    @Override
    public int[] getScaleStepArray() {
        return Arrays.copyOf(SCALE_STEPS, SCALE_STEPS.length);
    }

    /**
     * Note supported by Chromatic Scale.
     * 
     * @return throws an UnsupportedOperationException
     */
    @Override
    public int[] getLetterNumberArray() {
        throw new UnsupportedOperationException("The chromatic scale does not support the letter number array.");
    }

    @Override
    public void setNotePitchValues(Note note, NoteName noteName) {
        // the super class setNotePitchValues() uses getLetterNumberArray(), 
        // which won't work in this case, and it's actually much easier for a
        // chromatic scale...
        this.setNotePitchValues_Helper(note, noteName, noteName.getNoteNumber());
    }

    @Override
    public List<NoteName> getValidKeyNames() {
        return VALID_KEY_NAMES;
    }        
}
