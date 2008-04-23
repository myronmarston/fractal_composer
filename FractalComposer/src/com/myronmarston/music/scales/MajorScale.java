package com.myronmarston.music.scales;

import com.myronmarston.music.NoteName;

/**
 * The most common scale used in western music.  This scale uses the following
 * whole step (W) / half step (H): WWHWWWH
 * 
 * @author Myron
 */
public class MajorScale extends Scale {     
    /**
     * A constant that indicates a given note name cannot be used as a valid 
     * major key.
     */
    public final static int NOT_A_VALID_MAJOR_KEY = Integer.MAX_VALUE;    
    private final static int[] SCALE_STEPS = new int[] {0, 2, 4, 5, 7, 9, 11};    

    /**
     * Constructor.
     * 
     * @param keyName the name of the tonal center
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException thrown
     *         when the key is invalid
     */
    public MajorScale(NoteName keyName) throws InvalidKeySignatureException {        
        super(keyName);        
    }

    @Override
    public KeySignature getKeySignature() {
        return new KeySignature(this.getKeyName().getMajorKeySharpsOrFlats(), KeySignature.MajorOrMinor.Major);       
    }   

    @Override
    protected boolean isInvalidKeyName(NoteName keyName) {
        return keyName.getMajorKeySharpsOrFlats() == NOT_A_VALID_MAJOR_KEY;
    }

    @Override
    public int[] getScaleStepArray() {
        return MajorScale.SCALE_STEPS;
    }
}
