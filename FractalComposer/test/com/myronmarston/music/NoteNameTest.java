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

package com.myronmarston.music;

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
    
    @Test
    public void getNaturalNoteNameForLetterNumber() {       
        assertEquals(NoteName.C, NoteName.C.getNaturalNoteNameForLetterNumber(-7));
        assertEquals(NoteName.D, NoteName.C.getNaturalNoteNameForLetterNumber(-6));
        assertEquals(NoteName.E, NoteName.C.getNaturalNoteNameForLetterNumber(-5));
        assertEquals(NoteName.F, NoteName.C.getNaturalNoteNameForLetterNumber(-4));
        assertEquals(NoteName.G, NoteName.C.getNaturalNoteNameForLetterNumber(-3));
        assertEquals(NoteName.A, NoteName.C.getNaturalNoteNameForLetterNumber(-2));
        assertEquals(NoteName.B, NoteName.C.getNaturalNoteNameForLetterNumber(-1));
        assertEquals(NoteName.C, NoteName.C.getNaturalNoteNameForLetterNumber(0));
        assertEquals(NoteName.D, NoteName.C.getNaturalNoteNameForLetterNumber(1));
        assertEquals(NoteName.E, NoteName.C.getNaturalNoteNameForLetterNumber(2));
        assertEquals(NoteName.F, NoteName.C.getNaturalNoteNameForLetterNumber(3));
        assertEquals(NoteName.G, NoteName.C.getNaturalNoteNameForLetterNumber(4));
        assertEquals(NoteName.A, NoteName.C.getNaturalNoteNameForLetterNumber(5));
        assertEquals(NoteName.B, NoteName.C.getNaturalNoteNameForLetterNumber(6));
        
        assertEquals(NoteName.F, NoteName.Fs.getNaturalNoteNameForLetterNumber(-7));
        assertEquals(NoteName.G, NoteName.Fs.getNaturalNoteNameForLetterNumber(-6));
        assertEquals(NoteName.A, NoteName.Fs.getNaturalNoteNameForLetterNumber(-5));
        assertEquals(NoteName.B, NoteName.Fs.getNaturalNoteNameForLetterNumber(-4));
        assertEquals(NoteName.C, NoteName.Fs.getNaturalNoteNameForLetterNumber(-3));
        assertEquals(NoteName.D, NoteName.Fs.getNaturalNoteNameForLetterNumber(-2));
        assertEquals(NoteName.E, NoteName.Fs.getNaturalNoteNameForLetterNumber(-1));
        assertEquals(NoteName.F, NoteName.Fs.getNaturalNoteNameForLetterNumber(0));
        assertEquals(NoteName.G, NoteName.Fs.getNaturalNoteNameForLetterNumber(1));
        assertEquals(NoteName.A, NoteName.Fs.getNaturalNoteNameForLetterNumber(2));
        assertEquals(NoteName.B, NoteName.Fs.getNaturalNoteNameForLetterNumber(3));
        assertEquals(NoteName.C, NoteName.Fs.getNaturalNoteNameForLetterNumber(4));
        assertEquals(NoteName.D, NoteName.Fs.getNaturalNoteNameForLetterNumber(5));
        assertEquals(NoteName.E, NoteName.Fs.getNaturalNoteNameForLetterNumber(6));
        assertEquals(NoteName.F, NoteName.Fs.getNaturalNoteNameForLetterNumber(7)); 
        
        assertEquals(NoteName.A, NoteName.Abb.getNaturalNoteNameForLetterNumber(-7));
        assertEquals(NoteName.B, NoteName.Abb.getNaturalNoteNameForLetterNumber(-6));
        assertEquals(NoteName.C, NoteName.Abb.getNaturalNoteNameForLetterNumber(-5));
        assertEquals(NoteName.D, NoteName.Abb.getNaturalNoteNameForLetterNumber(-4));
        assertEquals(NoteName.E, NoteName.Abb.getNaturalNoteNameForLetterNumber(-3));
        assertEquals(NoteName.F, NoteName.Abb.getNaturalNoteNameForLetterNumber(-2));
        assertEquals(NoteName.G, NoteName.Abb.getNaturalNoteNameForLetterNumber(-1));
        assertEquals(NoteName.A, NoteName.Abb.getNaturalNoteNameForLetterNumber(0));
        assertEquals(NoteName.B, NoteName.Abb.getNaturalNoteNameForLetterNumber(1));
        assertEquals(NoteName.C, NoteName.Abb.getNaturalNoteNameForLetterNumber(2));
        assertEquals(NoteName.D, NoteName.Abb.getNaturalNoteNameForLetterNumber(3));
        assertEquals(NoteName.E, NoteName.Abb.getNaturalNoteNameForLetterNumber(4));
        assertEquals(NoteName.F, NoteName.Abb.getNaturalNoteNameForLetterNumber(5));
        assertEquals(NoteName.G, NoteName.Abb.getNaturalNoteNameForLetterNumber(6));
        assertEquals(NoteName.A, NoteName.Abb.getNaturalNoteNameForLetterNumber(7)); 
    }
    
    @Test
    public void getLetter() {
        assertEquals('C', NoteName.Cbb.getLetter(false));
        assertEquals('C', NoteName.Cb.getLetter(false));
        assertEquals('C', NoteName.C.getLetter(false));
        assertEquals('C', NoteName.Cs.getLetter(false));
        assertEquals('C', NoteName.Cx.getLetter(false));
        
        assertEquals('D', NoteName.Dbb.getLetter(false));
        assertEquals('D', NoteName.Db.getLetter(false));
        assertEquals('D', NoteName.D.getLetter(false));
        assertEquals('D', NoteName.Ds.getLetter(false));
        assertEquals('D', NoteName.Dx.getLetter(false));
        
        assertEquals('E', NoteName.Ebb.getLetter(false));
        assertEquals('E', NoteName.Eb.getLetter(false));
        assertEquals('E', NoteName.E.getLetter(false));
        assertEquals('E', NoteName.Es.getLetter(false));
        assertEquals('E', NoteName.Ex.getLetter(false));
        
        assertEquals('F', NoteName.Fbb.getLetter(false));
        assertEquals('F', NoteName.Fb.getLetter(false));
        assertEquals('F', NoteName.F.getLetter(false));
        assertEquals('F', NoteName.Fs.getLetter(false));
        assertEquals('F', NoteName.Fx.getLetter(false));
        
        assertEquals('G', NoteName.Gbb.getLetter(false));
        assertEquals('G', NoteName.Gb.getLetter(false));
        assertEquals('G', NoteName.G.getLetter(false));
        assertEquals('G', NoteName.Gs.getLetter(false));
        assertEquals('G', NoteName.Gx.getLetter(false));
        
        assertEquals('A', NoteName.Abb.getLetter(false));
        assertEquals('A', NoteName.Ab.getLetter(false));
        assertEquals('A', NoteName.A.getLetter(false));
        assertEquals('A', NoteName.As.getLetter(false));
        assertEquals('A', NoteName.Ax.getLetter(false));
        
        assertEquals('B', NoteName.Bbb.getLetter(false));
        assertEquals('B', NoteName.Bb.getLetter(false));
        assertEquals('B', NoteName.B.getLetter(false));
        assertEquals('B', NoteName.Bs.getLetter(false));
        assertEquals('B', NoteName.Bx.getLetter(false));
        
        assertEquals('c', NoteName.Cbb.getLetter(true));
        assertEquals('c', NoteName.Cb.getLetter(true));
        assertEquals('c', NoteName.C.getLetter(true));
        assertEquals('c', NoteName.Cs.getLetter(true));
        assertEquals('c', NoteName.Cx.getLetter(true));  
        
        assertEquals('d', NoteName.Dbb.getLetter(true));
        assertEquals('d', NoteName.Db.getLetter(true));
        assertEquals('d', NoteName.D.getLetter(true));
        assertEquals('d', NoteName.Ds.getLetter(true));
        assertEquals('d', NoteName.Dx.getLetter(true));
        
        assertEquals('e', NoteName.Ebb.getLetter(true));
        assertEquals('e', NoteName.Eb.getLetter(true));
        assertEquals('e', NoteName.E.getLetter(true));
        assertEquals('e', NoteName.Es.getLetter(true));
        assertEquals('e', NoteName.Ex.getLetter(true));
        
        assertEquals('f', NoteName.Fbb.getLetter(true));
        assertEquals('f', NoteName.Fb.getLetter(true));
        assertEquals('f', NoteName.F.getLetter(true));
        assertEquals('f', NoteName.Fs.getLetter(true));
        assertEquals('f', NoteName.Fx.getLetter(true));
        
        assertEquals('g', NoteName.Gbb.getLetter(true));
        assertEquals('g', NoteName.Gb.getLetter(true));
        assertEquals('g', NoteName.G.getLetter(true));
        assertEquals('g', NoteName.Gs.getLetter(true));
        assertEquals('g', NoteName.Gx.getLetter(true));
        
        assertEquals('a', NoteName.Abb.getLetter(true));
        assertEquals('a', NoteName.Ab.getLetter(true));
        assertEquals('a', NoteName.A.getLetter(true));
        assertEquals('a', NoteName.As.getLetter(true));
        assertEquals('a', NoteName.Ax.getLetter(true));
        
        assertEquals('b', NoteName.Bbb.getLetter(true));
        assertEquals('b', NoteName.Bb.getLetter(true));
        assertEquals('b', NoteName.B.getLetter(true));
        assertEquals('b', NoteName.Bs.getLetter(true));
        assertEquals('b', NoteName.Bx.getLetter(true));
    }        
}