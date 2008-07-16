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
import com.myronmarston.music.Tempo;
import com.myronmarston.util.FileHelper;

/**
 * Represents one instrumental part of the notation.  
 * 
 * @author Myron
 */
public class Part extends AbstractNotationElement {    
    private final NotationElementList notationElements = new NotationElementList();
    private final Piece piece;
    private final Instrument instrument;
    private String pieceTitle;
    private String pieceComposer;        
    
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
     * Checks to see if this part is the first part of the piece.
     * 
     * @return true if this is the first part of the piece
     */
    protected boolean isPartFirstPartOfPiece() {       
        return this == this.piece.getParts().get(0);
    }

    /**
     * Gets the composer of the piece.  It is available here because in 
     * Guido notation the composer is notated in the first part.
     * 
     * @return the piece composer
     */
    public String getPieceComposer() {
        return pieceComposer;
    }
    
    /**
     * Sets the composer of the piece.  It is available here because in 
     * Guido notation the composer is notated in the first part.
     * 
     * @param pieceComposer the composer of the piece
     */            
    public void setPieceComposer(String pieceComposer) {
        assert this.isPartFirstPartOfPiece() : "This should only be set on the first part of the piece.";
        this.pieceComposer = pieceComposer;
    }

    /**
     * Gets the title of the piece.  It is available here because in 
     * Guido notation the title is notated in the first part.
     * 
     * @return the title of the piece
     */
    public String getPieceTitle() {
        return pieceTitle;
    }

    /**
     * Sets the title of the piece.  It is available here because in 
     * Guido notation the title is notated in the first part.
     * 
     * @param pieceTitle the title of the piece
     */
    public void setPieceTitle(String pieceTitle) {
        assert this.isPartFirstPartOfPiece() : "This should only be set on the first part of the piece.";        
        this.pieceTitle = pieceTitle;
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
        list.setElementSeperator(" ");
        list.groupTuplets();
        return list;
    }

    /**
     * Indicates whether or not duration scaling is supported by this element.
     * 
     * @return true if any of the notation elements support duration scaling
     */
    public boolean supportsDurationScaling() {
        return this.getNotationElements().supportsDurationScaling();
    }

    @Override
    public long getLargestDurationDenominator() {
        // here we use the list with grouped tuplets because we want to get the
        // duration denominator that will actually be used for the notes, taking
        // into account tuplets. 
        return this.getNotationElementsWithGroupedTuplets().getLargestDurationDenominator();
    }

    @Override
    public void scaleDurations(long scaleFactor) {
        this.getNotationElements().scaleDurations(scaleFactor);
    }
                    
    /**
     * Gets the lilypond notation for this part.
     * 
     * @return the lilypond string
     */
    public String toLilypondString() {        
        //TODO: clef            
        StringBuilder str = new StringBuilder();

        str.append("\\new Voice \\with {" + FileHelper.NEW_LINE);
        str.append("    \\remove \"Note_heads_engraver\"" + FileHelper.NEW_LINE);
        str.append("    \\consists \"Completion_heads_engraver\"" + FileHelper.NEW_LINE);
        str.append("}" + FileHelper.NEW_LINE);
        str.append("{" + FileHelper.NEW_LINE); 
        if (this.isPartFirstPartOfPiece() && this.piece.getIncludeTempo()) str.append("       " + Tempo.toLilypondString(this.piece.getTempo()) + FileHelper.NEW_LINE);
        str.append("       " + this.piece.getTimeSignature().toLilypondString());
        str.append("       " + this.piece.getKeySignature().toLilypondString());
        if (this.piece.getIncludeInstruments()) str.append("       " + this.instrument.toLilypondString()); 
        str.append("       " + this.getNotationElementsWithGroupedTuplets().toLilypondString());
        str.append("       \\bar \"|.\"" + FileHelper.NEW_LINE); 
        str.append("}" + FileHelper.NEW_LINE);            

        return str.toString();        
    }

    /**
     * Gets the GUIDO notation for this part.
     * 
     * @return the GUIDO notation for this part
     */
    public String toGuidoString() {
        this.getNotationElements().setElementSeperator(" ");
        StringBuilder str = new StringBuilder();        
        str.append("[");
        //If a pageFormat should be specified, put it here, such as \pageFormat<"A4",10pt,10pt,10pt,10pt>
                
        if (this.piece.getIncludeInstruments()) {
            str.append(instrument.toGuidoString() + " ");
        }
        
        str.append(this.piece.getKeySignature().toGuidoString() + " ");
        str.append(this.piece.getTimeSignature().toGuidoString() + " ");                
        
        if (this.isPartFirstPartOfPiece()) {
            if (this.piece.getIncludeTempo()) {
                str.append(Tempo.toGuidoString(this.piece.getTempo()) + " ");
            }
            if (this.getPieceTitle() != null && !this.getPieceTitle().isEmpty()) {
                str.append("\\title<\"" + this.getPieceTitle() + "\">");
            }
            if (this.getPieceComposer() != null && !this.getPieceComposer().isEmpty()) {
                str.append("\\composer<\"" + this.getPieceComposer() + "\">");
            }
        }        

        str.append(this.getNotationElements().toGuidoString());
        str.append("]");           
        return str.toString();
    }
   
}
