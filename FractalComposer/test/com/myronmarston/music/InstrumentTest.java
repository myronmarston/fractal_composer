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

import com.myronmarston.util.FileHelper;
import java.util.*;
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
        assertTrue(Instrument.AVAILABLE_INSTRUMENTS.size() >= 120);
        assertTrue(Instrument.AVAILABLE_INSTRUMENTS.contains("Violin"));
        assertTrue(Instrument.AVAILABLE_INSTRUMENTS.contains("Viola"));
        assertTrue(Instrument.AVAILABLE_INSTRUMENTS.contains("Cello"));
        assertTrue(Instrument.AVAILABLE_INSTRUMENTS.contains("Trumpet"));
        
        // test that it is sorted
        List<String> copy = new ArrayList<String>(Instrument.AVAILABLE_INSTRUMENTS);
        Collections.sort(copy);
        
        for (int i = 0; i < copy.size(); i++) {
            assertEquals(copy.get(i), Instrument.AVAILABLE_INSTRUMENTS.get(i));
        }
    }
    
    @Test
    public void testDEFAULT() {        
        assertTrue(Instrument.DEFAULT.getName().contains("Piano"));        
    }

    @Test
    public void testGetInstrument() {        
        assertEquals("Violin", Instrument.getInstrument("violin").getName());
        assertEquals("Trumpet", Instrument.getInstrument("TRUMPET").getName());
        assertNull(Instrument.getInstrument("crazy instrument that doesn't exit"));
    }

    @Test
    public void testGetProgramChangeMidiEvent() {
        Instrument i = Instrument.getInstrument("Cello");
        assertMidiProgramChangeEventEquals(i.getProgramChangeMidiEvent(3), 0, 3, 42);
        
        i = Instrument.getInstrument("Violin");
        assertMidiProgramChangeEventEquals(i.getProgramChangeMidiEvent(12), 0, 12, 40);
    }
    
    @Test
    public void toGuidoString() {
        Instrument i = Instrument.getInstrument("Cello");
        
        // Cello is Midi patch number 42
        assertEquals("\\instr<\"Cello\", \"MIDI 42\">", i.toGuidoString());
    }
    
    @Test
    public void toLilypondString() {
        Instrument i = Instrument.getInstrument("Cello");
        assertEquals("\\set Staff.instrumentName = \"Cello\"" + FileHelper.NEW_LINE, i.toLilypondString());
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