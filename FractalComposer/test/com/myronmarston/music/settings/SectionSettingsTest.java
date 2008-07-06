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
import com.myronmarston.util.Subscriber;

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
    }        
    
    @Test
    public void equalsAndHashCode() {
        SectionSettings ss1 = new SectionSettings(true, true);
        SectionSettings ss2 = new SectionSettings(false, true);
        SectionSettings ss3 = new SectionSettings(true, false);
        SectionSettings ss4 = new SectionSettings(false, false);
        SectionSettings ss5 = new SectionSettings(true, true);
        SectionSettings ss6 = new SectionSettings(false, true);
        SectionSettings ss7 = new SectionSettings(true, false);
        SectionSettings ss8 = new SectionSettings(false, false);
        
        assertTrue(ss1.equals(ss5));
        assertTrue(ss2.equals(ss6));
        assertTrue(ss3.equals(ss7));
        assertTrue(ss4.equals(ss8));
        
        assertEquals(ss1.hashCode(), ss5.hashCode());
        assertEquals(ss2.hashCode(), ss6.hashCode());
        assertEquals(ss3.hashCode(), ss7.hashCode());
        assertEquals(ss4.hashCode(), ss8.hashCode());
        
        assertFalse(ss1.equals(ss2));
        assertFalse(ss1.equals(ss3));
        assertFalse(ss1.equals(ss4));
        
        assertNotSame(ss1.hashCode(), ss4.hashCode());
        assertNotSame(ss2.hashCode(), ss3.hashCode());
        assertNotSame(ss3.hashCode(), ss2.hashCode());
        assertNotSame(ss4.hashCode(), ss6.hashCode());        
    }

    @Test
    public void testClone() throws Exception {        
        SectionSettings instance = new SectionSettings();
        instance.setApplyInversion(true);
        instance.setApplyRetrograde(false);
        
        SectionSettings newInstance = (SectionSettings) instance.clone();
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