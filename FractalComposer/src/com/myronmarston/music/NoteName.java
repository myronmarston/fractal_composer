package com.myronmarston.music;

/**
 * Enumerates all the valid note names.
 * Also provides a "note number" -- number of half steps the note is above C.
 * 
 * @author Myron
 */
public enum NoteName {
    B_SHARP(0),
    C(0, true), // C is 0 because the octave designations begin with C as the first note
    C_SHARP(1, true),
    D_FLAT(1),
    D(2, true),
    D_SHARP(3),
    E_FLAT(3, true),           
    E(4, true),
    F_FLAT(4),
    E_SHARP(5),
    F(5, true),
    F_SHARP(6, true),
    G_FLAT(6),
    G(7, true),
    G_SHARP(8),
    A_FLAT(8, true),
    A(9, true), 
    A_SHARP(10),
    B_FLAT(10, true),
    B(11, true),
    C_FLAT(11);

    private final int noteNumber;
    private final boolean defaultNoteNameForNumber;

    NoteName(int noteNumber) {
        this(noteNumber, false);
    }
    
    NoteName(int noteNumber, boolean defaultNoteNameForNumber) {
        this.noteNumber = noteNumber;
        this.defaultNoteNameForNumber = defaultNoteNameForNumber;
    }

    /**
     * Gets the number of half steps this note name is above C.
     * 
     * @return the number of half steps this note name is above C
     */
    public int getNoteNumber() {
        return this.noteNumber;
    }
    
    /**
     * Gets the default note name for the given number.  For example, "5" would 
     * yield "F" rather than "E_SHARP".
     * 
     * @param noteNumber the number of half steps above C
     * @return the NoteName
     * @throws java.lang.IllegalArgumentException if the number is < 0 or > 11
     */
    static public NoteName getDefaultNoteNameForNumber(int noteNumber) throws IllegalArgumentException {
        for (NoteName noteName : NoteName.values()) {
            if ((noteNumber == noteName.getNoteNumber()) && (noteName.defaultNoteNameForNumber)) {
                return noteName;
            }
        }
        
        throw new IllegalArgumentException(String.format("%d is not a valid keyNumber.  The keyNumber should be between 0 and 11.", noteNumber));
    }
}
