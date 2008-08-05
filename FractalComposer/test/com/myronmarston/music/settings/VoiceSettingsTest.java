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

import com.myronmarston.music.NoteListTest;
import com.myronmarston.music.NoteList;
import com.myronmarston.music.NoteName;
import com.myronmarston.music.scales.MajorScale;
import com.myronmarston.music.scales.Scale;
import com.myronmarston.util.Fraction;
import com.myronmarston.util.Publisher;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class VoiceSettingsTest extends AbstractVoiceOrSectionSettingsTest {
    
    private VoiceSettings settings; 
    private int subscriberNotificationCount = 0;

    @Override
    public void publisherNotification(Publisher p, Object args) {
        assert p == settings : p;
        this.subscriberNotificationCount++;
        super.publisherNotification(p, args);
    }   
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        settings = getSettingsInstance();        
    }

    @Override
    protected VoiceSettings getSettingsInstance() {
        if (this.settings == null) settings = new VoiceSettings();
        return settings;
    }

    @Override
    protected void setDefaultSettingsValues() {
        super.setDefaultSettingsValues();
        this.settings.setSelfSimilaritySettings(new SelfSimilaritySettings(false, false, false, 1));
    }                
    
    @Test
    public void updateSelfSimilaritySettings() {
        // set our fields to a known state
        this.settings.getSelfSimilaritySettings().setApplyToPitch(true);
        this.settings.getSelfSimilaritySettings().setApplyToVolume(false);
        this.subscriberNotificationCount = 0;
        
        this.settings.getSelfSimilaritySettings().setApplyToPitch(false);        
        assertEquals(1, this.subscriberNotificationCount);
                
        this.settings.getSelfSimilaritySettings().setApplyToVolume(true);        
        assertEquals(2, this.subscriberNotificationCount);
    }

    @Test
    public void applySettingsToNoteList() throws Exception {
        Scale scale = new MajorScale(NoteName.C);
        VoiceSettings vs = new VoiceSettings();
        NoteList input = NoteList.parseNoteListString("G4,1/1 A4,1/2 B4,1/2 G4,1/1", scale);
                
        vs.setOctaveAdjustment(2);
        vs.setSpeedScaleFactor(new Fraction(4, 1));        
        
        NoteList expected = NoteList.parseNoteListString("G6,1/4 A6,1/8 B6,1/8 G6,1/4", scale);
        NoteListTest.assertNoteListsEqual(expected, vs.applySettingsToNoteList(input, scale));
        
        vs.setSpeedScaleFactor(new Fraction(1, 2));
        expected = NoteList.parseNoteListString("G6,2/1 A6,1/1 B6,1/1 G6,2/1", scale);        
        NoteListTest.assertNoteListsEqual(expected, vs.applySettingsToNoteList(input, scale));
        
        vs.setOctaveAdjustment(-1);
        expected = NoteList.parseNoteListString("G3,2/1 A3,1/1 B3,1/1 G3,2/1", scale);        
        NoteListTest.assertNoteListsEqual(expected, vs.applySettingsToNoteList(input, scale)); 
        
        vs.getSelfSimilaritySettings().setApplyToPitch(true);
        vs.getSelfSimilaritySettings().setApplyToRhythm(true);
        expected = NoteList.parseNoteListString("G3,2/1 A3,1/1 B3,1/1 G3,2/1  A3,1/1 B3,1/2 C4,1/2 A3,1/1  B3,1/1 C4,1/2 D4,1/2 B3,1/1  G3,2/1 A3,1/1 B3,1/1 G3,2/1", scale);        
        expected.setfirstNotesOfGermCopy(0, 4, 8, 12);
        NoteListTest.assertNoteListsEqual(expected, vs.applySettingsToNoteList(input, scale));                 
    }
    
    @Test
    public void equalsAndHashCode() throws Exception {        
        VoiceSettings vs1 = new VoiceSettings(3, new Fraction(3, 1), new SelfSimilaritySettings(true, false, true, 1));        
        VoiceSettings vs2;
        
        vs2 = vs1.clone();
        assertEqualsAndHashCode(true, vs1, vs2);
        vs2.getSelfSimilaritySettings().setApplyToPitch(!vs1.getSelfSimilaritySettings().getApplyToPitch());
        assertEqualsAndHashCode(false, vs1, vs2);
        
        vs2 = vs1.clone();
        assertEqualsAndHashCode(true, vs1, vs2);
        vs2.getSelfSimilaritySettings().setApplyToRhythm(!vs1.getSelfSimilaritySettings().getApplyToRhythm());
        assertEqualsAndHashCode(false, vs1, vs2);
        
        vs2 = vs1.clone();
        assertEqualsAndHashCode(true, vs1, vs2);
        vs2.getSelfSimilaritySettings().setApplyToVolume(!vs1.getSelfSimilaritySettings().getApplyToVolume());
        assertEqualsAndHashCode(false, vs1, vs2);

        vs2 = vs1.clone();
        assertEqualsAndHashCode(true, vs1, vs2);
        vs2.getSelfSimilaritySettings().setSelfSimilarityIterations(vs1.getSelfSimilaritySettings().getSelfSimilarityIterations() + 1);
        assertEqualsAndHashCode(false, vs1, vs2);
        
        super.testEqualsAndHashCode(vs1, vs2);
    }
    
    @Test
    public void testClone() throws Exception {        
        VoiceSettings instance = new VoiceSettings();        
        instance.setOctaveAdjustment(4);
        instance.setSpeedScaleFactor(new Fraction(3, 1));        
        
        VoiceSettings newInstance = instance.clone();
        assertTrue(instance != newInstance);
        assertTrue(instance.equals(newInstance));
        assertEquals(instance.hashCode(), newInstance.hashCode()); 
        
        // we want clone to make a deep copy, creating a new instance of the self-similarity settings
        assertTrue(instance.getSelfSimilaritySettings() != newInstance.getSelfSimilaritySettings());
        assertEquals(instance.getSelfSimilaritySettings(), newInstance.getSelfSimilaritySettings());
    }
    
    @Test
    public void getReadOnlyCopy() throws Exception {
        VoiceSettings VS = new VoiceSettings(0, new Fraction(1, 1), new SelfSimilaritySettings(false, false, false, 1));
        VoiceSettings roVS = VS.getReadOnlyCopy();
               
        try {
            roVS.setOctaveAdjustment(3);            
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertEquals(0, roVS.getOctaveAdjustment()); 
        
        try {
            roVS.setSpeedScaleFactor(new Fraction(1, 2));            
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertEquals(new Fraction(1, 1), roVS.getSpeedScaleFactor()); 
        
        SelfSimilaritySettings originalSSS = roVS.getSelfSimilaritySettings();
        try {
            roVS.setSelfSimilaritySettings(new SelfSimilaritySettings(false, false, false, 1));
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertTrue(originalSSS == roVS.getSelfSimilaritySettings());
        
        SelfSimilaritySettingsTest.testReadOnlySelfSimilaritySettings(roVS.getSelfSimilaritySettings());        
    }
}