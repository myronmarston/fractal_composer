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
import com.myronmarston.music.NoteName;
import com.myronmarston.util.FileHelper;
import javax.sound.midi.*;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Myron
 */
public class KeySignatureTest {  
    
    @Test
    public void getKeySignatureMidiEvent() throws InvalidKeySignatureException {
        KeySignature ks = new KeySignature(Tonality.Major, NoteName.E);        
        assertKeySignatureEventEqual(ks.getKeySignatureMidiEvent(0), (byte) 4, (byte) 0, 0);
        
        ks = new KeySignature(Tonality.Major, NoteName.Bb);        
        assertKeySignatureEventEqual(ks.getKeySignatureMidiEvent(0), (byte) -2, (byte) 0, 0);
        
        ks = new KeySignature(Tonality.Minor, NoteName.Bb);        
        assertKeySignatureEventEqual(ks.getKeySignatureMidiEvent(0), (byte) -5, (byte) 1, 0);
    }
    
    @Test(expected=InvalidKeySignatureException.class)
    public void majorScaleInvalidKeySignature() throws InvalidKeySignatureException {
        MajorScale s = new MajorScale(NoteName.As);
    }
    
    @Test(expected=InvalidKeySignatureException.class)
    public void minorScaleInvalidKeySignature() throws InvalidKeySignatureException {
        NaturalMinorScale s = new NaturalMinorScale(NoteName.Gb);
    }
    
    @Test
    public void majorScaleGetKeySignature() throws InvalidMidiDataException, InvalidKeySignatureException {
        MajorScale s = new MajorScale(NoteName.D);
        assertKeySignatureEventEqual(s.getKeySignature().getKeySignatureMidiEvent(0), (byte) 2, (byte) 0, 0);
        
        s = new MajorScale(NoteName.Ab);
        assertKeySignatureEventEqual(s.getKeySignature().getKeySignatureMidiEvent(0), (byte) -4, (byte) 0, 0);
    }
    
    @Test
    public void minorScaleGetKeySignature() throws InvalidMidiDataException, InvalidKeySignatureException {
        NaturalMinorScale s = new NaturalMinorScale(NoteName.D);
        assertKeySignatureEventEqual(s.getKeySignature().getKeySignatureMidiEvent(0), (byte) -1, (byte) 1, 0);
        
        s = new NaturalMinorScale(NoteName.Gs);
        assertKeySignatureEventEqual(s.getKeySignature().getKeySignatureMidiEvent(0), (byte) 5, (byte) 1, 0);
    }
    
    @Test
    public void laterKeySignatureEvent() throws Exception {
        NaturalMinorScale s = new NaturalMinorScale(NoteName.D);
        assertKeySignatureEventEqual(s.getKeySignature().getKeySignatureMidiEvent(37), (byte) -1, (byte) 1, 37);
    }
    
    @Test
    public void toGuidoString() throws Exception {
        KeySignature ks = new KeySignature(Tonality.Major, NoteName.Bb);
        assertEquals("\\key<-2>", ks.toGuidoString());
        
        ks = new KeySignature(Tonality.Locrian, NoteName.As);
        assertEquals("\\key<5>", ks.toGuidoString());
        
        ks = new KeySignature(Tonality.Dorian, NoteName.E);
        assertEquals("\\key<2>", ks.toGuidoString());
    }
    
    @Test
    public void toLilypondString() throws Exception {
        testToLilypondString(Tonality.Major, NoteName.Cs, "\\key cs \\major" + FileHelper.NEW_LINE);        
        testToLilypondString(Tonality.Minor, NoteName.Ab, "\\key af \\minor" + FileHelper.NEW_LINE);
        testToLilypondString(Tonality.Mixolydian, NoteName.D, "\\key d \\mixolydian" + FileHelper.NEW_LINE);
        testToLilypondString(Tonality.Phrygian, NoteName.B, "\\key b \\phrygian" + FileHelper.NEW_LINE);
    }
    
    private static void testToLilypondString(Tonality tonality, NoteName key, String expected) throws Exception {
        KeySignature ks = new KeySignature(tonality, key);
        assertEquals(expected, ks.toLilypondString());
    }
    
