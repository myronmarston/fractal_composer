package com.myronmarston.music.settings;

/**
 * Exception that is thrown when an invalid time signature is created.
 * 
 * @author Myron
 */
public class InvalidTimeSignatureException extends Exception {
    // Used to serialize the class.  Change this if the class has a change significant enough to change the way the class is serialized.
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructor.
     *     
     * @param message the error message
     */
    public InvalidTimeSignatureException(String message) {
        super(message);                
    }                
}
