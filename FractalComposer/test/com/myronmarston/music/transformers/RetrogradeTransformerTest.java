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
import com.myronmarston.music.scales.*;
import com.myronmarston.util.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.myronmarston.music.transformers.TransformerTest.*;

/**
 *
 * @author Myron
 */
public class RetrogradeTransformerTest {
          
    @Test
    public void retrogradeTransformer() throws Exception {
        NoteList input = new NoteList();
        Scale scale = new MinorScale(NoteName.G);
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));
        input.add(new Note(5, 5, 4, 0, new Fraction(1, 2), 64, scale, 0));
        input.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, scale, 0));
        input.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 112, scale, 0));
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));
        input.add(Note.createRest(new Fraction(1, 2)));
        
        Transformer t = new RetrogradeTransformer();
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 112, scale, 0));        
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, scale, 0));        
        expectedOutput.add(new Note(5, 5, 4, 0, new Fraction(1, 2), 64, scale, 0));        
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        //inverting an inversion should produce the original
        assertTransformerProducesExpectedOutput(t, expectedOutput, input);
    }
    
}
