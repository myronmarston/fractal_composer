package com.myronmarston.music.scales;

import com.myronmarston.music.MidiNote;
import com.myronmarston.music.Note;
import com.myronmarston.music.NoteName;

import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;

import java.util.*;
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
    public void setNotePitchValues() throws InvalidKeySignatureException {
        Note n = new Note();
        
        Scale s = new MajorScale(NoteName.G); // G A B C D E F# G         
        testSetNotePitchValues(s, n, NoteName.Abb, 1, -2);
        testSetNotePitchValues(s, n, NoteName.Ab, 1, -1);
        testSetNotePitchValues(s, n, NoteName.A, 1, 0);
        testSetNotePitchValues(s, n, NoteName.As, 1, 1);
        testSetNotePitchValues(s, n, NoteName.Ax, 1, 2); 
        
        testSetNotePitchValues(s, n, NoteName.Bbb, 2, -2);
        testSetNotePitchValues(s, n, NoteName.Bb, 2, -1);
        testSetNotePitchValues(s, n, NoteName.B, 2, 0);
        testSetNotePitchValues(s, n, NoteName.Bs, 2, 1);
        testSetNotePitchValues(s, n, NoteName.Bx, 2, 2);
        
        testSetNotePitchValues(s, n, NoteName.Cbb, 3, -2);
        testSetNotePitchValues(s, n, NoteName.Cb, 3, -1);
        testSetNotePitchValues(s, n, NoteName.C, 3, 0);
        testSetNotePitchValues(s, n, NoteName.Cs, 3, 1);
        testSetNotePitchValues(s, n, NoteName.Cx, 3, 2);
        
        testSetNotePitchValues(s, n, NoteName.Dbb, 4, -2);
        testSetNotePitchValues(s, n, NoteName.Db, 4, -1);
        testSetNotePitchValues(s, n, NoteName.D, 4, 0);
        testSetNotePitchValues(s, n, NoteName.Ds, 4, 1);
        testSetNotePitchValues(s, n, NoteName.Dx, 4, 2);
        
        testSetNotePitchValues(s, n, NoteName.Ebb, 5, -2);
        testSetNotePitchValues(s, n, NoteName.Eb, 5, -1);
        testSetNotePitchValues(s, n, NoteName.E, 5, 0);
        testSetNotePitchValues(s, n, NoteName.Es, 5, 1);
        testSetNotePitchValues(s, n, NoteName.Ex, 5, 2);
        
        testSetNotePitchValues(s, n, NoteName.Fbb, 6, -3);
        testSetNotePitchValues(s, n, NoteName.Fb, 6, -2);
        testSetNotePitchValues(s, n, NoteName.F, 6, -1);
        testSetNotePitchValues(s, n, NoteName.Fs, 6, 0);
        testSetNotePitchValues(s, n, NoteName.Fx, 6, 1);
        
        testSetNotePitchValues(s, n, NoteName.Gbb, 0, -2);
        testSetNotePitchValues(s, n, NoteName.Gb, 0, -1);
        testSetNotePitchValues(s, n, NoteName.G, 0, 0);
        testSetNotePitchValues(s, n, NoteName.Gs, 0, 1);
        testSetNotePitchValues(s, n, NoteName.Gx, 0, 2);     
        
        s = new MinorScale(NoteName.Ab); // Ab Bb Cb Db Eb Fb Gb Ab     
        testSetNotePitchValues(s, n, NoteName.Abb, 0, -1);
        testSetNotePitchValues(s, n, NoteName.Ab, 0, 0);
        testSetNotePitchValues(s, n, NoteName.A, 0, 1);
        testSetNotePitchValues(s, n, NoteName.As, 0, 2);
        testSetNotePitchValues(s, n, NoteName.Ax, 0, 3); 
        
        testSetNotePitchValues(s, n, NoteName.Bbb, 1, -1);
        testSetNotePitchValues(s, n, NoteName.Bb, 1, 0);
        testSetNotePitchValues(s, n, NoteName.B, 1, 1);
        testSetNotePitchValues(s, n, NoteName.Bs, 1, 2);
        testSetNotePitchValues(s, n, NoteName.Bx, 1, 3);
        
        testSetNotePitchValues(s, n, NoteName.Cbb, 2, -1);
        testSetNotePitchValues(s, n, NoteName.Cb, 2, 0);
        testSetNotePitchValues(s, n, NoteName.C, 2, 1);
        testSetNotePitchValues(s, n, NoteName.Cs, 2, 2);
        testSetNotePitchValues(s, n, NoteName.Cx, 2, 3);
        
        testSetNotePitchValues(s, n, NoteName.Dbb, 3, -1);
        testSetNotePitchValues(s, n, NoteName.Db, 3, 0);
        testSetNotePitchValues(s, n, NoteName.D, 3, 1);
        testSetNotePitchValues(s, n, NoteName.Ds, 3, 2);
        testSetNotePitchValues(s, n, NoteName.Dx, 3, 3);
        
        testSetNotePitchValues(s, n, NoteName.Ebb, 4, -1);
        testSetNotePitchValues(s, n, NoteName.Eb, 4, 0);
        testSetNotePitchValues(s, n, NoteName.E, 4, 1);
        testSetNotePitchValues(s, n, NoteName.Es, 4, 2);
        testSetNotePitchValues(s, n, NoteName.Ex, 4, 3);
        
        testSetNotePitchValues(s, n, NoteName.Fbb, 5, -1);
        testSetNotePitchValues(s, n, NoteName.Fb, 5, 0);
        testSetNotePitchValues(s, n, NoteName.F, 5, 1);
        testSetNotePitchValues(s, n, NoteName.Fs, 5, 2);
        testSetNotePitchValues(s, n, NoteName.Fx, 5, 3);
        
        testSetNotePitchValues(s, n, NoteName.Gbb, 6, -1);
        testSetNotePitchValues(s, n, NoteName.Gb, 6, 0);
        testSetNotePitchValues(s, n, NoteName.G, 6, 1);
        testSetNotePitchValues(s, n, NoteName.Gs, 6, 2);
        testSetNotePitchValues(s, n, NoteName.Gx, 6, 3);
        
        s = new HarmonicMinorScale(NoteName.Ab); // Ab Bb Cb Db Eb Fb G Ab               
        testSetNotePitchValues(s, n, NoteName.Abb, 0, -1);
        testSetNotePitchValues(s, n, NoteName.Ab, 0, 0);
        testSetNotePitchValues(s, n, NoteName.A, 0, 1);
        testSetNotePitchValues(s, n, NoteName.As, 0, 2);
        testSetNotePitchValues(s, n, NoteName.Ax, 0, 3); 
        
        testSetNotePitchValues(s, n, NoteName.Bbb, 1, -1);
        testSetNotePitchValues(s, n, NoteName.Bb, 1, 0);
        testSetNotePitchValues(s, n, NoteName.B, 1, 1);
        testSetNotePitchValues(s, n, NoteName.Bs, 1, 2);
        testSetNotePitchValues(s, n, NoteName.Bx, 1, 3);
        
        testSetNotePitchValues(s, n, NoteName.Cbb, 2, -1);
        testSetNotePitchValues(s, n, NoteName.Cb, 2, 0);
        testSetNotePitchValues(s, n, NoteName.C, 2, 1);
        testSetNotePitchValues(s, n, NoteName.Cs, 2, 2);
        testSetNotePitchValues(s, n, NoteName.Cx, 2, 3);
        
        testSetNotePitchValues(s, n, NoteName.Dbb, 3, -1);
        testSetNotePitchValues(s, n, NoteName.Db, 3, 0);
        testSetNotePitchValues(s, n, NoteName.D, 3, 1);
        testSetNotePitchValues(s, n, NoteName.Ds, 3, 2);
        testSetNotePitchValues(s, n, NoteName.Dx, 3, 3);
        
        testSetNotePitchValues(s, n, NoteName.Ebb, 4, -1);
        testSetNotePitchValues(s, n, NoteName.Eb, 4, 0);
        testSetNotePitchValues(s, n, NoteName.E, 4, 1);
        testSetNotePitchValues(s, n, NoteName.Es, 4, 2);
        testSetNotePitchValues(s, n, NoteName.Ex, 4, 3);
        
        testSetNotePitchValues(s, n, NoteName.Fbb, 5, -1);
        testSetNotePitchValues(s, n, NoteName.Fb, 5, 0);
        testSetNotePitchValues(s, n, NoteName.F, 5, 1);
        testSetNotePitchValues(s, n, NoteName.Fs, 5, 2);
        testSetNotePitchValues(s, n, NoteName.Fx, 5, 3);
        
        testSetNotePitchValues(s, n, NoteName.Gbb, 6, -2);
        testSetNotePitchValues(s, n, NoteName.Gb, 6, -1);
        testSetNotePitchValues(s, n, NoteName.G, 6, 0);
        testSetNotePitchValues(s, n, NoteName.Gs, 6, 1);
        testSetNotePitchValues(s, n, NoteName.Gx, 6, 2);
                
        s = new MajorPentatonicScale(NoteName.B); // B C# D# F# G# B
        testSetNotePitchValues(s, n, NoteName.Abb, 4, -1);
        testSetNotePitchValues(s, n, NoteName.Ab, 4, 0);
        testSetNotePitchValues(s, n, NoteName.A, 4, 1);
        testSetNotePitchValues(s, n, NoteName.As, 0, -1);
        testSetNotePitchValues(s, n, NoteName.Ax, 0, 0); 
        
        testSetNotePitchValues(s, n, NoteName.Bbb, 0, -2);
        testSetNotePitchValues(s, n, NoteName.Bb, 0, -1);
        testSetNotePitchValues(s, n, NoteName.B, 0, 0);
        testSetNotePitchValues(s, n, NoteName.Bs, 0, 1);
        testSetNotePitchValues(s, n, NoteName.Bx, 0, 2);
        
        testSetNotePitchValues(s, n, NoteName.Cbb, 1, -3);
        testSetNotePitchValues(s, n, NoteName.Cb, 1, -2);
        testSetNotePitchValues(s, n, NoteName.C, 1, -1);
        testSetNotePitchValues(s, n, NoteName.Cs, 1, 0);
        testSetNotePitchValues(s, n, NoteName.Cx, 1, 1);
        
        testSetNotePitchValues(s, n, NoteName.Dbb, 2, -3);
        testSetNotePitchValues(s, n, NoteName.Db, 2, -2);
        testSetNotePitchValues(s, n, NoteName.D, 2, -1);
        testSetNotePitchValues(s, n, NoteName.Ds, 2, 0);
        testSetNotePitchValues(s, n, NoteName.Dx, 2, 1);
        
        testSetNotePitchValues(s, n, NoteName.Ebb, 2, -1);
        testSetNotePitchValues(s, n, NoteName.Eb, 2, 0);
        testSetNotePitchValues(s, n, NoteName.E, 2, 1);
        testSetNotePitchValues(s, n, NoteName.Es, 3, -1);
        testSetNotePitchValues(s, n, NoteName.Ex, 3, 0);
        
        testSetNotePitchValues(s, n, NoteName.Fbb, 3, -3);
        testSetNotePitchValues(s, n, NoteName.Fb, 3, -2);
        testSetNotePitchValues(s, n, NoteName.F, 3, -1);
        testSetNotePitchValues(s, n, NoteName.Fs, 3, 0);
        testSetNotePitchValues(s, n, NoteName.Fx, 3, 1);
        
        testSetNotePitchValues(s, n, NoteName.Gbb, 4, -3);
        testSetNotePitchValues(s, n, NoteName.Gb, 4, -2);
        testSetNotePitchValues(s, n, NoteName.G, 4, -1);
        testSetNotePitchValues(s, n, NoteName.Gs, 4, 0);
        testSetNotePitchValues(s, n, NoteName.Gx, 4, 1);
        
        s = new MinorPentatonicScale(NoteName.F);  // F Ab Bb C Eb F                
        testSetNotePitchValues(s, n, NoteName.Abb, 1, -1);
        testSetNotePitchValues(s, n, NoteName.Ab, 1, 0);
        testSetNotePitchValues(s, n, NoteName.A, 1, 1);
        testSetNotePitchValues(s, n, NoteName.As, 1, 2);
        testSetNotePitchValues(s, n, NoteName.Ax, 1, 3); 
        
        testSetNotePitchValues(s, n, NoteName.Bbb, 2, -1);
        testSetNotePitchValues(s, n, NoteName.Bb, 2, 0);
        testSetNotePitchValues(s, n, NoteName.B, 2, 1);
        testSetNotePitchValues(s, n, NoteName.Bs, 2, 2);
        testSetNotePitchValues(s, n, NoteName.Bx, 2, 3);
        
        testSetNotePitchValues(s, n, NoteName.Cbb, 3, -2);
        testSetNotePitchValues(s, n, NoteName.Cb, 3, -1);
        testSetNotePitchValues(s, n, NoteName.C, 3, 0);
        testSetNotePitchValues(s, n, NoteName.Cs, 3, 1);
        testSetNotePitchValues(s, n, NoteName.Cx, 3, 2);
        
        testSetNotePitchValues(s, n, NoteName.Dbb, 3, 0);
        testSetNotePitchValues(s, n, NoteName.Db, 3, 1);
        testSetNotePitchValues(s, n, NoteName.D, 4, -1);
        testSetNotePitchValues(s, n, NoteName.Ds, 4, 0);
        testSetNotePitchValues(s, n, NoteName.Dx, 4, 1);
        
        testSetNotePitchValues(s, n, NoteName.Ebb, 4, -1);
        testSetNotePitchValues(s, n, NoteName.Eb, 4, 0);
        testSetNotePitchValues(s, n, NoteName.E, 4, 1);
        testSetNotePitchValues(s, n, NoteName.Es, 4, 2);
        testSetNotePitchValues(s, n, NoteName.Ex, 4, 3);
        
        testSetNotePitchValues(s, n, NoteName.Fbb, 0, -2);
        testSetNotePitchValues(s, n, NoteName.Fb, 0, -1);
        testSetNotePitchValues(s, n, NoteName.F, 0, 0);
        testSetNotePitchValues(s, n, NoteName.Fs, 0, 1);
        testSetNotePitchValues(s, n, NoteName.Fx, 0, 2);
        
        testSetNotePitchValues(s, n, NoteName.Gbb, 0, 0);
        testSetNotePitchValues(s, n, NoteName.Gb, 0, 1);
        testSetNotePitchValues(s, n, NoteName.G, 1, -1);
        testSetNotePitchValues(s, n, NoteName.Gs, 1, 0);
        testSetNotePitchValues(s, n, NoteName.Gx, 1, 1);
        
        s = new ChromaticScale();  // C C# D D# E F F# G G# A A# B
        testSetNotePitchValues(s, n, NoteName.Abb, 7, 0);
        testSetNotePitchValues(s, n, NoteName.Ab, 8, 0);
        testSetNotePitchValues(s, n, NoteName.A, 9, 0);
        testSetNotePitchValues(s, n, NoteName.As, 10, 0);
        testSetNotePitchValues(s, n, NoteName.Ax, 11, 0); 
        
        testSetNotePitchValues(s, n, NoteName.Bbb, 9, 0);
        testSetNotePitchValues(s, n, NoteName.Bb, 10, 0);
        testSetNotePitchValues(s, n, NoteName.B, 11, 0);
        testSetNotePitchValues(s, n, NoteName.Bs, 0, 0);
        testSetNotePitchValues(s, n, NoteName.Bx, 1, 0);
        
        testSetNotePitchValues(s, n, NoteName.Cbb, 10, 0);
        testSetNotePitchValues(s, n, NoteName.Cb, 11, 0);
        testSetNotePitchValues(s, n, NoteName.C, 0, 0);
        testSetNotePitchValues(s, n, NoteName.Cs, 1, 0);
        testSetNotePitchValues(s, n, NoteName.Cx, 2, 0);
        
        testSetNotePitchValues(s, n, NoteName.Dbb, 0, 0);
        testSetNotePitchValues(s, n, NoteName.Db, 1, 0);
        testSetNotePitchValues(s, n, NoteName.D, 2, 0);
        testSetNotePitchValues(s, n, NoteName.Ds, 3, 0);
        testSetNotePitchValues(s, n, NoteName.Dx, 4, 0);
        
        testSetNotePitchValues(s, n, NoteName.Ebb, 2, 0);
        testSetNotePitchValues(s, n, NoteName.Eb, 3, 0);
        testSetNotePitchValues(s, n, NoteName.E, 4, 0);
        testSetNotePitchValues(s, n, NoteName.Es, 5, 0);
        testSetNotePitchValues(s, n, NoteName.Ex, 6, 0);
        
        testSetNotePitchValues(s, n, NoteName.Fbb, 3, 0);
        testSetNotePitchValues(s, n, NoteName.Fb, 4, 0);
        testSetNotePitchValues(s, n, NoteName.F, 5, 0);
        testSetNotePitchValues(s, n, NoteName.Fs, 6, 0);
        testSetNotePitchValues(s, n, NoteName.Fx, 7, 0);
        
        testSetNotePitchValues(s, n, NoteName.Gbb, 5, 0);
        testSetNotePitchValues(s, n, NoteName.Gb, 6, 0);
        testSetNotePitchValues(s, n, NoteName.G, 7, 0);
        testSetNotePitchValues(s, n, NoteName.Gs, 8, 0);
        testSetNotePitchValues(s, n, NoteName.Gx, 9, 0); 
    }
    
    @Test
    public void majorScale_convertToMidiNote_accidental() throws InvalidKeySignatureException {
        MajorScale scale = new MajorScale(NoteName.C);
        MidiNote result;
        Note note;
        int midiTickResolution = 8;
        
        // F# - this chromatic note should convert OK.
        note = new Note(3, 4, 1, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 66, 70, 0, midiTickResolution); 
        
        // Fx - this is the same as G, another note in the scale, so we will use F# instead
        note = new Note(3, 4, 2, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 66, 70, 0, midiTickResolution); 
        
        // B# - this is the same as C, another note in the scale, so we will use B instead
        note = new Note(6, 4, 1, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 71, 70, 0, midiTickResolution); 
        
        // Bx - this chromatic note should convert OK.
        note = new Note(6, 4, 2, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 73, 70, 0, midiTickResolution); 
        
        // Eb - this chromatic note should convert OK.
        note = new Note(2, 4, -1, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 63, 70, 0, midiTickResolution); 
        
        // Ebb - this is the same as D, another note in the scale, so we will use Eb instead
        note = new Note(2, 4, -2, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 63, 70, 0, midiTickResolution); 
        
        // Fb - this is the same as E, another note in the scale, so we will use F instead
        note = new Note(3, 4, -1, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 65, 70, 0, midiTickResolution);
        
        // Fbb - this note should convert ok.
        note = new Note(3, 4, -2, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 63, 70, 0, midiTickResolution);
        
        // Cb - this is the same as B, another note in the scale, so we will use C instead
        note = new Note(0, 4, -1, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 60, 70, 0, midiTickResolution); 
        
        // Cbb - this note should convert ok.
        note = new Note(0, 4, -2, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 58, 70, 0, midiTickResolution); 
    }
    
    @Test
    public void majorScale_convertToMidiNote() throws InvalidKeySignatureException {                
        MajorScale scale = new MajorScale(NoteName.C);
        MidiNote result;
        Note note;
        
        note = new Note(0, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 60, 70, 0, 48);                
        
        note.setScaleStep(5);
        note.setOctave(5);
        result = note.convertToMidiNote(scale, new Fraction(5, 1), midiTickResolution);
        assertMidiNoteValues(result, 81, 70, 40, 48);                
        
        note.setScaleStep(2);
        note.setOctave(2);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 40, 40, 16, 24); 
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 31, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 69, 40, 16, 24);
        
        // TODO: check something at the octave change...        
    }
    
    @Test
    public void minorScale_convertToMidiNote() throws InvalidKeySignatureException {                
        MinorScale scale = new MinorScale(NoteName.C);
        MidiNote result;
        Note note;
        
        note = new Note(6, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 70, 70, 0, 48);                
        
        note.setScaleStep(5);
        note.setOctave(5);
        result = note.convertToMidiNote(scale, new Fraction(5, 1), midiTickResolution);
        assertMidiNoteValues(result, 80, 70, 40, 48);                
        
        note.setScaleStep(3);
        note.setOctave(2);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 41, 40, 16, 24); 
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 31, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 68, 40, 16, 24); 
    }
    
    @Test
    public void harmonicMinorScale_convertToMidiNote() throws InvalidKeySignatureException {                
        HarmonicMinorScale scale = new HarmonicMinorScale(NoteName.C);
        MidiNote result;
        Note note;
        
        note = new Note(6, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 71, 70, 0, 48);                
        
        note.setScaleStep(5);
        note.setOctave(5);
        result = note.convertToMidiNote(scale, new Fraction(5, 1), midiTickResolution);
        assertMidiNoteValues(result, 80, 70, 40, 48);                
        
        note.setScaleStep(3);
        note.setOctave(2);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 41, 40, 16, 24); 
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 31, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 68, 40, 16, 24); 
    }
    
    @Test
    public void majorPentatonicScale_convertToMidiNote() throws InvalidKeySignatureException {                
        MajorPentatonicScale scale = new MajorPentatonicScale(NoteName.C);
        MidiNote result;
        Note note;
        
        note = new Note(4, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 69, 70, 0, 48);                
        
        note.setScaleStep(3);        
        result = note.convertToMidiNote(scale, new Fraction(5, 1), midiTickResolution);
        assertMidiNoteValues(result, 67, 70, 40, 48);                        
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 52, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 81, 40, 16, 24); 
    }
    
    @Test
    public void minorPentatonicScale_convertToMidiNote() throws InvalidKeySignatureException {                
        MinorPentatonicScale scale = new MinorPentatonicScale(NoteName.C);
        MidiNote result;
        Note note;
        
        note = new Note(4, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 70, 70, 0, 48);                
        
        note.setScaleStep(3);        
        result = note.convertToMidiNote(scale, new Fraction(5, 1), midiTickResolution);
        assertMidiNoteValues(result, 67, 70, 40, 48);                        
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 53, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 82, 40, 16, 24); 
    }
    
    @Test
    public void chromaticScale_convertToMidiNote() throws InvalidKeySignatureException {
        ChromaticScale scale = new ChromaticScale();
        MidiNote result;
        Note note;
        
        note = new Note(4, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution);
        assertMidiNoteValues(result, 64, 70, 0, 48);                
        
        note.setScaleStep(3);        
        result = note.convertToMidiNote(scale, new Fraction(5, 1), midiTickResolution);
        assertMidiNoteValues(result, 63, 70, 40, 48);                        
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 57, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 55, 40, 16, 24); 
        
        // try some notes with chromatic adjustments...
        // chromatic adjustments should be ignored since all notes are part of
        // the scale
        note.setScaleStep(3);
        note.setChromaticAdjustment(-2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 39, 40, 16, 24);
        
        note.setChromaticAdjustment(5);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution);
        assertMidiNoteValues(result, 39, 40, 16, 24);
    }
    
    @Test
    public void getScaleTypes() {
        List<Class> scaleTypes = Scale.getScaleTypes();
        
        // Test that we have all the scales (at least all of them at the time 
        // of writing this test).  There shouldn't be a need to add more scales
        // to this test--if this tests pass, it is getting the scales 
        // dynamically and there's no reason it won't pick up new scales as well.
        assertTrue(scaleTypes.contains(MajorScale.class));
        assertTrue(scaleTypes.contains(MinorScale.class));
        assertTrue(scaleTypes.contains(HarmonicMinorScale.class));
        assertTrue(scaleTypes.contains(MajorPentatonicScale.class));
        assertTrue(scaleTypes.contains(MinorPentatonicScale.class));
        assertTrue(scaleTypes.contains(ChromaticScale.class));
    }
    
    @Test
    public void getDefaultScale() {
        assertEquals(ChromaticScale.class, Scale.getDefault().getClass());
    }
    
    @Test
    public void toStringTest() throws InvalidKeySignatureException {
        assertEquals("F# Minor Pentatonic Scale", (new MinorPentatonicScale(NoteName.Fs)).toString());
    }
    
    protected static void testSetNotePitchValues(Scale s, Note n, NoteName nn, int scaleStep, int chromaticAdjustment) {
        s.setNotePitchValues(n, nn);
        assertEquals(scaleStep, n.getScaleStep());
        assertEquals(chromaticAdjustment, n.getChromaticAdjustment());
    }
    
    protected static void assertMidiNoteValues(MidiNote midiNote, int pitch, int velocity, long startTime, long duration) {
        assertEquals(pitch, midiNote.getPitch());
        assertEquals(velocity, midiNote.getVelocity());
        assertEquals(startTime, midiNote.getStartTime());
        assertEquals(duration, midiNote.getDuration());        
    }
}