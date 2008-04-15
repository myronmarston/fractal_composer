package com.myronmarston.music.settings;

/**
 * Exception that is thrown when an invalid time signature is created.
 * 
 * @author Myron
 */
public abstract class InvalidTimeSignatureException extends Exception {
    // Used to serialize the class.  Change this if the class has a change significant enough to change the way the class is serialized.
    private static final long serialVersionUID = 1L;
    
    private int invalidNumeratorOrDenominatorValue;
   
    /**
     * Gets the invalid value.
     * 
     * @return either the numerator or denominator, whichever is invalid
     */
    public int getInvalidNumeratorOrDenominatorValue() {
        return invalidNumeratorOrDenominatorValue;
    }      

    /**
     * Constructor.
     * 
     * @param invalidNumeratorOrDenominatorValue the invalid value
     * @param message the error message
     */
    public InvalidTimeSignatureException(int invalidNumeratorOrDenominatorValue, String message) {
        super(message);        
        this.invalidNumeratorOrDenominatorValue = invalidNumeratorOrDenominatorValue;
    }                
}
