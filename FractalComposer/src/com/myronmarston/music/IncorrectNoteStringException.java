package com.myronmarston.music;

/**
 * Exception that indicates that the note string could note be parsed at all.
 * 
 * @author Myron
 */
public class IncorrectNoteStringException extends NoteStringParseException {
    // Used to serialize the class.  Change this if the class has a change significant enough to change the way the class is serialized.
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param noteString the invalid note string
     */
    public IncorrectNoteStringException(String noteString) {
        super(noteString, String.format("Error: the note string '%s' was entered incorrectly and could not be parsed.", noteString));
    }        
}
