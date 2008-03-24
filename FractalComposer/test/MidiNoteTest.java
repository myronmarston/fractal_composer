/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.sound.midi.MidiEvent;
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
public class MidiNoteTest {

    public MidiNoteTest() {
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

    /**
     * Test of getPitch method, of class MidiNote.
     */
    @Test
    public void getPitch() {
        System.out.println("getPitch");        
        int expResult = 60;
        MidiNote instance = new MidiNote(expResult, 10L, 10L);
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
        long expResult = 3L;
        MidiNote instance = new MidiNote(60, 10L, expResult);
        long result = instance.getDuration();
        assertEquals(expResult, result);        
    }

    /**
     * Test of setDuration method, of class MidiNote.
     */
    @Test
    public void setDuration() {
        System.out.println("setDuration");
        long duration = 0L;
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
        MidiNote instance = new MidiNote(55, expResult, 6L);        
        long result = instance.getStartTime();
        assertEquals(expResult, result);        
    }

    /**
     * Test of setStartTime method, of class MidiNote.
     */
    @Test
    public void setStartTime() {
        System.out.println("setStartTime");
        long startTime = 20L;
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
        MidiNote instance = new MidiNote(40, 10L, 10L, 0, expResult);        
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
        MidiNote instance = new MidiNote(76, 10L, 10L, expResult, 64);        
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

    /**
     * Test of getNoteOnEvent method, of class MidiNote.
     */
    @Test
    public void getNoteEvents() throws Exception {
        System.out.println("getNoteEvents");
        MidiNote instance = new MidiNote(70, 35L, 4L, 0, 80);
        MidiEvent noteOnEvent = instance.getNoteOnEvent();
        MidiEvent noteOffEvent = instance.getNoteOffEvent();                
        assertNoteEventEqual(noteOnEvent, 35L, (byte) -112, (byte) 70, (byte) 80);
        assertNoteEventEqual(noteOffEvent, 39L, (byte) -128, (byte) 70, (byte) 0);
    }
    
    @Test
    public void noteEventRefreshOnFieldChange() throws Exception {
        System.out.println("noteEventRefreshOnFieldChange");
        MidiNote instance = new MidiNote();
        MidiEvent noteOnEvent = instance.getNoteOnEvent();
        MidiEvent noteOffEvent = instance.getNoteOffEvent();                
        
        assertNoteEventEqual(noteOnEvent, 0L, (byte) -112, (byte) 0, (byte) 64);
        assertNoteEventEqual(noteOffEvent, 0L, (byte) -128, (byte) 0, (byte) 0);
        
        instance.setPitch(92);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 0L, (byte) -112, (byte) 92, (byte) 64);
        assertNoteEventEqual(noteOffEvent, 0L, (byte) -128, (byte) 92, (byte) 0);
        
        instance.setStartTime(16L);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 16L, (byte) -112, (byte) 92, (byte) 64);
        assertNoteEventEqual(noteOffEvent, 16L, (byte) -128, (byte) 92, (byte) 0);
        
        instance.setDuration(8L);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 16L, (byte) -112, (byte) 92, (byte) 64);
        assertNoteEventEqual(noteOffEvent, 24L, (byte) -128, (byte) 92, (byte) 0);
        
        instance.setVelocity(120);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 16L, (byte) -112, (byte) 92, (byte) 120);
        assertNoteEventEqual(noteOffEvent, 24L, (byte) -128, (byte) 92, (byte) 0);                
        
        instance.setChannel(2);
        noteOnEvent = instance.getNoteOnEvent();
        noteOffEvent = instance.getNoteOffEvent();
        assertNoteEventEqual(noteOnEvent, 16L, (byte) -110, (byte) 92, (byte) 120);
        assertNoteEventEqual(noteOffEvent, 24L, (byte) -126, (byte) 92, (byte) 0);                
    }
    
    protected void assertNoteEventEqual(MidiEvent noteEvent, long tick, byte byte1, byte byte2, byte byte3) {
        assertEquals(tick, noteEvent.getTick());
        javax.sound.midi.MidiMessage msg = noteEvent.getMessage();
        assertEquals(byte1, msg.getMessage()[0]);        
        assertEquals(byte2, msg.getMessage()[1]);
        assertEquals(byte3, msg.getMessage()[2]);
    }
}