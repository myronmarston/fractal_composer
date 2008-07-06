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

import javax.sound.midi.*;
import com.myronmarston.music.*;
import com.myronmarston.music.scales.*;
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
        Scale scale = fp.getScale();
        fp.getGerm().add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));
        fp.getGerm().add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, scale, 0));
        fp.getGerm().add(new Note(2, 2, 4, 0, new Fraction(1, 2), 64, scale, 0));
        fp.getGerm().add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));
        
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
        assertEquals(true, vs1.getOverrideVoiceSettings());
        assertEquals(true, vs2.getOverrideVoiceSettings());
        assertEquals(true, vs3.getOverrideVoiceSettings());
        
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
    public void createOutputManager() throws Exception {
        FractalPiece fp = new FractalPiece();        
        fp.createDefaultSettings();
        fp.setGermString("G4 A4 B4 G4");
        
        String inst1 = com.myronmarston.music.Instrument.AVAILABLE_INSTRUMENTS.get(3);
        String inst2 = com.myronmarston.music.Instrument.AVAILABLE_INSTRUMENTS.get(4);
        String inst3 = com.myronmarston.music.Instrument.AVAILABLE_INSTRUMENTS.get(5);
        assertFalse(inst1.equals(inst2));
        assertFalse(inst1.equals(inst3));
        assertFalse(inst2.equals(inst3));
        
        fp.getVoices().get(0).setInstrumentName(inst1);
        fp.getVoices().get(1).setInstrumentName(inst2);
        fp.getVoices().get(2).setInstrumentName(inst3);
        
        Section s = fp.getSections().get(0);
        // this will throw an exception if it fails...
        OutputManager om = s.createOutputManager();        
        assertNotNull(om);
        
        assertEquals(3, om.getNoteLists().size());
        assertEquals(com.myronmarston.music.Instrument.getInstrument(inst1), om.getNoteLists().get(0).getInstrument());
        assertEquals(com.myronmarston.music.Instrument.getInstrument(inst2), om.getNoteLists().get(1).getInstrument());
        assertEquals(com.myronmarston.music.Instrument.getInstrument(inst3), om.getNoteLists().get(2).getInstrument());
        
        Sequence seq = om.getSequence();
        
        // test that we get results like we expect...
        assertEquals(4, seq.getTracks().length);
        PieceTest.assertTrackHasRightNumEvents(seq.getTracks()[1], 16);
        PieceTest.assertTrackHasRightNumEvents(seq.getTracks()[2], 8);
        PieceTest.assertTrackHasRightNumEvents(seq.getTracks()[3], 4);
    }
    
    @Test
    public void getClassName() {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        assertEquals("Section", fp.getSections().get(0).getClassName());
    }
    
    @Test
    public void getSectionKeySignature() throws Exception {
        Scale FMajor = new MajorScale(NoteName.F);
        Scale GMinor = new MinorScale(NoteName.G);
        FractalPiece fp = new FractalPiece();
        fp.setScale(FMajor);
        fp.createDefaultSettings();
        
        Section s2 = fp.getSections().get(2);
        s2.setOverridePieceScale(true);
        s2.setScale(GMinor);
        for (Section s : fp.getSections()) {
            if (s == s2) 
                assertEquals(GMinor.getKeySignature(), s.getSectionKeySignature());
            else
                assertEquals(FMajor.getKeySignature(), s.getSectionKeySignature());
        }
    }
    
    @Test
    public void overridePieceScale() throws Exception {
        Scale FMajor = new MajorScale(NoteName.F);
        Scale GMinor = new MinorScale(NoteName.G);        
        FractalPiece fp = new FractalPiece();
        fp.setScale(FMajor);
        fp.createDefaultSettings();
        
        Section s2 = fp.getSections().get(2);
        assertEquals(false, s2.getOverridePieceScale());
        assertEquals(null, s2.getScale());
        
        // setting the scale should throw an exception...
        try {
            s2.setScale(GMinor);
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertEquals(null, s2.getScale());
        
        // overriding the settings should set the section's scale equal to the 
        // piece's scale
        s2.setOverridePieceScale(true);
        assertEquals(FMajor, s2.getScale());
        
        // we should be able to change the section scale now...
        s2.setScale(GMinor);
        assertEquals(GMinor, s2.getScale());
        
        // setting the scale to null should throw an exception
        try {
            s2.setScale(null);
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertEquals(GMinor, s2.getScale());
        
        
        // settting override to false should nullify the scale...
        s2.setOverridePieceScale(false);
        assertEquals(null, s2.getScale());
    }
    
    @Test
    public void getGermForSection() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        fp.setScale(new MinorScale(NoteName.E));
        String germString = "E4 G4 B4 A4 D4 E4";
        fp.setGermString(germString);
        
        for (Section s : fp.getSections()) {
            // The section germs should be the exact same instance as the piece germ,
            // since no sections have overriden the scale
            assertTrue(fp.getGerm() == s.getGermForSection());
        }
        
        // Override the scale with a scale that has the same number of scale
        // steps; we should still use the same instance of the germ
        Section s1 = fp.getSections().get(0);
        s1.setOverridePieceScale(true);
        s1.setScale(new MajorScale(NoteName.Bb));
        assertTrue(fp.getGerm() == s1.getGermForSection());
        
        // Override the scale with a scale that has a different number of scale
        // steps; we should have a different section germ.
        
        s1.setScale(new MinorPentatonicScale(NoteName.Bb));
        // parsing it with Bb as the tonic will produce lots of accidentals,
        // so the original tonic should be used instead
        NoteList expectedGerm = NoteList.parseNoteListString(germString, new MinorPentatonicScale(NoteName.E));                        
        NoteListTest.assertNoteListsEqual(expectedGerm, s1.getGermForSection());
        
        s1.setScale(new MajorPentatonicScale(NoteName.Bb));        
        // parsing it with Bb as the tonic will produce lots of accidentals,
        // and parsing it with the original tonic of E will also produce accidentals,
        // so try the relative tonic
        expectedGerm = NoteList.parseNoteListString(germString, new MajorPentatonicScale(NoteName.G));                        
        NoteListTest.assertNoteListsEqual(expectedGerm, s1.getGermForSection());
        
        germString = "G4 A4 B4 G4 F#4";
        fp.setScale(new ChromaticScale());
        fp.setGermString(germString);
        
        s1.setScale(new MajorScale(NoteName.G));
        // Parsing it with the given tonic of G will produce no accidentals, so we can
        // use that here!
        expectedGerm = NoteList.parseNoteListString(germString, new MajorScale(NoteName.G));                        
        NoteListTest.assertNoteListsEqual(expectedGerm, s1.getGermForSection());
        
        fp.setScale(new MajorScale(NoteName.C));        
        s1.setScale(new MajorPentatonicScale(NoteName.Bb));
        // we will have accidentals for each of the posibilities (C Major pentatonic, 
        // Bb Major pentatonic, A major pentatonic); choose
        // the one with the fewest accidentals--C Major pentatonic
        expectedGerm = NoteList.parseNoteListString(germString, new MajorPentatonicScale(NoteName.C));                        
        NoteListTest.assertNoteListsEqual(expectedGerm, s1.getGermForSection());
        
        s1.setScale(new MajorPentatonicScale(NoteName.G));
        expectedGerm = NoteList.parseNoteListString(germString, new MajorPentatonicScale(NoteName.G));                        
        NoteListTest.assertNoteListsEqual(expectedGerm, s1.getGermForSection());
        
        fp.setScale(new MinorScale(NoteName.E));
        s1.setScale(new MajorPentatonicScale(NoteName.Bb));
        expectedGerm = NoteList.parseNoteListString(germString, new MajorPentatonicScale(NoteName.G));                        
        NoteListTest.assertNoteListsEqual(expectedGerm, s1.getGermForSection());
    }
    
    @Test
    public void getGermForSection_caching() throws Exception {
        // Fields that effect the section germ:
        //    -Section.overridePieceScale
        //    -Section.scale
        //    -FractalPiece.scale
        //    -FractalPiece.germString
        // When any of these changes, the cached germForSection should be clared.
        
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        fp.setScale(new MinorScale(NoteName.E));        
        fp.setGermString("E4 G4 B4 A4 D4 E4");
        
        Section s1 = fp.getSections().get(0);
        
        // Section.overridePieceScale
        s1.setOverridePieceScale(true);
        s1.setScale(new MinorPentatonicScale(NoteName.G));
        NoteList cachedGerm = s1.getGermForSection();            
        assertTrue(cachedGerm == s1.getGermForSection());
        s1.setOverridePieceScale(false);
        assertFalse(cachedGerm == s1.getGermForSection());
        s1.setOverridePieceScale(true);
        assertFalse(cachedGerm == s1.getGermForSection());
        
        // Section.scale
        s1.setScale(new MinorPentatonicScale(NoteName.F));
        cachedGerm = s1.getGermForSection();
        assertTrue(cachedGerm == s1.getGermForSection());
        s1.setScale(new MajorPentatonicScale(NoteName.D));
        assertFalse(cachedGerm == s1.getGermForSection());
        s1.setScale(new MinorPentatonicScale(NoteName.F));
        assertFalse(cachedGerm == s1.getGermForSection());
        
        // FractalPiece.scale
        fp.setScale(new ChromaticScale());
        cachedGerm = s1.getGermForSection();
        assertTrue(cachedGerm == s1.getGermForSection());
        fp.setScale(new MajorScale(NoteName.D));
        assertFalse(cachedGerm == s1.getGermForSection());
        fp.setScale(new ChromaticScale());
        assertFalse(cachedGerm == s1.getGermForSection());
        
        // FractalPiece.germString
        fp.setGermString("A4 B4");
        cachedGerm = s1.getGermForSection();
        assertTrue(cachedGerm == s1.getGermForSection());
        fp.setGermString("A4 B4 A4");
        assertFalse(cachedGerm == s1.getGermForSection());
        fp.setGermString("A4 B4");
        assertFalse(cachedGerm == s1.getGermForSection());        
    }
}