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
        
/**
 * Exception indicating that a part of the note string cannot be read.
 * 
 * @author Myron
 */
public class NoteStringInvalidPartException extends NoteStringParseException {
    public enum NoteStringPart {
        NOTE_NAME("note name", "C, F#, Bb, Dx, Abb, etc., or R for a rest"),
        OCTAVE("octave", "0, 1, 2...9 (4 is the octave of middle C)"),
        RHYTHMIC_DURATION("rhythmic duration", "'1/4' for a quarter note, '3/8' for a dotted quarter, '1/6' for a quarter note triplet, etc"),
        DYNAMIC("dynamic", Dynamic.getDynamicExampleString());
        
        private String description;
        private String example;

        /**
         * Gets the human-readable description of this note string part.
         * 
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets a human-readable example of what this part should be like.
         * 
         * @return an example
         */
        public String getExample() {
            return example;
        }                
        
        private NoteStringPart(String description, String example) {        
            this.description = description;
            this.example = example;
        }        
    }
    
    // Used to serialize the class.  Change this if the class has a change significant enough to change the way the class is serialized.
    private static final long serialVersionUID = 1L;    
    private NoteStringPart noteStringPart;
    
    /**
     * Constructor.
     * 
     * @param noteString the note string being parsed
     * @param noteStringPart the part that cannot be read
     */
    public NoteStringInvalidPartException(String noteString, NoteStringPart noteStringPart) {
        super(noteString, String.format("Error: the %s of the note string '%s' could not be read.  This should be something like %s.", noteStringPart.getDescription(), noteString, noteStringPart.getExample()));
        this.noteStringPart = noteStringPart;        
    }
    
    /**
     * Gets the part of the note string that cannot be read.
     * 
     * @return the part that can't be read
     */
    public NoteStringPart getNoteStringPart() {
        return noteStringPart;
    }        
}
