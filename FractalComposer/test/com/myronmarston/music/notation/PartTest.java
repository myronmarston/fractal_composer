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
import com.myronmarston.music.scales.Scale;
import com.myronmarston.music.settings.TimeSignature;
import com.myronmarston.util.FileHelper;
import com.myronmarston.util.Fraction;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class PartTest {
        
    @Test
    public void toLilypondString() throws Exception {
        StringBuilder expectedLilypondString = new StringBuilder();
        Part part = createTestPart(expectedLilypondString, null, true, true);
        assertEquals(expectedLilypondString.toString(), part.toLilypondString());
        
        expectedLilypondString = new StringBuilder();
        part = createTestPart(expectedLilypondString, null, false, false);
        assertEquals(expectedLilypondString.toString(), part.toLilypondString());
    }
    
    @Test
    public void toGuidoString() throws Exception {
        StringBuilder expectedGuidoString = new StringBuilder();
        Part part = createTestPart(null, expectedGuidoString, true, true, "Etude 6", "Myron");
        part.setPieceTitle("Etude 6");
        part.setPieceComposer("Myron");
        assertEquals(expectedGuidoString.toString(), part.toGuidoString());
        
        expectedGuidoString = new StringBuilder();
        part = createTestPart(null, expectedGuidoString, false, false);
        assertEquals(expectedGuidoString.toString(), part.toGuidoString());
    }
    
    protected static Part createTestPart_barebones(boolean includeTempo, boolean includeInstrument) throws Exception {
        Piece piece = new Piece(Scale.DEFAULT.getKeySignature(), new TimeSignature(6, 8), 93, includeTempo, includeInstrument);        
        Part part = new Part(piece, Instrument.getInstrument("Cello"));
        piece.getParts().add(part);
        
        return part;
    }
    
    protected static Part createTestPart(StringBuilder expectedLilypondPartString, StringBuilder expectedGuidoPartString, boolean includeTempo, boolean includeInstrument) throws Exception {
        return createTestPart(expectedLilypondPartString, expectedGuidoPartString, includeTempo, includeInstrument, null, null);
    }
    
    protected static Part createTestPart(StringBuilder expectedLilypondPartString, StringBuilder expectedGuidoPartString, boolean includeTempo, boolean includeInstrument, String title, String composer) throws Exception {
        Part part = createTestPart_barebones(includeTempo, includeInstrument);
        part.getNotationElements().add(new NotationNote(part, 'c', 4, 0, new Fraction(1, 4)));
        part.getNotationElements().add(new NotationNote(part, 'd', 4, 0, new Fraction(1, 6)));
        part.getNotationElements().add(new NotationNote(part, 'e', 4, 0, new Fraction(1, 6)));
        part.getNotationElements().add(new NotationNote(part, 'f', 4, 0, new Fraction(1, 6)));
        
        if (expectedLilypondPartString != null) {            
            expectedLilypondPartString.append("\\new Voice \\with {" + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("    \\remove \"Note_heads_engraver\"" + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("    \\consists \"Completion_heads_engraver\"" + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("}" + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("{" + FileHelper.NEW_LINE);
            if (includeTempo) expectedLilypondPartString.append("       \\tempo 4=93" + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("       \\time 6/8" + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("       \\key c \\major" + FileHelper.NEW_LINE);
            if (includeInstrument) expectedLilypondPartString.append("       \\set Staff.instrumentName = \"Cello\"" + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("       c'4 \\times 2/3 { d'4 e'4 f'4 }");
            expectedLilypondPartString.append("       \\bar \"|.\"" + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("}" + FileHelper.NEW_LINE);            
        }
        
        if (expectedGuidoPartString != null) {
            expectedGuidoPartString.append("[");
            if (includeInstrument) expectedGuidoPartString.append("\\instr<\"Cello\", \"MIDI 42\"> ");
            expectedGuidoPartString.append("\\key<\"C\"> \\meter<\"6/8\"> ");
            if (includeTempo) expectedGuidoPartString.append("\\tempo<\"Andante\",\"1/4=93\"> ");
            if (title != null) expectedGuidoPartString.append("\\title<\"" + title + "\">");
            if (composer != null) expectedGuidoPartString.append("\\composer<\"" + composer + "\">");
            expectedGuidoPartString.append("c1/4 d1/6 e1/6 f1/6]");            
        }
        
        return part;
    }
    
    @Test
    public void isPartFirstPartOfPiece() throws Exception {
        Piece piece = new Piece(Scale.DEFAULT.getKeySignature(), new TimeSignature(6, 8), 93, true, true);        
        Part part1 = new Part(piece, Instrument.getInstrument("Cello"));
        Part part2 = new Part(piece, Instrument.getInstrument("Trumpet"));
        piece.getParts().add(part1);
        piece.getParts().add(part2);
        
        assertTrue(part1.isPartFirstPartOfPiece());
        assertFalse(part2.isPartFirstPartOfPiece());
    }
    
    @Test
    public void getLargestDurationDenominator() throws Exception {
        // this method should create tuplets if necessay, and use the values from that...
        testGetLargestDurationDenominator(64, "3/16", "1/96", "1/96", "1/96");
    }
            
    private static void testGetLargestDurationDenominator(long expected, String ... durations) throws Exception {
        Part part = createTestPart_barebones(false, false);
        
        for (String duration : durations) {
            part.getNotationElements().add(new NotationNote(NotationNoteTest.DEFAULT_PART, 'c', 3, 0, new Fraction(duration)));
        }
                        
        assertEquals(expected, part.getLargestDurationDenominator());
    }
    
    @Test
    public void scaleDurations() throws Exception {
        testScaleDurations(8L, Arrays.asList("1/12", "1/24", "1/24", "1/12"), Arrays.asList("2/3", "1/3", "1/3", "2/3"));
    }
    
    private static void testScaleDurations(long scaleFactor, List<String> originalDurations, List<String> expectedDurations) throws Exception {
        assertEquals(originalDurations.size(), expectedDurations.size());
        Part part = createTestPart_barebones(false, false);
        for (String duration : originalDurations) {
            part.getNotationElements().add(new NotationNote(NotationNoteTest.DEFAULT_PART, 'c', 3, 0, new Fraction(duration)));
        }
        
        part.scaleDurations(scaleFactor);
        
        for (int i = 0; i < originalDurations.size(); i++) {
            assertEquals(new Fraction(expectedDurations.get(i)), ((NotationNote) part.getNotationElements().get(i)).getDuration());
        }
    }

}
