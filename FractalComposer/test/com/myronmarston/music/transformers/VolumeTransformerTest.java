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
public class VolumeTransformerTest {
    
    @Test
    public void volumeTransformer() throws Exception {
        NoteList input = new NoteList();
        Scale scale = new MajorScale(NoteName.C);
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 64, scale, 0));
        input.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 96, scale, 0));
        input.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 32, scale, 0));
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 64, scale, 0));
        input.add(Note.createRest(new Fraction(1, 2)));
        
        Transformer t = new VolumeTransformer(0.5d);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 112, scale, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 80, scale, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, scale, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new VolumeTransformer(-0.5d);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 32, scale, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 48, scale, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 16, scale, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 32, scale, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void volumeTransformerBadScaleFactor() {
        // a scale factor <= -1 or > 1 should throw an exception
        Transformer t = new VolumeTransformer(-1);
    }    
}
