package com.myronmarston.music.settings;

import java.util.ConcurrentModificationException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class VoiceSectionListTest {

   @Test(expected=ConcurrentModificationException.class)
    public void exceptionIfModifyVoiceSectionListWhileIterating() {
        FractalPiece fp = new FractalPiece();
        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        
        boolean sectionCreated = false;
        for (VoiceSection vs : v1.getVoiceSections()) {
            // modify the list by creating another section...
            if (!sectionCreated) fp.createSection();
            sectionCreated = true;
        }
    }
          
    @Test(expected=UnsupportedOperationException.class)
    public void addVoiceSectionThrowsException() {
        FractalPiece fp = new FractalPiece();
        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        
        v2.getVoiceSections().add(v1.getVoiceSections().get(0));
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void removeVoiceSectionThrowsException() {
        FractalPiece fp = new FractalPiece();
        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        
        v2.getVoiceSections().remove(0);
    }        
    
    @Test
    public void getByOtherTypeUniqueIndex() {
        FractalPiece fp = new FractalPiece(); 
        fp.createDefaultSettings();
        
        // get a voice with a unique index that is different from it's regular index
        fp.getVoices().remove(0);
        Voice v = fp.createVoice(1);  
        assertEquals(v, fp.getVoices().get(1));
        assertEquals(4, v.getUniqueIndex());
        
        for (Section s : fp.getSections()) {
            assertEquals(v, s.getVoiceSections().getByOtherTypeUniqueIndex(4).getVoice());
        }        
        
        // now do the same with sections...
        fp.getSections().remove(0);
        Section s = fp.createSection(1);
        assertEquals(s, fp.getSections().get(1));
        assertEquals(5, s.getUniqueIndex());
        
        for (Voice v2 : fp.getVoices()) {
            assertEquals(s, v2.getVoiceSections().getByOtherTypeUniqueIndex(5).getSection());
        }            
    }
}