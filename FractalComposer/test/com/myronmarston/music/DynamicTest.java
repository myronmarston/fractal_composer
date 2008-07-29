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

package com.myronmarston.music;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class DynamicTest {
        
    @Test
    public void getDynamicForMidiVolume() {
        testGetDynamicForMidiVolume(-10, 0, null);
        testGetDynamicForMidiVolume(1, 25, Dynamic.PPP);
        testGetDynamicForMidiVolume(26, 40, Dynamic.PP);
        testGetDynamicForMidiVolume(41, 55, Dynamic.P);
        testGetDynamicForMidiVolume(56, 70, Dynamic.MP);
        testGetDynamicForMidiVolume(71, 85, Dynamic.MF);
        testGetDynamicForMidiVolume(86, 100, Dynamic.F);
        testGetDynamicForMidiVolume(101, 115, Dynamic.FF);
        testGetDynamicForMidiVolume(116, 127, Dynamic.FFF);
        testGetDynamicForMidiVolume(128, 150, null);        
    }
    
    private static void testGetDynamicForMidiVolume(int begin, int end, Dynamic dynamic) {
        for (int i = begin; i <= end; i++) {
            assertEquals(dynamic, Dynamic.getDynamicForMidiVolume(i));
        }
    }

    @Test
    public void sortWorksProperly() throws Exception {
        List<Dynamic> dynamics = Arrays.asList(Dynamic.MP, Dynamic.FF, Dynamic.F, Dynamic.MP, Dynamic.PPP, Dynamic.MF);
        Collections.sort(dynamics);
                
        assertEquals(Dynamic.PPP, dynamics.get(0));
        assertEquals(Dynamic.MP, dynamics.get(1));
        assertEquals(Dynamic.MP, dynamics.get(2));
        assertEquals(Dynamic.MF, dynamics.get(3));
        assertEquals(Dynamic.F, dynamics.get(4));
        assertEquals(Dynamic.FF, dynamics.get(5));
    }
    
}