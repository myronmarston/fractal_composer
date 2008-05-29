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

package com.myronmarston.music.transformers;

import com.myronmarston.music.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class TransformerTest {
  
    @Test
    public void emptyTest() {
        // This test is just present so that we don't get a failure error for
        // not having any tests.
        // We want to have this class to store the 
        // assertTransformerProducesExpectedOutput method
        assertTrue(true);
    }
    
    // this is used by all the transformer tests; we just put it here because it's an easy place to put it
    public static void assertTransformerProducesExpectedOutput(Transformer t, NoteList input, NoteList expectedOutput) {
        NoteList result = t.transform(input);
        assertEquals(expectedOutput.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(expectedOutput.get(i), result.get(i));
        }                                
    }    
}
