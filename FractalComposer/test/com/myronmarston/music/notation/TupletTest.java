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

import com.myronmarston.util.Fraction;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class TupletTest {
    
    @Test
    public void tupletMultiplier() {
        testTupletMultiplier("2/3", "1/6", "1/6", "1/6");
        testTupletMultiplier("2/3", "1/6", "1/8", "1/6", "1/6");
        testTupletMultiplier("2/3", "1/6", "1/18", "1/18", "1/18", "1/6");
        testTupletMultiplier("4/5", "1/10", "1/10", "1/10", "1/10", "1/10");
        
        // try some single durations...
        testTupletMultiplier("4/5", "3/5");
        testTupletMultiplier("2/3", "1/12");
    }
    
    private static void testTupletMultiplier(String expectedMultiplier, String ... durations) {
        Tuplet t = new Tuplet(NotationElementListTest.getTestList(durations));
        assertEquals(new Fraction(expectedMultiplier), t.getTupletMultiplier());
    }
    
    private static Tuplet constructTestTuplet() throws Exception {
        NotationElementList list = new NotationElementList();
        list.add(new NotationNote(NotationNoteTest.DEFAULT_PART, 'c', 3, 0, new Fraction("1/12")));
        list.add(new NotationNote(NotationNoteTest.DEFAULT_PART, 'd', 3, 0, new Fraction("1/12")));
        list.add(new NotationNote(NotationNoteTest.DEFAULT_PART, 'e', 3, 0, new Fraction("1/12")));
        
        return new Tuplet(list);
    }
    
    @Test
    public void toLilypondString() throws Exception {
        Tuplet tuplet = constructTestTuplet();        
        assertEquals("\\times 2/3 { c8 d8 e8 }", tuplet.toLilypondString());
    }
    
    @Test(expected=UnsupportedOperationException.class) 
    public void toGuidoString() throws Exception {
        Tuplet tuplet = constructTestTuplet();
        tuplet.toGuidoString();
    }
    
    @Test
    public void getLargestDurationDenominator() {
        testGetLargestDurationDenominator(24, "1/12", "1/36", "1/36", "1/36", "1/12");
        testGetLargestDurationDenominator(8, "3/10", "1/5");
    }
    
    private static void testGetLargestDurationDenominator(long expected, String ... durations) {
        NotationElementList list = new NotationElementList();
        for (String duration : durations) {
            list.add(new NotationNote(NotationNoteTest.DEFAULT_PART, 'c', 3, 0, new Fraction(duration)));
        }
                
        Tuplet tuplet = new Tuplet(list);
        assertEquals(expected, tuplet.getLargestDurationDenominator());
    }
    
    @Test
    public void scaleDurations() throws Exception {
        testScaleDurations(8L, Arrays.asList("1/12", "1/24", "1/24", "1/12"), Arrays.asList("1/1", "1/2", "1/2", "1/1"));
    }
    
    private static void testScaleDurations(long scaleFactor, List<String> originalDurations, List<String> expectedDurations) throws Exception {
        assertEquals(originalDurations.size(), expectedDurations.size());
        NotationElementList list = new NotationElementList();
        for (String duration : originalDurations) {
            list.add(new NotationNote(NotationNoteTest.DEFAULT_PART, 'c', 3, 0, new Fraction(duration)));
        }
        
        Tuplet tuplet = new Tuplet(list);        
        tuplet.scaleDurations(scaleFactor);
        
        for (int i = 0; i < originalDurations.size(); i++) {
            assertEquals(new Fraction(expectedDurations.get(i)), ((NotationNote) tuplet.getNotes().get(i)).getDuration());
        }
    }
    
}
