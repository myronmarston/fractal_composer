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

import com.myronmarston.music.NoteStringInvalidPartException.NoteStringPart;
import com.myronmarston.music.scales.*;
import com.myronmarston.util.Fraction;
import com.myronmarston.music.notation.*;

import com.myronmarston.music.settings.TimeSignature;
import javax.sound.midi.InvalidMidiDataException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class NoteTest {

    @Test(expected=UnsupportedOperationException.class)
    public void errorIfChangeRest() {        
        Note n = Note.createRest(new Fraction(5, 1));
        n.setScaleStep(2);
    }
    
    @Test
    public void cloneForRest() {
        Note n = Note.createRest(new Fraction(3, 1));
        assertEquals(n, n.clone());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructNoDurationNote() {
        Note n = new Note(2, 2, 2, 0, new Fraction(0, 1), 64, Scale.DEFAULT, 0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructNoDurationRest() {
        Note n = Note.createRest(new Fraction(0, 1));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void setNoteDurationToZero() {
        Note n = new Note(2, 2, 2, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0);
        n.setDuration(new Fraction(0, 1));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void setBadVolume() {
        Note n = new Note(2, 2, 2, 0, new Fraction(1, 1), 128, Scale.DEFAULT, 0);
    }
    
    @Test
    public void parseNoteString() throws InvalidKeySignatureException, NoteStringParseException {
        // basic test...
        parseNoteStringTestHelper(new Note(2, 2, 3, 0, new Fraction(3, 16), Dynamic.PP.getMidiVolume(), new MajorScale(NoteName.B), 0), "D#4,3/16,PP", new MajorScale(NoteName.B), null, null);                
        
        // an accidental...
        parseNoteStringTestHelper(new Note(6, 0, 3, -1, new Fraction(1, 2), Dynamic.FFF.getMidiVolume(), new MajorPentatonicScale(NoteName.C), 0), "B2,1/2,FFF", new MajorPentatonicScale(NoteName.C), null, null);
        
        // a non-standard rhythm and using a default volume
        parseNoteStringTestHelper(new Note(3, 3, 2, -2, new Fraction(1, 7), 37, new MajorScale(NoteName.D), 0), "Gbb2,1/7", new MajorScale(NoteName.D), null, 37);
        
        // a default volume and default rhythm
        parseNoteStringTestHelper(new Note(1, 1, 2, 1, new Fraction(2, 1), 67, new MajorScale(NoteName.B), 0), "Cx3", new MajorScale(NoteName.B), new Fraction(2, 1), 67);
        
        // a default rhythm, and an entered volume
        parseNoteStringTestHelper(new Note(4, 4, 2, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume(), new MinorScale(NoteName.B), 0), "F#3,MF", new MinorScale(NoteName.B), new Fraction(1, 4), null);
        
        // a rest
        parseNoteStringTestHelper(Note.createRest(new Fraction(1, 3)), "R,1/3", new MajorScale(NoteName.B), null, null);
        
        // mixed case test...
        parseNoteStringTestHelper(new Note(5, 5, 2, -2, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), new MajorScale(NoteName.D), 0), "BBB2,1/4,mP", new MajorScale(NoteName.D), null, null);
        parseNoteStringTestHelper(Note.createRest(new Fraction(1, 1)), "r,1/1", new MajorScale(NoteName.B), null, null);
        
        // try it with a chromatic scale...
        parseNoteStringTestHelper(new Note(4, 8, 4, 0, new Fraction(3, 16), Dynamic.PP.getMidiVolume(), new ChromaticScale(), 0), "G#4,3/16,PP", new ChromaticScale(), null, null);                
    }  
      
    private void parseNoteStringTestHelper(Note expectedNote, String noteString, Scale s, Fraction defaultDuration, Integer defaultVolume) throws InvalidKeySignatureException, NoteStringParseException {
        Note n = Note.parseNoteString(noteString, s, defaultDuration, defaultVolume);        
        assertEquals(expectedNote, n);
    }
    
    @Test
    public void parseNoteString_invalidString() throws InvalidKeySignatureException, NoteStringParseException {        
        Scale s = new MajorScale(NoteName.C);
        
        try {
            Note.parseNoteString("2342", s, null, null); //nonsense string beginning with a number
            fail("Exception was not thrown");
        } catch (IncorrectNoteStringException ex) {}
        
        try {
            Note.parseNoteString("", s, null, null); //empty string
            fail("Exception was not thrown");
        } catch (NoteStringInvalidPartException ex) {
            assertEquals(ex.getNoteStringPart(), NoteStringPart.NOTE_NAME);
        }
                
        try {
            Note.parseNoteString("qadklfjalsdkf", s, null, null); //nonsense string beginning with a letter
            fail("Exception was not thrown");
        } catch (NoteStringInvalidPartException ex) {
            assertEquals(ex.getNoteStringPart(), NoteStringPart.NOTE_NAME);
        } 
        
        try {
            Note.parseNoteString("Hb4,1/4,MF", s, null, null);  //wrong note name
            fail("Exception was not thrown");
        } catch (NoteStringInvalidPartException ex) {
            assertEquals(ex.getNoteStringPart(), NoteStringPart.NOTE_NAME);
        } 
        
        try {
            Note.parseNoteString("GbT,1/4,P", s, null, null);  //T octave
            fail("Exception was not thrown");
        } catch (NoteStringInvalidPartException ex) {
            assertEquals(ex.getNoteStringPart(), NoteStringPart.OCTAVE);
        }
        
        try {
            Note.parseNoteString("Gb,1/4,P", s, null, null);  //no octave
            fail("Exception was not thrown");
        } catch (NoteStringInvalidPartException ex) {
            assertEquals(ex.getNoteStringPart(), NoteStringPart.OCTAVE);
        }
         
        try {
            Note.parseNoteString("Gb4,1,P", s, null, null);  // invalid duration
            fail("Exception was not thrown");
        } catch (NoteStringInvalidPartException ex) {
            assertEquals(ex.getNoteStringPart(), NoteStringPart.RHYTHMIC_DURATION);
        } 
        
        try {
            Note.parseNoteString("Gb4,1/0,P", s, null, null);  //0 duration denominator
            fail("Exception was not thrown");
        } catch (NoteStringInvalidPartException ex) {
            assertEquals(ex.getNoteStringPart(), NoteStringPart.RHYTHMIC_DURATION);
        }   
        
        try {
            Note.parseNoteString("Gb4,1/4,MPF", s, null, null);  //wrong dynamic
            fail("Exception was not thrown");
        } catch (NoteStringInvalidPartException ex) {
            assertEquals(ex.getNoteStringPart(), NoteStringPart.DYNAMIC);
        }                              
        
        Note.parseNoteString("Gb4,MF", s, null, null); // valid note string!
    }
    
    @Test
    public void convertRestToMidiNote() throws InvalidKeySignatureException, InvalidMidiDataException {
        Note n = Note.createRest(new Fraction(1, 4));
        MidiNote mn = n.convertToMidiNote(new Fraction(4, 1), 4, MidiNote.DEFAULT_CHANNEL, true);
        MidiNoteTest.assertNoteEventEqual(mn.getNoteOnEvent(), 16, (byte) -112, (byte) 0, (byte) 0);
        MidiNoteTest.assertNoteEventEqual(mn.getNoteOffEvent(), 17, (byte) -128, (byte) 0, (byte) 0);
    }
    
    @Test
    public void testClone() throws Exception {
        Note n = new Note(4, 4, 4, 1, new Fraction(1, 4), 70, new MajorScale(NoteName.G), -1);
        Note cloned = (Note) n.clone();
        assertEquals(n, cloned);        
    }
        
    @Test
    public void testEqualsAndHashCode() throws Exception {
        Note n1 = new Note(0, 0, 3, 2, new Fraction(1, 3), 70, new MajorScale(NoteName.D), -1);
        
        Note n2 = (Note) n1.clone();
        assertTrue(n1.equals(n2));
        assertTrue(n2.equals(n1));
        assertTrue(n1.hashCode() == n2.hashCode());
        
        n2.setChromaticAdjustment(-1);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = (Note) n1.clone();
        n2.setDuration(new Fraction(1, 5));
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = (Note) n1.clone();
        n2.setOctave(7);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = (Note) n1.clone();
        n2.setScale(Scale.DEFAULT);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = (Note) n1.clone();
        n2.setScaleStep(-1);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = (Note) n1.clone();
        n2.setLetterNumber(3);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = (Note) n1.clone();
        n2.setSegmentChromaticAdjustment(2);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = (Note) n1.clone();
        n2.setVolume(37);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
    }       
    
    @Test
    public void testToString() throws Exception {
        String expectedWithScale = "Note = LN(1), SS(1), O(4), CA(1), D(1/4), V(70), S(Bb Major Scale), SCA(0)";        
        Note n = new Note(1, 1, 4, 1, new Fraction(1, 4), 70, new MajorScale(NoteName.Bb), 0);
        assertEquals(expectedWithScale, n.toString());            
    }
    
    @Test
    public void toNotationNote() throws Exception {
        testToNotationNote(new MajorScale(NoteName.G), "G4,3/8", 'g', 4, 0, "3/8");
        testToNotationNote(new ChromaticScale(), "B#4,1/16", 'b', 4, 1, "1/16");
        testToNotationNote(new ChromaticScale(), "Cb5,1/6", 'c', 5, -1, "1/6");
    }
    
    private static void testToNotationNote(Scale scale, String noteString, char letterName, int octave, int accidental, String duration) throws Exception {
        Piece piece = new Piece(scale.getKeySignature(), TimeSignature.DEFAULT, Tempo.DEFAULT);
        Part part = new Part(piece, Instrument.DEFAULT);
        Note n = Note.parseNoteString(noteString, scale, new Fraction("1/4"), Dynamic.MF.getMidiVolume());        
        NotationNote nn = n.toNotationNote(part, n.convertToMidiNote(new Fraction(0, 1), (int) n.getDuration().denominator() * 4, 0, true));
        
        assertEquals(accidental, nn.getAccidental());
        assertEquals(octave, nn.getOctave());
        assertEquals(letterName, nn.getLetterName());
        assertEquals(new Fraction(duration), nn.getDuration());
    }
    
    @Test
    public void toGuidoNoteString() throws Exception {
        Scale s = new MajorScale(NoteName.C);
        MidiNote md = new MidiNote();
        
        // test that the note name comes from the note's scale step,
        // and the accidentals/octave from the midi note's pitch...
        md.setPitch(61);        
        Note n = new Note(0, 0, 4, 1, new Fraction(1, 16), Dynamic.F.getMidiVolume(), s, 0);        
        assertEquals("c#1/16", n.toGuidoString(md));
        
        md.setPitch(64);
        assertEquals("c####1/16", n.toGuidoString(md));
        
        md.setPitch(58);
        assertEquals("c&&1/16", n.toGuidoString(md));                
        
        n.setLetterNumber(6);
        assertEquals("b&0/16", n.toGuidoString(md));                
        
        // test that the note is properly normalized...
        n.setScaleStep(-1);
        n.setLetterNumber(-1);
        assertEquals("b&0/16", n.toGuidoString(md));
        
        n.setScaleStep(10);
        n.setLetterNumber(3);
        md.setPitch(77);
        assertEquals("f2/16", n.toGuidoString(md));

        // test a scale with accidentals...
        s = new HarmonicMinorScale(NoteName.Bb);
        n = new Note(2, 2, 3, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume(), s, 0);
        md.setPitch(61);
        assertEquals("d&1/4", n.toGuidoString(md));
        
        n.setScaleStep(6);
        n.setLetterNumber(6);
        md.setPitch(70);
        assertEquals("a#1/4", n.toGuidoString(md));
        
        // test a chromatic scale...
        s = new ChromaticScale();
        md.setPitch(71);
        n.setScale(s);
        n.setDuration(new Fraction(2, 3));
        n.setScaleStep(11);        
        n.setLetterNumber(6);
        assertEquals("b1*2/3", n.toGuidoString(md));   
        
        // test a pentatonic scale...
        s = new MinorPentatonicScale(NoteName.Fs);
        n.setScale(s);
        n.setScaleStep(3);
        n.setLetterNumber(4);
        md.setPitch(23);
        assertEquals("c&-2*2/3", n.toGuidoString(md));   
        
        n.setLetterNumber(3);        
        assertEquals("b-3*2/3", n.toGuidoString(md));   
        
        md.setPitch(24);
        n.setLetterNumber(4);
        assertEquals("c-2*2/3", n.toGuidoString(md));   
        
        md.setPitch(25);
        assertEquals("c#-2*2/3", n.toGuidoString(md));   
    }
    
    @Test
    public void toGuidoNoteString_rest() throws Exception {
        Note n = Note.createRest(new Fraction(1, 8));        
        MidiNote md = new MidiNote();
        assertEquals("_/8", n.toGuidoString(md));
    }        
    
    @Test(expected=UnsupportedOperationException.class)
    public void setLetterNumberOnRest() throws Exception {
        Note n = Note.createRest(new Fraction(1, 4));
        n.setLetterNumber(3);
    }  
    
    @Test
    public void performTransformerAdjustment() throws Exception {
        Scale scale = new MajorPentatonicScale(NoteName.G);        
        Note n = new Note(2, 2, 4, 0, new Fraction(1, 4), MidiNote.DEFAULT_VELOCITY, scale, 0);
        Note clone = (Note) n.clone();
        
        // try a pentatonic scale
        Note expected = (Note) n.clone();
        n.performTransformerAdjustment(1, 1, 0);                
        expected.setScaleStep(3);
        expected.setLetterNumber(4);        
        assertEquals(expected, n);
        
        // try it with a pentatonic scale and accidentals
        n = (Note) clone.clone();
        n.setChromaticAdjustment(1);
        expected = (Note) n.clone();        
        n.performTransformerAdjustment(1, 1, -1);                
        
        expected.setScaleStep(3);
        expected.setLetterNumber(3);        
        expected.setOctave(3);
        assertEquals(expected, n); 
        
        n = (Note) clone.clone();
        n.setSegmentChromaticAdjustment(1);
        expected = (Note) n.clone();        
        n.performTransformerAdjustment(1, 1, -1);                
        
        expected.setScaleStep(3);
        expected.setLetterNumber(3);        
        expected.setOctave(3);
        assertEquals(expected, n);
        
        // try a diatonic scale
        n = (Note) clone.clone();
        n.setScale(new MajorScale(NoteName.G));
        expected = (Note) n.clone();        
        n.performTransformerAdjustment(1, 1, 0);                
        
        expected.setScaleStep(3);
        expected.setLetterNumber(3);        
        assertEquals(expected, n);                 
    }
    
    public static void assertNotesEqual(Note expected, Note actual) {                        
        // These could be null if we passed NoteList.getFirstAudibleNote() as
        // sometimes there is not an audible note
        // Return if both are null or the same instance--we treat them as equal
        if (expected == actual) return;
        
        // normalize the notes first...
        Note expectedNote = expected.getNormalizedNote();
        Note actualNote = actual.getNormalizedNote();
                
        assertEquals(expectedNote, actualNote);
    }
}