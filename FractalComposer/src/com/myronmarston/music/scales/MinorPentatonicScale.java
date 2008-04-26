package com.myronmarston.music.scales;

import com.myronmarston.music.NoteName;

/**
 * A 5-note scale with minor tonality.
 * 
 * @author Myron
 */
public class MinorPentatonicScale extends MinorScale {
    private final static int[] SCALE_STEPS = new int[] {0, 3, 5, 7, 10};
    protected final static int[] LETTER_NUMBERS = new int[] {0, 2, 3, 4, 6};
    
   /**
     * Constructor.
     * 
     * @param keyName the name of the tonal center
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException thrown
     *         when the key is invalid
     */
    public MinorPentatonicScale(NoteName keyName) throws InvalidKeySignatureException {
        super(keyName);
    }

    @Override
    public int[] getScaleStepArray() {
        return SCALE_STEPS;
    }  
    
    @Override
    public int[] getLetterNumberArray() {
        return MinorPentatonicScale.LETTER_NUMBERS;
    } 
}
