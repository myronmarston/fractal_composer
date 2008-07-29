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

import com.myronmarston.util.Publisher;
import com.myronmarston.util.Subscriber;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class SelfSimilaritySettingsTest implements Subscriber {

    private SelfSimilaritySettings settings; 
    private int subscriberNotificationCount = 0;

    public void publisherNotification(Publisher p, Object args) {
        assert p == settings : p;
        this.subscriberNotificationCount++;
    }   
    
    @Before
    public void setUp() throws Exception {
        settings = new SelfSimilaritySettings();
        settings.addSubscriber(this);
    }   
    
    @Test
    public void setApplyToPitch() {
        // set our fields to a known state
        this.settings.setApplyToPitch(false);
        this.subscriberNotificationCount = 0;
        
        this.settings.setApplyToPitch(true);
        assertEquals(true, this.settings.getApplyToPitch());
        assertEquals(1, this.subscriberNotificationCount);
        
        this.settings.setApplyToPitch(false);
        assertEquals(false, this.settings.getApplyToPitch());
        assertEquals(2, this.subscriberNotificationCount);
    }
    
    @Test
    public void setApplyToRhythm() {
        // set our fields to a known state
        this.settings.setApplyToRhythm(false);
        this.subscriberNotificationCount = 0;
        
        this.settings.setApplyToRhythm(true);
        assertEquals(true, this.settings.getApplyToRhythm());
        assertEquals(1, this.subscriberNotificationCount);
        
        this.settings.setApplyToRhythm(false);
        assertEquals(false, this.settings.getApplyToRhythm());
        assertEquals(2, this.subscriberNotificationCount);
    }
    
    @Test
    public void setApplyToVolume() {
        // set our fields to a known state
        this.settings.setApplyToVolume(false);
        this.subscriberNotificationCount = 0;
        
        this.settings.setApplyToVolume(true);
        assertEquals(true, this.settings.getApplyToVolume());
        assertEquals(1, this.subscriberNotificationCount);
        
        this.settings.setApplyToVolume(false);
        assertEquals(false, this.settings.getApplyToVolume());
        assertEquals(2, this.subscriberNotificationCount);                
    }
    
    @Test
    public void setSelfSimilarityIterations() {
        // set our fields to a known state
        this.settings.setSelfSimilarityIterations(1);
        this.subscriberNotificationCount = 0;
        
        this.settings.setSelfSimilarityIterations(3);
        assertEquals(3, this.settings.getSelfSimilarityIterations());
        assertEquals(1, this.subscriberNotificationCount);
        
        this.settings.setSelfSimilarityIterations(2);
        assertEquals(2, this.settings.getSelfSimilarityIterations());
        assertEquals(2, this.subscriberNotificationCount);                
    }
    
    @Test
    public void equalsAndHashCode() {
        SelfSimilaritySettings sss1 = new SelfSimilaritySettings(true, false, true, 2);
        SelfSimilaritySettings sss2 = new SelfSimilaritySettings(true, false, false, 2);
        SelfSimilaritySettings sss3 = new SelfSimilaritySettings(true, false, true, 3);        
        SelfSimilaritySettings sss4 = new SelfSimilaritySettings(false, false, true, 3);
        SelfSimilaritySettings sss5 = new SelfSimilaritySettings(true, false, true, 2);
        
        assertTrue(sss1.equals(sss5));
        assertFalse(sss1.equals(sss2));
        assertFalse(sss1.equals(sss3));
        assertFalse(sss1.equals(sss4));
        
        assertEquals(sss1.hashCode(), sss5.hashCode());
        assertNotSame(sss1.hashCode(), sss2.hashCode());
        assertNotSame(sss1.hashCode(), sss3.hashCode());
        assertNotSame(sss1.hashCode(), sss4.hashCode());        
    }

    @Test
    public void testClone() throws Exception {        
        SelfSimilaritySettings instance = new SelfSimilaritySettings(true, true, false, 6);
        
        SelfSimilaritySettings newInstance = instance.clone();
        assertTrue(instance != newInstance);
        assertTrue(instance.equals(newInstance));
        assertEquals(instance.hashCode(), newInstance.hashCode());        
    }       

    @Test
    public void testSelfSimilarityShouldBeAppliedToSomething() {        
        SelfSimilaritySettings sss = new SelfSimilaritySettings(false, false, false, 1);
        assertEquals(false, sss.selfSimilarityShouldBeAppliedToSomething());
        
        sss.setApplyToVolume(true);
        assertEquals(true, sss.selfSimilarityShouldBeAppliedToSomething());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void errorIfSelfSimilarityLevelsLessThanOne() {
        SelfSimilaritySettings sss = new SelfSimilaritySettings(false, false, false, 0);
    }
        
    @Test
    public void getReadOnlyCopy() throws Exception {
        SelfSimilaritySettings SSS = new SelfSimilaritySettings(false, false, false, 1);
        SelfSimilaritySettings roSSS = SSS.getReadOnlyCopy();
        
        testReadOnlySelfSimilaritySettings(roSSS);
    }
    
    public static void testReadOnlySelfSimilaritySettings(SelfSimilaritySettings roSSS) {
        try {
            roSSS.setApplyToPitch(true);
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertEquals(false, roSSS.getApplyToPitch());
        
        try {
            roSSS.setApplyToVolume(true);
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertEquals(false, roSSS.getApplyToVolume());
        
        try {
            roSSS.setApplyToRhythm(true);
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertEquals(false, roSSS.getApplyToRhythm());
        
        try {
            roSSS.setSelfSimilarityIterations(3);
            fail();
        } catch (UnsupportedOperationException ex) {}
        assertEquals(1, roSSS.getSelfSimilarityIterations());
    }
}