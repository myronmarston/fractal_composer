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
    
    @Test
    public void toLilypondString() throws Exception {
        NotationElementList list = new NotationElementList();
        list.add(new NotationNote(NotationNoteTest.DEFAULT_PART, 'c', 3, 0, new Fraction("1/12")));
        list.add(new NotationNote(NotationNoteTest.DEFAULT_PART, 'd', 3, 0, new Fraction("1/12")));
        list.add(new NotationNote(NotationNoteTest.DEFAULT_PART, 'e', 3, 0, new Fraction("1/12")));
        
        Tuplet tuplet = new Tuplet(list);
        
        assertEquals(" \\times 2/3 {  c8  d8  e8  } ", tuplet.toLilypondString());
    }
}