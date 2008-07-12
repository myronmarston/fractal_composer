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

import com.myronmarston.music.scales.KeySignature;
import com.myronmarston.music.settings.TimeSignature;

import java.util.*;

/**
 * This class contains the notation of an entire fractal piece.
 * 
 * @author Myron
 */
public class Piece implements NotationElement {
    private final TimeSignature timeSignature;
    private final KeySignature keySignature;
    private final int tempo;
    private String title;
    private String composer;    
    private final NotationElementList parts = new NotationElementList();

    /**
     * Constructor.
     * 
     * @param keySignature the key signature of the piece
     * @param timeSignature the time signature of the piece
     * @param tempo the tempo of the piece, in beats per minute
     */
    public Piece(KeySignature keySignature, TimeSignature timeSignature, int tempo) {
        this.keySignature = keySignature;
        this.timeSignature = timeSignature;        
        this.tempo = tempo;
    }   
    
    /**
     * Gets the name of the composer of this piece.
     * 
     * @return the composer
     */
    public String getComposer() {
        return composer;
    }

    /**
     * Sets the name of the composer of this piece.
     * 
     * @param composer the composer
     */
    public void setComposer(String composer) {
        this.composer = composer;
    }        

    /**
     * Gets the title of this piece.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this piece.
     * 
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the key signature of this piece.
     * 
     * @return the key signature
     */
    public KeySignature getKeySignature() {
        return keySignature;
    }

    /**
     * Gets the time signature of this piece.
     * 
     * @return the time signature
     */
    public TimeSignature getTimeSignature() {
        return timeSignature;
    }

    /**
     * Gets the tempo, in beats per minute, of this piece.
     * 
     * @return the tempo, in beats per minute
     */
    public int getTempo() {
        return tempo;
    }        

    /**
     * Gets the instrumental parts of this piece.
     * 
     * @return the instrumental parts
     */
    public NotationElementList getParts() {
        return parts;
    }        

    /**
     * Gets the lilypond notation of this piece.
     * 
     * @param title the title of the piece
     * @param composer the composer of the piece
     * @return the lilypond string
     */
    public String toLilypondString(String title, String composer) {
        this.setTitle(title);
        this.setComposer(composer);
        return this.toLilypondString();
    }
            
    /**
     * Gets the lilypond notation of this piece.
     * 
     * @return the lilypond string
     */
    public String toLilypondString() {
        StringBuilder str = new StringBuilder();
        str.append("\\version \"2.11.47\"\n\n");
        str.append("\\include \"english.ly\"\n\n");
        str.append("\\header {\n");
        if (this.getTitle() != null && !this.getTitle().isEmpty()) str.append("  title = \"" + this.getTitle() + "\"\n");
        if (this.getComposer() != null && !this.getComposer().isEmpty()) str.append("  composer = \"" + this.getComposer() + "\"\n");
        str.append("  copyright = \"Copyright " + Calendar.getInstance().get(Calendar.YEAR) + ",  fractalcomposer.com\"\n");
        str.append("}\n\n");                
        str.append("\\score {\n");        
        str.append("        \\new StaffGroup <<\n");
        str.append(this.getParts().toLilypondString());
        str.append("        >>\n");
        str.append("   \\layout { }\n");
        str.append("}");                        
        
        return str.toString();
    }    
}