package com.myronmarston.music;

/**
 *
 * @author Myron
 */
public class GermIsEmptyException extends Exception {
    // Used to serialize the class.  Change this if the class has a change significant enough to change the way the class is serialized.
    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new instance of <code>GermIsEmptyException</code> without detail message.
     */
    public GermIsEmptyException() {
        super("The germ is empty.  A midi sequence cannot be created without it.");
    }
}
