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

/**
 * Pre-defined volume levels based on musical dynamics.
 * 
 * @author Myron
 */
public enum Dynamic {
    PPP(10),
    PP(25),
    P(50),
    MP(60),
    MF(70),
    F(85),
    FF(100),
    FFF(120);
    
    private final int midiVolume;
    private static String regexPattern;

    private Dynamic(int midiVolume) {
        this.midiVolume = midiVolume;
    }
    
    /**
     * Gets the midi volume for this dynamic.
     * 
     * @return midi volume
     */
    public int getMidiVolume() {
        return this.midiVolume;
    }
    
    /**
     * Gets a list of possible dynamic values.
     * 
     * @return a string containing a list of possible dynamic values
     */
    public static String getDynamicExampleString() {
        StringBuilder str = new StringBuilder();
        Dynamic[] values = Dynamic.values(); // cache the array...
        
        for (int i = 0; i < values.length; i++) {
            str.append(values[i].toString());            
            
            if (i == values.length - 2) str.append(" or ");                
            else if (i < values.length - 2) str.append(", ");            
        }
        
        return str.toString();        
    }
    
    /**
     * Gets a regular expression pattern string based on this dynamic enum that 
     * can be used to parse a note string.
     * 
     * @return a regular expression pattern string
     */
    public static String getRegexPattern() {
        if (regexPattern == null) regexPattern = "(?:,(.*))?";        
        return regexPattern;
    }
}
