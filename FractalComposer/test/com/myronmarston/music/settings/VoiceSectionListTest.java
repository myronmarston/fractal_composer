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
}