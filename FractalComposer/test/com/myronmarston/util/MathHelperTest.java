package com.myronmarston.util;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class MathHelperTest {

    public MathHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of greatestCommonDivisor method, of class MathHelper.
     */
    @Test
    public void greatestCommonDivisor() {
        System.out.println("greatestCommonDivisor");
        long m = 12L;
        long n = 18L;
        long expResult = 6L;
        long result = MathHelper.greatestCommonDivisor(m, n);
        assertEquals(expResult, result);       
    }

    /**
     * Test of leastCommonMultiple method, of class MathHelper.
     */
    @Test
    public void leastCommonMultiple() {
        System.out.println("leastCommonMultiple");
        long m = 6L;
        long n = 8L;
        long expResult = 24L;
        long result = MathHelper.leastCommonMultiple(m, n);
        assertEquals(expResult, result);        
    }
    
    /**
     * Test of leastCommonMultiple method, of class MathHelper.
     */
    @Test
    public void leastCommonMultipleForList() {
        System.out.println("leastCommonMultiple");
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

}