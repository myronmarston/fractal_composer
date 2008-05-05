package com.myronmarston.music;

import com.myronmarston.music.NoteStringInvalidPartException.NoteStringPart;
import com.myronmarston.music.scales.*;
import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;

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
    public void copyConstructorForRest() {
        Note n = Note.createRest(new Fraction(3, 1));
        assertEquals(n, new Note(n));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructNoDurationNote() {
        Note n = new Note(2, 2, 0, new Fraction(0, 1), 64);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructNoDurationRest() {
        Note n = Note.createRest(new Fraction(0, 1));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void setNoteDurationToZero() {
        Note n = new Note(2, 2, 0, new Fraction(1, 1), 64);
        n.setDuration(new Fraction(0, 1));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void setBadVolume() {
        Note n = new Note(2, 2, 0, new Fraction(1, 1), 128);
    }
    
    @Test
    public void parseNoteString() throws InvalidKeySignatureException, NoteStringParseException {
        // basic test...
        parseNoteStringTestHelper(new Note(2, 3, 0, new Fraction(3, 16), Dynamic.PP.getMidiVolume()), "D#4,3/16,PP", new MajorScale(NoteName.B), null, null);                
        
        // an accidental...
        parseNoteStringTestHelper(new Note(0, 3, -1, new Fraction(1, 2), Dynamic.FFF.getMidiVolume()), "B2,1/2,FFF", new MajorPentatonicScale(NoteName.C), null, null);
        
        // a non-standard rhythm and using a default volume
        parseNoteStringTestHelper(new Note(3, 2, -2, new Fraction(1, 7), 37), "Gbb2,1/7", new MajorScale(NoteName.D), null, 37);
        
        // a default volume and default rhythm
        parseNoteStringTestHelper(new Note(1, 2, 1, new Fraction(2, 1), 67), "Cx3", new MajorScale(NoteName.B), new Fraction(2, 1), 67);
        
        // a default rhythm, and an entered volume
        parseNoteStringTestHelper(new Note(4, 2, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume()), "F#3,MF", new MinorScale(NoteName.B), new Fraction(1, 4), null);
        
        // a rest
        parseNoteStringTestHelper(Note.createRest(new Fraction(1, 3)), "R,1/3", new MajorScale(NoteName.B), null, null);
        
        // mixed case test...
        parseNoteStringTestHelper(new Note(5, 2, -2, new Fraction(1, 4), Dynamic.MP.getMidiVolume()), "BBB2,1/4,mP", new MajorScale(NoteName.D), null, null);
        parseNoteStringTestHelper(Note.createRest(new Fraction(1, 1)), "r,1/1", new MajorScale(NoteName.B), null, null);
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
        MidiNote mn = n.convertToMidiNote(new MajorScale(NoteName.C), new Fraction(4, 1), 4, MidiNote.DEFAULT_CHANNEL, true);
        MidiNoteTest.assertNoteEventEqual(mn.getNoteOnEvent(), 16, (byte) -112, (byte) 0, (byte) 0);
        MidiNoteTest.assertNoteEventEqual(mn.getNoteOffEvent(), 17, (byte) -128, (byte) 0, (byte) 0);
    }
}