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

package com.myronmarston.music.notation;

import com.myronmarston.util.Fraction;
import com.myronmarston.util.MathHelper;
import java.util.*;

/**
 * Represents one note of music notation.
 * 
 * @author Myron
 */
public class NotationNote implements NotationElement {
    
    /**
     * The character used for a flat in lilypond notation.
     */
    public static final char LILYPOND_FLAT_CHAR = 'f';
    
    /**
     * The character used for a flat in Guido notation.
     */
    public static final char GUIDO_FLAT_CHAR = '&';
    
    /**
     * The character used for a sharp in lilypond notation.
     */
    public static final char LILYPOND_SHARP_CHAR = 's';    
    
    /**
     * The character used for a sharp in Guido notation.
     */
    public static final char GUIDO_SHARP_CHAR = '#';
    
    /**
     * The character used to increase the octave in lilypond notation.
     */
    private static final char LILYPOND_POSITIVE_OCTAVE_CHAR = '\'';
    
    /**
     * The character used to decrease the octave in lilypond notation.
     */
    private static final char LILYPOND_NEGATIVE_OCTAVE_CHAR = ',';     
    
    /**
     * The symbol used to indicate a rest in lilypond notation.
     */
    private static final String LILYPOND_REST = "r";
    
    /**
     * The symbol used to indicate a rest in guido notation.
     */
    private static final String GUIDO_REST = "_";
    
    private final Part part;
    private final boolean rest;
    private final char letterName;
    private final int octave;
    private final int accidental;
    private final Fraction duration;
    private String lilypondString;
    private String guidoString;
    
    /**
     * Constructor.  Creates a rest.  
     * 
     * @param part the part that owns this notation note
     * @param duration the duration of this notation note
     */
    private NotationNote(Part part, Fraction duration) {                       
        this.part = part;
        this.letterName = java.lang.Character.MIN_VALUE;
        this.octave = 0;
        this.accidental = 0;
        this.duration = duration;
        this.rest = true;
    }
    
    /**
     * Constructor.  Creates a notation note having the same values as the
     * given notation note and duration.
     * 
     * @param note the notation note from which to copy values
     * @param duration the duration to use
     */
    private NotationNote(NotationNote note, Fraction duration) {
        this(note.part, note.letterName, note.octave, note.accidental, duration);
    }
    
    /**
     * Constructor.
     * 
     * @param part the part that owns this notation note
     * @param letterName the letter name for this notation note (a-g)
     * @param octave the octave for this notation note
     * @param accidental the accidental for this notation note; positive 
     *        indicates a number of sharps and negative indicates a number of
     *        flats
     * @param duration the rhythmic duration of this notation note
     * @throws IllegalArgumentException if the accidental outside of the range
     * -2 - 2, or the letterName is invalid, or the duration is negative
     */
    public NotationNote(Part part, char letterName, int octave, int accidental, Fraction duration) throws IllegalArgumentException {        
        if (Math.abs(accidental) > 2) throw new IllegalArgumentException("The accidental is outside the allowable range.");
        if (letterName < 'a' || letterName > 'g') throw new IllegalArgumentException("The letterName is outside the allowable range.");
        if (duration.asDouble() <= 0) throw new IllegalArgumentException("The duration must be positive.");

        this.part = part;
        this.letterName = letterName;
        this.octave = octave;
        this.accidental = accidental;
        this.duration = duration;
        this.rest = false;
    }
    
    /**
     * Creates a rest.
     * 
     * @param part the part that willl own the rest
     * @param duration the duration of the rest
     * @return the rest
     */
    static public NotationNote createRest(Part part, Fraction duration) {
        return new NotationNote(part, duration);
    }

    /**
     * Gets the part that owns this notation note.
     * 
     * @return the part
     */
    public Part getPart() {
        return part;
    }        

    /**
     * Gets the accidental.  Negative indicates flats and positive indicates
     * sharps.
     * 
     * @return the accidental
     */
    public int getAccidental() {
        return accidental;
    }

    /**
     * Gets the rhythmic duration of this notation note. 
     * 
     * @return the rhythmic duration
     */
    public Fraction getDuration() {
        return duration;
    }

