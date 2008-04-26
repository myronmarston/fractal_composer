package com.myronmarston.music.scales;

import com.myronmarston.music.NoteName;

/**
 * A natural minor scale: WHWWHWW.  This is also known as Aeolian mode when 
 * analyzing music modally.
 * 
 * @author Myron
 */
public class MinorScale extends Scale {    
    /**
     * A constant that indicates a given note name cannot be used as a valid 
     * minor key.
     */
    public final static int NOT_A_VALID_MINOR_KEY = Integer.MIN_VALUE;
    private final static int[] SCALE_STEPS = new int[] {0, 2, 3, 5, 7, 8, 10};
    private final static int[] LETTER_NUMBERS = new int[] {0, 1, 2, 3, 4, 5, 6};

    /**
     * Constructor.
     * 
     * @param keyName the name of the tonal center
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException thrown
     *         when the key is invalid
     */
    public MinorScale(NoteName keyName) throws InvalidKeySignatureException {        
        super(keyName);        
    }
        
    @Override
    public KeySignature getKeySignature() {
        return new KeySignature(this.getKeyName().getMinorKeySharpsOrFlats(), KeySignature.MajorOrMinor.Minor);
    }

    @Override
    protected boolean isInvalidKeyName(NoteName keyName) {
        return keyName.getMinorKeySharpsOrFlats() == NOT_A_VALID_MINOR_KEY;
    }

    @Override
    public int[] getScaleStepArray() {
        return MinorScale.SCALE_STEPS;
    }

    @Override
    public int[] getLetterNumberArray() {
        return MinorScale.LETTER_NUMBERS;
    }        
}
