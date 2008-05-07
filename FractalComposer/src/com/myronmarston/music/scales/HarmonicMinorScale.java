package com.myronmarston.music.scales;

import com.myronmarston.music.NoteName;

import org.simpleframework.xml.*;

/**
 * A minor scale with a leading tone, i.e. a raised 7th scale step.
 * 
 * @author Myron
 */
@Root
public class HarmonicMinorScale extends MinorScale {
    private final static int[] SCALE_STEPS = new int[] {0, 2, 3, 5, 7, 8, 11};
    
    /**
     * Constructor.
     * 
     * @param keyName the name of the tonal center
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException thrown
     *         when the key is invalid
     */
    public HarmonicMinorScale(NoteName keyName) throws InvalidKeySignatureException {        
        super(keyName);        
    }   
    
    /**
     * Provided to allow xml deserialization.
     * 
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException thrown
     *         when the key is invalid.
     */
    public HarmonicMinorScale() throws InvalidKeySignatureException {
        super();
    }
    
    @Override
    public int[] getScaleStepArray() {
        return HarmonicMinorScale.SCALE_STEPS;
    }
}
