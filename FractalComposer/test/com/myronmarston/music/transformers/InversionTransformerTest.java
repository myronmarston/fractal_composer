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
import com.myronmarston.util.Fraction;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.myronmarston.music.transformers.CopyTransformerTest.*;

/**
 *
 * @author Myron
 */
public class InversionTransformerTest {
    
    @Test
    public void inversionTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        input.add(new Note(5, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 112));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        input.add(Note.createRest(new Fraction(1, 2)));
        
        Transformer t = new InversionTransformer();
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(-5, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(-2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(-1, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        //inverting an inversion should produce the original
        assertTransformerProducesExpectedOutput(t, expectedOutput, input);
    }
    
    @Test
    public void inversionTransformerCrossOctaves() {
        // test that it works properly when the germ crosses octaves...
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(4, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(6, 3, 0, new Fraction(1, 2), 64));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        
        Transformer t = new InversionTransformer();
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(-4, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(-1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(-6, 5, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        //inverting an inversion should produce the original
        assertTransformerProducesExpectedOutput(t, expectedOutput, input);
        
        //test the germ when it crosses octaves up...
        input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(4, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(1, 5, 0, new Fraction(1, 2), 64));
        input.add(new Note(6, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(0, 5, 0, new Fraction(1, 1), 64));
        
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(-4, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(-1, 3, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(-6, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(0, 3, 0, new Fraction(1, 1), 64));
        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        //inverting an inversion should produce the original
        assertTransformerProducesExpectedOutput(t, expectedOutput, input);
    }
}