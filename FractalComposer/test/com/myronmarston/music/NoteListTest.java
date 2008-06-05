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

import com.myronmarston.music.scales.InvalidKeySignatureException;
import com.myronmarston.music.scales.MajorScale;
import com.myronmarston.music.scales.Scale;

import com.myronmarston.util.Fraction;
        
import java.util.*;

import javax.sound.midi.Track;
import javax.sound.midi.Sequence;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class NoteListTest {
    
    @Test
    public void getFirstAudibleNote() {
        Note soundedNote = new Note(2, 2, 2, 0, new Fraction(1, 1), 64, null, 0);
        NoteList notes = new NoteList();
        notes.add(Note.createRest(new Fraction(2, 1))); // a rest
        notes.add(soundedNote);
        assertEquals(soundedNote, notes.getFirstAudibleNote());
    }        
    
    @Test
    public void getDuration() {
        NoteList germ = new NoteList();
        
        germ.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, null, 0));
        germ.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, null, 0));
        germ.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 64, null, 0));
        germ.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, null, 0));
        
        assertEquals(new Fraction(3, 1), germ.getDuration());
    }    
   
    @Test
    public void parseNoteListString() throws InvalidKeySignatureException, NoteStringParseException { 
        NoteList germ = NoteList.parseNoteListString("D4,1/4,MF R,1/8 E4 F#4,F D4,1/4", new MajorScale(NoteName.D));
        
        NoteList expected = new NoteList();
        expected.add(new Note(0, 0, 4, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume(), null, 0));
        expected.add(Note.createRest(new Fraction(1,8)));
        expected.add(new Note(1, 1, 4, 0, new Fraction(1, 8), Dynamic.MF.getMidiVolume(), null, 0));
        expected.add(new Note(2, 2, 4, 0, new Fraction(1, 8), Dynamic.F.getMidiVolume(), null, 0));
        expected.add(new Note(0, 0, 4, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), null, 0));
        
        assertNoteListsEqual(expected, germ);             
    }
    
    @Test
    public void testClone() throws Exception {
        NoteList nl = NoteList.parseNoteListString("G4 C4 Ab4", Scale.DEFAULT);
        nl.setInstrument(Instrument.getInstrument("Viola"));
        NoteList cloned = (NoteList) nl.clone();
        assertNoteListsEqual(nl, cloned);
        
        // make sure that each note was cloned, rather than just pointing to
        // the same reference
        for (int i = 0; i < nl.size(); i++) {
            assertTrue(nl.get(i) != cloned.get(i));
        }                
    }
                
    public static void assertNoteListsEqual(NoteList expected, NoteList actual) {        
        assertNoteListsEqual(expected, actual, null);
    }
    
    public static void assertNoteListsEqual(NoteList expected, NoteList actual, Scale scaleForNormalization) {        
        assertEquals(expected.size(), actual.size());
        
        Instrument expectedInstr = (expected.getInstrument() == null ? Instrument.getDefault() : expected.getInstrument());
        Instrument actualInstr = (actual.getInstrument() == null ? Instrument.getDefault() : actual.getInstrument());                
        assertEquals(expectedInstr, actualInstr);
        
        assertEquals(expected.getDuration(), actual.getDuration());
        assertEquals(expected.getFirstAudibleNote(), actual.getFirstAudibleNote());
        for (int i = 0; i < actual.size(); i++) {
            Note expectedNote = expected.get(i);
            Note actualNote = actual.get(i);
            
            if (scaleForNormalization != null) {
                expectedNote = expectedNote.getNormalizedNote(scaleForNormalization);
                actualNote = actualNote.getNormalizedNote(scaleForNormalization);
            }
            assertEquals(expectedNote, actualNote);
        }                                
    }
}