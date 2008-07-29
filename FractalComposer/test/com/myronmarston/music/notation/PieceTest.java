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
public class PieceTest {
            
    @Test
    public void toLilypondString() throws Exception {
        StringBuilder expectedLilypondPartString = new StringBuilder();
        Part part = PartTest.createTestPart(expectedLilypondPartString, null, true, true);
        
        StringBuilder expectedPieceString = new StringBuilder();
        expectedPieceString.append("\\version \"2.11.47\"" + FileHelper.NEW_LINE + FileHelper.NEW_LINE);
        expectedPieceString.append("\\include \"english.ly\"" + FileHelper.NEW_LINE + FileHelper.NEW_LINE);
        expectedPieceString.append("\\header {" + FileHelper.NEW_LINE);
        expectedPieceString.append("  title = \"Etude 6\"" + FileHelper.NEW_LINE);
        expectedPieceString.append("  composer = \"Myron Marston\"" + FileHelper.NEW_LINE);
        expectedPieceString.append("  copyright = \"Copyright " + Calendar.getInstance().get(Calendar.YEAR) + ",  fractalcomposer.com\"" + FileHelper.NEW_LINE);
        expectedPieceString.append("}" + FileHelper.NEW_LINE + FileHelper.NEW_LINE);                
        expectedPieceString.append("\\score {" + FileHelper.NEW_LINE);        
        expectedPieceString.append("        \\new StaffGroup <<" + FileHelper.NEW_LINE);
        expectedPieceString.append(expectedLilypondPartString.toString());
        expectedPieceString.append("        >>" + FileHelper.NEW_LINE);
        expectedPieceString.append("   \\layout { }" + FileHelper.NEW_LINE);
        expectedPieceString.append("}");                        
               
        assertEquals(expectedPieceString.toString(), part.getPiece().toLilypondString("Etude 6", "Myron Marston"));
    }
    
    @Test
    public void toGuidoString() throws Exception {
        StringBuilder expectedGuidoPartString = new StringBuilder();
        Part part = PartTest.createTestPart(null, expectedGuidoPartString, true, true);
        
        StringBuilder expectedPieceString = new StringBuilder();        
        expectedPieceString.append("{" + FileHelper.NEW_LINE);
        expectedPieceString.append(expectedGuidoPartString.toString());
        expectedPieceString.append(FileHelper.NEW_LINE + "}");
               
        assertEquals(expectedPieceString.toString(), part.getPiece().toGuidoString(null, null));
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void scaleDurationsIfNecessary() throws Exception {
        testScaleDurationsIfNecessary(Arrays.asList(Arrays.asList("1/64", "1/64", "1/128", "1/128", "1/256", "1/512", "1/8", "3/17")), 512L, 64L, "6/8", "48/8");
        testScaleDurationsIfNecessary(Arrays.asList(Arrays.asList("1/6", "1/4", "1/32", "1/28", "1/25", "1/12", "1/8", "3/17")), 32L, 32L, "4/4", "4/4");
    }
    
    private void testScaleDurationsIfNecessary(List<List<String>> parts, long beforeLongestDuration, long afterLongestDuration, String beforeTimeSig, String afterTimeSig) throws Exception {
        Piece piece = new Piece(Scale.DEFAULT.getKeySignature(), new TimeSignature(beforeTimeSig), 93, false, false);        
                
        for (List<String> partDurations : parts) {
            Part part = new Part(piece, Instrument.getInstrument("Cello"));                        
            PartSection partSection = new PartSection(part, NotationNoteTest.DEFAULT_VOICE_SECTION);
            for (String duration : partDurations) {
                partSection.getNotationElements().add(NotationNoteTest.instantiateTestNote(duration));
            }
        }
        
        assertEquals(beforeLongestDuration, piece.getParts().getLargestDurationDenominator());
        piece.scaleDurationsIfNecessary();
        assertEquals(afterLongestDuration, piece.getParts().getLargestDurationDenominator());
        assertEquals(new TimeSignature(afterTimeSig), piece.getTimeSignature());
    }
    
}