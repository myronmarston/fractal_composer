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
    
    private static void testStringConstructor(String str, long num, long den) {
         Fraction f = new Fraction(str);
         assertEquals(num, f.numerator_);
         assertEquals(den, f.denominator_);
    }
}