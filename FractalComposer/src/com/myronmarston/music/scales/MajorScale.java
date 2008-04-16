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
        
    @Override
    protected int getNumScaleStepsInOctave() {
        return 7;
    }

    @Override
    protected int getHalfStepsAboveTonicForScaleStep(int scaleStep) {        
        switch (scaleStep) {
            case 0: return 0; // Do           
            case 1: return 2; // Re
            case 2: return 4; // Mi
            case 3: return 5; // Fa
            case 4: return 7; // So
            case 5: return 9; // La
            case 6: return 11; // Ti
            default: assert false : scaleStep;
        }      
        
        // required so we can compile, but we should never get here; the assert will fire instead.
        return 0;
    }

    /**
     * Constructor.
     * 
     * @param keyName the name of the tonal center
     */
    public MajorScale(NoteName keyName) {        
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
}
