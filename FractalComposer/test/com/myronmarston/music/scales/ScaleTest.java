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

package com.myronmarston.music.scales;

import com.myronmarston.music.MidiNote;
import com.myronmarston.music.Note;
import com.myronmarston.music.NoteName;

import com.myronmarston.util.Fraction;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class ScaleTest {

    public ScaleTest() {
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
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, false);
        assertMidiNoteValues(result, 66, 70, 0, midiTickResolution); 
        
        // Fx - this is the same as G, another note in the scale, so we will use F# instead
        note = new Note(3, 4, 2, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, false);
        assertMidiNoteValues(result, 66, 70, 0, midiTickResolution); 
        
        // B# - this is the same as C, another note in the scale, so we will use B instead
        note = new Note(6, 4, 1, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, false);
        assertMidiNoteValues(result, 71, 70, 0, midiTickResolution); 
        
        // Bx - this chromatic note should convert OK.
        note = new Note(6, 4, 2, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, false);
        assertMidiNoteValues(result, 73, 70, 0, midiTickResolution); 
        
        // Eb - this chromatic note should convert OK.
        note = new Note(2, 4, -1, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, false);
        assertMidiNoteValues(result, 63, 70, 0, midiTickResolution); 
        
        // Ebb - this is the same as D, another note in the scale, so we will use Eb instead
        note = new Note(2, 4, -2, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, false);
        assertMidiNoteValues(result, 63, 70, 0, midiTickResolution); 
        
        // Fb - this is the same as E, another note in the scale, so we will use F instead
        note = new Note(3, 4, -1, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, false);
        assertMidiNoteValues(result, 65, 70, 0, midiTickResolution);
        
        // Fbb - this note should convert ok.
        note = new Note(3, 4, -2, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, false);
        assertMidiNoteValues(result, 63, 70, 0, midiTickResolution);
        
        // Cb - this is the same as B, another note in the scale, so we will use C instead
        note = new Note(0, 4, -1, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, false);
        assertMidiNoteValues(result, 60, 70, 0, midiTickResolution); 
        
        // Cbb - this note should convert ok.
        note = new Note(0, 4, -2, new Fraction(1, 1), 70);                      
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, false);
        assertMidiNoteValues(result, 58, 70, 0, midiTickResolution); 
    }
    
    @Test
    public void majorScale_convertToMidiNote() throws InvalidKeySignatureException {                
        MajorScale scale = new MajorScale(NoteName.C);
        MidiNote result;
        Note note;
        
        note = new Note(0, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 60, 70, 0, 48);                
        
        note.setScaleStep(5);
        note.setOctave(5);
        result = note.convertToMidiNote(scale, new Fraction(5, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 81, 70, 40, 48);                
        
        note.setScaleStep(2);
        note.setOctave(2);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 40, 40, 16, 24); 
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 31, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
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
        
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 70, 70, 0, 48);                
        
        note.setScaleStep(5);
        note.setOctave(5);
        result = note.convertToMidiNote(scale, new Fraction(5, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 80, 70, 40, 48);                
        
        note.setScaleStep(3);
        note.setOctave(2);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 41, 40, 16, 24); 
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 31, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 68, 40, 16, 24); 
    }
    
    @Test
    public void harmonicMinorScale_convertToMidiNote() throws InvalidKeySignatureException {                
        HarmonicMinorScale scale = new HarmonicMinorScale(NoteName.C);
        MidiNote result;
        Note note;
        
        note = new Note(6, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 71, 70, 0, 48);                
        
        note.setScaleStep(5);
        note.setOctave(5);
        result = note.convertToMidiNote(scale, new Fraction(5, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 80, 70, 40, 48);                
        
        note.setScaleStep(3);
        note.setOctave(2);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 41, 40, 16, 24); 
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 31, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 68, 40, 16, 24); 
    }
    
    @Test
    public void majorPentatonicScale_convertToMidiNote() throws InvalidKeySignatureException {                
        MajorPentatonicScale scale = new MajorPentatonicScale(NoteName.C);
        MidiNote result;
        Note note;
        
        note = new Note(4, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 69, 70, 0, 48);                
        
        note.setScaleStep(3);        
        result = note.convertToMidiNote(scale, new Fraction(5, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 67, 70, 40, 48);                        
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 52, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 81, 40, 16, 24); 
    }
    
    @Test
    public void minorPentatonicScale_convertToMidiNote() throws InvalidKeySignatureException {                
        MinorPentatonicScale scale = new MinorPentatonicScale(NoteName.C);
        MidiNote result;
        Note note;
        
        note = new Note(4, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 70, 70, 0, 48);                
        
        note.setScaleStep(3);        
        result = note.convertToMidiNote(scale, new Fraction(5, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 67, 70, 40, 48);                        
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 53, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 82, 40, 16, 24); 
    }
    
    @Test
    public void chromaticScale_convertToMidiNote() throws InvalidKeySignatureException {
        ChromaticScale scale = new ChromaticScale();
        MidiNote result;
        Note note;
        
        note = new Note(4, 4, 0, new Fraction(6, 1), 70);      
        int midiTickResolution = 8;
        
        result = note.convertToMidiNote(scale, new Fraction(0, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 64, 70, 0, 48);                
        
        note.setScaleStep(3);        
        result = note.convertToMidiNote(scale, new Fraction(5, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 63, 70, 40, 48);                        
        
        // try a few unnormalized notes...
        note.setScaleStep(-3);
        note.setVolume(40);
        note.setDuration(new Fraction(3, 1));
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 57, 40, 16, 24); 
        
        note.setScaleStep(19);
        note.setOctave(2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, true);
        assertMidiNoteValues(result, 55, 40, 16, 24); 
        
        // try some notes with chromatic adjustments...
        // chromatic adjustments should be ignored since all notes are part of
        // the scale        
        note.setScaleStep(3);
        note.setChromaticAdjustment(-2);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, false);
        assertMidiNoteValues(result, 39, 40, 16, 24);
        
        note.setChromaticAdjustment(5);
        result = note.convertToMidiNote(scale, new Fraction(2, 1), midiTickResolution, MidiNote.DEFAULT_CHANNEL, false);
        assertMidiNoteValues(result, 39, 40, 16, 24);
    }
    
    @Test
    public void getScaleTypes() throws InvalidKeySignatureException {                
        // Test that we have all the scales (at least all of them at the time 
        // of writing this test).  There shouldn't be a need to add more scales
        // to this test--if this tests pass, it is getting the scales 
        // dynamically and there's no reason it won't pick up new scales as well.
        assertTrue(Scale.SCALE_TYPES.containsKey(MajorScale.class));
        assertEquals(Scale.SCALE_TYPES.get(MajorScale.class), Tonality.Major.getValidKeyNames());
        assertTrue(Scale.SCALE_TYPES.containsKey(MinorScale.class));
        assertEquals(Scale.SCALE_TYPES.get(MinorScale.class), Tonality.Minor.getValidKeyNames());
        assertTrue(Scale.SCALE_TYPES.containsKey(HarmonicMinorScale.class));
        assertEquals(Scale.SCALE_TYPES.get(MinorScale.class), Tonality.Minor.getValidKeyNames());
        assertTrue(Scale.SCALE_TYPES.containsKey(MajorPentatonicScale.class));
        assertEquals(Scale.SCALE_TYPES.get(MajorScale.class), Tonality.Major.getValidKeyNames());
        assertTrue(Scale.SCALE_TYPES.containsKey(MinorPentatonicScale.class));
        assertEquals(Scale.SCALE_TYPES.get(MinorScale.class), Tonality.Minor.getValidKeyNames());
        assertTrue(Scale.SCALE_TYPES.containsKey(ChromaticScale.class));
        assertEquals(Scale.SCALE_TYPES.get(ChromaticScale.class), (new ChromaticScale()).getValidKeyNames());
    }
    
    @Test
    public void defaultScale() {
        assertTrue(Scale.DEFAULT instanceof ChromaticScale);
    }
    
    @Test
    public void toStringTest() throws InvalidKeySignatureException {
        assertEquals("F# Minor Pentatonic Scale", (new MinorPentatonicScale(NoteName.Fs)).toString());
    }    
    
    @Test
    public void getValidKeyNames() throws InvalidKeySignatureException {
        assertEquals(Tonality.Major.getValidKeyNames(), (new MajorScale()).getValidKeyNames());
        assertEquals(Tonality.Major.getValidKeyNames(), (new MajorPentatonicScale()).getValidKeyNames());
        assertEquals(Tonality.Minor.getValidKeyNames(), (new MinorScale()).getValidKeyNames());
        assertEquals(Tonality.Minor.getValidKeyNames(), (new HarmonicMinorScale()).getValidKeyNames());
        assertEquals(Tonality.Minor.getValidKeyNames(), (new MinorPentatonicScale()).getValidKeyNames());        
        assertEquals(Collections.EMPTY_LIST, (new ChromaticScale()).getValidKeyNames());
    }
    
    @Test
    public void scaleDefaultKeys() throws InvalidKeySignatureException {
        assertEquals(NoteName.C, (new MajorScale()).getKeyName());
        assertEquals(NoteName.C, (new MajorPentatonicScale()).getKeyName());
        assertEquals(NoteName.C, (new ChromaticScale()).getKeyName());
        assertEquals(NoteName.A, (new MinorScale()).getKeyName());
        assertEquals(NoteName.A, (new MinorPentatonicScale()).getKeyName());
        assertEquals(NoteName.A, (new HarmonicMinorScale()).getKeyName());        
    }
    
    @Test
    public void getLetterNameForNote() throws Exception {
        Scale s = new MajorScale(NoteName.B);
                
        try {
            s.getLetterNameForScaleStep(-1);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        assertEquals(NoteName.B, s.getLetterNameForScaleStep(0));
        assertEquals(NoteName.C, s.getLetterNameForScaleStep(1));
        assertEquals(NoteName.D, s.getLetterNameForScaleStep(2));
        assertEquals(NoteName.E, s.getLetterNameForScaleStep(3));
        assertEquals(NoteName.F, s.getLetterNameForScaleStep(4));
        assertEquals(NoteName.G, s.getLetterNameForScaleStep(5));
        assertEquals(NoteName.A, s.getLetterNameForScaleStep(6));  
        
        try {
            s.getLetterNameForScaleStep(7);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        s = new ChromaticScale();
                
        try {
            s.getLetterNameForScaleStep(-1);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        assertEquals(NoteName.C, s.getLetterNameForScaleStep(0));
        assertEquals(NoteName.C, s.getLetterNameForScaleStep(1)); // C#
        assertEquals(NoteName.D, s.getLetterNameForScaleStep(2));
        assertEquals(NoteName.E, s.getLetterNameForScaleStep(3)); // Eb
        assertEquals(NoteName.E, s.getLetterNameForScaleStep(4));
        assertEquals(NoteName.F, s.getLetterNameForScaleStep(5));
        assertEquals(NoteName.F, s.getLetterNameForScaleStep(6)); // F#
        assertEquals(NoteName.G, s.getLetterNameForScaleStep(7));  
        assertEquals(NoteName.G, s.getLetterNameForScaleStep(8)); // G#
        assertEquals(NoteName.A, s.getLetterNameForScaleStep(9));  
        assertEquals(NoteName.B, s.getLetterNameForScaleStep(10)); // Bb  
        assertEquals(NoteName.B, s.getLetterNameForScaleStep(11));  
        
        try {
            s.getLetterNameForScaleStep(12);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        s = new MajorPentatonicScale(NoteName.Eb);
                
        try {
            s.getLetterNameForScaleStep(-1);
            fail();
        } catch (IllegalArgumentException ex) {}
        
        assertEquals(NoteName.E, s.getLetterNameForScaleStep(0));
        assertEquals(NoteName.F, s.getLetterNameForScaleStep(1));
        assertEquals(NoteName.G, s.getLetterNameForScaleStep(2));
        assertEquals(NoteName.B, s.getLetterNameForScaleStep(3));
        assertEquals(NoteName.C, s.getLetterNameForScaleStep(4));        
        
        try {
            s.getLetterNameForScaleStep(5);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testEquals() throws Exception {
        // two of the same scale
        assertTrue((new MajorScale(NoteName.D)).equals(new MajorScale(NoteName.D)));
        assertTrue((new MajorScale(NoteName.D)).hashCode() == (new MajorScale(NoteName.D)).hashCode());
        
        // same scale type, different note name
        assertFalse((new MajorScale(NoteName.Bb)).equals(new MajorScale(NoteName.G)));
        assertFalse((new MajorScale(NoteName.Bb)).hashCode() == (new MajorScale(NoteName.G)).hashCode());
        
        // same note name, different scale type
        assertFalse((new MajorScale(NoteName.D)).equals(new MajorPentatonicScale(NoteName.D)));        
        assertFalse((new MajorScale(NoteName.D)).hashCode() == (new MajorPentatonicScale(NoteName.D)).hashCode());
        
        // different scale type and note name
        assertFalse((new HarmonicMinorScale(NoteName.F)).equals(new ChromaticScale()));
        assertFalse((new HarmonicMinorScale(NoteName.F)).hashCode() == (new ChromaticScale()).hashCode());
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