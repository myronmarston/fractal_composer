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

import com.myronmarston.util.FileHelper;
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

}