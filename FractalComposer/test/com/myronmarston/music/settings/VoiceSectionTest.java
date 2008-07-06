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
public class VoiceSectionTest {
    
    @Test
    public void getSectionSettings() {
        FractalPiece fp = new FractalPiece();
        fp.createVoice();
        Section s = fp.createSection();
        VoiceSection vs = s.getVoiceSections().get(0);
        
        
        assertEquals(false, vs.getOverrideSectionSettings());
        assertEquals(s.getSettings(), vs.getSectionSettings()); 
        assertEquals(true, vs.getSectionSettings().isReadOnly());
        
        // we shouldn't be able to change the voice section's section settings directly...
        try {
            vs.getSectionSettings().setApplyInversion(true);
            fail();
        } catch (UnsupportedOperationException ex) {}
        
        // changes to the section's settings should be reflected in the voice section's section settings...
        assertEquals(false, s.getSettings().getApplyInversion());
        s.getSettings().setApplyInversion(true);
        assertEquals(true, s.getSettings().getApplyInversion());
        assertEquals(true, vs.getSectionSettings().getApplyInversion());
        assertEquals(s.getSettings(), vs.getSectionSettings()); 
        
        vs.setOverrideSectionSettings(true);
        assertEquals(false, vs.getSectionSettings().isReadOnly());
        
        // the section settings should have all the values of the section's settings
        assertTrue(s.getSettings().equals(vs.getSectionSettings()));
        
        s.getSettings().setApplyRetrograde(true);
        assertFalse(s.getSettings().equals(vs.getSectionSettings()));
        
        vs.setOverrideSectionSettings(false);
        assertEquals(true, vs.getSectionSettings().isReadOnly());
        
        vs.setOverrideSectionSettings(true);
        assertEquals(false, vs.getSectionSettings().isReadOnly());
        
        // the section settings should have all the values of the section's settings
        assertTrue(s.getSettings().equals(vs.getSectionSettings()));                
    }
    
    @Test
    public void getVoiceSettings() {
        FractalPiece fp = new FractalPiece();
        Voice v = fp.createVoice();
        fp.createSection();
        VoiceSection vs = v.getVoiceSections().get(0);
        
        assertEquals(false, vs.getOverrideVoiceSettings());
        assertEquals(v.getSettings(), vs.getVoiceSettings()); 
        assertEquals(true, vs.getVoiceSettings().isReadOnly());        
        
        // we shouldn't be able to change the voice section's Voice settings directly...
        try {
            vs.getVoiceSettings().setOctaveAdjustment(2);
            fail();
        } catch (UnsupportedOperationException ex) {}
        
        // changes to the Voice's settings should be reflected in the voice section's Voice settings...
        assertEquals(0, v.getSettings().getOctaveAdjustment());
        v.getSettings().setOctaveAdjustment(2);
        assertEquals(2, v.getSettings().getOctaveAdjustment());
        assertEquals(2, vs.getVoiceSettings().getOctaveAdjustment());
        assertEquals(v.getSettings(), vs.getVoiceSettings()); 
        
        
        vs.setOverrideVoiceSettings(true);
        assertEquals(false, vs.getVoiceSettings().isReadOnly());
        
        // the Voice settings should have all the values of the Voice's settings
        assertTrue(v.getSettings().equals(vs.getVoiceSettings()));
        
        v.getSettings().setSpeedScaleFactor(new Fraction(7, 4));
        assertFalse(v.getSettings().equals(vs.getVoiceSettings()));
        
        vs.setOverrideVoiceSettings(false);
        assertEquals(true, vs.getVoiceSettings().isReadOnly());
        
        vs.setOverrideVoiceSettings(true);
        assertEquals(false, vs.getVoiceSettings().isReadOnly());
        
        // the Voice settings should have all the values of the Voice's settings
        assertTrue(v.getSettings().equals(vs.getVoiceSettings()));                
    }
    
