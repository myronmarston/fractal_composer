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

package com.myronmarston.music.notation;

import com.myronmarston.music.Instrument;
import com.myronmarston.music.scales.Scale;
import com.myronmarston.music.settings.TimeSignature;
import com.myronmarston.music.Tempo;
import com.myronmarston.util.*;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class NotationNoteTest {
    public static final Piece DEFAULT_PIECE = new Piece(Scale.DEFAULT.getKeySignature(), TimeSignature.DEFAULT, Tempo.DEFAULT);
    public static final Part DEFAULT_PART = new Part(DEFAULT_PIECE, Instrument.DEFAULT);
    
    @Test
    public void badConstructorArgs() throws Exception {
        // accidental < -2
        try {
            NotationNote nn = new NotationNote(DEFAULT_PART, 'c', 4, -3, new Fraction(1, 4));
            fail();
        } catch (IllegalArgumentException ex) {}
        
        // accidental > 2
        try {
            NotationNote nn = new NotationNote(DEFAULT_PART, 'c', 4, 3, new Fraction(1, 4));
            fail();
        } catch (IllegalArgumentException ex) {}
        
        // letter name note in a-g
        try {
            NotationNote nn = new NotationNote(DEFAULT_PART, 'i', 4, 0, new Fraction(1, 4));
            fail();
        } catch (IllegalArgumentException ex) {}        
        
        // duration = 0
        try {
            NotationNote nn = new NotationNote(DEFAULT_PART, 'c', 4, 0, new Fraction(0, 4));
            fail();
        } catch (IllegalArgumentException ex) {}        
        
        // duration negative
        try {
            NotationNote nn = new NotationNote(DEFAULT_PART, 'c', 4, 0, new Fraction(-0, 4));
            fail();
        } catch (IllegalArgumentException ex) {} 
    }
    
    @Test
    public void getLilypondOctaveString() {        
        assertEquals(",,,", (new NotationNote(DEFAULT_PART, 'c', 0, 0, new Fraction(1, 4)).getLilypondOctaveString()));        
        assertEquals(",,", (new NotationNote(DEFAULT_PART, 'c', 1, 0, new Fraction(1, 4)).getLilypondOctaveString()));        
        assertEquals(",", (new NotationNote(DEFAULT_PART, 'c', 2, 0, new Fraction(1, 4)).getLilypondOctaveString()));
        assertEquals("", (new NotationNote(DEFAULT_PART, 'c', 3, 0, new Fraction(1, 4)).getLilypondOctaveString()));
        assertEquals("'", (new NotationNote(DEFAULT_PART, 'c', 4, 0, new Fraction(1, 4)).getLilypondOctaveString()));
        assertEquals("''", (new NotationNote(DEFAULT_PART, 'c', 5, 0, new Fraction(1, 4)).getLilypondOctaveString()));
        assertEquals("'''", (new NotationNote(DEFAULT_PART, 'c', 6, 0, new Fraction(1, 4)).getLilypondOctaveString()));                
    }
    
    @Test
    public void getLilypondAccidentalString() {
        assertEquals("ff", (new NotationNote(DEFAULT_PART, 'c', 4, -2, new Fraction(1, 4)).getLilypondAccidentalString()));        
        assertEquals("f", (new NotationNote(DEFAULT_PART, 'c', 4, -1, new Fraction(1, 4)).getLilypondAccidentalString()));        
        assertEquals("", (new NotationNote(DEFAULT_PART, 'c', 4, 0, new Fraction(1, 4)).getLilypondAccidentalString()));
        assertEquals("s", (new NotationNote(DEFAULT_PART, 'c', 4, 1, new Fraction(1, 4)).getLilypondAccidentalString()));
        assertEquals("ss", (new NotationNote(DEFAULT_PART, 'c', 4, 2, new Fraction(1, 4)).getLilypondAccidentalString()));
    }
        
    @Test
    public void toLilypondString() {
        assertEquals(" cs4. ", (new NotationNote(DEFAULT_PART, 'c', 3, 1, new Fraction(3, 8))).toLilypondString());
        assertEquals(" bf''2 ~ bf''8 ", (new NotationNote(DEFAULT_PART, 'b', 5, -1, new Fraction(5, 8))).toLilypondString());
        assertEquals(" dss,,4 ", (new NotationNote(DEFAULT_PART, 'd', 1, 2, new Fraction(1, 4))).toLilypondString());
        assertEquals(" r2.. ", (NotationNote.createRest(DEFAULT_PART, new Fraction(7, 8))).toLilypondString());
        assertEquals(" r1 ~ r1 ~ r2 ", (NotationNote.createRest(DEFAULT_PART, new Fraction(5, 2))).toLilypondString());        
                        
        assertEquals(" \\times 2/3 {  c4  } ", (new NotationNote(DEFAULT_PART, 'c', 3, 0, new Fraction(1, 6))).toLilypondString());
    }
         
    @Test
    public void getLilypondDurationString() {
        testLilypondDurationStrings("1/1", "%1$s1");
        testLilypondDurationStrings("1/2", "%1$s2");
        testLilypondDurationStrings("1/4", "%1$s4");
        testLilypondDurationStrings("1/8", "%1$s8");
        testLilypondDurationStrings("1/16", "%1$s16");
        testLilypondDurationStrings("1/32", "%1$s32");
        testLilypondDurationStrings("1/64", "%1$s64");
                
        testLilypondDurationStrings("3/8", "%1$s4.");
        testLilypondDurationStrings("7/16", "%1$s4..");
        testLilypondDurationStrings("7/32", "%1$s8..");
        testLilypondDurationStrings("7/4", "%1$s1..");
        testLilypondDurationStrings("3/2", "%1$s1.");
        testLilypondDurationStrings("3/1", "%1$s1 ~ %1$s1 ~ %1$s1");
        testLilypondDurationStrings("5/2", "%1$s1 ~ %1$s1 ~ %1$s2");
        testLilypondDurationStrings("21/32", "%1$s2 ~ %1$s8 ~ %1$s32");
        
        // durations with denoms greater than 64 are not allowed
        try {
            NotationNote.getLilypondDurationString(new Fraction("1/128"));
            fail();
        } catch (IllegalArgumentException ex) { }

        
// 128 and higher denoms are not supported by lilypond, so we use a grace note hack.  This could work,
// but a better solution is just to scale all the duration fractions so that we don't have any durations greater than 64
//        testLilypondDurationStrings("1/128", "\\grace %1$s8 \\hideNotes %1$s1*1/128 \\unHideNotes");
//        testLilypondDurationStrings("1/256", "\\grace %1$s16 \\hideNotes %1$s1*1/256 \\unHideNotes");
//        testLilypondDurationStrings("1/512", "\\grace %1$s32 \\hideNotes %1$s1*1/512 \\unHideNotes");
//        testLilypondDurationStrings("1/1024", "\\grace %1$s64 \\hideNotes %1$s1*1/1024 \\unHideNotes");
//        testLilypondDurationStrings("1/2048", "\\grace %1$s64 \\hideNotes %1$s1*1/2048 \\unHideNotes");
//        testLilypondDurationStrings("3/128", "%1$s64.");
//        testLilypondDurationStrings("3/256", "\\grace %1$s8. \\hideNotes %1$s1*3/256 \\unHideNotes");
//        testLilypondDurationStrings("7/256", "%1$s64..");
//        testLilypondDurationStrings("7/512", "\\grace %1$s8.. \\hideNotes %1$s1*7/512 \\unHideNotes");

    }
        
    public static void testLilypondDurationStrings(String durationFraction, String expectedString) {
        String actualString = NotationNote.getLilypondDurationString(new Fraction(durationFraction));
        assertEquals(expectedString, actualString);        
    }                
    
    @Test
    public void applyTupletMultiplier() throws Exception {
        testApplyTupletMultiplier("1/12", "2/3", "1/8");
        testApplyTupletMultiplier("1/10", "4/5", "1/8");
        
        try {
            testApplyTupletMultiplier("1/10", "-4/5", "1/8");            
            fail();
        } catch (IllegalArgumentException ex) {}
        
        try {
            testApplyTupletMultiplier("1/10", "0/5", "1/8");
            fail();
        } catch (IllegalArgumentException ex) {}                
    }
    
    private static void testApplyTupletMultiplier(String originalDuration, String multiplier, String expectedDuration) {
        NotationNote nn = new NotationNote(NotationNoteTest.DEFAULT_PART, 'c', 4, 0, new Fraction(originalDuration));
        NotationNote nn2 = nn.applyTupletMultiplier(new Fraction(multiplier));
        
        assertEquals(nn.getAccidental(), nn2.getAccidental());
        assertEquals(nn.getLetterName(), nn2.getLetterName());
        assertEquals(nn.getOctave(), nn2.getOctave());
        assertEquals(nn.getPart(), nn2.getPart());
        assertEquals(new Fraction(expectedDuration), nn2.getDuration());
    }
}