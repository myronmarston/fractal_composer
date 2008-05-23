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