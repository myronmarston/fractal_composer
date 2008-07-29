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

package com.myronmarston.music.notation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class AbstractNotationElementTest {
    public class SupportsScaling extends AbstractNotationElement {
        public boolean supportsDurationScaling() {
            return true;
        }

        public String toGuidoString() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String toLilypondString() {
            throw new UnsupportedOperationException("Not supported yet.");
        }        
    }
    
    public class DoesNotSupportScaling extends AbstractNotationElement {
        public boolean supportsDurationScaling() {
            return false;
        }

        public String toGuidoString() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String toLilypondString() {
            throw new UnsupportedOperationException("Not supported yet.");
        }                 
    }
    
    private SupportsScaling supports = new SupportsScaling();
    private DoesNotSupportScaling doesNotSupport = new DoesNotSupportScaling();
        
    @Test(expected=UnsupportedOperationException.class)
    public void testGetLargestDurationDenominator_supports() {
        supports.getLargestDurationDenominator();
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetLargestDurationDenominator_doesNotSupport() {
        doesNotSupport.getLargestDurationDenominator();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testScaleDurations_supports() {
        supports.scaleDurations(2L);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testScaleDurations_doesNotSupport() {
        doesNotSupport.scaleDurations(2L);
    }
    
    @Test
    public void getNotationNotes() {
        assertEquals(0, supports.getNotationNotes().size());
    }

}