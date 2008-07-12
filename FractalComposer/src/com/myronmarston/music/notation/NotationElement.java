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

/**
 * Interface that represents an element of musical notation.
 * 
 * @author Myron
 */
public interface NotationElement {    
    
    /**
     * Gets the representation of this element in the string format used by
     * lilypond.
     * 
     * @return the lilypond representation of this element
     */
    public String toLilypondString();
    
    //TODO: provide support here to scale duration denominators.
    //TODO: use this to create GUIDO string as well
}
