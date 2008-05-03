package com.myronmarston.music.scales;

import com.myronmarston.music.NoteName;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class TonalityTest {

    public TonalityTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void testIsValidKeyName() {
        assertTrue(Tonality.Major.isValidKeyName(NoteName.A));
        assertFalse(Tonality.Major.isValidKeyName(NoteName.As));
        
        assertTrue(Tonality.Minor.isValidKeyName(NoteName.A));
        assertFalse(Tonality.Minor.isValidKeyName(NoteName.Db));
    }

    @Test
    public void testGetValidKeyNames() {
        List<NoteName> majorKeyNames = Tonality.Major.getValidKeyNames();
        assertEquals(15, majorKeyNames.size());
        assertEquals(NoteName.Cb, majorKeyNames.get(0));
        assertEquals(NoteName.Gb, majorKeyNames.get(1));
        assertEquals(NoteName.Db, majorKeyNames.get(2));
        assertEquals(NoteName.Ab, majorKeyNames.get(3));
        assertEquals(NoteName.Eb, majorKeyNames.get(4));
        assertEquals(NoteName.Bb, majorKeyNames.get(5));
        assertEquals(NoteName.F, majorKeyNames.get(6));
        assertEquals(NoteName.C, majorKeyNames.get(7));
        assertEquals(NoteName.G, majorKeyNames.get(8));
        assertEquals(NoteName.D, majorKeyNames.get(9));
        assertEquals(NoteName.A, majorKeyNames.get(10));
        assertEquals(NoteName.E, majorKeyNames.get(11));
        assertEquals(NoteName.B, majorKeyNames.get(12));
        assertEquals(NoteName.Fs, majorKeyNames.get(13));
        assertEquals(NoteName.Cs, majorKeyNames.get(14));
        
        List<NoteName> minorKeyNames = Tonality.Minor.getValidKeyNames();
        assertEquals(15, minorKeyNames.size());
        assertEquals(NoteName.Ab, minorKeyNames.get(0));
        assertEquals(NoteName.Eb, minorKeyNames.get(1));
        assertEquals(NoteName.Bb, minorKeyNames.get(2));
        assertEquals(NoteName.F, minorKeyNames.get(3));
        assertEquals(NoteName.C, minorKeyNames.get(4));
        assertEquals(NoteName.G, minorKeyNames.get(5));
        assertEquals(NoteName.D, minorKeyNames.get(6));
        assertEquals(NoteName.A, minorKeyNames.get(7));
        assertEquals(NoteName.E, minorKeyNames.get(8));
        assertEquals(NoteName.B, minorKeyNames.get(9));
        assertEquals(NoteName.Fs, minorKeyNames.get(10));
        assertEquals(NoteName.Cs, minorKeyNames.get(11));
        assertEquals(NoteName.Gs, minorKeyNames.get(12));
        assertEquals(NoteName.Ds, minorKeyNames.get(13));
        assertEquals(NoteName.As, minorKeyNames.get(14));
    }

    @Test
    public void testGetSharpsOrFlatsForKeyName() {
        assertEquals(Tonality.Major.getSharpsOrFlatsForKeyName(NoteName.G), 1);
        assertEquals(Tonality.Major.getSharpsOrFlatsForKeyName(NoteName.E), 4);
        assertEquals(Tonality.Major.getSharpsOrFlatsForKeyName(NoteName.Ab), -4);
        assertEquals(Tonality.Minor.getSharpsOrFlatsForKeyName(NoteName.A), 0);
        assertEquals(Tonality.Minor.getSharpsOrFlatsForKeyName(NoteName.G), -2);
        assertEquals(Tonality.Minor.getSharpsOrFlatsForKeyName(NoteName.B), 2);
    }

}