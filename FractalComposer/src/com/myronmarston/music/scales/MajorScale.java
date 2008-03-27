package com.myronmarston.music.scales;

import com.myronmarston.music.NoteName;

/**
 * @author Myron
 */
public class MajorScale extends TonalScale {

    @Override
    protected int getHalfStepsAboveTonicForScaleStep(int scaleStep) {        
        switch (scaleStep) {
            case 1: return 0; // Do           
            case 2: return 2; // Re
            case 3: return 4; // Mi
            case 4: return 5; // Fa
            case 5: return 7; // So
            case 6: return 9; // La
            case 7: return 11; // Ti
        }
        
        // we should come back and deal with scale steps in < 1 and > 7
        throw new IllegalArgumentException();
    }

    public MajorScale(NoteName keyName) {
        super(keyName);
    }                  
}
