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

import com.myronmarston.music.*;
import com.myronmarston.music.scales.Scale;
import com.myronmarston.music.settings.*;
import com.myronmarston.util.*;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class NotationNoteTest { 
    public static final Section DEFAULT_SECTION = (new FractalPiece()).createSection();
    public static final VoiceSection DEFAULT_VOICE_SECTION = DEFAULT_SECTION.getFractalPiece().createVoice().getVoiceSections().get(0);
    public static final Piece DEFAULT_PIECE = new Piece(Scale.DEFAULT.getKeySignature(), TimeSignature.DEFAULT, Tempo.DEFAULT, true, true);
    public static final Part DEFAULT_PART = new Part(DEFAULT_PIECE, Instrument.DEFAULT);
    public static final PartSection DEFAULT_PART_SECTION = new PartSection(DEFAULT_PART, DEFAULT_VOICE_SECTION);
    
    public static NotationNote instantiateTestNote(String duration) {
        return new NotationNote(NotationNoteTest.DEFAULT_PART_SECTION, 'c', 4, 0, new Fraction(duration), new Fraction(4, 4), MidiNote.DEFAULT_VELOCITY, false);
    }
    
    public static NotationNote getNotationNoteForNoteString(String noteString) throws Exception {
        Note n = Note.parseNoteString(noteString, Scale.DEFAULT, new Fraction(1, 4), Dynamic.MF.getMidiVolume());
        NotationNote nn = n.toNotationNote(DEFAULT_PART_SECTION, n.convertToMidiNote(new Fraction(0, 1), 480, 0, true), new Fraction(4, 4));
        return nn;
    }
    
    @Test
    public void badConstructorArgs() throws Exception {
        // accidental < -2
        try {
            NotationNote nn = new NotationNote(DEFAULT_PART_SECTION, 'c', 4, -3, new Fraction(1, 4), new Fraction(4, 4), MidiNote.DEFAULT_VELOCITY, false);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        // accidental > 2
        try {
            NotationNote nn = new NotationNote(DEFAULT_PART_SECTION, 'c', 4, 3, new Fraction(1, 4), new Fraction(4, 4), MidiNote.DEFAULT_VELOCITY, false);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        // letter name note in a-g
        try {
            NotationNote nn = new NotationNote(DEFAULT_PART_SECTION, 'i', 4, 0, new Fraction(1, 4), new Fraction(4, 4), MidiNote.DEFAULT_VELOCITY, false);
            fail();
        } catch (IllegalArgumentException ex) {}        
        
        // duration = 0
        try {
            NotationNote nn = new NotationNote(DEFAULT_PART_SECTION, 'c', 4, 0, new Fraction(0, 4), new Fraction(4, 4), MidiNote.DEFAULT_VELOCITY, false);
            fail();
        } catch (IllegalArgumentException ex) {}        
        
        // duration negative
        try {
            NotationNote nn = new NotationNote(DEFAULT_PART_SECTION, 'c', 4, 0, new Fraction(-0, 4), new Fraction(4, 4), MidiNote.DEFAULT_VELOCITY, false);
            fail();
        } catch (IllegalArgumentException ex) {} 
        
        //TODO: volume
        try {
            NotationNote nn = new NotationNote(DEFAULT_PART_SECTION, 'c', 4, 0, new Fraction(1, 4), new Fraction(4, 4), MidiNote.MIN_VELOCITY - 1, false);
            fail();
        } catch (IllegalArgumentException ex) {} 
                        
        try {
            NotationNote nn = new NotationNote(DEFAULT_PART_SECTION, 'c', 4, 0, new Fraction(1, 4), new Fraction(4, 4), MidiNote.MAX_VELOCITY + 1, false);
            fail();
        } catch (IllegalArgumentException ex) {} 
    }
    
    @Test
    public void getLilypondOctaveString() {        
        testGetLilypondOctaveString(",,,", 0);
        testGetLilypondOctaveString(",,", 1);
        testGetLilypondOctaveString(",", 2);
        testGetLilypondOctaveString("", 3);
        testGetLilypondOctaveString("'", 4);
        testGetLilypondOctaveString("''", 5);
        testGetLilypondOctaveString("'''", 6);        
    }
    
    private static void testGetLilypondOctaveString(String result, int octave) {
        assertEquals(result, (new NotationNote(DEFAULT_PART_SECTION, 'c', octave, 0, new Fraction(1, 4), new Fraction(4, 4), MidiNote.DEFAULT_VELOCITY, false).getLilypondOctaveString()));
    }
    
    @Test
    public void getGuidoOctave() throws Exception {
        testGetGuidoOctave(0, -3);
        testGetGuidoOctave(1, -2);
        testGetGuidoOctave(2, -1);
        testGetGuidoOctave(3, 0);
        testGetGuidoOctave(4, 1);
        testGetGuidoOctave(5, 2);
        testGetGuidoOctave(6, 3);
        testGetGuidoOctave(7, 4);
        testGetGuidoOctave(8, 5);
    }
    
    private static void testGetGuidoOctave(int notationNoteOctave, int guidoOctave) throws Exception {
        NotationNote nn = new NotationNote(DEFAULT_PART_SECTION, 'c', notationNoteOctave, 0, new Fraction(1, 4), new Fraction(4, 4), MidiNote.DEFAULT_VELOCITY, false);
        assertEquals(guidoOctave, nn.getGuidoOctave());
    }
    
    @Test
    public void getLilypondAccidentalString() {
        testGetLilypondAccidentalString("ff", -2);
        testGetLilypondAccidentalString("f", -1);
        testGetLilypondAccidentalString("", 0);
        testGetLilypondAccidentalString("s", 1);
        testGetLilypondAccidentalString("ss", 2);        
    }
    
    private static void testGetLilypondAccidentalString(String result, int accidental) {
        assertEquals(result, (new NotationNote(DEFAULT_PART_SECTION, 'c', 4, accidental, new Fraction(1, 4), new Fraction(4, 4), MidiNote.DEFAULT_VELOCITY, false).getLilypondAccidentalString()));
    }
    
    @Test
    public void getGuidoAccidentalString() throws Exception {
        testGetGuidoAccidentalString(-2, "&&");
        testGetGuidoAccidentalString(-1, "&");
        testGetGuidoAccidentalString(0, "");
        testGetGuidoAccidentalString(1, "#");
        testGetGuidoAccidentalString(2, "##");
    }
    
    private static void testGetGuidoAccidentalString(int accidental, String result) throws Exception {
        NotationNote nn = new NotationNote(DEFAULT_PART_SECTION, 'c', 4, accidental, new Fraction(1, 4), new Fraction(4, 4), MidiNote.DEFAULT_VELOCITY, false);
        assertEquals(result, nn.getGuidoAccidentalString());
    }
        
    @Test
    public void toLilypondString() throws Exception {
        testToLilypondString("C#3,3/8", new NotationDynamic(Dynamic.F, NotationDynamic.Articulation.NONE), "cs4.\\f");
        testToLilypondString("Bb5,5/8", new NotationDynamic(Dynamic.MF, NotationDynamic.Articulation.ACCENT), "bf''2\\mf-> ~ bf''8");
        testToLilypondString("Dx1,1/4", NotationDynamic.DEFAULT_EMPTY, "dss,,4");
        testToLilypondString("R,7/8", NotationDynamic.DEFAULT_EMPTY, "r2..");
        testToLilypondString("R,5/2", NotationDynamic.DEFAULT_EMPTY, "r1 ~ r1 ~ r2");
        testToLilypondString("C3,1/6", NotationDynamic.DEFAULT_EMPTY, "\\times 2/3 { c4 }");
        testToLilypondString("C3,1/6", new NotationDynamic(Dynamic.FF, NotationDynamic.Articulation.NONE), "\\times 2/3 { c4\\ff }");
    }
    
    private static void testToLilypondString(String noteString, NotationDynamic dynamic, String lilypondString) throws Exception {
        NotationNote nn = getNotationNoteForNoteString(noteString);
        nn.setDynamic(dynamic);
        assertEquals(lilypondString, nn.toLilypondString());
    }
    
    @Test
    public void toGuidoString() throws Exception {
        testToGuidoString("C#4,1/16", NotationDynamic.DEFAULT_EMPTY, "c#1/16");
        testToGuidoString("Bb2,5/8", NotationDynamic.DEFAULT_EMPTY, "b&-1*5/8");
        testToGuidoString("R,1/4", NotationDynamic.DEFAULT_EMPTY, "_/4");
        
        // test notation dynamics...
        testToGuidoString("C#4,1/16,MF", new NotationDynamic(Dynamic.MF, NotationDynamic.Articulation.NONE), "\\intens<\"mf\"> c#1/16");
        testToGuidoString("C#4,1/16,PP", new NotationDynamic(Dynamic.PP, NotationDynamic.Articulation.ACCENT), "\\intens<\"pp\"> \\accent(c#1/16)");
        testToGuidoString("Bb2,5/8", new NotationDynamic(null, NotationDynamic.Articulation.MARCATO), "\\marcato(b&-1*5/8)");
    }
    
    private static void testToGuidoString(String noteString, NotationDynamic dynamic, String guidoString) throws Exception {
        NotationNote nn = getNotationNoteForNoteString(noteString);
        nn.setDynamic(dynamic);
        assertEquals(guidoString, nn.toGuidoString());
    }
             
    @Test
    public void applyTupletMultiplier() throws Exception {
        testApplyTupletMultiplier("2/3", "1/12", "1/8", "1/6");
        testApplyTupletMultiplier("4/5", "1/10", "1/8", "4/4");
        
        try {
            testApplyTupletMultiplier("-4/5", "1/10", "1/8", "1/1");            
            fail();
        } catch (IllegalArgumentException ex) {}
        
        try {
            testApplyTupletMultiplier("0/5", "1/10", "1/8", "1/1");
            fail();
        } catch (IllegalArgumentException ex) {}                
    }
    
    private static void testApplyTupletMultiplier(String multiplier, String originalDuration, String expectedDuration, String timeLeftInBar) {
        NotationNote nn = new NotationNote(NotationNoteTest.DEFAULT_PART_SECTION, 'c', 4, 0, new Fraction(originalDuration), new Fraction(timeLeftInBar), MidiNote.DEFAULT_VELOCITY, false);
        NotationNote nn2 = nn.applyTupletMultiplier(new Fraction(multiplier));
        
        assertEquals(nn.getAccidental(), nn2.getAccidental());
        assertEquals(nn.getLetterName(), nn2.getLetterName());
        assertEquals(nn.getOctave(), nn2.getOctave());
        assertEquals(nn.getPartSection(), nn2.getPartSection());
        assertEquals(new Fraction(expectedDuration), nn2.getDuration());
        assertEquals(new Fraction(timeLeftInBar), nn2.getTimeLeftInBar());
        assertEquals(new Fraction(multiplier), nn2.getTupletMultiplier());
    }
    
    @Test
    public void applyTupletMultiplier_rest() throws Exception {
        NotationNote nn = NotationNote.createRest(DEFAULT_PART_SECTION, new Fraction("1/6"), new Fraction("4/4"), false);
        NotationNote result = nn.applyTupletMultiplier(new Fraction("2/3"));
        assertEquals(new Fraction("1/4"), result.getDuration());
        assertEquals(new Fraction("4/4"), result.getTimeLeftInBar());
        assertTrue(result.isRest());
    }
    
    @Test
    public void getLargestDurationDenominator() throws Exception {
        testGetLargestDurationDenominator("3/8", 8L);
        testGetLargestDurationDenominator("3/100", 100L);
        testGetLargestDurationDenominator("4/64", 16L);
    }
    
    private static void testGetLargestDurationDenominator(String duration, long expected) {
        NotationNote nn = new NotationNote(NotationNoteTest.DEFAULT_PART_SECTION, 'c', 4, 0, new Fraction(duration), new Fraction(4, 4), MidiNote.DEFAULT_VELOCITY, false);
        assertEquals(expected, nn.getLargestDurationDenominator());
    }
    
    @Test
    public void scaleDurations() throws Exception {
        testScaleDurations("3/8", "3/2", "4/4", "4/1", 4);
        testScaleDurations("1/512", "1/64", "9/16", "9/2", 8);
    }
    
    private static void testScaleDurations(String originalDuration, String expectedDuration, String originalTimeLeftInBar, String expectedTimeLeftInBar, long scaleFactor) throws Exception {    
        NotationNote nn = new NotationNote(NotationNoteTest.DEFAULT_PART_SECTION, 'c', 4, 0, new Fraction(originalDuration), new Fraction(originalTimeLeftInBar), MidiNote.DEFAULT_VELOCITY, false);
        nn.scaleDurations(scaleFactor);
        assertEquals(new Fraction(expectedDuration), nn.getDuration());        
        assertEquals(new Fraction(expectedTimeLeftInBar), nn.getTimeLeftInBar());        
    }
    
    @Test
    public void getNotationStaffNoteNumber() throws Exception {
        testGetNotationStaffNoteNumber("C0,1/4", 0);
        testGetNotationStaffNoteNumber("C1,1/4", 7);
        testGetNotationStaffNoteNumber("Ebb2,1/4", 16);
        testGetNotationStaffNoteNumber("Fx5,1/4", 38);
        
        try {
            NotationNote.createRest(DEFAULT_PART_SECTION, new Fraction(1, 4), new Fraction(4, 4), false).getNotationStaffNoteNumber();
            fail();
        } catch (UnsupportedOperationException ex) {}
    }
    
    private static void testGetNotationStaffNoteNumber(String noteString, int expectedNumber) throws Exception {
        NotationNote nn = getNotationNoteForNoteString(noteString);
        assertEquals(expectedNumber, nn.getNotationStaffNoteNumber());
    }
    
    @Test
    public void getNotationNotes() throws Exception {
        NotationNote nn = NotationNoteTest.instantiateTestNote("1/4");
        List<NotationNote> list = nn.getNotationNotes();
        assertEquals(1, list.size());
        assertTrue(list.get(0) == nn);
    }
}