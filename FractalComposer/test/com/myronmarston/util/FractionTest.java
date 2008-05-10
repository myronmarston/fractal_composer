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