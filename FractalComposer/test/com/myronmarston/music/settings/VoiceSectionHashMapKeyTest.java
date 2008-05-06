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