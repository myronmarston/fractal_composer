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
public class ClefTest {

    @Test
    public void toLilypondString() {        
        assertEquals("\\clef \"treble^15\"", Clef.TREBLE_15VA.toLilypondString());
        assertEquals("\\clef \"treble^8\"", Clef.TREBLE_8VA.toLilypondString());
        assertEquals("\\clef \"treble\"", Clef.TREBLE.toLilypondString());
        assertEquals("\\clef \"treble_8\"", Clef.TREBLE_8VB.toLilypondString());
        assertEquals("\\clef \"bass\"", Clef.BASS.toLilypondString());
        assertEquals("\\clef \"bass_8\"", Clef.BASS_8VB.toLilypondString());
        assertEquals("\\clef \"bass_15\"", Clef.BASS_15VB.toLilypondString());        
    }
    
    @Test
    public void toGuidoString() {        
        assertEquals("\\clef<\"g2+15\">", Clef.TREBLE_15VA.toGuidoString());
        assertEquals("\\clef<\"g2+8\">", Clef.TREBLE_8VA.toGuidoString());
        assertEquals("\\clef<\"g2\">", Clef.TREBLE.toGuidoString());
        assertEquals("\\clef<\"g2-8\">", Clef.TREBLE_8VB.toGuidoString());
        assertEquals("\\clef<\"f4\">", Clef.BASS.toGuidoString());
        assertEquals("\\clef<\"f4-8\">", Clef.BASS_8VB.toGuidoString());
        assertEquals("\\clef<\"f4-15\">", Clef.BASS_15VB.toGuidoString());        
    }
    
    @Test
    public void getClosestClefToNoteNumber() throws Exception {
        testGetClosestClefToNoteNumber(45, 90, Clef.TREBLE_15VA);
        testGetClosestClefToNoteNumber(38, 44, Clef.TREBLE_8VA);
        testGetClosestClefToNoteNumber(31, 37, Clef.TREBLE);        
        testGetClosestClefToNoteNumber(25, 30, Clef.TREBLE_8VB);
        testGetClosestClefToNoteNumber(19, 24, Clef.BASS);
        testGetClosestClefToNoteNumber(12, 18, Clef.BASS_8VB);
        testGetClosestClefToNoteNumber(0, 11, Clef.BASS_15VB);
    }        
    
    private static void testGetClosestClefToNoteNumber(int begin, int end, Clef clef) {
        for (int i = begin; i <= end; i++) {
            assertEquals(clef, Clef.getClosestClefToNoteNumber(i));
        }        
    }
    
    @Test
    public void getMedianNotationNumber() throws Exception {
        // odd example: note number 30 the line for E4
        testGetMedianNotationNumber(30, "C#2", "E#4", "G5", "D3", "F4");                
        
        // even example: note number 34 - the line for B4, halfway between E4 and G5
        testGetMedianNotationNumber(34, "C#2", "E#4", "G5", "D3", "F6", "B5");
    }
    
    private static void testGetMedianNotationNumber(int median, String ... noteStrings) throws Exception {
        List<NotationNote> notes = getNotationNoteList(noteStrings);
        assertEquals(median, Clef.getMedianNoteNumber(notes));
    }
    
    public void getBestMatchForNoteList() throws Exception {
        testGetBestMatchForNoteList(Clef.TREBLE_8VB, "C#2", "R", "E#4", "G5", "D3", "F4", "R");                
        testGetBestMatchForNoteList(Clef.TREBLE, "C#2", "E#4", "G5", "D3", "F6", "B5");                
    }
    
    private static void testGetBestMatchForNoteList(Clef clef, String ... noteStrings) throws Exception {
        List<NotationNote> notes = getNotationNoteList(noteStrings);
        assertEquals(clef, Clef.getBestMatchForNoteList(notes));
    }
    
    private static List<NotationNote> getNotationNoteList(String[] noteStrings) throws Exception {
        List<NotationNote> notes = new ArrayList<NotationNote>();
        for (String noteString : noteStrings) {
            notes.add(NotationNoteTest.getNotationNoteForNoteString(noteString));
        }
        
        return notes;
    }
    
    

}