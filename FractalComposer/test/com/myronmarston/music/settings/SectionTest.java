package com.myronmarston.music.settings;

import com.myronmarston.music.*;
import com.myronmarston.util.Fraction;  
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class SectionTest {
        
    @Test
    public void getSectionDuration() {
        FractalPiece fp = new FractalPiece();
        fp.getGerm().add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        fp.getGerm().add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        fp.getGerm().add(new Note(2, 4, 0, new Fraction(1, 2), 64));
        fp.getGerm().add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        Voice v1 = fp.createVoice();
        v1.setOctaveAdjustment(1);
        v1.setSpeedScaleFactor(new Fraction(2, 1));
        
        Voice v2 = fp.createVoice();        
        
        Voice v3 = fp.createVoice();
        v3.setOctaveAdjustment(-1);
        v3.setSpeedScaleFactor(new Fraction(1, 2));        
        
        Section s1 = fp.createSection();
        s1.getVoiceSections().get(2).setRest(true);
        
        assertEquals(new Fraction(6, 1), s1.getDuration());
    }    

    @Test
    public void setSelfSimilaritySettingsOnAllVoices() {
        FractalPiece fp = new FractalPiece();
        Voice v1 = fp.createVoice();                
        Voice v2 = fp.createVoice();
        Voice v3 = fp.createVoice();
        Section s1 = fp.createSection();
        VoiceSection vs1 = v1.getVoiceSections().get(0);
        VoiceSection vs2 = v2.getVoiceSections().get(0);
        VoiceSection vs3 = v3.getVoiceSections().get(0);
        
        s1.setApplySelfSimilarityToPitchOnAllVoiceSections(true);
        assertEquals(true, vs1.getSelfSimilaritySettings().getApplyToPitch());
        assertEquals(true, vs2.getSelfSimilaritySettings().getApplyToPitch());
        assertEquals(true, vs3.getSelfSimilaritySettings().getApplyToPitch());
        
        s1.setApplySelfSimilarityToPitchOnAllVoiceSections(false);
        assertEquals(false, vs1.getSelfSimilaritySettings().getApplyToPitch());
        assertEquals(false, vs2.getSelfSimilaritySettings().getApplyToPitch());
        assertEquals(false, vs3.getSelfSimilaritySettings().getApplyToPitch());
        
        s1.setApplySelfSimilarityToPitchOnAllVoiceSections(true);
        assertEquals(true, vs1.getSelfSimilaritySettings().getApplyToPitch());
        assertEquals(true, vs2.getSelfSimilaritySettings().getApplyToPitch());
        assertEquals(true, vs3.getSelfSimilaritySettings().getApplyToPitch());
        
        s1.setApplySelfSimilarityToRhythmOnAllVoiceSections(true);
        assertEquals(true, vs1.getSelfSimilaritySettings().getApplyToRhythm());
        assertEquals(true, vs2.getSelfSimilaritySettings().getApplyToRhythm());
        assertEquals(true, vs3.getSelfSimilaritySettings().getApplyToRhythm());
        
        s1.setApplySelfSimilarityToRhythmOnAllVoiceSections(false);
        assertEquals(false, vs1.getSelfSimilaritySettings().getApplyToRhythm());
        assertEquals(false, vs2.getSelfSimilaritySettings().getApplyToRhythm());
        assertEquals(false, vs3.getSelfSimilaritySettings().getApplyToRhythm());
        
        s1.setApplySelfSimilarityToRhythmOnAllVoiceSections(true);
        assertEquals(true, vs1.getSelfSimilaritySettings().getApplyToRhythm());
        assertEquals(true, vs2.getSelfSimilaritySettings().getApplyToRhythm());
        assertEquals(true, vs3.getSelfSimilaritySettings().getApplyToRhythm());
        
        s1.setApplySelfSimilarityToVolumeOnAllVoiceSections(true);
        assertEquals(true, vs1.getSelfSimilaritySettings().getApplyToVolume());
        assertEquals(true, vs2.getSelfSimilaritySettings().getApplyToVolume());
        assertEquals(true, vs3.getSelfSimilaritySettings().getApplyToVolume());
        
        s1.setApplySelfSimilarityToVolumeOnAllVoiceSections(false);
        assertEquals(false, vs1.getSelfSimilaritySettings().getApplyToVolume());
        assertEquals(false, vs2.getSelfSimilaritySettings().getApplyToVolume());
        assertEquals(false, vs3.getSelfSimilaritySettings().getApplyToVolume());
        
        s1.setApplySelfSimilarityToVolumeOnAllVoiceSections(true);
        assertEquals(true, vs1.getSelfSimilaritySettings().getApplyToVolume());
        assertEquals(true, vs2.getSelfSimilaritySettings().getApplyToVolume());
        assertEquals(true, vs3.getSelfSimilaritySettings().getApplyToVolume());
        
        vs1.getSelfSimilaritySettings().setApplyToPitch(true);
        vs2.getSelfSimilaritySettings().setApplyToPitch(false);
        vs3.getSelfSimilaritySettings().setApplyToPitch(true);
        
        s1.setSelfSimilaritySettingsOnAllVoiceSections(null, false, true);
        assertEquals(true, vs1.getSelfSimilaritySettings().getApplyToPitch());
        assertEquals(false, vs2.getSelfSimilaritySettings().getApplyToPitch());
        assertEquals(true, vs3.getSelfSimilaritySettings().getApplyToPitch());
        
        assertEquals(false, vs1.getSelfSimilaritySettings().getApplyToRhythm());
        assertEquals(false, vs2.getSelfSimilaritySettings().getApplyToRhythm());
        assertEquals(false, vs3.getSelfSimilaritySettings().getApplyToRhythm());
        
        assertEquals(true, vs1.getSelfSimilaritySettings().getApplyToVolume());
        assertEquals(true, vs2.getSelfSimilaritySettings().getApplyToVolume());
        assertEquals(true, vs3.getSelfSimilaritySettings().getApplyToVolume());
    }
    
    @Test
    public void saveSectionResultToMidiFile() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        fp.setGermString("G4 A4");
        
        Section s = fp.getSections().get(0);
        // this will throw an exception if it fails...
        s.saveSectionResultToMidiFile(FractalPieceTest.getTempFileName());
    }
    
    @Test
    public void getClassName() {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        assertEquals("Section", fp.getSections().get(0).getClassName());
    }
}