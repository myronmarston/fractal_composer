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
    
    
    @Test
    public void getMidiPitchNumberForTonicAtOctave() {        
        assertEquals(58, NoteName.Cbb.getMidiPitchNumberAtOctave(4));
        assertEquals(59, NoteName.Cb.getMidiPitchNumberAtOctave(4));
        assertEquals(60, NoteName.C.getMidiPitchNumberAtOctave(4));
        assertEquals(61, NoteName.Cs.getMidiPitchNumberAtOctave(4));
        assertEquals(62, NoteName.Cx.getMidiPitchNumberAtOctave(4));
        
        assertEquals(60, NoteName.Dbb.getMidiPitchNumberAtOctave(4));
        assertEquals(61, NoteName.Db.getMidiPitchNumberAtOctave(4));
        assertEquals(62, NoteName.D.getMidiPitchNumberAtOctave(4));
        assertEquals(63, NoteName.Ds.getMidiPitchNumberAtOctave(4));
        assertEquals(64, NoteName.Dx.getMidiPitchNumberAtOctave(4));
        
        assertEquals(62, NoteName.Ebb.getMidiPitchNumberAtOctave(4));
        assertEquals(63, NoteName.Eb.getMidiPitchNumberAtOctave(4));
        assertEquals(64, NoteName.E.getMidiPitchNumberAtOctave(4));
        assertEquals(65, NoteName.Es.getMidiPitchNumberAtOctave(4));
        assertEquals(66, NoteName.Ex.getMidiPitchNumberAtOctave(4));
        
        assertEquals(63, NoteName.Fbb.getMidiPitchNumberAtOctave(4));
        assertEquals(64, NoteName.Fb.getMidiPitchNumberAtOctave(4));
        assertEquals(65, NoteName.F.getMidiPitchNumberAtOctave(4));
        assertEquals(66, NoteName.Fs.getMidiPitchNumberAtOctave(4));
        assertEquals(67, NoteName.Fx.getMidiPitchNumberAtOctave(4));
        
        assertEquals(65, NoteName.Gbb.getMidiPitchNumberAtOctave(4));
        assertEquals(66, NoteName.Gb.getMidiPitchNumberAtOctave(4));
        assertEquals(67, NoteName.G.getMidiPitchNumberAtOctave(4));
        assertEquals(68, NoteName.Gs.getMidiPitchNumberAtOctave(4));
        assertEquals(69, NoteName.Gx.getMidiPitchNumberAtOctave(4));
        
        assertEquals(67, NoteName.Abb.getMidiPitchNumberAtOctave(4));
        assertEquals(68, NoteName.Ab.getMidiPitchNumberAtOctave(4));
        assertEquals(69, NoteName.A.getMidiPitchNumberAtOctave(4));
        assertEquals(70, NoteName.As.getMidiPitchNumberAtOctave(4));
        assertEquals(71, NoteName.Ax.getMidiPitchNumberAtOctave(4));
        
        assertEquals(69, NoteName.Bbb.getMidiPitchNumberAtOctave(4));
        assertEquals(70, NoteName.Bb.getMidiPitchNumberAtOctave(4));
        assertEquals(71, NoteName.B.getMidiPitchNumberAtOctave(4));
        assertEquals(72, NoteName.Bs.getMidiPitchNumberAtOctave(4));
        assertEquals(73, NoteName.Bx.getMidiPitchNumberAtOctave(4));                     
    }
}