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
        assertNoteEventEqual(noteOnEvent, 35, (byte) -112, (byte) 70, (byte) 80, true);
        assertNoteEventEqual(noteOffEvent, 39, (byte) -128, (byte) 70, (byte) 0, true);
    }
    
    @Test
    public void noteEventRefreshOnFieldChange() throws InvalidMidiDataException {
        System.out.println("noteEventRefreshOnFieldChange");
        MidiNote instance = new MidiNote();
        MidiEvent noteOnEvent = instance.getNoteOnEvent();
        MidiEvent noteOffEvent = instance.getNoteOffEvent();                
        
        assertNoteEventEqual(noteOnEvent, 0, (byte) -112, (byte) 0, (byte) MidiNote.DEFAULT_VELOCITY, true);
        assertNoteEventEqual(noteOffEvent, 0, (byte) -128, (byte) 0, (byte) 0, true);
        
        instance.setPitch(92);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 0, (byte) -112, (byte) 92, (byte) MidiNote.DEFAULT_VELOCITY, true);
        assertNoteEventEqual(noteOffEvent, 0, (byte) -128, (byte) 92, (byte) 0, true);
        
        instance.setStartTime(16L);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 16, (byte) -112, (byte) 92, (byte) MidiNote.DEFAULT_VELOCITY, true);
        assertNoteEventEqual(noteOffEvent, 16, (byte) -128, (byte) 92, (byte) 0, true);
        
        instance.setDuration(8L);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 16, (byte) -112, (byte) 92, (byte) MidiNote.DEFAULT_VELOCITY, true);
        assertNoteEventEqual(noteOffEvent, 24, (byte) -128, (byte) 92, (byte) 0, true);
        
        instance.setVelocity(120);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 16, (byte) -112, (byte) 92, (byte) 120, true);
        assertNoteEventEqual(noteOffEvent, 24, (byte) -128, (byte) 92, (byte) 0, true);                
        
        instance.setChannel(2);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 16, (byte) -110, (byte) 92, (byte) 120, true);
        assertNoteEventEqual(noteOffEvent, 24, (byte) -126, (byte) 92, (byte) 0, true);                
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void channelIllegalArgumentException() {
        MidiNote mn = new MidiNote();
        mn.setChannel(16);
    }       
    
    static public void assertNoteEventEqual(MidiEvent noteEvent, long tick, byte byte1, byte byte2, byte byte3) {
        assertNoteEventEqual(noteEvent, tick, byte1, byte2, byte3, false);
    }
    
    static public void assertNoteEventEqual(MidiEvent noteEvent, long tick, byte byte1, byte byte2, byte byte3, boolean ignoreTickOffset) {
        int tickOffset = (ignoreTickOffset ? 0 : MidiNote.MIDI_SEQUENCE_START_SILENCE_TICK_OFFSET);
        
        assertEquals(tick, noteEvent.getTick() - tickOffset);
        javax.sound.midi.MidiMessage msg = noteEvent.getMessage();
        assertEquals(byte1, msg.getMessage()[0]);        
        assertEquals(byte2, msg.getMessage()[1]);
        assertEquals(byte3, msg.getMessage()[2]);
    }
    
    static public void assertMidiNoteEqual(MidiNote mn, int pitch, int velocity, long startTime, long duration, int channel) {
        assertEquals(pitch, mn.getPitch());
        assertEquals(velocity, mn.getVelocity());               
        assertEquals(startTime, mn.getStartTime());
        assertEquals(duration, mn.getDuration());
        assertEquals(channel, mn.getChannel());
    }    
}