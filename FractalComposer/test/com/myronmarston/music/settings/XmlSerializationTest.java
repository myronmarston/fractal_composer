package com.myronmarston.music.settings;

import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;
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
        Note n = new Note(3, 4, 0, new Fraction(1, 4), Dynamic.F.getMidiVolume());        
        String expected = 
        "<note id=\"0\" scaleStep=\"3\" octave=\"4\" chromaticAdjustment=\"0\" volume=\"85\">\n"+                
        "   <duration id=\"1\" numerator_=\"1\" denominator_=\"4\"/>\n" +
        "</note>";
        testSerialization(n, expected);        
        
        n.setSegmentSettings(new SegmentSettings(1));                
        expected = 
        "<note id=\"0\" scaleStep=\"3\" octave=\"4\" chromaticAdjustment=\"0\" volume=\"85\">\n"+                
        "   <duration id=\"1\" numerator_=\"1\" denominator_=\"4\"/>\n" +
        "   <segmentSettings id=\"2\" chromaticAdjustment=\"1\"/>\n" +
        "</note>";    
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
        SelfSimilaritySettings sss = new SelfSimilaritySettings(true, false, false);
        testSerialization(sss, "<selfSimilaritySettings id=\"0\" applyToPitch=\"true\" applyToRhythm=\"false\" applyToVolume=\"false\"/>");        
    }
    
    @Test
    public void serializeVoiceSection() throws Exception {
        VoiceSection vs = fpWithDefaultSettings.getVoices().get(0).getVoiceSections().get(0);        
        
        String expected = 
        "<voiceSection id=\"0\" rest=\"false\" applyInversion=\"false\" applyRetrograde=\"false\">\n" +
        "   <selfSimilaritySettings id=\"1\" applyToPitch=\"true\" applyToRhythm=\"true\" applyToVolume=\"true\"/>\n" +
        "   <voice id=\"2\" octaveAdjustment=\"1\">\n" +
        // fractal piece section that gets stripped goes here...
        "      <speedScaleFactor id=\"53\" numerator_=\"2\" denominator_=\"1\"/>\n" +
        "   </voice>\n" +
        "   <section reference=\"14\"/>\n" +        
        "</voiceSection>";
        
        //printSerializationResults(vs);
        testSerialization(vs, expected, true);        
    }
    
    @Test
    public void serializeVoice() throws Exception {
        Voice v = fpWithDefaultSettings.getVoices().get(0);
        
        String expected = 
        "<voice id=\"0\" octaveAdjustment=\"1\">\n" +
        // fractal piece section that gets stripped goes here...
        "   <speedScaleFactor id=\"53\" numerator_=\"2\" denominator_=\"1\"/>\n" +
        "</voice>";
        
        //printSerializationResults(v);
        testSerialization(v, expected, true);        
    }
    
    @Test
    public void serializeSection() throws Exception {
        Section s = fpWithDefaultSettings.getSections().get(0);
        
        String expected = 
        "<section id=\"0\">\n" +
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
        fp.setScale(new MinorScale(NoteName.A));
        fp.setGermString("A4,1/4 B4,1/8,F C4,1/2");        
        
        //modify the piece a bit...
        fp.setTimeSignature(new TimeSignature(7, 4));  
        fp.getSections().remove(0);
        fp.getVoices().remove(2);
        fp.setGenerateLayeredOutro(false);
        
        String xml = fp.getXmlRepresentation();        
        FractalPiece newFp = FractalPiece.loadFromXml(xml);
        
        // check that our fractal pieces have all the same values...
        NoteListTest.assertNoteListsEqual(fp.getGerm(), newFp.getGerm());
        assertEquals(fp.getScale(), newFp.getScale());
        assertEquals(fp.getTimeSignature(), newFp.getTimeSignature());
        assertEquals(fp.getGenerateLayeredIntro(), newFp.getGenerateLayeredIntro());
        assertEquals(fp.getGenerateLayeredOutro(), newFp.getGenerateLayeredOutro());
        assertEquals(fp.getGermString(), newFp.getGermString());
        
        // check voices...
        assertEquals(fp.getVoices().size(), newFp.getVoices().size());
        for (int i = 0; i < fp.getVoices().size(); i++) {
            Voice v = fp.getVoices().get(i);
            Voice newV = newFp.getVoices().get(i);
            assertEquals(v.getOctaveAdjustment(), newV.getOctaveAdjustment());
            assertEquals(v.getSpeedScaleFactor(), newV.getSpeedScaleFactor());
            NoteListTest.assertNoteListsEqual(v.getModifiedGerm(), newV.getModifiedGerm());
            NoteListTest.assertNoteListsEqual(v.getEntireVoice(), newV.getEntireVoice());
            assertEquals(newFp, newV.getFractalPiece());
        }
        
        // check sections...
        assertEquals(fp.getSections().size(), newFp.getSections().size());
        for (int i = 0; i < fp.getSections().size(); i++) {
            // our sections don't have any fields to compare, 
            // but we can at least check that the duration of each is the same...
            assertEquals(fp.getSections().get(i).getDuration(), newFp.getSections().get(i).getDuration());
            assertEquals(newFp, newFp.getSections().get(i).getFractalPiece());
        }        
        
        // check voice sections...
        assertEquals(fp.getVoiceSections().size(), newFp.getVoiceSections().size());
        for (int v = 0; v < fp.getVoices().size(); v++) {
            for (int s = 0; s < fp.getSections().size(); s++) {
                VoiceSection vs = fp.getVoiceSections().get(fp.getVoices().get(v).getHashMapKeyForOtherTypeIndex(s));
                VoiceSection newVs = newFp.getVoiceSections().get(newFp.getVoices().get(v).getHashMapKeyForOtherTypeIndex(s));
                assertEquals(vs.getSelfSimilaritySettings(), newVs.getSelfSimilaritySettings());
                assertEquals(vs.getApplyInversion(), newVs.getApplyInversion());
                assertEquals(vs.getApplyRetrograde(), newVs.getApplyRetrograde());
                assertEquals(vs.getRest(), newVs.getRest());
                assertEquals(vs.getVoiceSectionResult(), newVs.getVoiceSectionResult());                  
            }
        }
    }
    
    @Test
    public void serializeAndDeserialize() throws Exception {        
        testSerializeAndDeserialize(new Note(1, 6, 2, new Fraction(3, 8), Dynamic.P.getMidiVolume()));
        testSerializeAndDeserialize(new TimeSignature(7, 4));        
        testSerializeAndDeserialize(new KeySignature(Tonality.Minor, NoteName.G));                
        testSerializeAndDeserialize(new SelfSimilaritySettings(true, false, true));
        
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
        for (Class c : Scale.getScaleTypes()) {     
            // originally we used getConstructor(NoteName.class) but that seems
            // to only get public constructors.  Our chromatic scale has this
            // constructor declared private, so we have to iterate over
            // getDeclaredConstructors (which includes private ones) and pick
            // out the right one.
            for (Constructor con : c.getDeclaredConstructors()) {
                Class[] paramTypes = con.getParameterTypes();
                if (paramTypes.length == 1 && paramTypes[0] == NoteName.class) {
                    con.setAccessible(true); // in case it is private
                    list.add((Scale) con.newInstance(NoteName.D));                       
                    break;
                }
            }                                 
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