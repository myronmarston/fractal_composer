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
    protected int getHalfStepsAboveTonicForScaleStep(int scaleStep) {
        switch (scaleStep) {
            case 0: return 0; // Do           
            case 1: return 2; // Re
            case 2: return 3; // Me
            case 3: return 5; // Fa
            case 4: return 7; // So
            case 5: return 8; // Le
            case 6: return 10; // Te
            default: assert false : scaleStep;
        }      
        
        // required so we can compile, but we should never get here; the assert will fire instead.
        return 0;
    }

    @Override
    protected int getNumScaleStepsInOctave() {
        return 7;
    }

    @Override
    public KeySignature getKeySignature() {
        return new KeySignature(this.getKeyName().getMinorKeySharpsOrFlats(), KeySignature.MajorOrMinor.Minor);
    }

    @Override
    protected boolean isInvalidKeyName(NoteName keyName) {
        return keyName.getMinorKeySharpsOrFlats() == NOT_A_VALID_MINOR_KEY;
    }
}