    @Test
    public void getKeyNameWithSameNumAccidentals() throws Exception {
        testGetKeyNameWithSameNumAccidentals(NoteName.C, Tonality.Major, NoteName.C, NoteName.D, NoteName.E, NoteName.F, NoteName.G, NoteName.A, NoteName.B);
        testGetKeyNameWithSameNumAccidentals(NoteName.G, Tonality.Dorian, NoteName.F, NoteName.G, NoteName.A, NoteName.Bb, NoteName.C, NoteName.D, NoteName.E);
        testGetKeyNameWithSameNumAccidentals(NoteName.G, Tonality.Phrygian, NoteName.Eb, NoteName.F, NoteName.G, NoteName.Ab, NoteName.Bb, NoteName.C, NoteName.D);        
        testGetKeyNameWithSameNumAccidentals(NoteName.Fs, Tonality.Lydian, NoteName.Cs, NoteName.Ds, NoteName.Es, NoteName.Fs, NoteName.Gs, NoteName.As, NoteName.Bs);
        testGetKeyNameWithSameNumAccidentals(NoteName.A, Tonality.Mixolydian, NoteName.D, NoteName.E, NoteName.Fs, NoteName.G, NoteName.A, NoteName.B, NoteName.Cs);
        testGetKeyNameWithSameNumAccidentals(NoteName.Ab, Tonality.Minor, NoteName.Cb, NoteName.Db, NoteName.Eb, NoteName.Fb, NoteName.Gb, NoteName.Ab, NoteName.Bb);                
        testGetKeyNameWithSameNumAccidentals(NoteName.C, Tonality.Locrian, NoteName.Db, NoteName.Eb, NoteName.F, NoteName.Gb, NoteName.Ab, NoteName.Bb, NoteName.C);                
    }
    
    private static void testGetKeyNameWithSameNumAccidentals(NoteName orig, Tonality tonality, NoteName majorNN, NoteName dorianNN, NoteName phrygianNN, NoteName lydianNN, NoteName mixolydianNN, NoteName minorNN, NoteName locrianNN) throws Exception {
        KeySignature ks = new KeySignature(tonality, orig);
        assertEquals(majorNN, ks.getKeyNameWithSameNumAccidentals(Tonality.Major));
        assertEquals(dorianNN, ks.getKeyNameWithSameNumAccidentals(Tonality.Dorian));
        assertEquals(phrygianNN, ks.getKeyNameWithSameNumAccidentals(Tonality.Phrygian));
        assertEquals(lydianNN, ks.getKeyNameWithSameNumAccidentals(Tonality.Lydian));
        assertEquals(mixolydianNN, ks.getKeyNameWithSameNumAccidentals(Tonality.Mixolydian));
        assertEquals(minorNN, ks.getKeyNameWithSameNumAccidentals(Tonality.Minor));
        assertEquals(locrianNN, ks.getKeyNameWithSameNumAccidentals(Tonality.Locrian));
    }
    
    static public MidiEvent getIndexedKeySigEvent(Track t, int index) {
        int count = 0;
        for (int i = 0; i < t.size(); i++) {
            MidiEvent me = t.get(i);
            if (me.getMessage().getStatus() == 0xFF && me.getMessage().getMessage()[1] == KeySignature.KEY_SIGNATURE_META_MESSAGE_TYPE) {
                if (count == index) return me;
                count++;
            }
        }
        
        return null;
    }

    static public void assertKeySignatureEventEqual(MidiEvent keySignatureEvent, byte accidentals, byte tonalityValue, long tick) {        
        javax.sound.midi.MidiMessage msg = keySignatureEvent.getMessage();
        assertEquals(5, msg.getLength());
        assertEquals((byte) 0xFF, msg.getMessage()[0]); // 0xFF for a meta message
        assertEquals((byte) 89, msg.getMessage()[1]);   // 89 is key signature type
        assertEquals((byte) 2, msg.getMessage()[2]);    // the size of the rest of the message     
        assertEquals(accidentals, msg.getMessage()[3]);        
        assertEquals(tonalityValue, msg.getMessage()[4]);        
        assertEquals(tick, keySignatureEvent.getTick() - MidiNote.MIDI_SEQUENCE_START_SILENCE_TICK_OFFSET);
    }
}