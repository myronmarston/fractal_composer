package com.myronmarston.music.settings;

import java.util.ConcurrentModificationException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class VoiceOrSectionListTest {

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
    
    @Test
    public void testUniqueIndex() {
        FractalPiece fp = new FractalPiece();
        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        
        // each voice or section gets a unique index, incrementing by one each time...
        assertEquals(1, v1.getUniqueIndex());
        assertEquals(2, v2.getUniqueIndex());
        assertEquals(1, s1.getUniqueIndex());
        assertEquals(2, s2.getUniqueIndex());
        
        // even if we delete one and insert one at the start, the unique index should keep incrementing...
        fp.getVoices().remove(0);
        fp.createVoice(0);
        assertEquals(3, fp.getVoices().get(0).getUniqueIndex());
        
        fp.getSections().remove(0);
        fp.createSection(0);
        assertEquals(3, fp.getSections().get(0).getUniqueIndex());
        
        // check the getNextUniqueIndex method itself...
        assertEquals(4, fp.getSections().getNextUniqueIndex());
        assertEquals(5, fp.getSections().getNextUniqueIndex());
        assertEquals(4, fp.getVoices().getNextUniqueIndex());
        assertEquals(5, fp.getVoices().getNextUniqueIndex());                
    }
    
    @Test
    public void getByUniqueIndex() {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        assertEquals(fp.getVoices().get(0), fp.getVoices().getByUniqueIndex(1));
        assertEquals(fp.getVoices().get(1), fp.getVoices().getByUniqueIndex(2));
        assertEquals(fp.getVoices().get(2), fp.getVoices().getByUniqueIndex(3));
        
        fp.getVoices().remove(0);        
        fp.createVoice(0);
        assertEquals(fp.getVoices().get(0), fp.getVoices().getByUniqueIndex(4));        
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void getByUniqueIndex_OutOfBounds() {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        fp.getVoices().getByUniqueIndex(0);
    }
    
    @Test
    public void removeByUniqueIndex() {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        Voice v1 = fp.getVoices().getByUniqueIndex(1);
        assertTrue(fp.getVoices().contains(v1));
        
        fp.getVoices().removeByUniqueIndex(1);
        assertFalse(fp.getVoices().contains(v1));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void removeByUniqueIndex_OutOfBounds() {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        fp.getVoices().removeByUniqueIndex(0);
    }
    
    @Test
    public void normalizeUniqueIndices() {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        fp.createVoice();
        fp.getVoices().remove(2);
        fp.createVoice(0);
        fp.getSections().remove(1);
        
        fp.normalizeUniqueIndices();
        for (int i = 1; i <= fp.getVoices().size(); i++) {
            assertEquals(i, fp.getVoices().get(i - 1).getUniqueIndex());
        }
        
        for (int i = 1; i <= fp.getSections().size(); i++) {
            assertEquals(i, fp.getSections().get(i - 1).getUniqueIndex());
        }
        
        assertEquals(fp.getVoices().size() + 1, fp.getVoices().getNextUniqueIndex());
        assertEquals(fp.getSections().size() + 1, fp.getSections().getNextUniqueIndex());
    }
}