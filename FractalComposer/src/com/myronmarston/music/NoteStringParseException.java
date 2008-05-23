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
