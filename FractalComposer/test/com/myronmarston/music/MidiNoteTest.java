package com.myronmarston.music;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.InvalidMidiDataException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class MidiNoteTest {
    
    @Test
    public void getPitch() {
        System.out.println("getPitch");        
        int expResult = 60;
        MidiNote instance = new MidiNote(expResult, 10, 10);
        int result = instance.getPitch();
        assertEquals(expResult, result);        
    }

    /**
     * Test of setPitch method, of class MidiNote.
     */
    @Test
    public void setPitch() {
        System.out.println("setPitch");
        int pitch = 50;
        MidiNote instance = new MidiNote();
        instance.setPitch(pitch);
        int result = instance.getPitch();
        assertEquals(pitch, result);        
    }

    /**
     * Test of getDuration method, of class MidiNote.
     */
    @Test
    public void getDuration() {
        System.out.println("getDuration");
        long expResult = 3;
        MidiNote instance = new MidiNote(60, 10, expResult);
        long result = instance.getDuration();
        assertEquals(expResult, result);        
    }

    /**
     * Test of setDuration method, of class MidiNote.
     */
    @Test
    public void setDuration() {
        System.out.println("setDuration");
        long duration = 5;
        MidiNote instance = new MidiNote();
        instance.setDuration(duration);
        long result = instance.getDuration();
        assertEquals(duration, result);        
    }

    /**
     * Test of getStartTime method, of class MidiNote.
     */
    @Test
    public void getStartTime() {
        System.out.println("getStartTime");
        long expResult = 5L;
        MidiNote instance = new MidiNote(55, expResult, 6);        
        long result = instance.getStartTime();
        assertEquals(expResult, result);        
    }

    /**
     * Test of setStartTime method, of class MidiNote.
     */
    @Test
    public void setStartTime() {
        System.out.println("setStartTime");
        long startTime = 20;
        MidiNote instance = new MidiNote();
        instance.setStartTime(startTime);
        long result = instance.getStartTime();
        assertEquals(startTime, result);
    }

    /**
     * Test of getVelocity method, of class MidiNote.
     */
    @Test
    public void getVelocity() {
        System.out.println("getVelocity");
        int expResult = 0;
        MidiNote instance = new MidiNote(40, 10, 10, 0, expResult);        
        int result = instance.getVelocity();
        assertEquals(expResult, result);        
    }

    /**
     * Test of setVelocity method, of class MidiNote.
     */
    @Test
    public void setVelocity() {
        System.out.println("setVelocity");
        int velocity = 68;
        MidiNote instance = new MidiNote();
        instance.setVelocity(velocity);        
        int result = instance.getVelocity();
        assertEquals(velocity, result);
    }

    /**
     * Test of getChannel method, of class MidiNote.
     */
    @Test
    public void getChannel() {
        System.out.println("getChannel");
        int expResult = 3;
        MidiNote instance = new MidiNote(76, 10, 10, expResult, 64);        
        int result = instance.getChannel();
        assertEquals(expResult, result);        
    }

    /**
     * Test of setChannel method, of class MidiNote.
     */
    @Test
    public void setChannel() {
        System.out.println("setChannel");
        int channel = 4;
        MidiNote instance = new MidiNote();
        instance.setChannel(channel);
        int result = instance.getChannel();
        assertEquals(channel, result);
    }
    
    @Test
    public void defaultChannelValue() {
        System.out.println("defaultChannelValue");
        MidiNote instance = new MidiNote();
        assertEquals(MidiNote.DEFAULT_CHANNEL, instance.getChannel());
    }
    
    @Test
    public void defaultVelocityValue() {
        System.out.println("defaultVelocityValue");
        MidiNote instance = new MidiNote();
        assertEquals(MidiNote.DEFAULT_VELOCITY, instance.getVelocity());
    }
    
    @Test
    public void getNoteEnd() throws InvalidMidiDataException {
        MidiNote instance = new MidiNote(70, 35, 4, 0, 80);
        assertEquals(39L, instance.getNoteEnd());
    }

    /**
     * Test of getNoteOnEvent method, of class MidiNote.
     */
    @Test
    public void getNoteEvents() throws InvalidMidiDataException {
        System.out.println("getNoteEvents");
        MidiNote instance = new MidiNote(70, 35, 4, 0, 80);
        MidiEvent noteOnEvent = instance.getNoteOnEvent();
        MidiEvent noteOffEvent = instance.getNoteOffEvent();                
        assertNoteEventEqual(noteOnEvent, 35, (byte) -112, (byte) 70, (byte) 80);
        assertNoteEventEqual(noteOffEvent, 39, (byte) -128, (byte) 70, (byte) 0);
    }
    
    @Test
    public void noteEventRefreshOnFieldChange() throws InvalidMidiDataException {
        System.out.println("noteEventRefreshOnFieldChange");
        MidiNote instance = new MidiNote();
        MidiEvent noteOnEvent = instance.getNoteOnEvent();
        MidiEvent noteOffEvent = instance.getNoteOffEvent();                
        
        assertNoteEventEqual(noteOnEvent, 0, (byte) -112, (byte) 0, (byte) MidiNote.DEFAULT_VELOCITY);
        assertNoteEventEqual(noteOffEvent, 0, (byte) -128, (byte) 0, (byte) 0);
        
        instance.setPitch(92);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 0, (byte) -112, (byte) 92, (byte) MidiNote.DEFAULT_VELOCITY);
        assertNoteEventEqual(noteOffEvent, 0, (byte) -128, (byte) 92, (byte) 0);
        
        instance.setStartTime(16L);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 16, (byte) -112, (byte) 92, (byte) MidiNote.DEFAULT_VELOCITY);
        assertNoteEventEqual(noteOffEvent, 16, (byte) -128, (byte) 92, (byte) 0);
        
        instance.setDuration(8L);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 16, (byte) -112, (byte) 92, (byte) MidiNote.DEFAULT_VELOCITY);
        assertNoteEventEqual(noteOffEvent, 24, (byte) -128, (byte) 92, (byte) 0);
        
        instance.setVelocity(120);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 16, (byte) -112, (byte) 92, (byte) 120);
        assertNoteEventEqual(noteOffEvent, 24, (byte) -128, (byte) 92, (byte) 0);                
        
        instance.setChannel(2);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 16, (byte) -110, (byte) 92, (byte) 120);
        assertNoteEventEqual(noteOffEvent, 24, (byte) -126, (byte) 92, (byte) 0);                
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void channelIllegalArgumentException() {
        MidiNote mn = new MidiNote();
        mn.setChannel(16);
    }
    
    static public void assertNoteEventEqual(MidiEvent noteEvent, long tick, byte byte1, byte byte2, byte byte3) {
        assertEquals(tick, noteEvent.getTick());
        javax.sound.midi.MidiMessage msg = noteEvent.getMessage();
        assertEquals(byte1, msg.getMessage()[0]);        
        assertEquals(byte2, msg.getMessage()[1]);
        assertEquals(byte3, msg.getMessage()[2]);
    }
}