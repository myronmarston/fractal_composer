package com.myronmarston.music.settings;

/**
 * Exception that is thrown when a time signature is given a negative or zero
 * numerator or denominator.
 * 
 * @author Myron
 */
public class NonPositiveTimeSignatureException extends InvalidTimeSignatureException {
    // Used to serialize the class.  Change this if the class has a change significant enough to change the way the class is serialized.
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param nonPositiveNumeratorOrDenominator the non-positive numerator or denominator
     */
    public NonPositiveTimeSignatureException(int nonPositiveNumeratorOrDenominator) {
        super(nonPositiveNumeratorOrDenominator, "The time signature numerator and denominator must both be positive.");
    }        
}
