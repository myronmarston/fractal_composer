import com.myronmarston.music.settings.*;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;
import java.util.ConcurrentModificationException;
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
public class SettingsTest {

    public SettingsTest() {
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
    public void voiceSectionMapKey_hashCode_equals_Test() {
        FractalPiece fp = new FractalPiece();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        
        VoiceSectionHashMapKey vsmp1 = new VoiceSectionHashMapKey(v1, s1);
        VoiceSectionHashMapKey vsmp2 = new VoiceSectionHashMapKey(v1, s2);        
        assertFalse(vsmp1.equals(vsmp2));
        assertFalse(vsmp1.hashCode() == vsmp2.hashCode());
                
        VoiceSectionHashMapKey vsmp3 = new VoiceSectionHashMapKey(v1, s1);
        assertEquals(vsmp1, vsmp3);
        assertEquals(vsmp1.hashCode(), vsmp3.hashCode());                
    }     
    
    @Test
    public void creatingOrRemovingVoicesOrSettingsManagesVoiceSections() {
        FractalPiece fp = new FractalPiece();
        Voice v1 = fp.createVoice();
        assertEquals(0, v1.getVoiceSections().size());
        
        Section s1 = fp.createSection();
        assertEquals(1, v1.getVoiceSections().size());
        assertEquals(1, s1.getVoiceSections().size());
        VoiceSection vs1 = v1.getVoiceSections().get(0);
        assertNotNull(vs1);
        assertEquals(vs1, s1.getVoiceSections().get(0));
        
        Voice v2 = fp.createVoice();
        assertEquals(1, v1.getVoiceSections().size());
        assertEquals(1, v2.getVoiceSections().size());
        assertEquals(2, s1.getVoiceSections().size());
        VoiceSection vs2 = v2.getVoiceSections().get(0);
        assertNotNull(vs2);
        assertNotSame(vs2, vs1);
        assertEquals(vs2, s1.getVoiceSections().get(1));
        
        Voice v3 = fp.createVoice();
        assertEquals(1, v1.getVoiceSections().size());
        assertEquals(1, v2.getVoiceSections().size());
        assertEquals(1, v3.getVoiceSections().size());
        assertEquals(3, s1.getVoiceSections().size());
        VoiceSection vs3 = v3.getVoiceSections().get(0);
        assertNotNull(vs3);
        assertNotSame(vs3, vs1);
        assertNotSame(vs3, vs2);
        assertEquals(vs3, s1.getVoiceSections().get(2));
        
        Section s2 = fp.createSection();
        assertEquals(2, v1.getVoiceSections().size());
        assertEquals(2, v2.getVoiceSections().size());
        assertEquals(2, v3.getVoiceSections().size());
        assertEquals(3, s1.getVoiceSections().size());
        assertEquals(3, s2.getVoiceSections().size());
        VoiceSection vs4 = v1.getVoiceSections().get(1);
        VoiceSection vs5 = v2.getVoiceSections().get(1);
        VoiceSection vs6 = v3.getVoiceSections().get(1);
        assertNotNull(vs4);
        assertNotNull(vs5);
        assertNotNull(vs6);
        assertEquals(vs4, s2.getVoiceSections().get(0));
        assertEquals(vs5, s2.getVoiceSections().get(1));
        assertEquals(vs6, s2.getVoiceSections().get(2));   
        
        // now let's remove some Voices and Sections...
        fp.getVoices().remove(v1);        
        assertEquals(0, v1.getVoiceSections().size());
        assertEquals(2, s1.getVoiceSections().size());
        assertEquals(2, s2.getVoiceSections().size());
        assertEquals(vs2, s1.getVoiceSections().get(0));
        assertEquals(vs3, s1.getVoiceSections().get(1));
        assertEquals(vs5, s2.getVoiceSections().get(0));
        assertEquals(vs6, s2.getVoiceSections().get(1));
    }
    
    @Test(expected=ConcurrentModificationException.class)
    public void exceptionIfModifyVoiceSectionListWhileIterating() {
        FractalPiece fp = new FractalPiece();
        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        
        boolean sectionCreated = false;
        for (VoiceSection vs : v1.getVoiceSections()) {
            // modify the list be creating another section...
            if (!sectionCreated) fp.createSection();
            sectionCreated = true;
        }
    }
    
    @Test(expected=ConcurrentModificationException.class)
    public void exceptionIfModifyVoiceListWhileIterating() {
        FractalPiece fp = new FractalPiece();
        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        
        boolean voiceCreated = false;
        for (Voice v : fp.getVoices()) {
            // modify the list be creating another voice...
            if (!voiceCreated) fp.createVoice();
            voiceCreated = true;            
        }
    }
    
    @Test(expected=ConcurrentModificationException.class)
    public void exceptionIfModifySectionListWhileIterating() {
        FractalPiece fp = new FractalPiece();
        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        
        boolean sectionCreated = false;
        for (Section s : fp.getSections()) {
            // modify the list be creating another voice...
            if (!sectionCreated) fp.createSection();
            sectionCreated = true;            
        }
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void addVoiceSectionThrowsException() {
        FractalPiece fp = new FractalPiece();
        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        
        v2.getVoiceSections().add(v1.getVoiceSections().get(0));
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void removeVoiceSectionThrowsException() {
        FractalPiece fp = new FractalPiece();
        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        
        v2.getVoiceSections().remove(0);
    }
    
    @Test
    public void generateVoiceGerm() {
        FractalPiece fp = new FractalPiece();
        fp.getGerm().add(new Note(0, 4, 0, 1d, 96));
        fp.getGerm().add(new Note(1, 4, 0, 0.5d, 64));
        fp.getGerm().add(new Note(2, 4, 0, 0.5d, 64));
        fp.getGerm().add(new Note(0, 4, 0, 1d, 96));
        
        Voice v1 = fp.createVoice();
        v1.setOctaveAdjustment(2);
        v1.setSpeedScaleFactor(4);
        
        NoteList expected = new NoteList();
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        expected.add(new Note(1, 6, 0, 0.125d, 64));
        expected.add(new Note(2, 6, 0, 0.125d, 64));
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        assertNoteListsEqual(expected, v1.getModifiedGerm());
        
        v1.setSpeedScaleFactor(0.5d);
        expected.clear();
        expected.add(new Note(0, 6, 0, 2d, 96));
        expected.add(new Note(1, 6, 0, 1d, 64));
        expected.add(new Note(2, 6, 0, 1d, 64));
        expected.add(new Note(0, 6, 0, 2d, 96));
        assertNoteListsEqual(expected, v1.getModifiedGerm());
        
        v1.setOctaveAdjustment(-1);
        expected.clear();
        expected.add(new Note(0, 3, 0, 2d, 96));
        expected.add(new Note(1, 3, 0, 1d, 64));
        expected.add(new Note(2, 3, 0, 1d, 64));
        expected.add(new Note(0, 3, 0, 2d, 96));
        assertNoteListsEqual(expected, v1.getModifiedGerm());
    }
    
    @Test
    public void voiceSectionGetVoiceSectionResult() {
        FractalPiece fp = new FractalPiece();
        fp.getGerm().add(new Note(0, 4, 0, 1d, 96));
        fp.getGerm().add(new Note(1, 4, 0, 0.5d, 64));
        fp.getGerm().add(new Note(2, 4, 0, 0.5d, 64));
        fp.getGerm().add(new Note(0, 4, 0, 1d, 96));
        
        Voice v1 = fp.createVoice();
        v1.setOctaveAdjustment(2);
        v1.setSpeedScaleFactor(4);
        
        Section s1 = fp.createSection();
        VoiceSection vs1 = v1.getVoiceSections().get(0);
        
        NoteList expected = new NoteList();
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        expected.add(new Note(1, 6, 0, 0.125d, 64));
        expected.add(new Note(2, 6, 0, 0.125d, 64));
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        expected.add(new Note(1, 6, 0, 0.125d, 64));
        expected.add(new Note(2, 6, 0, 0.125d, 64));
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        expected.add(new Note(1, 6, 0, 0.125d, 64));
        expected.add(new Note(2, 6, 0, 0.125d, 64));
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        expected.add(new Note(1, 6, 0, 0.125d, 64));
        expected.add(new Note(2, 6, 0, 0.125d, 64));
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        assertNoteListsEqual(expected, vs1.getVoiceSectionResult());
                
        expected.clear();
        vs1.setApplyInversion(true);
        vs1.setApplyRetrograde(true);
        vs1.getSelfSimilaritySettings().setApplyToPitch(true);
        vs1.getSelfSimilaritySettings().setApplyToRhythm(true);
        vs1.getSelfSimilaritySettings().setApplyToVolume(true);
        
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        expected.add(new Note(-2, 6, 0, 0.125d, 64));
        expected.add(new Note(-1, 6, 0, 0.125d, 64));
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        
        expected.add(new Note(-2, 6, 0, 0.125d, 64));
        expected.add(new Note(-4, 6, 0, 0.0625d, 43));
        expected.add(new Note(-3, 6, 0, 0.0625d, 43));
        expected.add(new Note(-2, 6, 0, 0.125d, 64));
        
        expected.add(new Note(-1, 6, 0, 0.125d, 64));
        expected.add(new Note(-3, 6, 0, 0.0625d, 43));
        expected.add(new Note(-2, 6, 0, 0.0625d, 43));
        expected.add(new Note(-1, 6, 0, 0.125d, 64));
        
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        expected.add(new Note(-2, 6, 0, 0.125d, 64));
        expected.add(new Note(-1, 6, 0, 0.125d, 64));
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        assertNoteListsEqual(expected, vs1.getVoiceSectionResult());
        
        expected.clear();
        vs1.getSelfSimilaritySettings().setApplyToVolume(false);
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        expected.add(new Note(-2, 6, 0, 0.125d, 64));
        expected.add(new Note(-1, 6, 0, 0.125d, 64));
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        
        expected.add(new Note(-2, 6, 0, 0.125d, 96));
        expected.add(new Note(-4, 6, 0, 0.0625d, 64));
        expected.add(new Note(-3, 6, 0, 0.0625d, 64));
        expected.add(new Note(-2, 6, 0, 0.125d, 96));
        
        expected.add(new Note(-1, 6, 0, 0.125d, 96));
        expected.add(new Note(-3, 6, 0, 0.0625d, 64));
        expected.add(new Note(-2, 6, 0, 0.0625d, 64));
        expected.add(new Note(-1, 6, 0, 0.125d, 96));
        
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        expected.add(new Note(-2, 6, 0, 0.125d, 64));
        expected.add(new Note(-1, 6, 0, 0.125d, 64));
        expected.add(new Note(0, 6, 0, 0.25d, 96));
        assertNoteListsEqual(expected, vs1.getVoiceSectionResult());
        
        vs1.setRest(true);
        expected.clear();
        expected.add(new Note(0, 0, 0, 0.75d, 0));
        assertNoteListsEqual(expected, vs1.getVoiceSectionResult());
    }
    
    @Test
    public void getSectionDuration() {
        FractalPiece fp = new FractalPiece();
        fp.getGerm().add(new Note(0, 4, 0, 1d, 96));
        fp.getGerm().add(new Note(1, 4, 0, 0.5d, 64));
        fp.getGerm().add(new Note(2, 4, 0, 0.5d, 64));
        fp.getGerm().add(new Note(0, 4, 0, 1d, 96));
        
        Voice v1 = fp.createVoice();
        v1.setOctaveAdjustment(1);
        v1.setSpeedScaleFactor(2);
        
        Voice v2 = fp.createVoice();        
        
        Voice v3 = fp.createVoice();
        v3.setOctaveAdjustment(-1);
        v3.setSpeedScaleFactor(0.5d);        
        
        Section s1 = fp.createSection();
        s1.getVoiceSections().get(2).setRest(true);
        
        assertEquals(12d, s1.getDuration());
    }
    
    @Test(expected=IllegalArgumentException.class)    
    public void getLengthenedVoiceSectionResultErrorIfLengthTooShort() {
        FractalPiece fp = new FractalPiece();
        fp.getGerm().add(new Note(0, 4, 0, 1d, 96));
        fp.getGerm().add(new Note(1, 4, 0, 0.5d, 64));
        fp.getGerm().add(new Note(2, 4, 0, 0.5d, 64));
        fp.getGerm().add(new Note(0, 4, 0, 1d, 96));
        
        Voice v1 = fp.createVoice();                
        Section s1 = fp.createSection();
        VoiceSection vs1 = v1.getVoiceSections().get(0);
        
        vs1.getLengthenedVoiceSectionResult(11d);
    }
    
    @Test
    public void getLengthenedVoiceSectionResult() {
        FractalPiece fp = new FractalPiece();
        fp.getGerm().add(new Note(0, 4, 0, 1d, 96));
        fp.getGerm().add(new Note(1, 4, 0, 0.5d, 64));
        fp.getGerm().add(new Note(2, 4, 0, 0.5d, 64));
        fp.getGerm().add(new Note(0, 4, 0, 1d, 96));
        
        Voice v1 = fp.createVoice();                
        Section s1 = fp.createSection();
        VoiceSection vs1 = v1.getVoiceSections().get(0);
                
        assertEquals(12d, vs1.getVoiceSectionResult().getDuration());
        
        NoteList expected = new NoteList();
        expected.addAll(vs1.getVoiceSectionResult());
        assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(12d));
        
        expected.add(Note.createRest(3.5d));
        assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(15.5d));
        
        expected.clear();
        expected.addAll(vs1.getVoiceSectionResult());
        expected.addAll(vs1.getVoiceSectionResult());
        assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(24d));
        
        expected.add(Note.createRest(2d));
        assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(26d));
        
