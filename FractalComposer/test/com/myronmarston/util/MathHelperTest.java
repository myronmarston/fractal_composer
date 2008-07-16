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

import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class MathHelperTest {
    
    @Test
    public void greatestCommonDivisor() {
        long m = 12L;
        long n = 18L;
        long expResult = 6L;
        long result = MathHelper.greatestCommonDivisor(m, n);
        assertEquals(expResult, result);       
    }

    @Test
    public void leastCommonMultiple() {
        long m = 6L;
        long n = 8L;
        long expResult = 24L;
        long result = MathHelper.leastCommonMultiple(m, n);
        assertEquals(expResult, result);        
    }
    
    @Test
    public void leastCommonMultipleForList() {
        ArrayList<Long> list = new ArrayList<Long>();
        list.add(3L);
        list.add(8L);
        list.add(6L);
        list.add(12L);
        list.add(10L);
                
        long expResult = 120L;
        long result = MathHelper.leastCommonMultiple(list);
        assertEquals(expResult, result);        
    }
    
    @Test
    public void log2() {
        assertEquals(-3d, MathHelper.log2(0.125d));
        assertEquals(-2d, MathHelper.log2(0.25d));
        assertEquals(-1d, MathHelper.log2(0.5d));
        assertEquals(0d, MathHelper.log2(1));
        assertEquals(1d, MathHelper.log2(2));
        assertEquals(2d, MathHelper.log2(4));
        assertEquals(3d, MathHelper.log2(8));
        assertEquals(4d, MathHelper.log2(16));      
        
        // this should be 0.5, but doubles are not completely accurate....
        assertEquals(0.5000000000000001d, MathHelper.log2(Math.sqrt(2)));
    }

    @Test
    public void numIsPowerOf2() {
        assertEquals(false, MathHelper.numIsPowerOf2(0L));
        assertEquals(true, MathHelper.numIsPowerOf2(1L));
        assertEquals(true, MathHelper.numIsPowerOf2(2L));
        assertEquals(false, MathHelper.numIsPowerOf2(3L));
        assertEquals(true, MathHelper.numIsPowerOf2(4L));
        assertEquals(true, MathHelper.numIsPowerOf2(8L));
        assertEquals(true, MathHelper.numIsPowerOf2(16L));
        assertEquals(false, MathHelper.numIsPowerOf2(15L));
        assertEquals(true, MathHelper.numIsPowerOf2(256L));
    }
    
    @Test
    public void getNormalizedValue() {
        assertEquals(4, MathHelper.getNormalizedValue(4, 7));
        assertEquals(4, MathHelper.getNormalizedValue(11, 7));
        assertEquals(4, MathHelper.getNormalizedValue(18, 7));
        assertEquals(4, MathHelper.getNormalizedValue(-3, 7));
        assertEquals(4, MathHelper.getNormalizedValue(-10, 7));
    }
    
    @Test
    public void getLargestPowerOf2LessThanGivenNumber() {
        assertEquals(2L, MathHelper.getLargestPowerOf2LessThanGivenNumber(3));
        assertEquals(4L, MathHelper.getLargestPowerOf2LessThanGivenNumber(5));
        assertEquals(4L, MathHelper.getLargestPowerOf2LessThanGivenNumber(6));
        assertEquals(4L, MathHelper.getLargestPowerOf2LessThanGivenNumber(7));
        assertEquals(8L, MathHelper.getLargestPowerOf2LessThanGivenNumber(9));
        assertEquals(16L, MathHelper.getLargestPowerOf2LessThanGivenNumber(21));
    }        
}