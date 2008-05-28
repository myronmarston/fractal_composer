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
        v1.getSettings().setOctaveAdjustment(1);
        v1.getSettings().setSpeedScaleFactor(new Fraction(2, 1));
        
        Voice v2 = fp.createVoice();        
        
        Voice v3 = fp.createVoice();
        v3.getSettings().setOctaveAdjustment(-1);
        v3.getSettings().setSpeedScaleFactor(new Fraction(1, 2));        
        
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
        
        s1.setSelfSimilaritySettingsOnAllVoiceSections(true, false, true, 2);
        assertEquals(false, vs1.getUseDefaultVoiceSettings());
        assertEquals(false, vs2.getUseDefaultVoiceSettings());
        assertEquals(false, vs3.getUseDefaultVoiceSettings());
        
        assertEquals(true, vs1.getVoiceSettings().getSelfSimilaritySettings().getApplyToPitch());
        assertEquals(true, vs2.getVoiceSettings().getSelfSimilaritySettings().getApplyToPitch());
        assertEquals(true, vs3.getVoiceSettings().getSelfSimilaritySettings().getApplyToPitch());
        
        assertEquals(false, vs1.getVoiceSettings().getSelfSimilaritySettings().getApplyToRhythm());
        assertEquals(false, vs2.getVoiceSettings().getSelfSimilaritySettings().getApplyToRhythm());
        assertEquals(false, vs3.getVoiceSettings().getSelfSimilaritySettings().getApplyToRhythm());
        
        assertEquals(true, vs1.getVoiceSettings().getSelfSimilaritySettings().getApplyToVolume());
        assertEquals(true, vs2.getVoiceSettings().getSelfSimilaritySettings().getApplyToVolume());
        assertEquals(true, vs3.getVoiceSettings().getSelfSimilaritySettings().getApplyToVolume());
        
        assertEquals(2, vs1.getVoiceSettings().getSelfSimilaritySettings().getSelfSimilarityIterations());
        assertEquals(2, vs2.getVoiceSettings().getSelfSimilaritySettings().getSelfSimilarityIterations());
        assertEquals(2, vs3.getVoiceSettings().getSelfSimilaritySettings().getSelfSimilarityIterations());
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
    
    @Test(expected=GermIsEmptyException.class)
    public void saveSectionResultToMidiFileWithEmptyGerm() throws Exception {
        FractalPiece fp = new FractalPiece();     
        fp.createDefaultSettings();
        Section s = fp.getSections().get(0);        
        s.saveSectionResultToMidiFile(FractalPieceTest.getTempFileName());
    }
        
    @Test
    public void getClassName() {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        assertEquals("Section", fp.getSections().get(0).getClassName());
    }
}