    @Test
    public void voiceSectionGetVoiceSectionResult() {
        // this method is meant to test two things:
        // 1. that the voice section results are correct
        // 2. that our caching of the results are cleared when settings change
        FractalPiece fp = new FractalPiece();
        Scale scale = fp.getScale();
        fp.getGerm().add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));
        fp.getGerm().add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, scale, 0));
        fp.getGerm().add(new Note(2, 2, 4, 0, new Fraction(1, 2), 64, scale, 0));
        fp.getGerm().add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));
        
        Voice v1 = fp.createVoice();
        v1.getSettings().setOctaveAdjustment(2);
        v1.getSettings().setSpeedScaleFactor(new Fraction(4, 1));
        
        Section s1 = fp.createSection();
        VoiceSection vs1 = v1.getVoiceSections().get(0);
        
        NoteList expected = new NoteList();
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));
        expected.add(new Note(1, 1, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(2, 2, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));                
        NoteListTest.assertNoteListsEqual(expected, vs1.getVoiceSectionResult());
                
        // apply self-similarity...        
        v1.getSettings().getSelfSimilaritySettings().setApplyToPitch(true);
        v1.getSettings().getSelfSimilaritySettings().setApplyToRhythm(true);
        v1.getSettings().getSelfSimilaritySettings().setApplyToVolume(true);
        expected.clear();
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));
        expected.add(new Note(1, 1, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(2, 2, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));
        
        expected.add(new Note(1, 1, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(2, 2, 6, 0, new Fraction(1, 16), 43, scale, 0));
        expected.add(new Note(3, 3, 6, 0, new Fraction(1, 16), 43, scale, 0));
        expected.add(new Note(1, 1, 6, 0, new Fraction(1, 8), 64, scale, 0));
        
        expected.add(new Note(2, 2, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(3, 3, 6, 0, new Fraction(1, 16), 43, scale, 0));
        expected.add(new Note(4, 4, 6, 0, new Fraction(1, 16), 43, scale, 0));
        expected.add(new Note(2, 2, 6, 0, new Fraction(1, 8), 64, scale, 0));
        
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));
        expected.add(new Note(1, 1, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(2, 2, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));
        NoteListTest.assertNoteListsEqual(expected, vs1.getVoiceSectionResult());
        
        // set retrograde on the section, the voice section should use this default
        expected.clear();        
        s1.getSettings().setApplyRetrograde(true);        
        assertEquals(false, s1.getSettings().getApplyInversion());
        assertEquals(true, s1.getSettings().getApplyRetrograde());       
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));
        expected.add(new Note(2, 2, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(1, 1, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));
        
        expected.add(new Note(2, 2, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(4, 4, 6, 0, new Fraction(1, 16), 43, scale, 0));
        expected.add(new Note(3, 3, 6, 0, new Fraction(1, 16), 43, scale, 0));
        expected.add(new Note(2, 2, 6, 0, new Fraction(1, 8), 64, scale, 0));
        
        expected.add(new Note(1, 1, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(3, 3, 6, 0, new Fraction(1, 16), 43, scale, 0));
        expected.add(new Note(2, 2, 6, 0, new Fraction(1, 16), 43, scale, 0));
        expected.add(new Note(1, 1, 6, 0, new Fraction(1, 8), 64, scale, 0));
        
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));
        expected.add(new Note(2, 2, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(1, 1, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));
        NoteListTest.assertNoteListsEqual(expected, vs1.getVoiceSectionResult());
        
        // overide the inversion
        expected.clear();
        vs1.setOverrideSectionSettings(true);
        vs1.getSectionSettings().setApplyInversion(true);
        assertEquals(false, s1.getSettings().getApplyInversion());
        assertEquals(true, s1.getSettings().getApplyRetrograde());
        assertEquals(true, vs1.getSectionSettings().getApplyInversion());
        assertEquals(true, vs1.getSectionSettings().getApplyRetrograde());
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));
        expected.add(new Note(-2, -2, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(-1, -1, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));
        
        expected.add(new Note(-2, -2, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(-4, -4, 6, 0, new Fraction(1, 16), 43, scale, 0));
        expected.add(new Note(-3, -3, 6, 0, new Fraction(1, 16), 43, scale, 0));
        expected.add(new Note(-2, -2, 6, 0, new Fraction(1, 8), 64, scale, 0));
        
        expected.add(new Note(-1, -1, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(-3, -3, 6, 0, new Fraction(1, 16), 43, scale, 0));
        expected.add(new Note(-2, -2, 6, 0, new Fraction(1, 16), 43, scale, 0));
        expected.add(new Note(-1, -1, 6, 0, new Fraction(1, 8), 64, scale, 0));
        
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));
        expected.add(new Note(-2, -2, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(-1, -1, 6, 0, new Fraction(1, 8), 64, scale, 0));
        expected.add(new Note(0, 0, 6, 0, new Fraction(1, 4), 96, scale, 0));
        NoteListTest.assertNoteListsEqual(expected, vs1.getVoiceSectionResult());
                       
        vs1.setRest(true);
        expected.clear();        
        expected.add(Note.createRest(new Fraction(3, 4)));
        NoteListTest.assertNoteListsEqual(expected, vs1.getVoiceSectionResult());                
    }
    
    @Test
    public void getVoiceSectionResult_withScaleStepOffsetAndVolumeAdjustment() throws Exception {
        // these features were added later and it was easier just to make a seperate test for them
        
        FractalPiece fp = new FractalPiece();
        fp.setScale(new MajorScale(NoteName.C));
        fp.setGermString("G4 A4");
        fp.createDefaultSettings();
        
        VoiceSection vs = fp.getVoices().get(1).getVoiceSections().get(0);
        VoiceSettings vSettings = vs.getVoice().getSettings();
        SectionSettings sSettings = vs.getSection().getSettings();
        
        vSettings.setVolumeAdjustment(0.5d);
        vSettings.setScaleStepOffset(2);        
        
        sSettings.setVolumeAdjustment(0.5d);
        sSettings.setScaleStepOffset(1);
        
        int expectedVolume = 113;
        NoteList expected = NoteList.parseNoteListString("C5 D5", new MajorScale(NoteName.C));
        expected.get(0).setVolume(expectedVolume);
        expected.get(1).setVolume(expectedVolume);
        
        NoteListTest.assertNoteListsEqual(expected, vs.getVoiceSectionResult());
        
        //test that the cached results are cleared...
        vSettings.setScaleStepOffset(-2);
        expected = NoteList.parseNoteListString("F4 G4", new MajorScale(NoteName.C));
        expected.get(0).setVolume(expectedVolume);
        expected.get(1).setVolume(expectedVolume);
        
        NoteListTest.assertNoteListsEqual(expected, vs.getVoiceSectionResult());
                
        sSettings.setVolumeAdjustment(-0.5d);
        expectedVolume = 81;
        expected = NoteList.parseNoteListString("F4 G4", new MajorScale(NoteName.C));
        expected.get(0).setVolume(expectedVolume);
        expected.get(1).setVolume(expectedVolume);
        NoteListTest.assertNoteListsEqual(expected, vs.getVoiceSectionResult());
    }
        
    @Test(expected=IllegalArgumentException.class)    
    public void getLengthenedVoiceSectionResultErrorIfLengthTooShort() {
        FractalPiece fp = new FractalPiece();
        Scale scale = fp.getScale();
        fp.getGerm().add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));
        fp.getGerm().add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, scale, 0));
        fp.getGerm().add(new Note(2, 2, 4, 0, new Fraction(1, 2), 64, scale, 0));
        fp.getGerm().add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));
        
        Voice v1 = fp.createVoice();                
        Section s1 = fp.createSection();
        VoiceSection vs1 = v1.getVoiceSections().get(0);
        
        vs1.getLengthenedVoiceSectionResult(new Fraction(5, 2));
    }
            
    @Test
    public void getLengthenedVoiceSectionResult() {
        FractalPiece fp = new FractalPiece();
        Scale scale = fp.getScale();
        fp.getGerm().add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));
        fp.getGerm().add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, scale, 0));
        fp.getGerm().add(new Note(2, 2, 4, 0, new Fraction(1, 2), 64, scale, 0));
        fp.getGerm().add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));
        
        Voice v1 = fp.createVoice();                
        Section s1 = fp.createSection();
        VoiceSection vs1 = v1.getVoiceSections().get(0);
                
        assertEquals(new Fraction(3, 1), vs1.getVoiceSectionResult().getDuration());
        
        NoteList expected = new NoteList();
        expected.addAll(vs1.getVoiceSectionResult());        
        NoteListTest.assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(new Fraction(3, 1)));
        
        expected.add(Note.createRest(new Fraction(5, 2)));
        NoteListTest.assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(new Fraction(11, 2)));
        
        expected.clear();
        expected.addAll(vs1.getVoiceSectionResult());
        expected.addAll(vs1.getVoiceSectionResult());
        NoteListTest.assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(new Fraction(6, 1)));
        
        expected.add(Note.createRest(new Fraction(2, 1)));
        NoteListTest.assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(new Fraction(8, 1)));
        
        expected.clear();
        expected.addAll(vs1.getVoiceSectionResult());
        expected.addAll(vs1.getVoiceSectionResult());
        expected.addAll(vs1.getVoiceSectionResult());
        expected.addAll(vs1.getVoiceSectionResult());
        expected.add(Note.createRest(new Fraction(1, 2)));
        NoteListTest.assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(new Fraction(25, 2)));        
    }          
    
    @Test
    public void createOutputManager() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        fp.setGermString("G4 A4");

        String inst1 = Instrument.AVAILABLE_INSTRUMENTS.get(10);
        assertFalse(Instrument.getInstrument(inst1).equals(Instrument.DEFAULT));
        
        Voice v1 = fp.getVoices().get(0);
        v1.setInstrumentName(inst1);
        VoiceSection vs = v1.getVoiceSections().get(0);
                                        
        OutputManager om = vs.createOutputManager();
        assertNotNull(om);
        
        // Check that it is the full voice section (as it would be in the context
        // of the generated piece), including repeats due to lengthening.
        
        // We want to test a caser where the voice section "normal" length is less than
        // the section's length, so test that first...        
        Fraction sectionDuration = vs.getSection().getDuration();
        Fraction naturalVSDuration = vs.getVoiceSectionResult().getDuration();        
        assertFalse(sectionDuration.equals(naturalVSDuration));
        
        // we should have only one note list for out output manager
        assertEquals(1, om.getNoteLists().size());
        assertEquals(sectionDuration, om.getNoteLists().get(0).getDuration());        
        
        assertEquals(Instrument.getInstrument(inst1), om.getNoteLists().get(0).getInstrument());
    }

    @Test
    public void getOtherVoiceOrSection() {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        
        VoiceSection vs00 = fp.getVoices().get(0).getVoiceSections().get(0);
        assertEquals(fp.getVoices().get(0), vs00.getOtherVoiceOrSection(fp.getSections().get(0)));
        assertEquals(fp.getSections().get(0), vs00.getOtherVoiceOrSection(fp.getVoices().get(0)));
    }
    
    @Test
    public void voiceSectionResultUpdatedOnSelfSimilarityLevelsChange() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.setScale(new MajorScale(NoteName.C));
        fp.setGermString("C4 D4 C4");
        Voice v = fp.createVoice();
        Section s = fp.createSection();
        VoiceSection vs = v.getVoiceSections().get(0);
        
        v.getSettings().getSelfSimilaritySettings().setApplyToPitch(true);
        v.getSettings().getSelfSimilaritySettings().setSelfSimilarityIterations(1);
        
        NoteList expectedResult = NoteList.parseNoteListString("C4 D4 C4  D4 E4 D4  C4 D4 C4", fp.getScale());        
        NoteListTest.assertNoteListsEqual(expectedResult, vs.getVoiceSectionResult());
        
        v.getSettings().getSelfSimilaritySettings().setSelfSimilarityIterations(3);
        expectedResult = NoteList.parseNoteListString("C4 D4 C4  D4 E4 D4  C4 D4 C4   D4 E4 D4  E4 F4 E4  D4 E4 D4   C4 D4 C4  D4 E4 D4  C4 D4 C4   D4 E4 D4  E4 F4 E4  D4 E4 D4   E4 F4 E4  F4 G4 F4  E4 F4 E4   D4 E4 D4  E4 F4 E4  D4 E4 D4   C4 D4 C4  D4 E4 D4  C4 D4 C4   D4 E4 D4  E4 F4 E4  D4 E4 D4   C4 D4 C4  D4 E4 D4  C4 D4 C4", fp.getScale());
        NoteListTest.assertNoteListsEqual(expectedResult, vs.getVoiceSectionResult());
        
        vs.setOverrideVoiceSettings(true);
        vs.getVoiceSettings().getSelfSimilaritySettings().setSelfSimilarityIterations(1);
        expectedResult = NoteList.parseNoteListString("C4 D4 C4  D4 E4 D4  C4 D4 C4", fp.getScale());
        NoteListTest.assertNoteListsEqual(expectedResult, vs.getVoiceSectionResult());
    }
    
    @Test
    public void getSectionResult_withInstrument() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.setGermString("G4 A4 B4");
        fp.createDefaultSettings();
        String inst1 = Instrument.AVAILABLE_INSTRUMENTS.get(10);        
        assertFalse(Instrument.getInstrument(inst1).equals(Instrument.DEFAULT));
        
        Voice v1 = fp.getVoices().get(0);
        v1.setInstrumentName(inst1);
        VoiceSection vs = v1.getVoiceSections().get(0);
        
        NoteList result = vs.getVoiceSectionResult();
        assertEquals(Instrument.getInstrument(inst1), result.getInstrument());
        
        NoteList lengthenedResult = vs.getLengthenedVoiceSectionResult(result.getDuration().times(2L));
        assertEquals(Instrument.getInstrument(inst1), lengthenedResult.getInstrument());
    }
    
    @Test
    public void getVoiceSectionResult_withScale() throws Exception {
        FractalPiece fp = new FractalPiece();
        Scale scale = new MajorScale(NoteName.C);
        fp.setScale(scale);
        fp.setGermString("C4");
        Voice v = fp.createVoice();
        Section s = fp.createSection();
        VoiceSection vs = v.getVoiceSections().get(0);
                
        NoteList expected = new NoteList();
        expected.add(new Note(0, 0, 4, 0, new Fraction(1, 4), MidiNote.DEFAULT_VELOCITY, scale, 0));
        NoteListTest.assertNoteListsEqual(expected, vs.getVoiceSectionResult());
        
        s.setOverridePieceScale(true);
        s.setScale(new HarmonicMinorScale(NoteName.Bb));
        expected.clear();
        expected.add(new Note(0, 0, 4, 0, new Fraction(1, 4), MidiNote.DEFAULT_VELOCITY, new HarmonicMinorScale(NoteName.Bb), 0));
        NoteListTest.assertNoteListsEqual(expected, vs.getVoiceSectionResult());
                
        s.setOverridePieceScale(false);        
        expected.clear();
        expected.add(new Note(0, 0, 4, 0, new Fraction(1, 4), MidiNote.DEFAULT_VELOCITY, scale, 0));
        NoteListTest.assertNoteListsEqual(expected, vs.getVoiceSectionResult());
    }
    
    @Test
    public void getVoiceSectionResult_withDifferentNumScaleStepsScale() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.setGermString("G4 E5 D5 G4");
        fp.setScale(new MajorScale(NoteName.G));
        fp.createDefaultSettings();
        
        Section s1 = fp.getSections().get(0);
        VoiceSection vs1 = s1.getVoiceSections().get(0);
        
        NoteList expectedResult = NoteList.parseNoteListString("G5,1/8 E6 D6 G5  E6 C7 B6 E6  D6 B6 A6 D6  G5 E6 D6 G5", new MajorScale(NoteName.G));
        NoteListTest.assertNoteListsEqual(expectedResult, vs1.getVoiceSectionResult());
        
        s1.setOverridePieceScale(true);
        s1.setScale(new MajorPentatonicScale(NoteName.D));
        expectedResult = NoteList.parseNoteListString("D5,1/8 B5 A5 D5  B5 A6 F#6 B5  A5 F#6 E6 A5  D5 B5 A5 D5", new MajorPentatonicScale(NoteName.D));
        NoteListTest.assertNoteListsEqual(expectedResult, vs1.getVoiceSectionResult());
        
        s1.setScale(new ChromaticScale());
        expectedResult = NoteList.parseNoteListString("G5,1/8 E6 D6 G5  E6 C#7 B6 E6  D6 B6 A6 D6  G5 E6 D6 G5", new ChromaticScale());
        NoteListTest.assertNoteListsEqual(expectedResult, vs1.getVoiceSectionResult());
    }
}