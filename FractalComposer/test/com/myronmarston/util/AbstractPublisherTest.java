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

package com.myronmarston.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class AbstractPublisherTest {
    public class TestPublisher extends AbstractPublisher {}
    public class TestSubscriber implements Subscriber {
        public int notificationCount = 0;
        
        public void publisherNotification(Publisher p, Object args) {
            notificationCount++;
        }        
        
        @Override
        public boolean equals(Object obj) {
            // we want to test that the removeSubscriber() method uses
            // reference equality instead of the equals() method, so we 
            // give it an implementation that will always return true if
            // the given object is a TestSubscriber...
            return (obj instanceof TestSubscriber);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            return hash;
        }
    }        
    
    @Test
    public void notifySubscribers() {
        TestSubscriber s1 = new TestSubscriber();
        TestSubscriber s2 = new TestSubscriber();        
        
        Publisher p = new TestPublisher();
        
        p.addSubscriber(s1);
        p.notifySubscribers(null);
        assertEquals(1, s1.notificationCount);
        assertEquals(0, s2.notificationCount);        
        
        p.addSubscriber(s2);
        p.notifySubscribers(null);
        p.notifySubscribers(null);
        assertEquals(3, s1.notificationCount);
        assertEquals(2, s2.notificationCount);        
    }
    
    @Test
    public void removeSubscriberUsesReferenceEquals() {
        TestSubscriber s1 = new TestSubscriber();
        TestSubscriber s2 = new TestSubscriber();                
        
        TestPublisher p = new TestPublisher();      
        assertEquals(0, p.getSubscribers().size());
        
        p.addSubscriber(s1);
        p.addSubscriber(s2);        
        assertEquals(2, p.getSubscribers().size());
                
        p.removeSubscriber(s2);
        assertEquals(1, p.getSubscribers().size());        
        assertTrue(p.getSubscribers().get(0) == s1);
        assertFalse(p.getSubscribers().get(0) == s2);
    }
    
    @Test
    public void testClone() {
        TestSubscriber s1 = new TestSubscriber();
        TestSubscriber s2 = new TestSubscriber();                
        
        TestPublisher p = new TestPublisher(); 
        p.addSubscriber(s1);
        p.addSubscriber(s2);
        
        TestPublisher clonedP = (TestPublisher) p.clone();        
        
        // our clone should have a seperate, empty list of subscribers...
        assertEquals(0, clonedP.getSubscribers().size());        
    }
    
}