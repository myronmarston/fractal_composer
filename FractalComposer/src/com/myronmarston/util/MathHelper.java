package com.myronmarston.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A class containing static helper methods for some math functions used to
 * generate fractal music.
 * 
 * @author Myron
 */
public class MathHelper {
    
    /**
     * Calculates the greatest common divisor of two numbers, using Euclid's 
     * algorithm.
     * 
     * @param m a number
     * @param n another number
     * @return the greatest common divisor of these two numbers
     */
    public static long greatestCommonDivisor(final long m, final long n) {
        return n == 0 ? m : greatestCommonDivisor(n, m % n);        
    }
    
    /**
     * Calculates the least common multiple of two numbers.
     * 
     * @param m a number
     * @param n another number
     * @return the least common multiple of these two numbers
     */
    public static long leastCommonMultiple(final long m, final long n) {
        return  m * (n / greatestCommonDivisor(m, n));
    }
    
    /**
     * Calculates the least common multiple of a list of numbers.
     * 
     * @param integers a list of integers
     * @return the least common multiple of this list
     */
    public static long leastCommonMultiple(List<Long> integers) {
        if (integers.size() == 0) throw new IllegalArgumentException("You passed an empty list.  The list must contain at least one value.");
        
        // if we only have one number, that's our LCM...
        if (integers.size() == 1) return integers.get(0);
        
        // get the LCM for the first two numbers...
        long firstTwoNumLCM = leastCommonMultiple(integers.get(0), integers.get(1));
                
        // if this is our whole list, we can return this...
        if (integers.size() == 2) return firstTwoNumLCM;
                
        // otherwise, construct a list, replacing the first two items with their LCM
        ArrayList<Long> restOfList = new ArrayList<Long>(integers.size() - 1);
        restOfList.add(firstTwoNumLCM);
        restOfList.addAll(integers.subList(2, integers.size()));
        
        // get the LCM of this smaller list...
        return leastCommonMultiple(restOfList);
    }        
    
    /**
     * Calculates the base two logarithm of the given number.
     * 
     * @param a number to take the log of     
     * @return the base 2 logarithm
     */
    public static double log2(double a) {
        return Math.log(a) / Math.log(2);        
    }    
}
