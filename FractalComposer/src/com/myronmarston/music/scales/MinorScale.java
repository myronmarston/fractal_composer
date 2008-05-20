package com.myronmarston.music.scales;

import com.myronmarston.music.NoteName;
import java.util.Arrays;
import org.simpleframework.xml.*;

/**
 * A natural minor scale: WHWWHWW.  This is also known as Aeolian mode when 
 * analyzing music modally.
 * 
 * @author Myron
 */
@Root
public class MinorScale extends Scale {        
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
        super(new KeySignature(Tonality.Minor, keyName));        
    }    
    
    /**
     * Provided to allow xml deserialization.
     * 
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException thrown
     *         when the key is invalid.
     */
    public MinorScale() throws InvalidKeySignatureException {
        this(Tonality.Minor.getDefaultKey());
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
