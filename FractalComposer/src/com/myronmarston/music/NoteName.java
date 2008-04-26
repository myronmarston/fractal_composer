package com.myronmarston.music;

import com.myronmarston.music.scales.Scale;
import com.myronmarston.music.scales.MajorScale;
import com.myronmarston.music.scales.MinorScale;
import java.util.HashMap;
import java.util.Locale;

/**
 * Enumerates all the valid note names.  These are used to parse a note string
 * and to provide key signatures.  Each of the 7 letter names (A-G) has 5 
 * variations: double flat (bb), flat (b), natural (just the letter name), 
 * sharp (s, since java does not allow # in enum names) and double sharp (x).
 * 
 * @author Myron
 */
public enum NoteName {    
    Cbb(0, 10),
    Cb(0, 11, -7, MinorScale.NOT_A_VALID_MINOR_KEY),
    C(0, 0, 0, -3, true), // C is 0 because the octave designations begin with C as the first note
    Cs(0, 1, 7, 4, true),    
    Cx(0, 2),
    
    Dbb(1, 0),
    Db(1, 1, -5, MinorScale.NOT_A_VALID_MINOR_KEY),    
    D(1, 2, 2, -1, true),
    Ds(1, 3, MajorScale.NOT_A_VALID_MAJOR_KEY, 6),
    Dx(1, 4),
    
    Ebb(2, 2),
    Eb(2, 3, -3, -6, true),           
    E(2, 4, 4, 1, true),
    Es(2, 5),
    Ex(2, 6),
    
    Fbb(3, 3),
    Fb(3, 4),    
    F(3, 5, -1, -4, true),
    Fs(3, 6, 6, 3, true),
    Fx(3, 7),
    
    Gbb(4, 5),
    Gb(4, 6, -6, MinorScale.NOT_A_VALID_MINOR_KEY),
    G(4, 7, 1, -2, true),
    Gs(4, 8, MajorScale.NOT_A_VALID_MAJOR_KEY, 5),
    Gx(4, 9),
    
    Abb(5, 7),
    Ab(5, 8, -4, -7, true),
    A(5, 9, 3, 0, true), 
    As(5, 10, MajorScale.NOT_A_VALID_MAJOR_KEY, 7),
    Ax(5, 11),
    
    Bbb(6, 9),
    Bb(6, 10, -2, -5, true),
    B(6, 11, 5, 2, true),  
    Bs(6, 0),
    Bx(6, 1);
    
    private final int noteNumber;
    private final int letterNumber;
    private final boolean defaultNoteNameForNumber;
    private final int majorKeySharpsOrFlats;
    private final int minorKeySharpsOrFlats;      
    private static HashMap<String, NoteName> noteNameHash;
    public static final int NUM_LETTER_NAMES = 7;
    
    private NoteName(int letterNumber, int noteNumber) {
        this(letterNumber, noteNumber, MajorScale.NOT_A_VALID_MAJOR_KEY, MinorScale.NOT_A_VALID_MINOR_KEY);
    }
    
    private NoteName(int letterNumber, int noteNumber, int majorKeySharpsOrFlats, int minorKeySharpsOrFlats) {
        this(letterNumber, noteNumber, majorKeySharpsOrFlats, minorKeySharpsOrFlats, false);
    }
    
    private NoteName(int letterNumber, int noteNumber, int majorKeySharpsOrFlats, int minorKeySharpsOrFlats, boolean defaultNoteNameForNumber) {
        this.letterNumber = letterNumber;
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
        return noteNumber;
    }

    /**
     * Gets the size of the interval of this note name's musical letter compared 
     * to C.  The interval is 0-based, rather than 1-based, as intervals are in
     * music theory (i.e. C to E is considered to be a 2nd, rather than a 3rd).
     * 
     * @return the letter number
     */
    public int getLetterNumber() {
        return letterNumber;
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
     * Gets the positive interval size of the given note name compared to this 
     * note name.  Note that this is 0-based, rather than 1-based, as intervals 
     * are in music theory (i.e. C to E is considered to be a 2nd, rather than a 
     * 3rd).
     * 
     * @param other the note name to compare to this one to get the interval 
     *        size
     * @return the size of the interval
     */
    public int getPositiveIntervalSize(NoteName other) {
        return (other.getLetterNumber() - this.getLetterNumber()
                + NoteName.NUM_LETTER_NAMES) 
                % NoteName.NUM_LETTER_NAMES; 
    }
    
    /**
     * Gets the number of positive chromatic steps between two notes.
     * 
     * @param other the note name to compare to
     * @return the number of positive chromatic steps
     */
    public int getPositiveChromaticSteps(NoteName other) {
        return (other.getNoteNumber() - this.getNoteNumber()
                + Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE) 
                % Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE; 
    }        
    
    /**
     * Gets the default note name for the given number.  For example, "5" would 
     * yield "F" rather than "E_SHARP".
     * 
     * @param noteNumber the number of half steps above C
     * @return the NoteName
     * @throws java.lang.IllegalArgumentException if the number is < 0 or > 11
     */
    public static NoteName getDefaultNoteNameForNumber(int noteNumber) throws IllegalArgumentException {
        for (NoteName noteName : NoteName.values()) {
            if ((noteNumber == noteName.getNoteNumber()) && (noteName.defaultNoteNameForNumber)) {
                return noteName;
            }
        }
        
        throw new IllegalArgumentException(String.format("%d is not a valid keyNumber.  The keyNumber should be between 0 and 11.", noteNumber));
    }
    
    private static HashMap<String, NoteName> getNoteNameHash() {
        if (noteNameHash == null) {
            noteNameHash = new HashMap<String, NoteName>(NoteName.values().length);
            for (NoteName nn : NoteName.values()) {
                noteNameHash.put(nn.toString().replace('s', '#').toUpperCase(Locale.ENGLISH), nn);
            }
        }
        
        return noteNameHash;
    }
    
    /**
     * Gets the NoteName that corresponds to the given string.
     * 
     * @param str note name string, such as 'C#' or 'Eb'
     * @return the NoteName, or null if the string does not match any NoteName
     */
    public static NoteName getNoteName(String str) {
        return NoteName.getNoteNameHash().get(str.toUpperCase(Locale.ENGLISH));
    }        
    
    /**
     * Gets a regular expression pattern for parsing the NoteName from a Note
     * string.
     * 
     * @return the regular expression pattern string
     */
    public static String getRegexPattern() {
        return "(" +             // opening paren for capturing group
               "(?:" +           //     opening paren for non-capturing group
               "[A-G]" +         //         letter name
               "(?:bb|b||#|x)" + //         accidental (non-capturing group)
               ")" +             //     closing parent for non-capturing group               
               "|[^\\d,]*" +     //     or a Rest or whatever junk the user types for the note name--our parse code will handle this
               ")";              // closing paren for capturing group
    }
}
