package com.myronmarston.music.scales;

import com.myronmarston.music.MidiNote;
import com.myronmarston.music.Note;
import com.myronmarston.music.NoteName;

import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class ScaleTest {

    public ScaleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void keyNameDefaultNumberCorrect() {
        assertEquals(NoteName.C, NoteName.getDefaultNoteNameForNumber(0));
        assertEquals(NoteName.C_SHARP, NoteName.getDefaultNoteNameForNumber(1));
        assertEquals(NoteName.D, NoteName.getDefaultNoteNameForNumber(2));
        assertEquals(NoteName.E_FLAT, NoteName.getDefaultNoteNameForNumber(3));
        assertEquals(NoteName.E, NoteName.getDefaultNoteNameForNumber(4));
        assertEquals(NoteName.F, NoteName.getDefaultNoteNameForNumber(5));
        assertEquals(NoteName.F_SHARP, NoteName.getDefaultNoteNameForNumber(6));
        assertEquals(NoteName.G, NoteName.getDefaultNoteNameForNumber(7));
        assertEquals(NoteName.A_FLAT, NoteName.getDefaultNoteNameForNumber(8));
        assertEquals(NoteName.A, NoteName.getDefaultNoteNameForNumber(9));
        assertEquals(NoteName.B_FLAT, NoteName.getDefaultNoteNameForNumber(10));
        assertEquals(NoteName.B, NoteName.getDefaultNoteNameForNumber(11));                
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getDefaultKeyNameForNumber_ThrowsException() {
        NoteName k = NoteName.getDefaultNoteNameForNumber(12);
    }   
    
    @Test
    public void majorScale_convertToMidiNote_accidental() throws InvalidKeySignatureException {
        MajorScale scale = new MajorScale(NoteName.C);
        MidiNote result;
        Note note;
        int midiTickResolution = 8;
        
        // F# - this chromatic note should convert OK.
        note = new Note(3, 4, 1, new Fraction(1, 1), 70);                      
        result = scale.convertToMidiNote(note, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 66, 70, 0, midiTickResolution); 
        
        // Fx - this is the same as G, another note in the scale, so we will use F# instead
        note = new Note(3, 4, 2, new Fraction(1, 1), 70);                      
        result = scale.convertToMidiNote(note, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 66, 70, 0, midiTickResolution); 
        
        // B# - this is the same as C, another note in the scale, so we will use B instead
        note = new Note(6, 4, 1, new Fraction(1, 1), 70);                      
        result = scale.convertToMidiNote(note, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 71, 70, 0, midiTickResolution); 
        
        // Bx - this chromatic note should convert OK.
        note = new Note(6, 4, 2, new Fraction(1, 1), 70);                      
        result = scale.convertToMidiNote(note, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 73, 70, 0, midiTickResolution); 
        
        // Eb - this chromatic note should convert OK.
        note = new Note(2, 4, -1, new Fraction(1, 1), 70);                      
        result = scale.convertToMidiNote(note, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 63, 70, 0, midiTickResolution); 
        
        // Ebb - this is the same as D, another note in the scale, so we will use Eb instead
        note = new Note(2, 4, -2, new Fraction(1, 1), 70);                      
        result = scale.convertToMidiNote(note, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 63, 70, 0, midiTickResolution); 
        
        // Fb - this is the same as E, another note in the scale, so we will use F instead
        note = new Note(3, 4, -1, new Fraction(1, 1), 70);                      
        result = scale.convertToMidiNote(note, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 65, 70, 0, midiTickResolution);
        
        // Fbb - this note should convert ok.
        note = new Note(3, 4, -2, new Fraction(1, 1), 70);                      
        result = scale.convertToMidiNote(note, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 63, 70, 0, midiTickResolution);
        
        // Cb - this is the same as B, another note in the scale, so we will use C instead
        note = new Note(0, 4, -1, new Fraction(1, 1), 70);                      
        result = scale.convertToMidiNote(note, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 60, 70, 0, midiTickResolution); 
        
        // Cbb - this note should convert ok.
        note = new Note(0, 4, -2, new Fraction(1, 1), 70);                      
        result = scale.convertToMidiNote(note, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 58, 70, 0, midiTickResolution); 
    }
    
    @Test
    public void majorScale_convertToMidiNote() throws InvalidKeySignatureException {                
        MajorScale scale = new MajorScale(NoteName.C);
        MidiNote result;
        Note note;
        
        note = new Note(0, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = scale.convertToMidiNote(note, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 60, 70, 0, 48);                
        
        note.setScaleStep(5);
        note.setOctave(5);
        result = scale.convertToMidiNote(note, new Fraction(5, 1), midiTickResolution);
        assertMidiNoteValues(result, 81, 70, 40, 48);                
        
        note.setScaleStep(2);
        note.setOctave(2);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = scale.convertToMidiNote(note, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 40, 40, 16, 24); 
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        result = scale.convertToMidiNote(note, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 31, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = scale.convertToMidiNote(note, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 69, 40, 16, 24); 
    }
    
    @Test
    public void minorScale_convertToMidiNote() throws InvalidKeySignatureException {                
        MinorScale scale = new MinorScale(NoteName.C);
        MidiNote result;
        Note note;
        
        note = new Note(6, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = scale.convertToMidiNote(note, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 70, 70, 0, 48);                
        
        note.setScaleStep(5);
        note.setOctave(5);
        result = scale.convertToMidiNote(note, new Fraction(5, 1), midiTickResolution);
        assertMidiNoteValues(result, 80, 70, 40, 48);                
        
        note.setScaleStep(3);
        note.setOctave(2);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = scale.convertToMidiNote(note, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 41, 40, 16, 24); 
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        result = scale.convertToMidiNote(note, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 31, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = scale.convertToMidiNote(note, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 68, 40, 16, 24); 
    }
    
    @Test
    public void harmonicMinorScale_convertToMidiNote() throws InvalidKeySignatureException {                
        HarmonicMinorScale scale = new HarmonicMinorScale(NoteName.C);
        MidiNote result;
        Note note;
        
        note = new Note(6, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = scale.convertToMidiNote(note, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 71, 70, 0, 48);                
        
        note.setScaleStep(5);
        note.setOctave(5);
        result = scale.convertToMidiNote(note, new Fraction(5, 1), midiTickResolution);
        assertMidiNoteValues(result, 80, 70, 40, 48);                
        
        note.setScaleStep(3);
        note.setOctave(2);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = scale.convertToMidiNote(note, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 41, 40, 16, 24); 
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        result = scale.convertToMidiNote(note, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 31, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = scale.convertToMidiNote(note, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 68, 40, 16, 24); 
    }
    
    protected void assertMidiNoteValues(MidiNote midiNote, int pitch, int velocity, long startTime, long duration) {
        assertEquals(pitch, midiNote.getPitch());
        assertEquals(velocity, midiNote.getVelocity());
        assertEquals(startTime, midiNote.getStartTime());
        assertEquals(duration, midiNote.getDuration());        
    }

}