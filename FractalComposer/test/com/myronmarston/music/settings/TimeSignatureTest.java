package com.myronmarston.music.settings;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class TimeSignatureTest {

    public TimeSignatureTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test(expected=NonPositiveTimeSignatureException.class)
    public void negativeNumerator() throws InvalidTimeSignatureException {
        TimeSignature ts = new TimeSignature(-3, 2);
    }
    
    @Test(expected=NonPositiveTimeSignatureException.class)
    public void negativeDenominator() throws InvalidTimeSignatureException {
        TimeSignature ts = new TimeSignature(3, -2);
    }
    
    @Test(expected=TimeSignatureDenominatorNotAPowerOf2Exception.class)
    public void denominatorNotAPowerOf2() throws InvalidTimeSignatureException {
        TimeSignature ts = new TimeSignature(4, 5);
    }
    
    @Test
    public void denominatorPowerOf2() throws InvalidTimeSignatureException {
        TimeSignature ts = new TimeSignature(4, 4);
        assertEquals(2, ts.getDenominatorPowerOf2());
        
        ts.setDenominator(8);
        assertEquals(3, ts.getDenominatorPowerOf2());
        
        ts.setDenominator(2);
        assertEquals(1, ts.getDenominatorPowerOf2());
    }
    
    @Test
    public void createMidiTimeSignatureEvent() throws InvalidTimeSignatureException, InvalidMidiDataException {
        TimeSignature ts = new TimeSignature(4, 4);
        assertTimeSignatureEventEqual(ts.createMidiTimeSignatureEvent(), (byte) 4, (byte) 2);
        
        ts = new TimeSignature(6, 8);
        assertTimeSignatureEventEqual(ts.createMidiTimeSignatureEvent(), (byte) 6, (byte) 3);
        
        ts = new TimeSignature(15, 16);
        assertTimeSignatureEventEqual(ts.createMidiTimeSignatureEvent(), (byte) 15, (byte) 4);        
    }
    
    static public void assertTimeSignatureEventEqual(MidiEvent timeSignatureEvent, byte byte1, byte byte2) {
        assertEquals(0L, timeSignatureEvent.getTick());
        javax.sound.midi.MidiMessage msg = timeSignatureEvent.getMessage();
        assertEquals(7, msg.getLength());
        assertEquals((byte) 0xFF, msg.getMessage()[0]); // 0xFF for a meta message
        assertEquals((byte) 88, msg.getMessage()[1]);   // 88 is time signature type
        assertEquals((byte) 4, msg.getMessage()[2]);    // the size of the rest of the message     
        assertEquals(byte1, msg.getMessage()[3]);        
        assertEquals(byte2, msg.getMessage()[4]);
        assertEquals((byte) 24, msg.getMessage()[5]);
        assertEquals((byte) 8, msg.getMessage()[6]);
    }
}