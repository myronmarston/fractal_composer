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
import com.myronmarston.util.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class PartTest {
        
    @Test
    public void testToLilypondString() throws Exception {
        StringBuilder expected = new StringBuilder();
        Part part = createTestPart(expected);
        assertEquals(expected.toString(), part.toLilypondString());
    }
    
    protected static Part createTestPart(StringBuilder expectedPartString) throws Exception {
        Piece piece = new Piece(Scale.DEFAULT.getKeySignature(), new TimeSignature(6, 8), 93);        
        Part part = new Part(piece, Instrument.getInstrument("Cello"));
        piece.getParts().add(part);
        part.getNotationElements().add(new NotationNote(part, 'c', 4, 0, new Fraction(1, 4)));
        part.getNotationElements().add(new NotationNote(part, 'd', 4, 0, new Fraction(1, 6)));
        part.getNotationElements().add(new NotationNote(part, 'e', 4, 0, new Fraction(1, 6)));
        part.getNotationElements().add(new NotationNote(part, 'f', 4, 0, new Fraction(1, 6)));
        
        expectedPartString.append("\\new Voice \\with {\n");
        expectedPartString.append("    \\remove \"Note_heads_engraver\"\n");
        expectedPartString.append("    \\consists \"Completion_heads_engraver\"\n");
        expectedPartString.append("}\n");
        expectedPartString.append("{\n");
        expectedPartString.append("        \\tempo 4=93\n");
        expectedPartString.append("        \\time 6/8\n");
        expectedPartString.append("        \\key c \\major\n");
        expectedPartString.append("        \\set Staff.instrumentName = \"Cello\"\n");
        expectedPartString.append("        c'4  \\times 2/3 {  d'4  e'4  f'4  }");
        expectedPartString.append("        \\bar \"|.\"\n");
        expectedPartString.append("}\n\n");            
        
        return part;
    }

}