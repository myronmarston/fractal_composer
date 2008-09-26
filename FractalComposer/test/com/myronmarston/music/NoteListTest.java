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

import com.myronmarston.music.scales.*;
import com.myronmarston.music.settings.*;
import com.myronmarston.util.Fraction;        
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class NoteListTest {
    
    @Test
    public void getFirstAudibleNote() {
        Note soundedNote = new Note(2, 2, 2, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0);
        NoteList notes = new NoteList();
        notes.add(Note.createRest(new Fraction(2, 1))); // a rest
        notes.add(soundedNote);
        assertEquals(soundedNote, notes.getFirstAudibleNote());
    }        
    
    @Test
    public void getDuration() {
        NoteList germ = new NoteList();
        
        germ.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        germ.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        germ.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        germ.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        
        assertEquals(new Fraction(3, 1), germ.getDuration());
    }    
   
    @Test
    public void parseNoteListString() throws InvalidKeySignatureException, NoteStringParseException { 
        Scale s = new MajorScale(NoteName.D);
        NoteList germ = NoteList.parseNoteListString("D4,1/4,MF R,1/8 E4 F#4,F D4,1/4", s);
        
        NoteList expected = new NoteList();
        expected.add(new Note(0, 0, 4, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume(), s, 0));
        expected.add(Note.createRest(new Fraction(1,8)));
        expected.add(new Note(1, 1, 4, 0, new Fraction(1, 8), Dynamic.MF.getMidiVolume(), s, 0));
        expected.add(new Note(2, 2, 4, 0, new Fraction(1, 8), Dynamic.F.getMidiVolume(), s, 0));
        expected.add(new Note(0, 0, 4, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), s, 0));
        expected.get(0).setIsFirstNoteOfGermCopy(true);
        
        assertNoteListsEqual(expected, germ);             
        assertNoteListsEqual(expected, germ.clone());             
    }    
    
    @Test
    public void testClone() throws Exception {
        NoteList nl = NoteList.parseNoteListString("G4 C4 Ab4", Scale.DEFAULT);
        nl.setInstrument(Instrument.getInstrument("Viola"));
        NoteList cloned = nl.clone();
        assertNoteListsEqual(nl, cloned);
        
        // make sure that each note was cloned, rather than just pointing to
        // the same reference
        for (int i = 0; i < nl.size(); i++) {
            assertTrue(nl.get(i) != cloned.get(i));
        }                
    }
    
    @Test
    public void updateScale() throws Exception {
        Scale s1 = new MajorScale(NoteName.G);
        NoteList nl = NoteList.parseNoteListString("G4 A4 B4", s1);
        for (Note n : nl) {
            assertEquals(s1, n.getScale());
        }
        
        Scale s2 = new MajorPentatonicScale(NoteName.Bb);
        nl.updateScale(s2);
        for (Note n : nl) {
            assertEquals(s2, n.getScale());
        }
    }
    
    @Test
    public void getNumberOfAccidentals() throws Exception {
        String germString = "G4 A4 F4 E4 B4";
        NoteList nl = NoteList.parseNoteListString(germString, new MajorScale(NoteName.C));
        assertEquals(0, nl.getNumberOfAccidentals());
        
        nl = NoteList.parseNoteListString(germString, new MajorScale(NoteName.A));
        assertEquals(2, nl.getNumberOfAccidentals());
        
        nl = NoteList.parseNoteListString(germString, new MajorScale(NoteName.G));
        assertEquals(1, nl.getNumberOfAccidentals());
        
        nl = NoteList.parseNoteListString(germString, new MajorPentatonicScale(NoteName.C));
        assertEquals(2, nl.getNumberOfAccidentals());
    }
    
    @Test
    public void setSoureVoiceSectionOnAllNotes() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        
        VoiceSection vs = fp.getVoices().get(0).getVoiceSections().get(0);
        
        NoteList nl = NoteList.parseNoteListString("C4 D4 E4 F4", Scale.DEFAULT);        
        for (Note n : nl) assertNull(n.getSourceVoiceSection());
        
        nl.setSourceVoiceSectionOnAllNotes(vs);
        for (Note n : nl) assertTrue(n.getSourceVoiceSection() == vs);
    }
    
    @Test
    public void getListWithNormalizedRest() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.setGermString("C3");
        fp.createDefaultSettings();
        Voice v1 = fp.getVoices().get(0);
                
        v1.getVoiceSections().get(0).setRest(true);
        v1.getVoiceSections().get(1).setRest(true);
        v1.getVoiceSections().get(2).setRest(true);
        NoteList nl = v1.getEntireVoice();
        
        NoteList normalized = nl.getListWithNormalizedRests();
        assertTrue(normalized.size() < nl.size());
        NoteList expected = NoteList.parseNoteListString("R,1/2 R,1/2 R,1/2 C4,1/8 C4 C4 C4", Scale.DEFAULT);
        expected.setSourceVoiceSectionOnAllNotes(v1.getVoiceSections().get(3));        
        expected.get(0).setSourceVoiceSection(v1.getVoiceSections().get(0));
        expected.get(1).setSourceVoiceSection(v1.getVoiceSections().get(1));
        expected.get(2).setSourceVoiceSection(v1.getVoiceSections().get(2));
        
        expected.setfirstNotesOfGermCopy(3, 4, 5, 6);        
        NoteListTest.assertNoteListsEqual(expected, normalized);                                
    }
    
    @Test    
    public void getListWithNormalizedRests_oneNote() throws Exception {
        NoteList nl = NoteList.parseNoteListString("G4", Scale.DEFAULT);
        NoteList normalized = nl.getListWithNormalizedRests();
        NoteList expected = NoteList.parseNoteListString("G4", Scale.DEFAULT);
        NoteListTest.assertNoteListsEqual(expected, normalized);
    }        
    
    @Test
    public void getReadOnlyCopy() throws Exception {
        NoteList nl = NoteList.parseNoteListString("G4 A4 B4", Scale.DEFAULT);
        NoteList readOnlyCopy = nl.getReadOnlyCopy();
        
        try {
            readOnlyCopy.add(Note.createRest(new Fraction("1/4")));
            fail();
        } catch (UnsupportedOperationException ex) {}
        
        try {
            readOnlyCopy.remove(0);
            fail();
        } catch (UnsupportedOperationException ex) {}
        
        try {
            readOnlyCopy.set(1, Note.createRest(new Fraction("1/4")));
            fail();
        } catch (UnsupportedOperationException ex) {}
        
        assertFalse(nl.isReadOnly());
        assertTrue(readOnlyCopy.isReadOnly());
        
        // the clone should be modifiable...
        assertFalse(readOnlyCopy.clone().isReadOnly());
    }
    
    @Test(expected=NoteStringParseException.class)
    public void errorIfOnlyRests() throws Exception {
        NoteList.parseNoteListString("R,1/4", Scale.DEFAULT);
    }
    
    @Test
    public void regexMatchesValidStrings() {
        String[] validStrings = new String[] {
            "Ab4",
            "G4,3/8 A4,MF",
            " A4 Gb4,1/4 c2,3/7,PPP   d#4,MF",            
            " D#4,2 Gb4,4 c2,7,PPP   d#4,MF",    
            // originally, we didn't properly match a 
            // string with a note that has a dynamic
            // after a rest, because of the lookahead...
            " A4 GBb4,1/4 c2,3/7,PpP R d#4,MF",            
        };
        
        for (String validString : validStrings) {
            assertTrue("The note list string '" + validString + "' did not match the regex as expected.", NoteList.REGEX_PATTERN.matcher(validString).matches());
        }
    }
    
    @Test
    public void regexDoesNotMatchInValidStrings() {
        String[] invalidStrings = new String[] {
            "Ab43",
            "G4,3/8 A4,MF x",
            "G4,3/8 %  A4,MF",
            " A4 Gb4,1/4 c2,.3/7,PPP d#4,MF",            
            " A4 Gb4,1/4 c2,3/7,PPP R2 d#4,MF",
            "R,1/4"
        };
        
        for (String invalidString : invalidStrings) {
            assertFalse("The note list string '" + invalidString + "' did match the regex, and should not have.", NoteList.REGEX_PATTERN.matcher(invalidString).matches());
        }
    }
                
    public static void assertNoteListsEqual(NoteList expected, NoteList actual) {        
        assertNoteListsEqual(expected, actual, false);
    }
    
    public static void assertNoteListsEqual(NoteList expected, NoteList actual, boolean allowDifferentVoiceSectionReferences) {        
        assertEquals(expected.size(), actual.size());
        
        Instrument expectedInstr = (expected.getInstrument() == null ? Instrument.DEFAULT : expected.getInstrument());
        Instrument actualInstr = (actual.getInstrument() == null ? Instrument.DEFAULT : actual.getInstrument());                
        assertEquals(expectedInstr, actualInstr);
        
        assertEquals(expected.getDuration(), actual.getDuration());        
        NoteTest.assertNotesEqual(expected.getFirstAudibleNote(), actual.getFirstAudibleNote(), allowDifferentVoiceSectionReferences);
        for (int i = 0; i < actual.size(); i++) {            
            NoteTest.assertNotesEqual(expected.get(i), actual.get(i), allowDifferentVoiceSectionReferences);
        }   
        
        assertEquals(expected.isReadOnly(), actual.isReadOnly());
        testNoteListReadOnlyness(expected);
        testNoteListReadOnlyness(actual);
    }            
    
    public static void testNoteListReadOnlyness(NoteList nl) {
        if (!nl.isReadOnly()) return;
        try {
            nl.add(Note.createRest(new Fraction("1/4")));
            fail();
        } catch (UnsupportedOperationException ex) {}
        
        try {
            nl.remove(0);
            fail();
        } catch (UnsupportedOperationException ex) {}
        
        try {
            nl.set(1, Note.createRest(new Fraction("1/4")));
            fail();
        } catch (UnsupportedOperationException ex) {}
    }
} 