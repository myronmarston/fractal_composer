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
        StringBuilder expectedPartString = new StringBuilder();
        Part part = PartTest.createTestPart(expectedPartString);
        
        StringBuilder expectedPieceString = new StringBuilder();
        expectedPieceString.append("\\version \"2.11.47\"\n\n");
        expectedPieceString.append("\\include \"english.ly\"\n\n");
        expectedPieceString.append("\\header {\n");
        expectedPieceString.append("  title = \"Etude 6\"\n");
        expectedPieceString.append("  composer = \"Myron Marston\"\n");
        expectedPieceString.append("  copyright = \"Copyright " + Calendar.getInstance().get(Calendar.YEAR) + ",  fractalcomposer.com\"\n");
        expectedPieceString.append("}\n\n");                
        expectedPieceString.append("\\score {\n");        
        expectedPieceString.append("        \\new StaffGroup <<\n");
        expectedPieceString.append(expectedPartString.toString());
        expectedPieceString.append("        >>\n");
        expectedPieceString.append("   \\layout { }\n");
        expectedPieceString.append("}");                        
               
        assertEquals(expectedPieceString.toString(), part.getPiece().toLilypondString("Etude 6", "Myron Marston"));
    }

}