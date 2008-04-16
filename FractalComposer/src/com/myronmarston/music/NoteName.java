package com.myronmarston.music;

import com.myronmarston.music.scales.MajorScale;
import com.myronmarston.music.scales.MinorScale;

/**
 * Enumerates all the valid note names.
 * Also provides a "note number" -- number of half steps the note is above C.
 * 
 * @author Myron
 */
public enum NoteName {
    B_SHARP(0, MajorScale.NOT_A_VALID_MAJOR_KEY, MinorScale.NOT_A_VALID_MINOR_KEY),
    C(0, 0, -3, true), // C is 0 because the octave designations begin with C as the first note
    C_SHARP(1, 7, 4, true),
    D_FLAT(1, -5, MinorScale.NOT_A_VALID_MINOR_KEY),
    D(2, 2, -1, true),
    D_SHARP(3, MajorScale.NOT_A_VALID_MAJOR_KEY, 6),
    E_FLAT(3, -3, -6, true),           
    E(4, 4, 1, true),
    F_FLAT(4, MajorScale.NOT_A_VALID_MAJOR_KEY, MinorScale.NOT_A_VALID_MINOR_KEY),
    E_SHARP(5, MajorScale.NOT_A_VALID_MAJOR_KEY, MinorScale.NOT_A_VALID_MINOR_KEY),
    F(5, -1, -4, true),
    F_SHARP(6, 6, 3, true),
    G_FLAT(6, -6, MinorScale.NOT_A_VALID_MINOR_KEY),
    G(7, 1, -2, true),
    G_SHARP(8, MajorScale.NOT_A_VALID_MAJOR_KEY, 5),
    A_FLAT(8, -4, -7, true),
    A(9, 3, 0, true), 
    A_SHARP(10, MajorScale.NOT_A_VALID_MAJOR_KEY, 7),
    B_FLAT(10, -2, -5, true),
    B(11, 5, 2, true),
    C_FLAT(11, -7, MinorScale.NOT_A_VALID_MINOR_KEY);

    private final int noteNumber;
    private final boolean defaultNoteNameForNumber;
    private final int majorKeySharpsOrFlats;
    private final int minorKeySharpsOrFlats;    
    
    NoteName(int noteNumber, int majorKeySharpsOrFlats, int minorKeySharpsOrFlats) {
        this(noteNumber, majorKeySharpsOrFlats, minorKeySharpsOrFlats, false);
    }
    
    NoteName(int noteNumber, int majorKeySharpsOrFlats, int minorKeySharpsOrFlats, boolean defaultNoteNameForNumber) {
        this.noteNumber = noteNumber;
        this.defaultNoteNameForNumber = defaultNoteNameForNumber;
        this.majorKeySharpsOrFlats = majorKeySharpsOrFlats;
        this.minorKeySharpsOrFlats = minorKeySharpsOrFlats;
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
     * Gets the number of sharps or flats for this major key.  Positive 
     * indicates sharps; negative indicates flats.
     * 
     * @return the number of sharps or flats for the major key
     */
    public int getMajorKeySharpsOrFlats() {
        return majorKeySharpsOrFlats;
    }

    /**
     * Gets the number of sharps or flats for this minor key.  Positive 
     * indicates sharps; negative indicates flats.
     * 
     * @return the number of sharps or flats for the minor key
     */
    public int getMinorKeySharpsOrFlats() {
        return minorKeySharpsOrFlats;
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
