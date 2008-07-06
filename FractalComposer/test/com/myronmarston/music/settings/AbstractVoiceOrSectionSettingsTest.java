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

import com.myronmarston.util.*;
import com.myronmarston.music.*;
import com.myronmarston.music.scales.*;
import com.myronmarston.music.transformers.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class AbstractVoiceOrSectionSettingsTest implements Subscriber {
    // Each test method should have the line    
    // "if (!isInstanceOfSubclass()) return;"
    // as the first line of the method, so that the test methods
    // are not actually run when this abstract class is tested,
    // but instead only runs when a sub class is tested.
    
    private AbstractVoiceOrSectionSettings settings;
    private int subscriberNotificationCount = 0;
        
    public void setUp() throws Exception {
        settings = getSettingsInstance();
        settings.addSubscriber(this);
    }           
   
    public void publisherNotification(Publisher p, Object args) {
        assert p == this.settings : p;
        subscriberNotificationCount++;
    }
    
    protected AbstractVoiceOrSectionSettings getSettingsInstance() {
        throw new UnsupportedOperationException("This must be implemented on the subclass.");       
    }        
    
    protected void setDefaultSettingsValues() {
        throw new UnsupportedOperationException("This must be implemented on the subclass.");       
    }
    
    protected boolean isInstanceOfSubclass() {
        return (this.getClass() != AbstractVoiceOrSectionSettingsTest.class);
    }
            
    @Test
    public void setVolumeAdjustment() {                        
        if (!isInstanceOfSubclass()) return;
        
        // set our fields to a known state
        this.settings.setVolumeAdjustment(0.25d);
        this.subscriberNotificationCount = 0;
        
        this.settings.setVolumeAdjustment(0.5d);
        assertEquals(0.5d, this.settings.getVolumeAdjustment());
        assertEquals(1, this.subscriberNotificationCount);
        
        this.settings.setVolumeAdjustment(-0.5d);
        assertEquals(-0.5d, this.settings.getVolumeAdjustment());
        assertEquals(2, this.subscriberNotificationCount);          
    }
        
    @Test
    public void volumeAdjustmentValidRange() {        
        if (!isInstanceOfSubclass()) return;
        
        this.settings.setVolumeAdjustment(0d);
        try {
            this.settings.setVolumeAdjustment(-1d);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(0d, this.settings.getVolumeAdjustment());
        
        this.settings.setVolumeAdjustment(-0.999999d);
        assertEquals(-0.999999d, this.settings.getVolumeAdjustment());
        
        this.settings.setVolumeAdjustment(1d);
        assertEquals(1.0d, this.settings.getVolumeAdjustment());
        
        try {
            this.settings.setVolumeAdjustment(1.000001d);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(1d, this.settings.getVolumeAdjustment());        
    }
        
    @Test
    public void setScaleStepOffsetAdjustment() {
        if (!isInstanceOfSubclass()) return;
        
        // set our fields to a known state
        this.settings.setScaleStepOffset(0);
        this.subscriberNotificationCount = 0;
        
        this.settings.setScaleStepOffset(2);
        assertEquals(2, this.settings.getScaleStepOffset());
        assertEquals(1, this.subscriberNotificationCount);
        
        this.settings.setScaleStepOffset(-3);
        assertEquals(-3, this.settings.getScaleStepOffset());
        assertEquals(2, this.subscriberNotificationCount);          
    }    
    
    @Test
    public void scaleStepOffsetValidRange() {
        if (!this.isInstanceOfSubclass()) return;                
        
        this.settings.setScaleStepOffset(0);
        try {
            this.settings.setScaleStepOffset(-12);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(0, this.settings.getScaleStepOffset());
        
        this.settings.setScaleStepOffset(-11);
        assertEquals(-11, this.settings.getScaleStepOffset());
        
        this.settings.setScaleStepOffset(11);
        assertEquals(11, this.settings.getScaleStepOffset());
        
        try {
            this.settings.setScaleStepOffset(12);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(11, this.settings.getScaleStepOffset());        
    }
    
    @Test
    public void applyVolumeSettingToNoteList() throws Exception {
        if (!this.isInstanceOfSubclass()) return;                        
        this.setDefaultSettingsValues();
        
        Scale scale = new MajorScale(NoteName.C);        
        NoteList input = NoteList.parseNoteListString("G4 A4", scale);        
        this.settings.setVolumeAdjustment(0.5d);
        int expectedVolume = 99;
        
        NoteList expected = (NoteList) input.clone();
        expected.get(0).setVolume(expectedVolume);
        expected.get(1).setVolume(expectedVolume);
                
        NoteListTest.assertNoteListsEqual(expected, settings.applySettingsToNoteList(input, scale));

        this.settings.setVolumeAdjustment(-0.5d);
        expectedVolume = 35;
        expected.get(0).setVolume(expectedVolume);
        expected.get(1).setVolume(expectedVolume);
        NoteListTest.assertNoteListsEqual(expected, settings.applySettingsToNoteList(input, scale));                
    }
    
    @Test
    public void applyScaleStepOffsetSettingToNoteList() throws Exception {
        if (!this.isInstanceOfSubclass()) return;                                
                
        // try a diatonic scale...
        applyScaleStepOffsetSettingToNoteList_helper(new MajorScale(NoteName.C), 0, "C4 Bb4 F#4 D4", "C4 Bb4 F#4 D4");        
        applyScaleStepOffsetSettingToNoteList_helper(new MajorScale(NoteName.C), 1, "C4 Bb4 F#4 D4", "D4 Cb5 G#4 E4");        
        applyScaleStepOffsetSettingToNoteList_helper(new MajorScale(NoteName.C), 2, "C4 Bb4 F#4 D4", "E4 Db5 A#4 F4");        
        applyScaleStepOffsetSettingToNoteList_helper(new MajorScale(NoteName.C), 3, "C4 Bb4 F#4 D4", "F4 Eb5 B#4 G4");        
        applyScaleStepOffsetSettingToNoteList_helper(new MajorScale(NoteName.C), 4, "C4 Bb4 F#4 D4", "G4 Fb5 C#5 A4");        
        applyScaleStepOffsetSettingToNoteList_helper(new MajorScale(NoteName.C), 5, "C4 Bb4 F#4 D4", "A4 Gb5 D#5 B4");        
        applyScaleStepOffsetSettingToNoteList_helper(new MajorScale(NoteName.C), 6, "C4 Bb4 F#4 D4", "B4 Ab5 E#5 C5");        
        applyScaleStepOffsetSettingToNoteList_helper(new MajorScale(NoteName.C), 7, "C4 Bb4 F#4 D4", "C4 Bb4 F#4 D4");        
                
        // try a major pentatonic scale...
        applyScaleStepOffsetSettingToNoteList_helper(new MajorPentatonicScale(NoteName.C), 0, "C4 A4 D4 G4 E4", "C4 A4 D4 G4 E4");        
        applyScaleStepOffsetSettingToNoteList_helper(new MajorPentatonicScale(NoteName.C), 1, "C4 A4 D4 G4 E4", "D4 C5 E4 A4 G4");  
        applyScaleStepOffsetSettingToNoteList_helper(new MajorPentatonicScale(NoteName.C), 2, "C4 A4 D4 G4 E4", "E4 D5 G4 C5 A4");                        
        applyScaleStepOffsetSettingToNoteList_helper(new MajorPentatonicScale(NoteName.C), 3, "C4 A4 D4 G4 E4", "G4 E5 A4 D5 C5");                        
        applyScaleStepOffsetSettingToNoteList_helper(new MajorPentatonicScale(NoteName.C), 4, "C4 A4 D4 G4 E4", "A4 G5 C5 E5 D5");                        
        applyScaleStepOffsetSettingToNoteList_helper(new MajorPentatonicScale(NoteName.C), 5, "C4 A4 D4 G4 E4", "C4 A4 D4 G4 E4");                                
        
        // try a major pentatonic scale...
        applyScaleStepOffsetSettingToNoteList_helper(new MinorPentatonicScale(NoteName.A), 0, "A4 G5 C5 E5 D5", "A4 G5 C5 E5 D5");        
        applyScaleStepOffsetSettingToNoteList_helper(new MinorPentatonicScale(NoteName.A), 1, "A4 G5 C5 E5 D5", "C5 A5 D5 G5 E5");        
        applyScaleStepOffsetSettingToNoteList_helper(new MinorPentatonicScale(NoteName.A), 2, "A4 G5 C5 E5 D5", "D5 C6 E5 A5 G5");        
        applyScaleStepOffsetSettingToNoteList_helper(new MinorPentatonicScale(NoteName.A), 3, "A4 G5 C5 E5 D5", "E5 D6 G5 C6 A5");        
        applyScaleStepOffsetSettingToNoteList_helper(new MinorPentatonicScale(NoteName.A), 4, "A4 G5 C5 E5 D5", "G5 E6 A5 D6 C6");        
        applyScaleStepOffsetSettingToNoteList_helper(new MinorPentatonicScale(NoteName.A), 5, "A4 G5 C5 E5 D5", "A4 G5 C5 E5 D5");        
        
        // try a chromatic scale...
        applyScaleStepOffsetSettingToNoteList_helper(new ChromaticScale(), 0, "C4 E4 Bb4 F#4", "C4 E4 Bb4 F#4");
        applyScaleStepOffsetSettingToNoteList_helper(new ChromaticScale(), 1, "C4 E4 Bb4 F#4", "Db4 F4 Cb5 G4");        
        applyScaleStepOffsetSettingToNoteList_helper(new ChromaticScale(), 2, "C4 E4 Bb4 F#4", "D4 F#4 C5 G#4");        
        applyScaleStepOffsetSettingToNoteList_helper(new ChromaticScale(), 3, "C4 E4 Bb4 F#4", "Eb4 G4 Db5 A4");        
        applyScaleStepOffsetSettingToNoteList_helper(new ChromaticScale(), 4, "C4 E4 Bb4 F#4", "E4 G#4 D5 A#4");        
        applyScaleStepOffsetSettingToNoteList_helper(new ChromaticScale(), 5, "C4 E4 Bb4 F#4", "F4 A4 Eb5 B4");        
        applyScaleStepOffsetSettingToNoteList_helper(new ChromaticScale(), 6, "C4 E4 Bb4 F#4", "F#4 A#4 E5 B#4");        
        applyScaleStepOffsetSettingToNoteList_helper(new ChromaticScale(), 7, "C4 E4 Bb4 F#4", "G4 B4 F5 C#5");        
        applyScaleStepOffsetSettingToNoteList_helper(new ChromaticScale(), 8, "C4 E4 Bb4 F#4", "Ab4 C5 Gb5 D5");        
        applyScaleStepOffsetSettingToNoteList_helper(new ChromaticScale(), 9, "C4 E4 Bb4 F#4", "A4 C#5 G5 D#5");        
        applyScaleStepOffsetSettingToNoteList_helper(new ChromaticScale(), 10, "C4 E4 Bb4 F#4", "Bb4 D5 Ab5 E5");        
        applyScaleStepOffsetSettingToNoteList_helper(new ChromaticScale(), 11, "C4 E4 Bb4 F#4", "B4 D#5 A5 E#5");                
    }        
    
    private void applyScaleStepOffsetSettingToNoteList_helper(Scale scale, int offset, String input, String expected) throws Exception {
        assert offset >= 0 : offset;
        this.setDefaultSettingsValues();
        this.settings.setScaleStepOffset(offset);
        
        NoteList inputNL = NoteList.parseNoteListString(input, scale);
        NoteList expectedNL = NoteList.parseNoteListString(expected, scale);
        NoteListTest.assertNoteListsEqual(expectedNL, this.settings.applySettingsToNoteList(inputNL, scale));        
        
        // try it down an octave as well...
        offset -= scale.getScaleStepArray().length;
        
        if (offset <= -Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE) return; // we'll get an exception if we allow this...
        if (offset == 0) return; // no point in testing this
        
        this.settings.setScaleStepOffset(offset);        
        Transformer t = new OctaveTransformer(-1);
        expectedNL = t.transform(expectedNL);
        NoteListTest.assertNoteListsEqual(expectedNL, this.settings.applySettingsToNoteList(inputNL, scale));
    }
    
    @Test
    public void testGetReadOnlyCopy() throws Exception {
        if (!this.isInstanceOfSubclass()) return;                
        this.setDefaultSettingsValues();
        this.settings.setScaleStepOffset(3);
        this.settings.setVolumeAdjustment(0.5d);
        
        AbstractVoiceOrSectionSettings ros = this.settings.getReadOnlyCopy();
               
        try {
            ros.setScaleStepOffset(-2);            
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertEquals(3, ros.getScaleStepOffset()); 
        
        try {
            ros.setVolumeAdjustment(-0.33d);
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertEquals(0.5d, ros.getVolumeAdjustment());         
    }
    
    @Test
    public void testEqualsAndHashCode() throws Exception {
        if (!this.isInstanceOfSubclass()) return;                
        
        this.setDefaultSettingsValues();
        AbstractVoiceOrSectionSettings s1 = (AbstractVoiceOrSectionSettings) this.settings.clone();
        AbstractVoiceOrSectionSettings s2 = (AbstractVoiceOrSectionSettings) this.settings.clone();
        s1.setScaleStepOffset(3);
        s1.setVolumeAdjustment(0.5d);
        
        s2.setScaleStepOffset(s1.getScaleStepOffset());
        s2.setVolumeAdjustment(s1.getVolumeAdjustment());
        assertTrue(s1.equals(s2));
        assertTrue(s2.equals(s1));
        assertEquals(s1.hashCode(), s2.hashCode());
        
        s2.setScaleStepOffset(s2.getScaleStepOffset() - 1);
        assertFalse(s1.equals(s2));
        assertFalse(s2.equals(s1));
        assertFalse(s1.hashCode() == s2.hashCode());
        
        s2.setScaleStepOffset(s1.getScaleStepOffset());
        s2.setVolumeAdjustment(s2.getVolumeAdjustment() * 0.5d);
        assertFalse(s1.equals(s2));
        assertFalse(s2.equals(s1));
        assertFalse(s1.hashCode() == s2.hashCode());        
    }
}