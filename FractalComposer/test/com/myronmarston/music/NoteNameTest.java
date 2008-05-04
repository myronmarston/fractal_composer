package com.myronmarston.music;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class NoteNameTest {

    public NoteNameTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void getDefaultNoteNameForNumber() {
        assertEquals(NoteName.C, NoteName.getDefaultNoteNameForNumber(0));
        assertEquals(NoteName.Cs, NoteName.getDefaultNoteNameForNumber(1));
        assertEquals(NoteName.D, NoteName.getDefaultNoteNameForNumber(2));
        assertEquals(NoteName.Eb, NoteName.getDefaultNoteNameForNumber(3));
        assertEquals(NoteName.E, NoteName.getDefaultNoteNameForNumber(4));
        assertEquals(NoteName.F, NoteName.getDefaultNoteNameForNumber(5));
        assertEquals(NoteName.Fs, NoteName.getDefaultNoteNameForNumber(6));
        assertEquals(NoteName.G, NoteName.getDefaultNoteNameForNumber(7));
        assertEquals(NoteName.Ab, NoteName.getDefaultNoteNameForNumber(8));
        assertEquals(NoteName.A, NoteName.getDefaultNoteNameForNumber(9));
        assertEquals(NoteName.Bb, NoteName.getDefaultNoteNameForNumber(10));
        assertEquals(NoteName.B, NoteName.getDefaultNoteNameForNumber(11));                
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getDefaultNoteNameForNumber_ThrowsException() {
        NoteName k = NoteName.getDefaultNoteNameForNumber(12);
    } 
   
    @Test
    public void getPositiveIntervalSize() {
        assertEquals(2, NoteName.C.getPositiveIntervalSize(NoteName.Ex));
        assertEquals(4, NoteName.B.getPositiveIntervalSize(NoteName.F));
        assertEquals(3, NoteName.F.getPositiveIntervalSize(NoteName.Bbb));
        assertEquals(6, NoteName.Gbb.getPositiveIntervalSize(NoteName.Fx));        
    }
    
    @Test
    public void getPositiveChromaticSteps() {
        assertEquals(6, NoteName.C.getPositiveChromaticSteps(NoteName.Ex));
        assertEquals(6, NoteName.B.getPositiveChromaticSteps(NoteName.F));
        assertEquals(4, NoteName.F.getPositiveChromaticSteps(NoteName.Bbb));
        assertEquals(2, NoteName.Gbb.getPositiveChromaticSteps(NoteName.Fx)); 
    }      
    
    @Test
    public void toStringTest() {
        assertEquals("F#", NoteName.Fs.toString());
    }
}