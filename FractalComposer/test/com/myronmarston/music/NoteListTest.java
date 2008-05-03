package com.myronmarston.music;

import com.myronmarston.music.scales.InvalidKeySignatureException;
import com.myronmarston.music.scales.MajorScale;
import com.myronmarston.music.scales.Scale;

import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;
        
import javax.sound.midi.Track;
import javax.sound.midi.Sequence;
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
public class NoteListTest {

    public NoteListTest() {
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
    public void getFirstAudibleNote() {
        Note soundedNote = new Note(2, 2, 0, new Fraction(1, 1), 64);
        NoteList notes = new NoteList();
        notes.add(Note.createRest(new Fraction(2, 1))); // a rest
        notes.add(soundedNote);
        assertEquals(soundedNote, notes.getFirstAudibleNote());
    }        
    
    @Test
    public void getDuration() {
        NoteList germ = new NoteList();
        
        germ.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        germ.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        germ.add(new Note(2, 4, 0, new Fraction(1, 2), 64));
        germ.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        assertEquals(new Fraction(3, 1), germ.getDuration());
    }
    
    @Test
    public void createAndFillMidiTrack() throws Exception {
        System.out.println("fillMidiTrack");
        NoteList germ = new NoteList();        
        germ.add(new Note(0, 4, 0, new Fraction(1, 8), 100));
        germ.add(new Note(1, 4, 0, new Fraction(1, 8), 64));
        germ.add(new Note(4, 4, 0, new Fraction(1, 8), 64));
        germ.add(new Note(0, 4, 0, new Fraction(1, 8), 64));
        germ.add(new Note(1, 4, 0, new Fraction(1, 8), 100));
        germ.add(new Note(3, 4, 0, new Fraction(1, 8), 64));
        
        Scale scale = new MajorScale(NoteName.F);                                
        Sequence sequence = new Sequence(Sequence.PPQ, 8);
                 
        Track track = germ.createAndFillMidiTrack(sequence, scale, new Fraction(0, 1));
        MidiNoteTest.assertNoteEventEqual(track.get(0), 0, (byte) -112, (byte) 65, (byte) 100);
        MidiNoteTest.assertNoteEventEqual(track.get(1), 4, (byte) -128, (byte) 65, (byte) 0);
        
        MidiNoteTest.assertNoteEventEqual(track.get(2), 4, (byte) -112, (byte) 67, (byte) 64);
        MidiNoteTest.assertNoteEventEqual(track.get(3), 8, (byte) -128, (byte) 67, (byte) 0);
        
        MidiNoteTest.assertNoteEventEqual(track.get(4), 8, (byte) -112, (byte) 72, (byte) 64);
        MidiNoteTest.assertNoteEventEqual(track.get(5), 12, (byte) -128, (byte) 72, (byte) 0);
        
        MidiNoteTest.assertNoteEventEqual(track.get(6), 12, (byte) -112, (byte) 65, (byte) 64);
        MidiNoteTest.assertNoteEventEqual(track.get(7), 16, (byte) -128, (byte) 65, (byte) 0);
        
        MidiNoteTest.assertNoteEventEqual(track.get(8), 16, (byte) -112, (byte) 67, (byte) 100);
        MidiNoteTest.assertNoteEventEqual(track.get(9), 20, (byte) -128, (byte) 67, (byte) 0);
        
        MidiNoteTest.assertNoteEventEqual(track.get(10), 20, (byte) -112, (byte) 70, (byte) 64);
        MidiNoteTest.assertNoteEventEqual(track.get(11), 24, (byte) -128, (byte) 70, (byte) 0);                
    }

    @Test
    public void parseNoteListString() throws InvalidKeySignatureException, NoteStringParseException { 
        NoteList germ = new NoteList();
        germ.parseNoteListString("D4,1/4,MF E4,1/8 F#4,F D4,1/4", new MajorScale(NoteName.D));
        
        NoteList expected = new NoteList();
        expected.add(new Note(0, 4, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume()));
        expected.add(new Note(1, 4, 0, new Fraction(1, 8), Dynamic.MF.getMidiVolume()));
        expected.add(new Note(2, 4, 0, new Fraction(1, 8), Dynamic.F.getMidiVolume()));
        expected.add(new Note(0, 4, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume()));
        
        assertNoteListsEqual(expected, germ);        
    }
    
    public static void assertNoteListsEqual(NoteList expected, NoteList actual) {        
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }                                
    }
}