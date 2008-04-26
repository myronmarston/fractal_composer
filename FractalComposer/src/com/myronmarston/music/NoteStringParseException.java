package com.myronmarston.music;

/**
 * Base exception class for all errors encountered while parsing a note string.
 * 
 * @author Myron
 */
public abstract class NoteStringParseException extends Exception {
    // Used to serialize the class.  Change this if the class has a change significant enough to change the way the class is serialized.
    private static final long serialVersionUID = 1L;
    
    private String noteString;
    
    /**
     * Constructor.
     * 
     * @param noteString the note string being parsed
     * @param msg the detail message
     */
    public NoteStringParseException(String noteString, String msg) {
        super(msg);
        this.noteString = noteString;
    }

    /**
     * Gets the note string that could not be parsed.
     * 
     * @return the note string
     */
    public String getNoteString() {
        return noteString;
    }        
}
