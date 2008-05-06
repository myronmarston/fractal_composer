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
}