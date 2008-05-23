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
public class TransposeTransformerTest {
    
    @Test
    public void transposeTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(2, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        input.add(Note.createRest(new Fraction(1, 1)));
        
        Transformer t = new TransposeTransformer(4);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(5, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(6, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 1), 64));     
        expectedOutput.add(Note.createRest(new Fraction(1, 1)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new TransposeTransformer(-2);
        expectedOutput.clear();
        expectedOutput.add(new Note(-2, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(-1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(-2, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(Note.createRest(new Fraction(1, 1)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
    
}
