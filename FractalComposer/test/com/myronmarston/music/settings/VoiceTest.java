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
import com.myronmarston.music.scales.*;
import com.myronmarston.util.Fraction;        
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class VoiceTest {            
          
    @Test
    public void getEntireVoiceResult() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.setScale(new MajorScale(NoteName.C));
        Scale scale = fp.getScale();
        fp.setGermString("C4,1/1,F D4,1/2,MP E4,1/2,MP C4,1/1,F");
        
        Voice v1 = fp.createVoice();                
        Voice v2 = fp.createVoice();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        
        VoiceSection v1s1 = v1.getVoiceSections().get(0);
        VoiceSection v1s2 = v1.getVoiceSections().get(1);
        VoiceSection v2s1 = v2.getVoiceSections().get(0);
        VoiceSection v2s2 = v2.getVoiceSections().get(1);
        
        v1.getSettings().setOctaveAdjustment(1);
        v1.getSettings().setSpeedScaleFactor(new Fraction(2, 1));
        
        v1s1.setOverrideVoiceSettings(true);
        v1s2.setOverrideVoiceSettings(true);
        v1s1.setOverrideSectionSettings(true);
        v1s2.setOverrideSectionSettings(true);
        
        v2s1.setOverrideVoiceSettings(true);
        v2s2.setOverrideVoiceSettings(true);
        v2s1.setOverrideSectionSettings(true);
        v2s2.setOverrideSectionSettings(true);
        
        v1s1.getVoiceSettings().getSelfSimilaritySettings().setApplyToPitch(true);
        v1s1.getVoiceSettings().getSelfSimilaritySettings().setApplyToRhythm(true);
        
        v1s2.getSectionSettings().setApplyInversion(true);
        v1s2.getVoiceSettings().getSelfSimilaritySettings().setApplyToPitch(true);
        v1s2.getVoiceSettings().getSelfSimilaritySettings().setApplyToRhythm(true);
        
        v2s1.getVoiceSettings().getSelfSimilaritySettings().setApplyToPitch(true);
        v2s1.getVoiceSettings().getSelfSimilaritySettings().setApplyToRhythm(true);
        
        v2s2.getSectionSettings().setApplyInversion(true);
        v2s2.getVoiceSettings().getSelfSimilaritySettings().setApplyToPitch(true);
        v2s2.getVoiceSettings().getSelfSimilaritySettings().setApplyToRhythm(true);
        
        NoteList expected = new NoteList();
        expected.setInstrument(Instrument.DEFAULT);
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(1, 1, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(2, 2, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        
        expected.add(new Note(1, 1, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(2, 2, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(3, 3, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(1, 1, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        
        expected.add(new Note(2, 2, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(3, 3, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(4, 4, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(2, 2, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(1, 1, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(2, 2, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        
        // repeat that whole thing again since this voice is twice as fast
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(1, 1, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(2, 2, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        
        expected.add(new Note(1, 1, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(2, 2, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(3, 3, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(1, 1, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        
        expected.add(new Note(2, 2, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(3, 3, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(4, 4, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(2, 2, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(1, 1, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(2, 2, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        
        // now do the inversion...
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(-1, -1, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(-2, -2, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        
        expected.add(new Note(-1, -1, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(-2, -2, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(-3, -3, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(-1, -1, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        
        expected.add(new Note(-2, -2, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(-3, -3, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(-4, -4, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(-2, -2, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(-1, -1, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(-2, -2, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        
        // and repeat the inversion once more...
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(-1, -1, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(-2, -2, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        
        expected.add(new Note(-1, -1, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(-2, -2, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(-3, -3, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(-1, -1, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        
        expected.add(new Note(-2, -2, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(-3, -3, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(-4, -4, 5, 0, new Fraction(1, 8), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(-2, -2, 5, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), scale, 0));
        
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        expected.add(new Note(-1, -1, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(-2, -2, 5, 0, new Fraction(1, 4), Dynamic.MP.getMidiVolume(), scale, 0));
        expected.add(new Note(0, 0, 5, 0, new Fraction(1, 2), Dynamic.F.getMidiVolume(), scale, 0));
        
        expected.setfirstNotesOfGermCopy(0, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48, 52, 56, 60);
        
        // half of the voice is from voice section 1, half from voice section 2
        for (int i = 0; i < expected.size(); i++) {
            expected.get(i).setSourceVoiceSection(i < expected.size() / 2 ? v1s1 : v1s2);
        }
        
        NoteListTest.assertNoteListsEqual(expected, v1.getEntireVoice());
    }        
    
    @Test
    public void createOutputManager() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        fp.setGermString("G4 A4");
        
        Voice v = fp.getVoices().get(0);
        assertNotNull(v.createOutputManager());
    }

    @Test
    public void getClassName() {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        assertEquals("Voice", fp.getVoices().get(0).getClassName());
    }
}