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

import com.myronmarston.music.Instrument;

/**
 * Represents one instrumental part of the notation.  
 * 
 * @author Myron
 */
public class Part implements NotationElement {    
    private final NotationElementList notationElements = new NotationElementList();
    private final Piece piece;
    private final Instrument instrument;
    private String lilypondString;

    /**
     * Constructor.
     * 
     * @param piece the notation piece that owns this part
     * @param instrument the instrument for this part
     */
    public Part(Piece piece, Instrument instrument) {
        assert piece != null : "Piece should not be null.";
        assert instrument != null : "Instrument should not be null.";
        
        this.piece = piece;
        this.instrument = instrument;
    }

    /**
     * Gets the notation piece that owns this part.
     * 
     * @return the piece
     */
    public Piece getPiece() {
        return piece;
    }
        
    
    /**
     * Gets the notation elements for this part.
     * 
     * @return the notation elements
     */
    public NotationElementList getNotationElements() {
        return notationElements;
    }    
    
    /**
     * Gets a copy of the list of notation elements, with notes that have tuplet 
     * durations properly grouped.
     * 
     * @return list of elements with tuplets properly grouped
     */
    private NotationElementList getNotationElementsWithGroupedTuplets() {
        NotationElementList list = (NotationElementList) this.getNotationElements().clone();
        list.groupTuplets();
        return list;
    }
    
    /**
     * Gets the lilypond notation for this part.
     * 
     * @return the lilypond string
     */
    public String toLilypondString() {
        if (lilypondString == null) {
            //TODO: clef
            lilypondString =
            "\\new Voice \\with {\n" +
            "    \\remove \"Note_heads_engraver\"\n" +
            "    \\consists \"Completion_heads_engraver\"\n" +
            "}\n" +
            "{\n" + 
            (this == this.piece.getParts().get(0) ? "        \\tempo 4=" + this.piece.getTempo() + "\n" : "") +
            "       " + this.piece.getTimeSignature().toLilypondString() +
            "       " + this.piece.getKeySignature().toLilypondString() +
            "       " + this.instrument.toLilypondString() + 
            "       " + this.getNotationElementsWithGroupedTuplets().toLilypondString() +
            "       \\bar \"|.\"\n" + 
            "}\n\n";            
        }
        
        return lilypondString;        
    }

}