    /**
     * Gets the letter name of this notation note (a-g).
     * 
     * @return the letter name of this notation note
     */
    public char getLetterName() {
        return letterName;
    }

    /**
     * Gets the octave number of this notation note.
     * 
     * @return the octave number
     */
    public int getOctave() {
        return octave;
    }

    /**
     * Checks to see if this notation note is a rest.
     * 
     * @return true if this notation note is a rest
     */
    public boolean isRest() {
        return rest;
    }        
      
    /**
     * Gets a string representing the octave in Lilypond notation.
     * 
     * @return the lilypond octave string
     */
    protected String getLilypondOctaveString() {
        // In lilypond, octave 3 is treated as the central octave with no 
        // special mark.
        int adjustedOct = this.octave - 3;        
        return createCharCopyString(adjustedOct, LILYPOND_NEGATIVE_OCTAVE_CHAR, LILYPOND_POSITIVE_OCTAVE_CHAR);        
    }        
    
    /**
     * Gets the octave number used for guido notation.
     * 
     * @return the octave number used by guido notation
     */
    protected int getGuidoOctave() {
        return this.getOctave() - 3;
    }
    
    /**
     * Gets a string representing the accidental in lilypond notation.
     * 
     * @return the lilypond accidental string
     */
    protected String getLilypondAccidentalString() {
        return createCharCopyString(this.accidental, LILYPOND_FLAT_CHAR, LILYPOND_SHARP_CHAR); 
    }
    
    /**
     * Gets a string representing the accidental in guido notation.
     * 
     * @return the guido accidental string
     */
    protected String getGuidoAccidentalString() {
        return createCharCopyString(this.accidental, GUIDO_FLAT_CHAR, GUIDO_SHARP_CHAR); 
    }
    
    /**
     * Creates a string containing copies of the given negChar or posChar based
     * on the value of count.
     * 
     * @param count number of characters; if positive, posChar will be used; if
     *        negative, negChar will be used
     * @param negChar the character to use if count is negative
     * @param posChar the character to use if count is positive
     * @return a string containing copies of the appropriate characters
     */
    private static String createCharCopyString(int count, char negChar, char posChar) {
        if (count == 0) return "";        
        char[] chars = new char[Math.abs(count)];
        Arrays.fill(chars, (count < 0 ? negChar : posChar));
        return String.copyValueOf(chars);        
    }

    /**
     * Gets the representation of this note in lilypond notation.
     * 
     * @return the representation of this note in lilypond notation
     */
    public String toLilypondString() {
        //TODO: if a rest crosses a bar line, the rest in the next bar is not printed. i.e., whole note rest in 3/4
        if (lilypondString == null) {
            if (this.duration.denomIsPowerOf2()) {
                String allButDuration = (
                    this.rest ? LILYPOND_REST : 
                    this.getLetterName() + this.getLilypondAccidentalString() + this.getLilypondOctaveString());
                   
                lilypondString = String.format(this.getDuration().toLilypondString(), allButDuration);
            } else {
                Tuplet tuplet = new Tuplet(Arrays.asList((NotationElement)this));
                lilypondString = tuplet.toLilypondString();
            }  
        }        
        
        return lilypondString;
    }

    /**
     * Gets the representation of this note in GUIDO notation.
     * 
     * @return the representation of this note in GUIDO notation
     */
    public String toGuidoString() {        
        if (guidoString == null) {
            String allButDuration = (
                this.rest ? GUIDO_REST : 
                this.getLetterName() + this.getGuidoAccidentalString() + this.getGuidoOctave());

            guidoString = allButDuration + this.getDuration().toGuidoString();
        }
        
        return guidoString;        
    }
            
    /**
     * Creates a new notation note with a duration that is scaled by the 
     * given multiplier, which can be used when grouping tuplets.
     * 
     * @param multiplier the tuplet multiplier
     * @return a new notation note
     * @throws java.lang.IllegalArgumentException if the multiplier is note
     *         positive
     */
    public NotationNote applyTupletMultiplier(Fraction multiplier) throws IllegalArgumentException {
        return new NotationNote(this, this.getDuration().dividedBy(multiplier));        
    }
}