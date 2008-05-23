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

import javax.sound.midi.MidiEvent;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class InstrumentTest {

    @Test
    public void testAVAILABLE_INSTRUMENTS() {
        // test the size and that it has some basic instruments
        assertEquals(128, Instrument.AVAILABLE_INSTRUMENTS.size());
        assertTrue(Instrument.AVAILABLE_INSTRUMENTS.contains("Piano"));
        assertTrue(Instrument.AVAILABLE_INSTRUMENTS.contains("Violin"));
        assertTrue(Instrument.AVAILABLE_INSTRUMENTS.contains("Viola"));
        assertTrue(Instrument.AVAILABLE_INSTRUMENTS.contains("Cello"));
        assertTrue(Instrument.AVAILABLE_INSTRUMENTS.contains("Trumpet"));
    }
    
    @Test
    public void testGetDefault() {        
        assertEquals("Piano", Instrument.getDefault().getName());        
    }

    @Test
    public void testGetInstrument() {        
        assertEquals("Violin", Instrument.getInstrument("violin").getMidiInstrument().getName());
        assertEquals("Trumpet", Instrument.getInstrument("TRUMPET").getMidiInstrument().getName());
        assertNull(Instrument.getInstrument("crazy instrument that doesn't exit"));
    }

    @Test
    public void testGetProgramChangeMidiEvent() {
        Instrument i = Instrument.getInstrument("Cello");
        assertMidiProgramChangeEventEquals(i.getProgramChangeMidiEvent(3), 0, 3, 42);
        
        i = Instrument.getInstrument("Violin");
        assertMidiProgramChangeEventEquals(i.getProgramChangeMidiEvent(12), 0, 12, 40);
    }
    
    public static void assertMidiProgramChangeEventEquals(MidiEvent event, long tick, int channel, int patch) {
        assertEquals(tick, event.getTick());
        javax.sound.midi.MidiMessage msg = event.getMessage();
        assertEquals(2, msg.getMessage().length);
        byte byte1 = (byte) (javax.sound.midi.ShortMessage.PROGRAM_CHANGE + channel);
        byte byte2 = (byte) patch;        
        assertEquals(byte1, msg.getMessage()[0]);        
        assertEquals(byte2, msg.getMessage()[1]);        
    }

}