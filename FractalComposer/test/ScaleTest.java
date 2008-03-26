/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
    public void keyNameDefaultNumberCorrect() {
        assertEquals(NoteName.C, NoteName.getDefaultNoteNameForNumber(0));
        assertEquals(NoteName.C_SHARP, NoteName.getDefaultNoteNameForNumber(1));
        assertEquals(NoteName.D, NoteName.getDefaultNoteNameForNumber(2));
        assertEquals(NoteName.E_FLAT, NoteName.getDefaultNoteNameForNumber(3));
        assertEquals(NoteName.E, NoteName.getDefaultNoteNameForNumber(4));
        assertEquals(NoteName.F, NoteName.getDefaultNoteNameForNumber(5));
        assertEquals(NoteName.F_SHARP, NoteName.getDefaultNoteNameForNumber(6));
        assertEquals(NoteName.G, NoteName.getDefaultNoteNameForNumber(7));
        assertEquals(NoteName.A_FLAT, NoteName.getDefaultNoteNameForNumber(8));
        assertEquals(NoteName.A, NoteName.getDefaultNoteNameForNumber(9));
        assertEquals(NoteName.B_FLAT, NoteName.getDefaultNoteNameForNumber(10));
        assertEquals(NoteName.B, NoteName.getDefaultNoteNameForNumber(11));                
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getDefaultKeyNameForNumber_ThrowsException() {
        NoteName k = NoteName.getDefaultNoteNameForNumber(12);
    }   
    
    /**
     * Test of convertToMidiNote method, of class MajorScale.
     */
    @Test
    public void majorScale_convertToMidiNote() {
        System.out.println("convertToMidiNote");
        
        MajorScale scale = new MajorScale(NoteName.C);
        MidiNote result;
        Note note;
        
        note = new Note(1, 4, 0, 6d, 70);                
        
        result = scale.convertToMidiNote(note, 0d);
        assertMidiNoteValues(result, 60, 70, 0, 6d);                
        
        note.setScaleStep(6);
        note.setOctave(5);
        result = scale.convertToMidiNote(note, 5d);
        assertMidiNoteValues(result, 81, 70, 5d, 6d);                
        
        note.setScaleStep(3);
        note.setOctave(2);
        note.setVolume(40);
        note.setDuration(3d);
        result = scale.convertToMidiNote(note, 2d);
        assertMidiNoteValues(result, 40, 40, 2d, 3d);                
    }
    
    protected void assertMidiNoteValues(MidiNote midiNote, int pitch, int velocity, double startTime, double duration) {
        assertEquals(pitch, midiNote.getPitch());
        assertEquals(velocity, midiNote.getVelocity());
        assertEquals(startTime, midiNote.getStartTime());
        assertEquals(duration, midiNote.getDuration());        
    }

}