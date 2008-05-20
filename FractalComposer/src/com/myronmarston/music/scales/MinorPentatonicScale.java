package com.myronmarston.music.scales;

import com.myronmarston.music.NoteName;
import java.util.Arrays;
import org.simpleframework.xml.*;

/**
 * A 5-note scale with minor tonality.
 * 
 * @author Myron
 */
@Root
public class MinorPentatonicScale extends MinorScale {
    private final static int[] SCALE_STEPS = new int[] {0, 3, 5, 7, 10};
    private final static int[] LETTER_NUMBERS = new int[] {0, 2, 3, 4, 6};
    
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
    
    /**
     * Provided to allow xml deserialization.
     * 
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException thrown
     *         when the key is invalid.
     */
    public MinorPentatonicScale() throws InvalidKeySignatureException {
        super();
    }

    @Override
    public int[] getScaleStepArray() {
        return Arrays.copyOf(SCALE_STEPS, SCALE_STEPS.length);
    }  
    
    @Override
    public int[] getLetterNumberArray() {
        return Arrays.copyOf(LETTER_NUMBERS, LETTER_NUMBERS.length);        
    } 
}
