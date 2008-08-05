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

package com.myronmarston.music.scales;

import com.myronmarston.music.NoteName;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class TonalityTest {
    
    @Test
    public void testIsValidKeyName() {
        assertTrue(Tonality.Major.isValidKeyName(NoteName.A));
        assertFalse(Tonality.Major.isValidKeyName(NoteName.As));
        
        assertTrue(Tonality.Minor.isValidKeyName(NoteName.A));
        assertFalse(Tonality.Minor.isValidKeyName(NoteName.Db));
    }

    @Test
    public void getValidKeyNames() throws Exception {
        testGetValidKeyNames(Tonality.Major, 
            NoteName.Cb, NoteName.Gb, NoteName.Db, 
            NoteName.Ab, NoteName.Eb, NoteName.Bb, 
            NoteName.F, NoteName.C, NoteName.G, 
            NoteName.D, NoteName.A, NoteName.E,
            NoteName.B, NoteName.Fs, NoteName.Cs);        
        
        testGetValidKeyNames(Tonality.Dorian, 
            NoteName.Db, 
            NoteName.Ab, NoteName.Eb, NoteName.Bb, 
            NoteName.F, NoteName.C, NoteName.G, 
            NoteName.D, NoteName.A, NoteName.E,
            NoteName.B, NoteName.Fs, NoteName.Cs, NoteName.Gs, NoteName.Ds);        
        
        testGetValidKeyNames(Tonality.Phrygian,             
            NoteName.Eb, NoteName.Bb, 
            NoteName.F, NoteName.C, NoteName.G, 
            NoteName.D, NoteName.A, NoteName.E,
            NoteName.B, NoteName.Fs, NoteName.Cs, 
            NoteName.Gs, NoteName.Ds, NoteName.As, NoteName.Es);        
        
        testGetValidKeyNames(Tonality.Lydian, 
            NoteName.Fb, NoteName.Cb, NoteName.Gb, NoteName.Db, 
            NoteName.Ab, NoteName.Eb, NoteName.Bb, 
            NoteName.F, NoteName.C, NoteName.G, 
            NoteName.D, NoteName.A, NoteName.E,
            NoteName.B, NoteName.Fs);        
        
        testGetValidKeyNames(Tonality.Mixolydian, 
            NoteName.Gb, NoteName.Db, 
            NoteName.Ab, NoteName.Eb, NoteName.Bb, 
            NoteName.F, NoteName.C, NoteName.G, 
            NoteName.D, NoteName.A, NoteName.E,
            NoteName.B, NoteName.Fs, NoteName.Cs, NoteName.Gs);        
        
        testGetValidKeyNames(Tonality.Minor,             
            NoteName.Ab, NoteName.Eb, NoteName.Bb, 
            NoteName.F, NoteName.C, NoteName.G, 
            NoteName.D, NoteName.A, NoteName.E,
            NoteName.B, NoteName.Fs, NoteName.Cs, 
            NoteName.Gs, NoteName.Ds, NoteName.As);        
        
        testGetValidKeyNames(Tonality.Locrian,             
            NoteName.Bb, 
            NoteName.F, NoteName.C, NoteName.G, 
            NoteName.D, NoteName.A, NoteName.E,
            NoteName.B, NoteName.Fs, NoteName.Cs, 
            NoteName.Gs, NoteName.Ds, NoteName.As, NoteName.Es, NoteName.Bs);                
    }
    
    private static void testGetValidKeyNames(Tonality tonality, NoteName ... expectedValidKeyNames) {
        List<NoteName> keyNames = tonality.getValidKeyNames();
        assertEquals(expectedValidKeyNames.length, keyNames.size());
        for (int i = 0; i < keyNames.size(); i++) {
           assertEquals(expectedValidKeyNames[i], keyNames.get(i));
        }        
    }

    @Test
    public void testGetSharpsOrFlatsForKeyName() {
        // just test a few cases for Major and for Minor...
        assertEquals(Tonality.Major.getSharpsOrFlatsForKeyName(NoteName.G), 1);
        assertEquals(Tonality.Major.getSharpsOrFlatsForKeyName(NoteName.E), 4);
        assertEquals(Tonality.Major.getSharpsOrFlatsForKeyName(NoteName.Ab), -4);
        assertEquals(Tonality.Minor.getSharpsOrFlatsForKeyName(NoteName.A), 0);
        assertEquals(Tonality.Minor.getSharpsOrFlatsForKeyName(NoteName.G), -2);
        assertEquals(Tonality.Minor.getSharpsOrFlatsForKeyName(NoteName.B), 2);
    }

    @Test
    public void getDefaultKey() {
        assertEquals(NoteName.C, Tonality.Major.getDefaultKey());
        assertEquals(NoteName.A, Tonality.Minor.getDefaultKey());
    }
}