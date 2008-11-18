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

import com.myronmarston.util.Fraction;
import com.myronmarston.music.*;
import com.myronmarston.music.scales.*;
import org.simpleframework.xml.*;
import org.simpleframework.xml.load.*;
import org.simpleframework.xml.graph.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class XmlSerializationTest {
    FractalPiece fpWithDefaultSettings;
    
    public XmlSerializationTest() {
    }

    @Before
    public void setUp() throws Exception {
        fpWithDefaultSettings = new FractalPiece();
        fpWithDefaultSettings.createDefaultSettings();
    }

    @After
    public  void tearDown() throws Exception {
    }
    
    @Test
    public void serializeNote() throws Exception {
        Note n = new Note(2, 3, 4, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume(), new MajorScale(NoteName.E), 1);                        
        String expected = 
        "<note id=\"0\" scaleStep=\"3\" octave=\"4\" chromaticAdjustment=\"0\" volume=\"86\" segmentChromaticAdjustment=\"1\" letterNumber=\"2\" isFirstNoteOfGermCopy=\"false\">\n"+                
        "   <duration id=\"1\" numerator_=\"1\" denominator_=\"4\"/>\n" +
        "   <scale class=\"com.myronmarston.music.scales.MajorScale\" id=\"2\">\n" +
        "      <keySignature id=\"3\" keyName=\"E\" tonality=\"Major\"/>\n" +
        "   </scale>\n" +
        "</note>";    
        //this.printSerializationResults(n);
        testSerialization(n, expected);        
    }
    
    @Test
    public void serializeTimeSignature() throws Exception {
        TimeSignature ts = new TimeSignature(11, 8);        
        testSerialization(ts, "<timeSignature id=\"0\" numerator=\"11\" denominator=\"8\" denominatorPowerOf2=\"3\"/>");
    }
    
    @Test
    public void serializeKeySignature() throws Exception {        
        KeySignature ks = new KeySignature(Tonality.Major, NoteName.G);
        testSerialization(ks, "<keySignature id=\"0\" keyName=\"G\" tonality=\"Major\"/>");
    }
    
    @Test
    public void serializeSelfSimilaritySettings() throws Exception {
        SelfSimilaritySettings sss = new SelfSimilaritySettings(true, false, false, 1);
        testSerialization(sss, "<selfSimilaritySettings id=\"0\" applyToPitch=\"true\" applyToRhythm=\"false\" applyToVolume=\"false\" selfSimilarityIterations=\"1\" readOnly=\"false\"/>");        
    }    
    
    @Test
    public void serializeVoice() throws Exception {
        Voice v = fpWithDefaultSettings.getVoices().get(0);
        
        int idStart = 51;
        String expected = 
        "<voice id=\"0\" uniqueIndex=\"1\" instrumentName=\"" + Instrument.DEFAULT.getName() + "\">\n" +
        // fractal piece section that gets stripped goes here...
        "   <settings id=\"" + idStart++ + "\" scaleStepOffset=\"0\" octaveAdjustment=\"1\" readOnly=\"false\">\n" +
        "      <volumeAdjustment id=\"" + idStart++ + "\" numerator_=\"0\" denominator_=\"1\"/>\n" +
        "      <speedScaleFactor id=\"" + idStart++ + "\" numerator_=\"2\" denominator_=\"1\"/>\n" +
        "      <selfSimilaritySettings id=\"" + idStart++ + "\" applyToPitch=\"true\" applyToRhythm=\"false\" applyToVolume=\"true\" selfSimilarityIterations=\"1\" readOnly=\"false\"/>\n" +
        "   </settings>\n" +
        "</voice>";                
        
        //printSerializationResults(v);
        testSerialization(v, expected, true);        
    }
    
    @Test
    public void serializeSection() throws Exception {
        Section s = fpWithDefaultSettings.getSections().get(0);
        s.getSettings().setApplyInversion(true);
        s.getSettings().setApplyRetrograde(false);
        s.setOverridePieceScale(true);
        s.setScale(new MinorPentatonicScale(NoteName.G));
        s.getSettings().setVolumeAdjustment(new Fraction(3, 4));
        s.getSettings().setSpeedScaleFactor(new Fraction(1, 3));
        s.getSettings().setOctaveAdjustment(3);
        s.getSettings().setScaleStepOffset(2);
        s.getGermForSection(); //cause it to get a value...
        
        int idStart = 52;
        
        String expected =   
        "<section id=\"0\" uniqueIndex=\"1\" overridePieceScale=\"true\">\n" +
        "   <settings id=\"" + idStart++ + "\" scaleStepOffset=\"2\" octaveAdjustment=\"3\" readOnly=\"false\" applyInversion=\"true\" applyRetrograde=\"false\">\n" +
        "      <volumeAdjustment id=\"" + idStart++ + "\" numerator_=\"3\" denominator_=\"4\"/>\n" +
        "      <speedScaleFactor id=\"" + idStart++ + "\" numerator_=\"1\" denominator_=\"3\"/>\n" + 
        "   </settings>\n" +
        "   <scale class=\"com.myronmarston.music.scales.MinorPentatonicScale\" id=\"" + idStart++ + "\">\n" +
        "      <keySignature id=\"" + idStart++ + "\" keyName=\"G\" tonality=\"Minor\"/>\n" +
        "   </scale>\n" +        
        // fractal piece section that gets stripped goes here...
        "</section>";

        //printSerializationResults(s);
        testSerialization(s, expected, true);        
    }
    
    @Test
    public void serializeScale() throws InvalidKeySignatureException, Exception {
        String expectedFormat = 
        "<%1$s id=\"0\">\n"+
        "   <keySignature id=\"1\" keyName=\"%3$s\" tonality=\"%2$s\"/>\n" +
        "</%1$s>";
                
        for (Scale s : getScales()) {                 
            String className = s.getClass().getSimpleName().substring(0, 1).toLowerCase(Locale.ENGLISH) + s.getClass().getSimpleName().substring(1);
            String expected = String.format(expectedFormat, className, s.getKeySignature().getTonality().name(), s.getKeySignature().getKeyName().name());
            testSerialization(s, expected);            
        }        
    }       
    
    @Test
    public void serializeAndDeserializeFractalPiece() throws Exception {
        FractalPiece fp = this.fpWithDefaultSettings;
        fp.setScale(new NaturalMinorScale(NoteName.A));
        fp.setGermString("A4,1/4 B4,1/8,F C4,1/2");        
        
        //modify the piece a bit...
        fp.setTempo(160);
        fp.setTimeSignature(new TimeSignature(7, 4));  
        fp.getSections().remove(0);
        fp.getVoices().remove(2);
        fp.setGenerateLayeredOutro(false);
        fp.getVoices().get(0).setInstrumentName("Violin");
        fp.getVoices().get(0).getSettings().setScaleStepOffset(2);
        fp.getVoices().get(1).getSettings().setVolumeAdjustment(new Fraction(1, 4));
        fp.getSections().get(2).setOverridePieceScale(true);
        fp.getSections().get(2).setScale(new MajorPentatonicScale(NoteName.B));
        fp.getSections().get(0).getSettings().setScaleStepOffset(-1);
        fp.getSections().get(0).getSettings().setVolumeAdjustment(new Fraction(-1, 4));
        fp.getSections().get(0).getSettings().setOctaveAdjustment(-1);
        fp.getSections().get(0).getSettings().setSpeedScaleFactor(new Fraction(1, 3));
        fp.getVoices().get(0).getVoiceSections().get(0).setOverrideVoiceSettings(true);        
        fp.getVoices().get(0).getVoiceSections().get(0).getVoiceSettings().getSelfSimilaritySettings().setSelfSimilarityIterations(3);
        fp.getVoices().get(0).getVoiceSections().get(0).getVoiceSettings().setVolumeAdjustment(new Fraction(3, 10));
        fp.getSections().get(0).getVoiceSections().get(0).setOverrideSectionSettings(true);
        fp.getSections().get(0).getVoiceSections().get(0).getSectionSettings().setApplyRetrograde(true);
        fp.getSections().get(0).getVoiceSections().get(0).getSectionSettings().setScaleStepOffset(4);
        
        
        String xml = fp.getXmlRepresentation();  
        // re-serializing the same thing should produce the same xml
        assertEquals(xml, fp.getXmlRepresentation());
        //System.out.println(xml);
        FractalPiece newFp = FractalPiece.loadFromXml(xml);
        
        // serializing the deserialized fractal piece should produce the same result...
        assertEquals(xml, newFp.getXmlRepresentation());
        
        // at one point in time, creating the output manager slightly modified some settings
        // on the piece on accident.  this code will check that that no longer happens
        newFp.createPieceResultOutputManager();
        assertEquals(xml, newFp.getXmlRepresentation());
        
        // check that our fractal pieces have all the same values...
        NoteListTest.assertNoteListsEqual(fp.getGerm(), newFp.getGerm());
        assertEquals(fp.getScale(), newFp.getScale());
        assertEquals(fp.getTimeSignature(), newFp.getTimeSignature());
        assertEquals(fp.getGenerateLayeredIntro(), newFp.getGenerateLayeredIntro());
        assertEquals(fp.getGenerateLayeredOutro(), newFp.getGenerateLayeredOutro());
        assertEquals(fp.getGermString(), newFp.getGermString());
        assertEquals(fp.getTempo(), newFp.getTempo());                        
        
        // check voices...
        assertEquals(((VoiceOrSectionList) fp.getVoices()).getNextUniqueIndex(), ((VoiceOrSectionList) newFp.getVoices()).getNextUniqueIndex());
        assertEquals(fp.getVoices().size(), newFp.getVoices().size());
        for (int i = 0; i < fp.getVoices().size(); i++) {
            Voice v = fp.getVoices().get(i);
            Voice newV = newFp.getVoices().get(i);
            assertEquals(v.getUniqueIndex(), newV.getUniqueIndex());
            assertEquals(v.getSettings(), newV.getSettings());
            assertEquals(v.getInstrumentName(), newV.getInstrumentName());            
            NoteListTest.assertNoteListsEqual(v.getEntireVoice(), newV.getEntireVoice(), true);
            assertEquals(newFp, newV.getFractalPiece());
        }
        
        // check sections...
        assertEquals(fp.getSections().size(), newFp.getSections().size());
        assertEquals(((VoiceOrSectionList) fp.getSections()).getNextUniqueIndex(), ((VoiceOrSectionList) newFp.getSections()).getNextUniqueIndex());
        for (int i = 0; i < fp.getSections().size(); i++) {
            Section s = fp.getSections().get(i);
            Section newS = newFp.getSections().get(i);
            assertEquals(s.getUniqueIndex(), newS.getUniqueIndex());
            assertEquals(s.getScale(), newS.getScale());
            assertEquals(s.getSettings(), newS.getSettings());
            assertEquals(s.getDuration(), newS.getDuration());
            assertEquals(s.getOverridePieceScale(), newS.getOverridePieceScale());
            assertEquals(newFp, newFp.getSections().get(i).getFractalPiece());
            NoteListTest.assertNoteListsEqual(s.getGermForSection(), newS.getGermForSection(), true);            
        }        
        
        // check voice sections...
        assertEquals(fp.getVoiceSections().size(), newFp.getVoiceSections().size());
        for (int v = 0; v < fp.getVoices().size(); v++) {
            for (int s = 0; s < fp.getSections().size(); s++) {
                VoiceSection vs = fp.getVoiceSections().get(fp.getVoices().get(v).getHashMapKeyForOtherTypeIndex(s));
                VoiceSection newVs = newFp.getVoiceSections().get(newFp.getVoices().get(v).getHashMapKeyForOtherTypeIndex(s));
                
                VoiceSectionTest.assertVoiceSectionsEqual(vs, newVs, true);                
            }
        }        
    }
    
    @Test
    public void serializeAndDeserialize() throws Exception {        
        testSerializeAndDeserialize(new Note(1, 1, 6, 2, new Fraction(3, 8), Dynamic.P.getMidiVolume(), Scale.DEFAULT, 0));
        testSerializeAndDeserialize(new TimeSignature(7, 4));        
        testSerializeAndDeserialize(new KeySignature(Tonality.Minor, NoteName.G));                
        testSerializeAndDeserialize(new SelfSimilaritySettings(true, false, true, 1));
        
        for (Scale s : getScales()) { testSerializeAndDeserialize(s);}        
        
        testSerializeAndDeserialize(fpWithDefaultSettings.getVoices().get(0), false);
        testSerializeAndDeserialize(fpWithDefaultSettings.getSections().get(0), false);
        testSerializeAndDeserialize(fpWithDefaultSettings.getVoices().get(0).getVoiceSections().get(0), false);                
    }
    
    // this method is provided to assist with debugging purposes...
    private static void printSerializationResults(Object object) throws Exception {
        String result = serializeObject(object);
        System.out.println(stripFractalPieceSection(result));
    }
    
    private static List<Scale> getScales() throws Exception {
        List<Scale> list = new ArrayList<Scale>();
        for (Class c : Scale.SCALE_TYPES.keySet()) {                 
            @SuppressWarnings("unchecked")
            Constructor con = c.getConstructor(NoteName.class);
            list.add((Scale) con.newInstance(NoteName.D));                                                                         
        }
        
        return list;
    }       
            
    private static void testSerializeAndDeserialize(Object object, boolean assertObjectsEqual) throws Exception {        
        String serializedXml = serializeObject(object);        
        Serializer serializer = new Persister(new CycleStrategy());
        Object result = serializer.read(object.getClass(), serializedXml);
        if (assertObjectsEqual) 
            assertEquals(result, object);
        else
            assertNotNull(result);
    }
    
    private static void testSerializeAndDeserialize(Object object) throws Exception {        
        testSerializeAndDeserialize(object, true);
    }
    
    private static String serializeObject(Object objectToSerialize) throws Exception {
        Serializer serializer = new Persister(new CycleStrategy());
        StringWriter result = new StringWriter();
        serializer.write(objectToSerialize, result);
        
        return result.toString();
    }
            
    private static void testSerialization(Object objectToSerialize, String expectedXml, boolean ignoreFractalPieceSection) throws Exception {
        String result = serializeObject(objectToSerialize);
        if (ignoreFractalPieceSection) result = stripFractalPieceSection(result);
        assertEquals(expectedXml, result);    
    }
    
    private static String stripFractalPieceSection(String xml) {
       return xml.replaceFirst("(?s)(\n[ ]*<fractalPiece id.*<\\/fractalPiece>)", ""); 
    }
    
    private static void testSerialization(Object objectToSerialize, String expectedXml) throws Exception {
        testSerialization(objectToSerialize, expectedXml, false);
    }
}