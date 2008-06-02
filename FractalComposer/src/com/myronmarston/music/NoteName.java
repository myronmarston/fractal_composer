/*
 * Copyright 2008, Myron Marston <myron DOT marston AT gmail DOT com>
 *
 * This file is part of Fractal Composer.
 *
 * Fractal Composer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option any later version.
 *
 * Fractal Composer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Fractal Composer.  If not, see <http://www.gnu.org/licenses/>. 
 */

package com.myronmarston.music;

import com.myronmarston.music.scales.Scale;
import com.myronmarston.music.scales.Tonality;
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
    Cbb(0, -2),
    Cb(0, -1, -7, Tonality.INVALID_KEY),
    C(0, 0, 0, -3, true), // C is 0 because the octave designations begin with C as the first note
    Cs(0, 1, 7, 4, true),    
    Cx(0, 2),
    
    Dbb(1, 0),
    Db(1, 1, -5, Tonality.INVALID_KEY),    
    D(1, 2, 2, -1, true),
    Ds(1, 3, Tonality.INVALID_KEY, 6),
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
    Gb(4, 6, -6, Tonality.INVALID_KEY),
    G(4, 7, 1, -2, true),
    Gs(4, 8, Tonality.INVALID_KEY, 5),
    Gx(4, 9),
    
    Abb(5, 7),
    Ab(5, 8, -4, -7, true),
    A(5, 9, 3, 0, true), 
    As(5, 10, Tonality.INVALID_KEY, 7),
    Ax(5, 11),
    
    Bbb(6, 9),
    Bb(6, 10, -2, -5, true),
    B(6, 11, 5, 2, true),  
    Bs(6, 12),
    Bx(6, 13);
    
    private final int pitchNumberAtOctaveZero;
    private final int noteNumber;
    private final int letterNumber;
    private final boolean defaultNoteNameForNumber;
    private final int majorKeySharpsOrFlats;
    private final int minorKeySharpsOrFlats;      
    private final static HashMap<String, NoteName> NOTE_NAME_HASH;
    public final static int NUM_LETTER_NAMES = 7;
    private final static int MIDI_KEY_OFFSET = 12; //C0 is Midi pitch 12 (http://www.phys.unsw.edu.au/jw/notes.html)    
    
    /**
     * Initializes our note name hash.
     */
    static {            
        NOTE_NAME_HASH = new HashMap<String, NoteName>(NoteName.values().length);
        for (NoteName nn : NoteName.values()) {
            // convert to upper case so that the case doesn't matter...
            NOTE_NAME_HASH.put(nn.toString().toUpperCase(Locale.ENGLISH), nn);
        }        
    }
    
    private NoteName(int letterNumber, int noteNumber) {
        this(letterNumber, noteNumber, Tonality.INVALID_KEY, Tonality.INVALID_KEY);
    }
    
    private NoteName(int letterNumber, int noteNumber, int majorKeySharpsOrFlats, int minorKeySharpsOrFlats) {
        this(letterNumber, noteNumber, majorKeySharpsOrFlats, minorKeySharpsOrFlats, false);
    }
    
    private NoteName(int letterNumber, int noteNumber, int majorKeySharpsOrFlats, int minorKeySharpsOrFlats, boolean defaultNoteNameForNumber) {
        this.letterNumber = letterNumber;
        this.pitchNumberAtOctaveZero = noteNumber + MIDI_KEY_OFFSET;
        this.noteNumber = getNormalizedValue(pitchNumberAtOctaveZero, Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE);
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
     * Gets the midi pitch number for this note name at the given octave.
     * 
     * @param octave the octave
     * @return the midi pitch number
     */
    public int getMidiPitchNumberAtOctave(int octave) {
        return this.pitchNumberAtOctaveZero + (Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE * octave);
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
     * Gets the letter (A-G) of this note name.
     * 
     * @param lowerCase true to return a-g, false to return A-G
     * @return the letter of this note name
     */
    public char getLetter(boolean lowerCase) {
        String letterStr = 
                (lowerCase ? 
                 this.toString().toLowerCase(Locale.ENGLISH) : 
                 this.toString().toUpperCase(Locale.ENGLISH));
        return letterStr.charAt(0);
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
        return getNormalizedValue(other.getLetterNumber() - this.getLetterNumber(), NoteName.NUM_LETTER_NAMES);                
    }
    
    /**
     * Gets the number of positive chromatic steps between two notes.
     * 
     * @param other the note name to compare to
     * @return the number of positive chromatic steps
     */
    public int getPositiveChromaticSteps(NoteName other) {
        return getNormalizedValue(other.getNoteNumber() - this.getNoteNumber(), Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE);                
    }        
       
    private static int getNormalizedValue(int value, int max) {
        // the value could be negative, but it should not be more negative then our mod
        assert value > 0 - max : value;        
        int returnVal = (value + max) % max;
        assert returnVal >= 0 && returnVal < max : returnVal;
        
        return returnVal;
    }

    @Override
    public String toString() {
        return super.toString().replace('s', '#');
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
    
    /**
     * Gets the note name without any accidentals for the given letter number.
     * 
     * @param letterNumber the letter number (0-6)
     * @return the natural note name
     * @throws java.lang.IllegalArgumentException if the letterNumber is not in 
     *         the range 0-6
     */
    public static NoteName getNaturalNoteNameForLetterNumber(int letterNumber) throws IllegalArgumentException {
        switch (letterNumber) {
            case 0: return NoteName.C;
            case 1: return NoteName.D;
            case 2: return NoteName.E;
            case 3: return NoteName.F;
            case 4: return NoteName.G;
            case 5: return NoteName.A;
            case 6: return NoteName.B;
            default: throw new IllegalArgumentException("The letter number must be in the range 0-6.");
        }
    }
        
    /**
     * Gets the NoteName that corresponds to the given string.
     * 
     * @param str note name string, such as 'C#' or 'Eb'
     * @return the NoteName, or null if the string does not match any NoteName
     */
    public static NoteName getNoteName(String str) {
        // convert to upper case so that the case doesn't matter...
        return NOTE_NAME_HASH.get(str.toUpperCase(Locale.ENGLISH));
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
