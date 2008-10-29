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
        parseNoteStringTestHelper(new Note(4, 4, 2, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume(), new NaturalMinorScale(NoteName.B), 0), "F#3,MF", new NaturalMinorScale(NoteName.B), new Fraction(1, 4), null);
        
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
        } catch (NoteStringParseException ex) {}
        
        try {
            Note.parseNoteString("", s, null, null); //empty string
            fail("Exception was not thrown");
        } catch (NoteStringParseException ex) {}
                
        try {
            Note.parseNoteString("qadklfjalsdkf", s, null, null); //nonsense string beginning with a letter
            fail("Exception was not thrown");
        } catch (NoteStringParseException ex) {} 
        
        try {
            Note.parseNoteString("Hb4,1/4,MF", s, null, null);  //wrong note name
            fail("Exception was not thrown");
        } catch (NoteStringParseException ex) {} 
        
        try {
            Note.parseNoteString("GbT,1/4,P", s, null, null);  //T octave
            fail("Exception was not thrown");
        } catch (NoteStringParseException ex) {}
        
        try {
            Note.parseNoteString("Gb,1/4,P", s, null, null);  //no octave
            fail("Exception was not thrown");
        } catch (NoteStringParseException ex) {}
         
        try {
            Note.parseNoteString("Gb4,1/0,P", s, null, null);  //0 duration denominator
            fail("Exception was not thrown");
        } catch (NoteStringParseException ex) {}   
        
        try {
            Note.parseNoteString("Gb4,1/4,MPF", s, null, null);  //wrong dynamic
            fail("Exception was not thrown");
        } catch (NoteStringParseException ex) {}                              
        
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
        Note cloned = n.clone();
        assertEquals(n, cloned);        
    }
        
    @Test
    public void testEqualsAndHashCode() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        VoiceSection vs1 = fp.getVoices().get(0).getVoiceSections().get(0);
        VoiceSection vs2 = fp.getVoices().get(1).getVoiceSections().get(0);
        
        Note n1 = new Note(0, 0, 3, 2, new Fraction(1, 3), 70, new MajorScale(NoteName.D), -1);
        n1.setSourceVoiceSection(vs1);
        
        Note n2 = n1.clone();
        assertTrue(n1.equals(n2));
        assertTrue(n2.equals(n1));
        assertTrue(n1.hashCode() == n2.hashCode());
        
        n2.setChromaticAdjustment(-1);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = n1.clone();
        n2.setDuration(new Fraction(1, 5));
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = n1.clone();
        n2.setOctave(7);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = n1.clone();
        n2.setScale(Scale.DEFAULT);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = n1.clone();
        n2.setScaleStep(-1);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = n1.clone();
        n2.setLetterNumber(3);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = n1.clone();
        n2.setSegmentChromaticAdjustment(2);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = n1.clone();
        n2.setVolume(37);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = n1.clone();
        n2.setSourceVoiceSection(vs2);
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
        
        n2 = n1.clone();
        n2.setIsFirstNoteOfGermCopy(!n2.isFirstNoteOfGermCopy());
        assertFalse(n1.equals(n2));
        assertFalse(n2.equals(n1));
        assertFalse(n1.hashCode() == n2.hashCode());
    }       
    
    @Test
    public void testToString() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        VoiceSection vs = fp.getVoices().get(0).getVoiceSections().get(0);                          
        
        String vsString = vs.toString();
        
        String expectedWithScale = "Note = LN(1), SS(1), O(4), CA(1), D(1/4), V(70), S(Bb Major Scale), SCA(0), SVS(" + vsString + "), IFN(false)";        
        Note n = new Note(1, 1, 4, 1, new Fraction(1, 4), 70, new MajorScale(NoteName.Bb), 0);
        n.setSourceVoiceSection(vs);
        assertEquals(expectedWithScale, n.toString());            
    }
    
    @Test
    public void toNotationNote() throws Exception {
        testToNotationNote(new MajorScale(NoteName.G), "G4,3/8", 'g', 4, 0, "3/8", "4/4");
        testToNotationNote(new ChromaticScale(), "B#4,1/16", 'b', 4, 1, "1/16", "3/8");
        testToNotationNote(new ChromaticScale(), "Cb5,1/6", 'c', 5, -1, "1/6", "1/4");               
        
        // test that if the chromatic adjustment is more than 2, it changes to a
        // different letter name
        // triple sharp of a B# in C# major = B#### = D#
        Note n = new Note(6, 6, 4, 2, new Fraction(1, 4), MidiNote.DEFAULT_VELOCITY, new MajorScale(NoteName.Cs), 1);
        MidiNote mn = n.convertToMidiNote(new Fraction(0, 1), 4, 0, true);
        NotationNote nn = n.toNotationNote(NotationNoteTest.DEFAULT_PART_SECTION, mn, new Fraction(4, 4));
        assertNotationNote(nn, 'd', 5, 1, "1/4", "4/4");
    }
    
    private static void testToNotationNote(Scale scale, String noteString, char letterName, int octave, int accidental, String duration, String timeLeftInBar) throws Exception {
        Piece piece = new Piece(scale.getKeySignature(), TimeSignature.DEFAULT, Tempo.DEFAULT, true, true);
        Part part = new Part(piece, Instrument.DEFAULT);
        PartSection partSection = new PartSection(part, NotationNoteTest.DEFAULT_VOICE_SECTION);
        Note n = Note.parseNoteString(noteString, scale, new Fraction("1/4"), Dynamic.MF.getMidiVolume());        
        NotationNote nn = n.toNotationNote(partSection, n.convertToMidiNote(new Fraction(0, 1), (int) n.getDuration().denominator() * 4, 0, true), new Fraction(timeLeftInBar));
        
        assertNotationNote(nn, letterName, octave, accidental, duration, timeLeftInBar);        
    }               
    
    private static void assertNotationNote(NotationNote nn, char letterName, int octave, int accidental, String duration, String timeLeftInBar) throws Exception {
        assertEquals(accidental, nn.getAccidental());
        assertEquals(octave, nn.getOctave());
        assertEquals(letterName, nn.getLetterName());
        assertEquals(new Fraction(duration), nn.getDuration());        
        assertEquals(new Fraction(timeLeftInBar), nn.getTimeLeftInBar());        
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void setLetterNumberOnRest() throws Exception {
        Note n = Note.createRest(new Fraction(1, 4));
        n.setLetterNumber(3);
    }  
    
    @Test
    public void octaveOutOfRangeProducesValidMidiNote() throws Exception {
        Note n = new Note(0, 0, -6, 0, new Fraction(1, 4), MidiNote.DEFAULT_VELOCITY, new MajorScale(NoteName.Cs), 1);
        MidiNote mn = n.convertToMidiNote(new Fraction(1, 1), 480, 1, true);
        assertTrue(mn.getPitch() >= MidiNote.MIN_PITCH_NUM);
        assertTrue(mn.getPitch() <= MidiNote.MAX_PITCH_NUM);
        
        n = new Note(0, 0, 963, 0, new Fraction(1, 4), MidiNote.DEFAULT_VELOCITY, new MajorScale(NoteName.Cs), 1);
        mn = n.convertToMidiNote(new Fraction(1, 1), 480, 1, true);
        assertTrue(mn.getPitch() >= MidiNote.MIN_PITCH_NUM);
        assertTrue(mn.getPitch() <= MidiNote.MAX_PITCH_NUM);        
    }
    
    @Test
    public void performTransformerAdjustment() throws Exception {
        Scale scale = new MajorPentatonicScale(NoteName.G);        
        Note n = new Note(2, 2, 4, 0, new Fraction(1, 4), MidiNote.DEFAULT_VELOCITY, scale, 0);
        Note clone = n.clone();
        
        // try a pentatonic scale
        Note expected = n.clone();
        n.performTransformerAdjustment(1, 1, 0);                
        expected.setScaleStep(3);
        expected.setLetterNumber(4);        
        assertEquals(expected, n);
        
        // try it with a pentatonic scale and accidentals
        n = clone.clone();
        n.setChromaticAdjustment(1);
        expected = n.clone();        
        n.performTransformerAdjustment(1, 1, -1);                
        
        expected.setScaleStep(3);
        expected.setLetterNumber(3);        
        expected.setOctave(3);
        assertEquals(expected, n); 
        
        n = clone.clone();
        n.setSegmentChromaticAdjustment(1);
        expected = n.clone();        
        n.performTransformerAdjustment(1, 1, -1);                
        
        expected.setScaleStep(3);
        expected.setLetterNumber(3);        
        expected.setOctave(3);
        assertEquals(expected, n);
        
        // try a diatonic scale
        n = clone.clone();
        n.setScale(new MajorScale(NoteName.G));
        expected = n.clone();        
        n.performTransformerAdjustment(1, 1, 0);                
        
        expected.setScaleStep(3);
        expected.setLetterNumber(3);        
        assertEquals(expected, n);                 
    }
    
    @Test
    public void regexMatchesValidNotes() {
        String[] validNotes = new String[] {
            "A4",
            "Gb4,1/4",
            "d#4,MF",
            "c2,3/7,PPP",
            "R",
            "R,3/2",
            "C4,1"
        };
        
        for (String validNote : validNotes) {
            assertTrue("The note " + validNote + " did not match the regex as expected.", Note.REGEX_PATTERN.matcher(validNote).matches());
        }
    }
    
    @Test
    public void regexDoesNotMatcheInValidNotes() {
        String[] inValidNotes = new String[] {
            "H4",
            "C##4",
            "Hbbb3",
            "Gb4, 1/4",
            "d#4,MF,1/4",
            "c.2,3/7,PPP",
            "c2,3\\7,PPP",
            "R4",
            "R,MF",
            "R,3/2,MF",
            "G4,MF,MF",
            "G3,1/4,1/4",
            "G4,0/4",
            "G2,1/0",
            "G2,0/0"
        };
        
        for (String invalidNote : inValidNotes) {
            assertFalse("The note " + invalidNote + " did match the regex, and should not have.", Note.REGEX_PATTERN.matcher(invalidNote).matches());
        }
    }
    
    public static void assertNotesEqual(Note expected, Note actual, boolean allowDifferentVoiceSectionReferences) {                        
        // These could be null if we passed NoteList.getFirstAudibleNote() as
        // sometimes there is not an audible note
        // Return if both are null or the same instance--we treat them as equal
        if (expected == actual) return;
        
        // normalize the notes first...
        Note expectedNote = expected.getNormalizedNote();
        Note actualNote = actual.getNormalizedNote();
        
        if (allowDifferentVoiceSectionReferences && expectedNote.getSourceVoiceSection() != actualNote.getSourceVoiceSection()) {
            VoiceSectionTest.assertVoiceSectionsEqual_withoutResult(expected.getSourceVoiceSection(), actual.getSourceVoiceSection());
            expectedNote.setSourceVoiceSection(null);
            actualNote.setSourceVoiceSection(null);                    
        }        
        
        assertEquals(expectedNote, actualNote);
    }
}