package com.myronmarston.music.scales;

import com.myronmarston.music.NoteName;

/**
 * A 5-note scale with major tonality.
 * 
 * @author Myron
 */
public class MajorPentatonicScale extends MajorScale {
    private final static int[] SCALE_STEPS = new int[] {0, 2, 4, 7, 9};
    protected final static int[] LETTER_NUMBERS = new int[] {0, 1, 2, 4, 5};
    
   /**
     * Constructor.
     * 
     * @param keyName the name of the tonal center
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException thrown
     *         when the key is invalid
     */
    public MajorPentatonicScale(NoteName keyName) throws InvalidKeySignatureException {
        super(keyName);
    }

    @Override
    public int[] getScaleStepArray() {
        return SCALE_STEPS;
    } 
    
    @Override
    public int[] getLetterNumberArray() {
        return MajorPentatonicScale.LETTER_NUMBERS;
    }
}