        expected.clear();
        expected.addAll(vs1.getVoiceSectionResult());
        expected.addAll(vs1.getVoiceSectionResult());
        expected.addAll(vs1.getVoiceSectionResult());
        expected.addAll(vs1.getVoiceSectionResult());
        expected.add(Note.createRest(0.5d));
        assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(48.5d));        
    }
    
    @Test
    public void getEntireVoiceResult() {
        FractalPiece fp = new FractalPiece();
        fp.getGerm().add(new Note(0, 4, 0, 1d, 96));
        fp.getGerm().add(new Note(1, 4, 0, 0.5d, 64));
        fp.getGerm().add(new Note(2, 4, 0, 0.5d, 64));
        fp.getGerm().add(new Note(0, 4, 0, 1d, 96));
        
        Voice v1 = fp.createVoice();                
        Voice v2 = fp.createVoice();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        
        v1.setOctaveAdjustment(1);
        v1.setSpeedScaleFactor(2);
        
        v1.getVoiceSections().get(0).getSelfSimilaritySettings().setApplyToPitch(true);
        v1.getVoiceSections().get(0).getSelfSimilaritySettings().setApplyToRhythm(true);
        v1.getVoiceSections().get(1).setApplyInversion(true);
        v1.getVoiceSections().get(1).getSelfSimilaritySettings().setApplyToPitch(true);
        v1.getVoiceSections().get(1).getSelfSimilaritySettings().setApplyToRhythm(true);
        
        v2.getVoiceSections().get(0).getSelfSimilaritySettings().setApplyToPitch(true);
        v2.getVoiceSections().get(0).getSelfSimilaritySettings().setApplyToRhythm(true);
        v2.getVoiceSections().get(1).setApplyInversion(true);
        v2.getVoiceSections().get(1).getSelfSimilaritySettings().setApplyToPitch(true);
        v2.getVoiceSections().get(1).getSelfSimilaritySettings().setApplyToRhythm(true);
        
        NoteList expected = new NoteList();
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        expected.add(new Note(1, 5, 0, 0.25d, 64));
        expected.add(new Note(2, 5, 0, 0.25d, 64));
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        
        expected.add(new Note(1, 5, 0, 0.25d, 96));
        expected.add(new Note(2, 5, 0, 0.125d, 64));
        expected.add(new Note(3, 5, 0, 0.125d, 64));
        expected.add(new Note(1, 5, 0, 0.25d, 96));
        
        expected.add(new Note(2, 5, 0, 0.25d, 96));
        expected.add(new Note(3, 5, 0, 0.125d, 64));
        expected.add(new Note(4, 5, 0, 0.125d, 64));
        expected.add(new Note(2, 5, 0, 0.25d, 96));
        
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        expected.add(new Note(1, 5, 0, 0.25d, 64));
        expected.add(new Note(2, 5, 0, 0.25d, 64));
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        
        // repeat that whole thing again since this voice is twice as fast
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        expected.add(new Note(1, 5, 0, 0.25d, 64));
        expected.add(new Note(2, 5, 0, 0.25d, 64));
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        
        expected.add(new Note(1, 5, 0, 0.25d, 96));
        expected.add(new Note(2, 5, 0, 0.125d, 64));
        expected.add(new Note(3, 5, 0, 0.125d, 64));
        expected.add(new Note(1, 5, 0, 0.25d, 96));
        
        expected.add(new Note(2, 5, 0, 0.25d, 96));
        expected.add(new Note(3, 5, 0, 0.125d, 64));
        expected.add(new Note(4, 5, 0, 0.125d, 64));
        expected.add(new Note(2, 5, 0, 0.25d, 96));
        
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        expected.add(new Note(1, 5, 0, 0.25d, 64));
        expected.add(new Note(2, 5, 0, 0.25d, 64));
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        
        // now do the inversion...
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        expected.add(new Note(-1, 5, 0, 0.25d, 64));
        expected.add(new Note(-2, 5, 0, 0.25d, 64));
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        
        expected.add(new Note(-1, 5, 0, 0.25d, 96));
        expected.add(new Note(-2, 5, 0, 0.125d, 64));
        expected.add(new Note(-3, 5, 0, 0.125d, 64));
        expected.add(new Note(-1, 5, 0, 0.25d, 96));
        
        expected.add(new Note(-2, 5, 0, 0.25d, 96));
        expected.add(new Note(-3, 5, 0, 0.125d, 64));
        expected.add(new Note(-4, 5, 0, 0.125d, 64));
        expected.add(new Note(-2, 5, 0, 0.25d, 96));
        
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        expected.add(new Note(-1, 5, 0, 0.25d, 64));
        expected.add(new Note(-2, 5, 0, 0.25d, 64));
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        
        // and repeat the inversion once more...
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        expected.add(new Note(-1, 5, 0, 0.25d, 64));
        expected.add(new Note(-2, 5, 0, 0.25d, 64));
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        
        expected.add(new Note(-1, 5, 0, 0.25d, 96));
        expected.add(new Note(-2, 5, 0, 0.125d, 64));
        expected.add(new Note(-3, 5, 0, 0.125d, 64));
        expected.add(new Note(-1, 5, 0, 0.25d, 96));
        
        expected.add(new Note(-2, 5, 0, 0.25d, 96));
        expected.add(new Note(-3, 5, 0, 0.125d, 64));
        expected.add(new Note(-4, 5, 0, 0.125d, 64));
        expected.add(new Note(-2, 5, 0, 0.25d, 96));
        
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        expected.add(new Note(-1, 5, 0, 0.25d, 64));
        expected.add(new Note(-2, 5, 0, 0.25d, 64));
        expected.add(new Note(0, 5, 0, 0.5d, 96));
        
        assertNoteListsEqual(expected, v1.getEntireVoice());
    }
    
    static protected void assertNoteListsEqual(NoteList expected, NoteList actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {            
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}