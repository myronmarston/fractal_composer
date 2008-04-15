package com.myronmarston.music.settings;

/**
 * Exception that is thrown when the time signature denominator is not a power 
 * of 2.
 * 
 * @author Myron
 */
public class TimeSignatureDenominatorNotAPowerOf2Exception extends InvalidTimeSignatureException {
    // Used to serialize the class.  Change this if the class has a change significant enough to change the way the class is serialized.
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param denominator the denominator
     */
    public TimeSignatureDenominatorNotAPowerOf2Exception(int denominator) {
        super(denominator, String.format("The given time signature denominator (%d) is invalid.  It must be a power of 2.", denominator));
    }        
}
