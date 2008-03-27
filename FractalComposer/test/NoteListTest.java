import com.myronmarston.music.MidiNote;
import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;
import com.myronmarston.music.NoteName;
import com.myronmarston.music.scales.MajorScale;
import com.myronmarston.music.scales.Scale;
        
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

    @Test(expected=UnsupportedOperationException.class)
    public void errorIfChangeRest() {        
        Note n = Note.createRest(5d);
        n.setScaleStep(3);
    }
    
    /**
     * Test of fillMidiTrack method, of class NoteList.
     */
    @Test
    public void fillMidiTrack() throws Exception {
        System.out.println("fillMidiTrack");
        NoteList germ = new NoteList();        
        germ.add(new Note(1, 4, 0, 0.5d, 100));
        germ.add(new Note(2, 4, 0, 0.5d, 64));
        germ.add(new Note(5, 4, 0, 0.5d, 64));
        germ.add(new Note(1, 4, 0, 0.5d, 64));
        germ.add(new Note(2, 4, 0, 0.5d, 100));
        germ.add(new Note(4, 4, 0, 0.5d, 64));
        
        Scale scale = new MajorScale(NoteName.F);                                
        Sequence sequence = new Sequence(Sequence.PPQ, MidiNote.TICKS_PER_QUARTER_NOTE);
        Track track = sequence.createTrack();        
        
        germ.fillMidiTrack(track, scale, 0d);
        MidiNoteTest.assertNoteEventEqual(track.get(0), 0d, (byte) -112, (byte) 65, (byte) 100);
        MidiNoteTest.assertNoteEventEqual(track.get(1), 0.5d, (byte) -128, (byte) 65, (byte) 0);
        
        MidiNoteTest.assertNoteEventEqual(track.get(2), 0.5d, (byte) -112, (byte) 67, (byte) 64);
        MidiNoteTest.assertNoteEventEqual(track.get(3), 1d, (byte) -128, (byte) 67, (byte) 0);
        
        MidiNoteTest.assertNoteEventEqual(track.get(4), 1d, (byte) -112, (byte) 72, (byte) 64);
        MidiNoteTest.assertNoteEventEqual(track.get(5), 1.5d, (byte) -128, (byte) 72, (byte) 0);
        
        MidiNoteTest.assertNoteEventEqual(track.get(6), 1.5d, (byte) -112, (byte) 65, (byte) 64);
        MidiNoteTest.assertNoteEventEqual(track.get(7), 2d, (byte) -128, (byte) 65, (byte) 0);
        
        MidiNoteTest.assertNoteEventEqual(track.get(8), 2d, (byte) -112, (byte) 67, (byte) 100);
        MidiNoteTest.assertNoteEventEqual(track.get(9), 2.5d, (byte) -128, (byte) 67, (byte) 0);
        
        MidiNoteTest.assertNoteEventEqual(track.get(10), 2.5d, (byte) -112, (byte) 70, (byte) 64);
        MidiNoteTest.assertNoteEventEqual(track.get(11), 3d, (byte) -128, (byte) 70, (byte) 0);                
    }

}