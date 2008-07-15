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

package com.myronmarston.util;

import java.util.regex.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class FractionTest {
    
    @Test(expected=IllegalArgumentException.class)
    public void zeroDenominator() {
        Fraction f = new Fraction(2, 0);
    }
    
    @Test
    public void badFractionStrings() {
        try{ Fraction f = new Fraction("asdjf"); fail(); } catch (IllegalArgumentException ex) {} // success.
        try{ Fraction f = new Fraction("1/03"); fail(); } catch (IllegalArgumentException ex) {} // success.
        try{ Fraction f = new Fraction("01/3"); fail(); } catch (IllegalArgumentException ex) {} // success.
        try{ Fraction f = new Fraction("1a3/3"); fail(); } catch (IllegalArgumentException ex) {} // success.
        try{ Fraction f = new Fraction("1/0"); fail(); } catch (IllegalArgumentException ex) {} // success.        
        try{ Fraction f = new Fraction("02"); fail(); } catch (IllegalArgumentException ex) {} // success.        
    }
    
    @Test
    public void goodFractionStrings() {
       testStringConstructor("1/3", 1L, 3L);       
       testStringConstructor("4/6", 2L, 3L);
       testStringConstructor("0/10", 0L, 1L);
       testStringConstructor("92342/77777", 92342L, 77777L);
       testStringConstructor("1", 1L, 1L);
       testStringConstructor("0", 0L, 1L);
       testStringConstructor("23422", 23422L, 1L);
    }
    
    @Test
    public void toGuidoString() {
        assertEquals("*3/4", (new Fraction(3, 4)).toGuidoString());
        assertEquals("*4", (new Fraction(12, 3)).toGuidoString());
        assertEquals("/7", (new Fraction(1, 7)).toGuidoString());        
    }
    
    @Test
    public void toLilypondString() {
        testToLilypondString("1/1", "%1$s1");
        testToLilypondString("1/2", "%1$s2");
        testToLilypondString("1/4", "%1$s4");
        testToLilypondString("1/8", "%1$s8");
        testToLilypondString("1/16", "%1$s16");
        testToLilypondString("1/32", "%1$s32");
        testToLilypondString("1/64", "%1$s64");
                
        testToLilypondString("3/8", "%1$s4.");
        testToLilypondString("7/16", "%1$s4..");
        testToLilypondString("7/32", "%1$s8..");
        testToLilypondString("7/4", "%1$s1..");
        testToLilypondString("3/2", "%1$s1.");
        testToLilypondString("3/1", "%1$s1 ~ %1$s1 ~ %1$s1");
        testToLilypondString("5/2", "%1$s1 ~ %1$s1 ~ %1$s2");
        testToLilypondString("21/32", "%1$s2 ~ %1$s8 ~ %1$s32");
        
        // durations with denoms greater than 64 are not allowed
        try {
            (new Fraction("1/128")).toLilypondString();
            fail();
        } catch (IllegalArgumentException ex) { }

        
        // 128 and higher denoms are not supported by lilypond, so we use a grace note hack.  This could work,
        // but a better solution is just to scale all the duration fractions so that we don't have any durations greater than 64
        //        testToLilypondString("1/128", "\\grace %1$s8 \\hideNotes %1$s1*1/128 \\unHideNotes");
        //        testToLilypondString("1/256", "\\grace %1$s16 \\hideNotes %1$s1*1/256 \\unHideNotes");
        //        testToLilypondString("1/512", "\\grace %1$s32 \\hideNotes %1$s1*1/512 \\unHideNotes");
        //        testToLilypondString("1/1024", "\\grace %1$s64 \\hideNotes %1$s1*1/1024 \\unHideNotes");
        //        testToLilypondString("1/2048", "\\grace %1$s64 \\hideNotes %1$s1*1/2048 \\unHideNotes");
        //        testToLilypondString("3/128", "%1$s64.");
        //        testToLilypondString("3/256", "\\grace %1$s8. \\hideNotes %1$s1*3/256 \\unHideNotes");
        //        testToLilypondString("7/256", "%1$s64..");
        //        testToLilypondString("7/512", "\\grace %1$s8.. \\hideNotes %1$s1*7/512 \\unHideNotes");

    }
        
    public static void testToLilypondString(String durationFraction, String expectedString) {
        String actualString = (new Fraction(durationFraction)).toLilypondString();
        assertEquals(expectedString, actualString);        
    }                
    
    
    @Test
    public void positiveRegex() {
        Pattern p = Pattern.compile(Fraction.POSITIVE_FRACTION_REGEX_STRING);          
        assertTrue(p.matcher("4/5").matches());
        assertTrue(p.matcher("23").matches());
        assertFalse(p.matcher("0/4").matches());
        assertFalse(p.matcher("0").matches());
        assertFalse(p.matcher("-3/7").matches());
        assertFalse(p.matcher("-1").matches());
    }
    
    private static void testStringConstructor(String str, long num, long den) {
         Fraction f = new Fraction(str);
         assertEquals(num, f.numerator_);
         assertEquals(den, f.denominator_);
    }
    
    @Test  
    public void getLargestPowerOf2FractionThatIsLessThanThis() {        
        assertEquals(new Fraction(2, 1), (new Fraction(5, 2)).getLargestPowerOf2FractionThatIsLessThanThis());
        assertEquals(new Fraction(1, 8), (new Fraction(3, 16)).getLargestPowerOf2FractionThatIsLessThanThis());
        assertEquals(new Fraction(1, 4), (new Fraction(5, 16)).getLargestPowerOf2FractionThatIsLessThanThis());
        assertEquals(new Fraction(1, 4), (new Fraction(3, 8)).getLargestPowerOf2FractionThatIsLessThanThis());
        assertEquals(new Fraction(3, 8), (new Fraction(7, 16)).getLargestPowerOf2FractionThatIsLessThanThis());
        assertEquals(new Fraction(1, 2), (new Fraction(9, 16)).getLargestPowerOf2FractionThatIsLessThanThis());
        assertEquals(new Fraction(3, 4), (new Fraction(7, 8)).getLargestPowerOf2FractionThatIsLessThanThis());
        
        try {
            (new Fraction(1, 8)).getLargestPowerOf2FractionThatIsLessThanThis();
            fail();
        } catch (UnsupportedOperationException ex) {}
        try {
            (new Fraction(3, 7)).getLargestPowerOf2FractionThatIsLessThanThis();
            fail();
        } catch (UnsupportedOperationException ex) {}
    }
    
    @Test
    public void denomIsPowerOf2() throws Exception {
        assertTrue((new Fraction("1/4")).denomIsPowerOf2());
        assertTrue((new Fraction("19/128")).denomIsPowerOf2());
        assertFalse((new Fraction("3/7")).denomIsPowerOf2());
    }
}