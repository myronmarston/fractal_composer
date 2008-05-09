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
        byte byte1 = (byte) (javax.sound.midi.ShortMessage.PROGRAM_CHANGE - 256 + channel);
        byte byte2 = (byte) patch;        
        assertEquals(byte1, msg.getMessage()[0]);        
        assertEquals(byte2, msg.getMessage()[1]);        
    }

}