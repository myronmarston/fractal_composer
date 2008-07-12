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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class NotationElementListTest {
    
    @Test
    public void getLastIndexForTupletCollection() {        
        testLastIndexForTupletCollection(1, 3, "1/4", "1/6", "1/6", "1/6", "1/8");
        testLastIndexForTupletCollection(1, -1, "1/4", "1/6", "1/6", "1/8");
        testLastIndexForTupletCollection(1, 4, "1/4", "1/6", "1/6", "1/8", "1/6", "1/4");
        testLastIndexForTupletCollection(0, 3, "1/6", "1/6", "1/12", "1/12", "1/6");
        testLastIndexForTupletCollection(0, 7, "1/6", "1/18", "1/18", "1/18", "1/4", "1/8", "1/12", "1/12", "1/10");
    }
    
    private static void testLastIndexForTupletCollection(int firstIndex, int lastIndex, String ... durations) {
        assertEquals(lastIndex, getTestList(durations).getEndIndexForTupletGroup(firstIndex));
    }
    
    @Test
    public void getSmallestNoteDenominator() {
        testGetSmallestNoteDenominator(3L, "1/6", "1/4" ,"1/3");
        testGetSmallestNoteDenominator(4L, "5/6", "3/4" ,"4/9");
    }
    
    private static void testGetSmallestNoteDenominator(long expectedResult, String ... durations) {
        NotationElementList testList = getTestList(durations);        
        assertEquals(expectedResult, testList.getLowestNoteDurationDenominator());
    }
    
    @Test
    public void totalDurationDenomAddsToPowerOf2() {
        testTotalDurationDenomAddsToPowerOf2(true, "1/6", "1/6", "1/6");
        testTotalDurationDenomAddsToPowerOf2(true, "1/6", "1/4", "1/6", "1/6");
        testTotalDurationDenomAddsToPowerOf2(false, "1/6", "1/4", "1/5", "1/6");        
        testTotalDurationDenomAddsToPowerOf2(true, "1/6", "1/6", "1/10", "1/10", "1/10", "1/10", "1/10", "1/6");
    }
    
    private static void testTotalDurationDenomAddsToPowerOf2(boolean expectedResult, String ... durations) {
        assertEquals(expectedResult, NotationElementList.totalDurationDenomAddsToPowerOf2(getTestList(durations)));
    }
    
    @Test
    public void removeConsecutiveNotesWhoseDenomsAddToPowerOf2() {
        testRemoveConsecutiveNotesWhoseDenomsAddToPowerOf2(
            new String[] {"1/6", "1/6", "1/6"}, 
            new String[] {"1/6", "1/6", "1/6"});
        
        testRemoveConsecutiveNotesWhoseDenomsAddToPowerOf2(
            new String[] {"1/6", "1/6", "1/8", "1/6"}, 
            new String[] {"1/6", "1/6", "1/6"});
        
        testRemoveConsecutiveNotesWhoseDenomsAddToPowerOf2(
            new String[] {"1/6", "1/8", "1/6", "1/8", "1/6", "1/4"}, 
            new String[] {"1/6", "1/6", "1/6"});
        
        testRemoveConsecutiveNotesWhoseDenomsAddToPowerOf2(
            new String[] {"1/6", "1/6", "1/10", "1/10", "1/10", "1/10", "1/10", "1/6"}, 
            new String[] {"1/6", "1/6", "1/6"});
        
        testRemoveConsecutiveNotesWhoseDenomsAddToPowerOf2(
            new String[] {"1/6", "1/6", "1/10", "1/10", "1/10", "1/10", "1/6"}, 
            new String[] {"1/6", "1/6", "1/10", "1/10", "1/10", "1/10", "1/6"});
    }
    
    private static void testRemoveConsecutiveNotesWhoseDenomsAddToPowerOf2(String[] beforeDurations, String[] afterDurations) {
        NotationElementList beforeList = getTestList(beforeDurations);
        NotationElementList afterList = getTestList(afterDurations);
        beforeList.removeConsecutiveNotesWhoseDenomsAddToPowerOf2();
        
        assertEquals(afterList.size(), beforeList.size());
        for (int i = 0; i < afterList.size(); i++) {
            NotationNote beforeNote = (NotationNote) beforeList.get(i);
            NotationNote afterNote = (NotationNote) afterList.get(i);
            
            assertEquals(afterNote.getDuration(), beforeNote.getDuration());
        }
    }
    
    public static NotationElementList getTestList(String[] durations) {
        NotationElementList list = new NotationElementList();
        for (String duration : durations) {
            list.add(new NotationNote(NotationNoteTest.DEFAULT_PART, 'c', 4, 0, new Fraction(duration)));
        }   
        
        return list;
    }
    
    public static NotationElementList getTestList2(String ... durations) {
        return getTestList(durations);
    }
    
    @Test
    public void groupTuplets() throws Exception {
        NotationElementList list, tupletList;
        Tuplet tuplet;
        
        // nested tuplets...
        list = getTestList2("1/4", "1/12", "1/8", "1/8", "1/12", "1/36", "1/36", "1/36", "1/8", "1/2", "1/5", "1/5", "1/5", "1/5", "1/5", "3/8", "1/7", "7/16");
        list.groupTuplets();
        
        assertEquals(8, list.size());
        assertNotationElementDurationEquals("1/4", list.get(0));
        assertNotationElementDurationEquals("1/8", list.get(2));
        assertNotationElementDurationEquals("1/2", list.get(3));
        assertNotationElementDurationEquals("3/8", list.get(5));
        assertNotationElementDurationEquals("1/7", list.get(6));
        assertNotationElementDurationEquals("7/16", list.get(7));
        
        assertTrue(list.get(1) instanceof Tuplet);
        tuplet = (Tuplet) list.get(1);
        assertEquals(new Fraction(2, 3), tuplet.getTupletMultiplier());
        tupletList = (tuplet).getNotes();                            
        assertEquals(5, tupletList.size());
        assertNotationElementDurationEquals("1/8", tupletList.get(0));
        assertNotationElementDurationEquals("3/16", tupletList.get(1));
        assertNotationElementDurationEquals("3/16", tupletList.get(2));
        assertNotationElementDurationEquals("1/8", tupletList.get(3));
        
        assertTrue(tupletList.get(4) instanceof Tuplet);
        tuplet = (Tuplet) tupletList.get(4);
        assertEquals(new Fraction(2, 3), tuplet.getTupletMultiplier());
        tupletList = (tuplet).getNotes();                            
        assertEquals(3, tupletList.size());
        assertNotationElementDurationEquals("1/16", tupletList.get(0));
        assertNotationElementDurationEquals("1/16", tupletList.get(1));
        assertNotationElementDurationEquals("1/16", tupletList.get(2));
        
        assertTrue(list.get(4) instanceof Tuplet);
        tuplet = (Tuplet) list.get(4);
        assertEquals(new Fraction(4, 5), tuplet.getTupletMultiplier());
        tupletList = (tuplet).getNotes();                            
        assertEquals(5, tupletList.size());
        assertNotationElementDurationEquals("1/4", tupletList.get(0));
        assertNotationElementDurationEquals("1/4", tupletList.get(1));
        assertNotationElementDurationEquals("1/4", tupletList.get(2));
        assertNotationElementDurationEquals("1/4", tupletList.get(3));
        assertNotationElementDurationEquals("1/4", tupletList.get(4));
    }
    
    public void assertNotationElementDurationEquals(String duration, NotationElement element) {
        assertTrue(element instanceof NotationNote);
        NotationNote note = (NotationNote) element;
        assertEquals(new Fraction(duration), note.getDuration());
    }
}