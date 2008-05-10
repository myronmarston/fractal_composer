package com.myronmarston.music.settings;

import com.myronmarston.util.Fraction;

import com.myronmarston.music.*;
import com.myronmarston.music.scales.*;
import javax.sound.midi.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class FractalPieceTest {
    
    @Test
    public void createDefaultVoices() {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultVoices();
        
        assertEquals(fp.getVoices().get(0).getOctaveAdjustment(), 1);
        assertEquals(fp.getVoices().get(1).getOctaveAdjustment(), 0);
        assertEquals(fp.getVoices().get(2).getOctaveAdjustment(), -1);
        
        assertEquals(fp.getVoices().get(0).getSpeedScaleFactor(), new Fraction(2, 1));
        assertEquals(fp.getVoices().get(1).getSpeedScaleFactor(), new Fraction(1, 1));
        assertEquals(fp.getVoices().get(2).getSpeedScaleFactor(), new Fraction(1, 2));
        
        Voice v = fp.createVoice(1);
        
        // calling the method again should leave the existing voices alone.
        fp.createDefaultVoices();
        
        assertEquals(fp.getVoices().get(0).getOctaveAdjustment(), 1);
        assertEquals(fp.getVoices().get(1).getOctaveAdjustment(), 0);
        assertEquals(fp.getVoices().get(2).getOctaveAdjustment(), 0);
        assertEquals(fp.getVoices().get(3).getOctaveAdjustment(), -1);
        
        assertEquals(fp.getVoices().get(0).getSpeedScaleFactor(), new Fraction(2, 1));
        assertEquals(fp.getVoices().get(1).getSpeedScaleFactor(), new Fraction(1, 1));
        assertEquals(fp.getVoices().get(2).getSpeedScaleFactor(), new Fraction(1, 1));
        assertEquals(fp.getVoices().get(3).getSpeedScaleFactor(), new Fraction(1, 2));        
    }
    
    @Test
    public void createDefaultSections() {
        FractalPiece fp = new FractalPiece();
        
        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        Voice v3 = fp.createVoice();
        
        v1.setSpeedScaleFactor(new Fraction(2, 1));
        v3.setSpeedScaleFactor(new Fraction(1, 2));
        
        fp.createDefaultSections();
        assertEquals(4, fp.getSections().size());
                
        assertVoiceSectionEqual(v1.getVoiceSections().get(0), null, false, null, false, false, true, true, true);
        assertVoiceSectionEqual(v2.getVoiceSections().get(0), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(0), null, false, null, false, false, false, false, false);
        
        assertVoiceSectionEqual(v1.getVoiceSections().get(1), null, true, null, false, false, true, true, true);
        assertVoiceSectionEqual(v2.getVoiceSections().get(1), null, true, null, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(1), null, true, null, false, false, false, false, false);
        
        assertVoiceSectionEqual(v1.getVoiceSections().get(2), null, true, null, true, false, true, true, true);
        assertVoiceSectionEqual(v2.getVoiceSections().get(2), null, true, null, true, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(2), null, true, null, true, false, false, false, false);
        
        assertVoiceSectionEqual(v1.getVoiceSections().get(3), null, false, null, true, false, true, true, true);
        assertVoiceSectionEqual(v2.getVoiceSections().get(3), null, false, null, true, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(3), null, false, null, true, false, false, false, false);               
        
        // add another section, so that we can test that it gets stomped when we call createDefaultSections()
        fp.createSection();
        
        // modify the speed scale factor so that a different voice gets the self-similarity...
        v2.setSpeedScaleFactor(new Fraction(4, 1));
        fp.createDefaultSections();
        assertEquals(4, fp.getSections().size());
                
        assertVoiceSectionEqual(v1.getVoiceSections().get(0), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(0), null, false, null, false, false, true, true, true);
        assertVoiceSectionEqual(v3.getVoiceSections().get(0), null, false, null, false, false, false, false, false);
        
        assertVoiceSectionEqual(v1.getVoiceSections().get(1), null, true, null, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(1), null, true, null, false, false, true, true, true);
        assertVoiceSectionEqual(v3.getVoiceSections().get(1), null, true, null, false, false, false, false, false);
        
        assertVoiceSectionEqual(v1.getVoiceSections().get(2), null, true, null, true, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(2), null, true, null, true, false, true, true, true);
        assertVoiceSectionEqual(v3.getVoiceSections().get(2), null, true, null, true, false, false, false, false);
        
        assertVoiceSectionEqual(v1.getVoiceSections().get(3), null, false, null, true, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(3), null, false, null, true, false, true, true, true);
        assertVoiceSectionEqual(v3.getVoiceSections().get(3), null, false, null, true, false, false, false, false);        
    }
            
    @Test
    public void introAndOutroTest() throws InvalidMidiDataException, InvalidKeySignatureException {
        FractalPiece fp = new FractalPiece();

        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        Voice v3 = fp.createVoice();
        
        v1.setSpeedScaleFactor(new Fraction(2, 1));
        v3.setSpeedScaleFactor(new Fraction(1, 2));
        
        fp.createIntroSections();
        assertVoiceSectionEqual(v1.getVoiceSections().get(0), null, false, null, false, true, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(1), null, false, null, false, true, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(2), null, false, null, false, false, false, false, false);
        
        assertVoiceSectionEqual(v2.getVoiceSections().get(0), null, false, null, false, true, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(1), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(2), null, false, null, false, false, false, false, false);
        
        assertVoiceSectionEqual(v3.getVoiceSections().get(0), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(1), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(2), null, false, null, false, false, false, false, false);
        fp.clearTempIntroOutroSections();
        assertEquals(0, fp.getSections().size());
        
        // change the speed factors; this should change which voices gets rests when...
        v2.setSpeedScaleFactor(new Fraction(1, 4));
        
        fp.createIntroSections();
        assertVoiceSectionEqual(v1.getVoiceSections().get(0), null, false, null, false, true, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(1), null, false, null, false, true, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(2), null, false, null, false, false, false, false, false);
        
        assertVoiceSectionEqual(v2.getVoiceSections().get(0), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(1), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(2), null, false, null, false, false, false, false, false);
        
        assertVoiceSectionEqual(v3.getVoiceSections().get(0), null, false, null, false, true, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(1), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(2), null, false, null, false, false, false, false, false);
        fp.clearTempIntroOutroSections();
        
        fp.createOutroSections();
        assertVoiceSectionEqual(v1.getVoiceSections().get(0), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(1), null, false, null, false, true, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(2), null, false, null, false, true, false, false, false);
        
        assertVoiceSectionEqual(v2.getVoiceSections().get(0), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(1), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(2), null, false, null, false, false, false, false, false);
        
        assertVoiceSectionEqual(v3.getVoiceSections().get(0), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(1), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(2), null, false, null, false, true, false, false, false);
        fp.clearTempIntroOutroSections();    
        
        // change the speed scale factor of a voice; this should change where the rests go...
        v1.setSpeedScaleFactor(new Fraction(1, 8));
        
        fp.createOutroSections();
        assertVoiceSectionEqual(v1.getVoiceSections().get(0), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(1), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(2), null, false, null, false, false, false, false, false);
        
        assertVoiceSectionEqual(v2.getVoiceSections().get(0), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(1), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(2), null, false, null, false, true, false, false, false);
        
        assertVoiceSectionEqual(v3.getVoiceSections().get(0), null, false, null, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(1), null, false, null, false, true, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(2), null, false, null, false, true, false, false, false);
        fp.clearTempIntroOutroSections();  
    }
    
    
    
    @Test
    public void setGerm() throws NoteStringParseException, InvalidKeySignatureException {
        FractalPiece fp = new FractalPiece();
        fp.setScale(new MajorScale(NoteName.G));
        
        NoteList expected = new NoteList();
        expected.add(new Note(0, 4, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume()));
        expected.add(new Note(1, 4, 0, new Fraction(1, 8), Dynamic.F.getMidiVolume()));
        expected.add(new Note(2, 4, 0, new Fraction(1, 8), Dynamic.F.getMidiVolume()));
        expected.add(new Note(0, 4, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume()));
        
        fp.setGermString("G4,1/4,MF A4,1/8,F B4,1/8,F G4,1/4,MF");
        NoteListTest.assertNoteListsEqual(expected, fp.getGerm());
    }
    
    @Test
    public void createAndSaveMidiFile() throws IOException, InvalidKeySignatureException, NoteStringParseException, InvalidMidiDataException {     
        String tempFileName = getTempFileName();        
                
        FractalPiece fp = new FractalPiece();
        fp.setScale(new MajorScale(NoteName.G));
        fp.setGermString("G4,1/4,MF A4,1/8,F B4,1/8,F G4,1/4,MF");  
        fp.createDefaultSettings();
        fp.createAndSaveMidiFile(tempFileName);
        
        // this will throw an exception if the midi file was not saved...
        Sequence seq = MidiSystem.getSequence(new File(tempFileName));        
    }
    
    @Test
    public void saveGermToMidiFile() throws IOException, InvalidKeySignatureException, NoteStringParseException, InvalidMidiDataException {
        String tempFileName = getTempFileName();
        
        FractalPiece fp = new FractalPiece();        
        fp.setGermString("C4,MF D4,Mf");          
        fp.saveGermToMidiFile(tempFileName);
        
        // this will throw an exception if the midi file was not saved...
        Sequence seq = MidiSystem.getSequence(new File(tempFileName));    
        Track t2 = seq.getTracks()[1];
        assertEquals(6, t2.size()); // instrument event, 2 note on events, 2 not off events, end-of-track event
        // we could test each event, but that's overkill.
        // all our other tests test the various parts of this, so there's no 
        // need to duplicate that here
    }
    
    @Test(expected=IllegalArgumentException.class) 
    public void setScaleToNull() {
        FractalPiece fp = new FractalPiece();
        fp.setScale(null);        
    }
    
    @Test(expected=IllegalArgumentException.class) 
    public void setTimeSignatureToNull() {
        FractalPiece fp = new FractalPiece();
        fp.setTimeSignature(null);
    }
    
    @Test
    public void setScaleReparsesGerm() throws InvalidKeySignatureException, NoteStringParseException {
        FractalPiece fp = new FractalPiece();
        fp.setScale(new MajorScale(NoteName.C));
        fp.setGermString("C4 E4 F4 G4");
        
        NoteList expected = new NoteList();
        expected.add(new Note(0, 4, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume()));
        expected.add(new Note(2, 4, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume()));
        expected.add(new Note(3, 4, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume()));
        expected.add(new Note(4, 4, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume()));
        
        NoteListTest.assertNoteListsEqual(expected, fp.getGerm());
        
        fp.setScale(new MajorScale(NoteName.G));
        expected.clear();        
        expected.add(new Note(3, 3, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume()));
        expected.add(new Note(5, 3, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume()));
        expected.add(new Note(6, 3, -1, new Fraction(1, 4), Dynamic.MF.getMidiVolume()));
        expected.add(new Note(0, 4, 0, new Fraction(1, 4), Dynamic.MF.getMidiVolume()));
        
        NoteListTest.assertNoteListsEqual(expected, fp.getGerm());
    }
    
    @Test
    public void createDefaultSettingsAfterDeserialization() throws Exception {
        FractalPiece fp = new FractalPiece();        
        fp.setGermString("C4 E4 F4 G4");
        fp = FractalPiece.loadFromXml(fp.getXmlRepresentation());
        
        // at one point we were getting null pointer exceptions when calling createDefaultSettings()
        // after deserialization, because we didn't setup the section list and voice list
        // correctly.  This tests that it is now correct...
        fp.createDefaultSettings();
    }
    
    @Test
    public void germMidiSameForAllScales() throws Exception {
        FractalPiece fp = new FractalPiece();        
        fp.setGermString("C4 E4 F4 G4");
        String tempFileName = getTempFileName();
        fp.saveGermToMidiFile(tempFileName);
                
        Track baselineTrack = MidiSystem.getSequence(new File(tempFileName)).getTracks()[1];
        
        for (Scale s : getAllScalePossibilities()) {
            System.out.println("Testing " + s.toString());
            fp.setScale(s);
            
            tempFileName = getTempFileName();
            fp.saveGermToMidiFile(tempFileName);
            
            Track t = MidiSystem.getSequence(new File(tempFileName)).getTracks()[1];            
            assertTracksEqual(baselineTrack, t);
        }
    }
    
    static protected List<Scale> getAllScalePossibilities() throws IllegalAccessException, IllegalArgumentException, InstantiationException, NoSuchMethodException {
        Scale s;
        List<Scale> list = new ArrayList<Scale>();
        for (Class c : Scale.SCALE_TYPES.keySet()) {              
            // originally we used getConstructor(NoteName.class) but that seems
            // to only get public constructors.  Our chromatic scale has this
            // constructor declared private, so we have to iterate over
            // getDeclaredConstructors (which includes private ones) and pick
            // out the right one.
            @SuppressWarnings("unchecked")
            Constructor con = c.getConstructor(NoteName.class);
            for (NoteName nn : NoteName.values()) { 
                try {
                    s = (Scale) con.newInstance(nn);                       
                    if (!list.contains(s)) list.add(s);        
                } catch (InvocationTargetException ex) {
                    // we expect some of these exceptions,
                    // such as for when an invalid key signature is created
                    // in this case, we just simply ignore that scale instance.
                }
            }                                                   
        }
        
        return list;
    }
    
    static protected void assertTracksEqual(Track t1, Track t2) {
        assertEquals(t1.size(), t2.size());
        assertEquals(t2.ticks(), t2.ticks());
        for (int i = 0; i < t1.size(); i++) {
            System.out.println("    Testing midi event " + i);
            MidiEvent me1 = t1.get(i);
            MidiEvent me2 = t2.get(i);
            
            assertEquals(me1.getTick(), me2.getTick());
            assertEquals(me1.getMessage().getClass(), me2.getMessage().getClass());
            assertEquals(me1.getMessage().getStatus(), me2.getMessage().getStatus());
            assertEquals(me1.getMessage().getLength(), me2.getMessage().getLength());
            for (int j = 0; j < me1.getMessage().getLength(); j++) {
                System.out.println("        Testing message byte " + j);
                assertEquals(me1.getMessage().getMessage()[j], me2.getMessage().getMessage()[j]);            
            }            
        }
    }        
    
    static protected String getTempFileName() throws IOException {
        File temp = File.createTempFile("TempMidiFile", ".mid");
        temp.deleteOnExit();
        return temp.getCanonicalPath();         
    }
        
    static protected void assertVoiceSectionEqual(VoiceSection vs, Boolean applyInversion, boolean guarenteedApplyInversion, Boolean applyRetrograde, boolean guarenteedApplyRetrograde, boolean isRest, boolean applySelfSimilarityToPitch, boolean applySelfSimilarityToRhythm, boolean applySelfSimilarityToVolume) {
        assertEquals(applyInversion, vs.getApplyInversion());
        assertEquals(applyRetrograde, vs.getApplyRetrograde());
        assertEquals(guarenteedApplyInversion, vs.getGuarenteedApplyInversion());
        assertEquals(guarenteedApplyRetrograde, vs.getGuarenteedApplyRetrograde());
        assertEquals(isRest, vs.getRest());
        assertEquals(applySelfSimilarityToPitch, vs.getSelfSimilaritySettings().getApplyToPitch());
        assertEquals(applySelfSimilarityToRhythm, vs.getSelfSimilaritySettings().getApplyToRhythm());
        assertEquals(applySelfSimilarityToVolume, vs.getSelfSimilaritySettings().getApplyToVolume());
    }
}