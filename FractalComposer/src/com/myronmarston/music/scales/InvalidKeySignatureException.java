package com.myronmarston.music.scales;

/**
 * Exception that is thrown when a key signature is invalid.
 * 
 * @author Myron
 */
public class InvalidKeySignatureException extends Exception {
    // Used to serialize the class.  Change this if the class has a change significant enough to change the way the class is serialized.
    private static final long serialVersionUID = 1L;
        
    /**
     * Constructor.
     * 
     * @param keyName the name of the key
     */
    public InvalidKeySignatureException(String keyName) {
        super(String.format("The key %s is invalid.", keyName));
    }
}
