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
import com.myronmarston.util.*;
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
        
        assertEquals(fp.getVoices().get(0).getSettings().getOctaveAdjustment(), 1);
        assertEquals(fp.getVoices().get(1).getSettings().getOctaveAdjustment(), 0);
        assertEquals(fp.getVoices().get(2).getSettings().getOctaveAdjustment(), -1);
        
        assertEquals(fp.getVoices().get(0).getSettings().getSpeedScaleFactor(), new Fraction(2, 1));
        assertEquals(fp.getVoices().get(1).getSettings().getSpeedScaleFactor(), new Fraction(1, 1));
        assertEquals(fp.getVoices().get(2).getSettings().getSpeedScaleFactor(), new Fraction(1, 2));
        
        assertEquals(fp.getVoices().get(0).getSettings().getSelfSimilaritySettings().getApplyToPitch(), true);
        assertEquals(fp.getVoices().get(1).getSettings().getSelfSimilaritySettings().getApplyToPitch(), false);
        assertEquals(fp.getVoices().get(2).getSettings().getSelfSimilaritySettings().getApplyToPitch(), false);
        
        assertEquals(fp.getVoices().get(0).getSettings().getSelfSimilaritySettings().getApplyToRhythm(), false);
        assertEquals(fp.getVoices().get(1).getSettings().getSelfSimilaritySettings().getApplyToRhythm(), false);
        assertEquals(fp.getVoices().get(2).getSettings().getSelfSimilaritySettings().getApplyToRhythm(), false);
        
        assertEquals(fp.getVoices().get(0).getSettings().getSelfSimilaritySettings().getApplyToVolume(), true);
        assertEquals(fp.getVoices().get(1).getSettings().getSelfSimilaritySettings().getApplyToVolume(), false);
        assertEquals(fp.getVoices().get(2).getSettings().getSelfSimilaritySettings().getApplyToVolume(), false);
        
        assertEquals(fp.getVoices().get(0).getSettings().getSelfSimilaritySettings().getSelfSimilarityIterations(), 1);
        assertEquals(fp.getVoices().get(1).getSettings().getSelfSimilaritySettings().getSelfSimilarityIterations(), 1);
        assertEquals(fp.getVoices().get(2).getSettings().getSelfSimilaritySettings().getSelfSimilarityIterations(), 1);
    }
    
    @Test
    public void createDefaultSections() {
        FractalPiece fp = new FractalPiece();
        
        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        Voice v3 = fp.createVoice();
        
        v1.getSettings().setSpeedScaleFactor(new Fraction(2, 1));
        v3.getSettings().setSpeedScaleFactor(new Fraction(1, 2));
        
        fp.createDefaultSections();
        assertEquals(4, fp.getSections().size());
        
        assertEquals(fp.getSections().get(0).getSettings().getApplyInversion(), false);
        assertEquals(fp.getSections().get(1).getSettings().getApplyInversion(), true);
        assertEquals(fp.getSections().get(2).getSettings().getApplyInversion(), true);
        assertEquals(fp.getSections().get(3).getSettings().getApplyInversion(), false);
        
        assertEquals(fp.getSections().get(0).getSettings().getApplyRetrograde(), false);
        assertEquals(fp.getSections().get(1).getSettings().getApplyRetrograde(), false);
        assertEquals(fp.getSections().get(2).getSettings().getApplyRetrograde(), true);
        assertEquals(fp.getSections().get(3).getSettings().getApplyRetrograde(), true);       
    }
            
    @Test
    public void introAndOutroTest() throws InvalidKeySignatureException {
        FractalPiece fp = new FractalPiece();

        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        Voice v3 = fp.createVoice();
        
        v1.getSettings().setSpeedScaleFactor(new Fraction(2, 1));
        v3.getSettings().setSpeedScaleFactor(new Fraction(1, 2));
        
        fp.createIntroSections();
        assertVoiceSectionEqual(v1.getVoiceSections().get(0), false, false, true, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(1), false, false, true, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(2), false, false, false, false, false, false);
        
        assertVoiceSectionEqual(v2.getVoiceSections().get(0), false, false, true, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(1), false, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(2), false, false, false, false, false, false);
        
        assertVoiceSectionEqual(v3.getVoiceSections().get(0), false, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(1), false, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(2), false, false, false, false, false, false);
        fp.clearTempIntroOutroSections();
        assertEquals(0, fp.getSections().size());
        
        // change the speed factors; this should change which voices gets rests when...
        v2.getSettings().setSpeedScaleFactor(new Fraction(1, 4));
        
        fp.createIntroSections();
        assertVoiceSectionEqual(v1.getVoiceSections().get(0), false, false, true, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(1), false, false, true, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(2), false, false, false, false, false, false);
        
        assertVoiceSectionEqual(v2.getVoiceSections().get(0), false, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(1), false, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(2), false, false, false, false, false, false);
        
        assertVoiceSectionEqual(v3.getVoiceSections().get(0), false, false, true, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(1), false, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(2), false, false, false, false, false, false);
        fp.clearTempIntroOutroSections();
        
        fp.createOutroSections();
        assertVoiceSectionEqual(v1.getVoiceSections().get(0), false, false, false, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(1), false, false, true, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(2), false, false, true, false, false, false);
        
        assertVoiceSectionEqual(v2.getVoiceSections().get(0), false, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(1), false, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(2), false, false, false, false, false, false);
        
        assertVoiceSectionEqual(v3.getVoiceSections().get(0), false, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(1), false, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(2), false, false, true, false, false, false);
        fp.clearTempIntroOutroSections();    
        
        // change the speed scale factor of a voice; this should change where the rests go...
        v1.getSettings().setSpeedScaleFactor(new Fraction(1, 8));
        
        fp.createOutroSections();
        assertVoiceSectionEqual(v1.getVoiceSections().get(0), false, false, false, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(1), false, false, false, false, false, false);
        assertVoiceSectionEqual(v1.getVoiceSections().get(2), false, false, false, false, false, false);
        
        assertVoiceSectionEqual(v2.getVoiceSections().get(0), false, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(1), false, false, false, false, false, false);
        assertVoiceSectionEqual(v2.getVoiceSections().get(2), false, false, true, false, false, false);
        
        assertVoiceSectionEqual(v3.getVoiceSections().get(0), false, false, false, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(1), false, false, true, false, false, false);
        assertVoiceSectionEqual(v3.getVoiceSections().get(2), false, false, true, false, false, false);
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
    public void createPieceResultOutputManager() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.setScale(new MajorScale(NoteName.G));
        fp.setGermString("G4,1/4,MF A4,1/8,F B4,1/8,F G4,1/4,MF");  
        fp.createDefaultSettings();
        
        // we have other tests that test that the output manager properly works
        assertNotNull(fp.createPieceResultOutputManager());
    }
    
    @Test
    public void createGermOutputManager() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.setScale(new MajorScale(NoteName.G));
        fp.setGermString("G4,1/4,MF A4,1/8,F B4,1/8,F G4,1/4,MF");  
        fp.createDefaultSettings();
        
        // we have other tests that test that the output manager properly works
        assertNotNull(fp.createGermOutputManager());
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
        final FractalPiece fp = new FractalPiece();        
        fp.setGermString("C4 E4 F4 G4");
        FileHelper.createAndUseTempFile("TestMidiFile", ".mid", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {                
                fp.createGermOutputManager().saveMidiFile(tempFileName);
                final Track baselineTrack = MidiSystem.getSequence(new File(tempFileName)).getTracks()[1];

                for (Scale s : getAllScalePossibilities()) {
                    //System.out.println("Testing " + s.toString());
                    fp.setScale(s);

                    FileHelper.createAndUseTempFile("TestMidiFile", ".mid", new FileHelper.TempFileUser() {
                        public void useTempFile(String tempFileName) throws Exception {                            
                            fp.createGermOutputManager().saveMidiFile(tempFileName);

                            Track t = MidiSystem.getSequence(new File(tempFileName)).getTracks()[1];            
                            assertTracksEqual(baselineTrack, t);
                        }
                    });                    
                }
            }
        });        
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
            //System.out.println("    Testing midi event " + i);
            MidiEvent me1 = t1.get(i);
            MidiEvent me2 = t2.get(i);
            
            assertEquals(me1.getTick(), me2.getTick());
            assertEquals(me1.getMessage().getClass(), me2.getMessage().getClass());
            assertEquals(me1.getMessage().getStatus(), me2.getMessage().getStatus());
            assertEquals(me1.getMessage().getLength(), me2.getMessage().getLength());
            for (int j = 0; j < me1.getMessage().getLength(); j++) {
                //System.out.println("        Testing message byte " + j);
                assertEquals(me1.getMessage().getMessage()[j], me2.getMessage().getMessage()[j]);            
            }            
        }        
    }        
     
    static protected void assertVoiceSectionEqual(VoiceSection vs, boolean applyInversion, boolean applyRetrograde, boolean isRest, boolean applySelfSimilarityToPitch, boolean applySelfSimilarityToRhythm, boolean applySelfSimilarityToVolume) {
        assertEquals(applyInversion, vs.getSectionSettings().getApplyInversion());
        assertEquals(applyRetrograde, vs.getSectionSettings().getApplyRetrograde());
        assertEquals(isRest, vs.getRest());
        assertEquals(applySelfSimilarityToPitch, vs.getVoiceSettings().getSelfSimilaritySettings().getApplyToPitch());
        assertEquals(applySelfSimilarityToRhythm, vs.getVoiceSettings().getSelfSimilaritySettings().getApplyToRhythm());
        assertEquals(applySelfSimilarityToVolume, vs.getVoiceSettings().getSelfSimilaritySettings().getApplyToVolume());
    }
}