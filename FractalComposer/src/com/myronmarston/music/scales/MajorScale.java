package com.myronmarston.music.scales;

import com.myronmarston.music.NoteName;

import org.simpleframework.xml.*;

/**
 * The most common scale used in western music.  This scale uses the following
 * whole step (W) / half step (H): WWHWWWH
 * 
 * @author Myron
 */
@Root
public class MajorScale extends Scale {     
    private final static int[] SCALE_STEPS = new int[] {0, 2, 4, 5, 7, 9, 11};    
    private final static int[] LETTER_NUMBERS = new int[] {0, 1, 2, 3, 4, 5, 6};

    /**
     * Constructor.
     * 
     * @param keyName the name of the tonal center
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException thrown
     *         when the key is invalid
     */
    public MajorScale(NoteName keyName) throws InvalidKeySignatureException {        
        super(new KeySignature(Tonality.Major, keyName));        
    }
    
    /**
     * Provided to allow xml deserialization.
     * 
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException thrown
     *         when the key is invalid.
     */
    public MajorScale() throws InvalidKeySignatureException {
        this(Tonality.Major.getDefaultKey());
    }

    @Override
    public int[] getScaleStepArray() {
        return MajorScale.SCALE_STEPS;
    }
    
    @Override
    public int[] getLetterNumberArray() {
        return MajorScale.LETTER_NUMBERS;
    } 
}
