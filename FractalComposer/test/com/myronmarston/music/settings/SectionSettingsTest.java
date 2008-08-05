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

import com.myronmarston.music.*;
import com.myronmarston.music.scales.*;
import com.myronmarston.util.Publisher;
import com.myronmarston.util.Fraction;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class SectionSettingsTest extends AbstractVoiceOrSectionSettingsTest {

    private SectionSettings settings; 
    private int subscriberNotificationCount = 0;

    @Override
    protected SectionSettings getSettingsInstance() {
        if (settings == null) settings = new SectionSettings();
        return settings;
    }   
    
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
        settings = this.getSettingsInstance();        
    }   
    
    @Override
    protected void setDefaultSettingsValues() {
        super.setDefaultSettingsValues();
        this.settings.setApplyInversion(false);
        this.settings.setApplyRetrograde(false);
    }        

    @Test
    public void setApplyInversion() {
        // set our fields to a known state
        this.settings.setApplyInversion(false);
        this.subscriberNotificationCount = 0;
        
        this.settings.setApplyInversion(true);
        assertEquals(true, this.settings.getApplyInversion());
        assertEquals(1, this.subscriberNotificationCount);
        
        this.settings.setApplyInversion(false);
        assertEquals(false, this.settings.getApplyInversion());
        assertEquals(2, this.subscriberNotificationCount);
    }

    @Test
    public void setApplyRetrograde() {
        // set our fields to a known state
        this.settings.setApplyRetrograde(false);
        this.subscriberNotificationCount = 0;
        
        this.settings.setApplyRetrograde(true);
        assertEquals(true, this.settings.getApplyRetrograde());
        assertEquals(1, this.subscriberNotificationCount);
        
        this.settings.setApplyRetrograde(false);
        assertEquals(false, this.settings.getApplyRetrograde());
        assertEquals(2, this.subscriberNotificationCount);
    }
    
    @Test
    public void applySettingsToNoteList() throws Exception {
        Scale scale = new MajorScale(NoteName.G);        
        SectionSettings ss = new SectionSettings();
        NoteList input = NoteList.parseNoteListString("G4,1/1 A4,1/2 B4,1/2 G4,1/1", scale);
        
        ss.setApplyInversion(false);
        ss.setApplyRetrograde(false);
        
        NoteList expected = NoteList.parseNoteListString("G4,1/1 A4,1/2 B4,1/2 G4,1/1", scale);
        NoteListTest.assertNoteListsEqual(expected, ss.applySettingsToNoteList(input, scale));
        
        ss.setApplyInversion(true);
        expected = NoteList.parseNoteListString("G4,1/1 F#4,1/2 E4,1/2 G4,1/1", scale);
        NoteListTest.assertNoteListsEqual(expected, ss.applySettingsToNoteList(input, scale));
        
        ss.setApplyRetrograde(true);
        expected = NoteList.parseNoteListString("G4,1/1 E4,1/2 F#4,1/2 G4,1/1", scale);        
        NoteListTest.assertNoteListsEqual(expected, ss.applySettingsToNoteList(input, scale));
        
        ss.setApplyInversion(false);
        expected = NoteList.parseNoteListString("G4,1/1 B4,1/2 A4,1/2 G4,1/1", scale);
        NoteListTest.assertNoteListsEqual(expected, ss.applySettingsToNoteList(input, scale));
        
        ss.setOctaveAdjustment(1);
        ss.setSpeedScaleFactor(new Fraction(4, 1));
        ss.setVolumeAdjustment(new Fraction(1, 2));
        ss.setScaleStepOffset(-2);
        expected = NoteList.parseNoteListString("E5,1/4 G5,1/8 F#5,1/8 E5,1/4", scale);
        for (Note n : expected) n.setVolume(99);
        NoteListTest.assertNoteListsEqual(expected, ss.applySettingsToNoteList(input, scale));
    }        
    
    @Test
    public void equalsAndHashCode() throws Exception {
        SectionSettings ss1 = new SectionSettings(true, true);
        SectionSettings ss2;
                
        ss2 = ss1.clone();
        assertEqualsAndHashCode(true, ss1, ss2);
        ss2.setApplyInversion(!ss1.getApplyInversion());
        assertEqualsAndHashCode(false, ss1, ss2);
        
        ss2 = ss1.clone();
        assertEqualsAndHashCode(true, ss1, ss2);
        ss2.setApplyRetrograde(!ss1.getApplyRetrograde());
        assertEqualsAndHashCode(false, ss1, ss2);
        
        super.testEqualsAndHashCode(ss1, ss2);            
    }

    @Test
    public void testClone() throws Exception {        
        SectionSettings instance = new SectionSettings();
        instance.setApplyInversion(true);
        instance.setApplyRetrograde(false);
        instance.setOctaveAdjustment(4);
        instance.setSpeedScaleFactor(new Fraction(3, 1));        
        
        SectionSettings newInstance = instance.clone();
        assertTrue(instance != newInstance);
        assertTrue(instance.equals(newInstance));
        assertEquals(instance.hashCode(), newInstance.hashCode());        
    }
    
    @Test
    public void getReadOnlyCopy() throws Exception {
        SectionSettings SS = new SectionSettings(false, false);
        SectionSettings roSS = SS.getReadOnlyCopy();
        
        try {
            roSS.setApplyInversion(true);
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertEquals(false, roSS.getApplyInversion());
        
        try {
            roSS.setApplyRetrograde(true);
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertEquals(false, roSS.getApplyRetrograde()); 
    }
}