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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class VoiceSectionHashMapKeyTest {
    
    @Test
    public void voiceSectionMapKey_hashCode_equals_Test() {
        FractalPiece fp = new FractalPiece();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        Voice v1 = fp.createVoice();
        Voice v2 = fp.createVoice();
        
        VoiceSectionHashMapKey vsmp1 = new VoiceSectionHashMapKey(v1, s1);
        VoiceSectionHashMapKey vsmp2 = new VoiceSectionHashMapKey(v1, s2);        
        assertFalse(vsmp1.equals(vsmp2));
        assertFalse(vsmp1.hashCode() == vsmp2.hashCode());
                
        VoiceSectionHashMapKey vsmp3 = new VoiceSectionHashMapKey(v1, s1);
        assertEquals(vsmp1, vsmp3);
        assertEquals(vsmp1.hashCode(), vsmp3.hashCode());                
    }    
}