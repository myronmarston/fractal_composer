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
public class RhythmicDurationTransformerTest {
       
    @Test
    public void rhythmicDurationTransformer() throws Exception{
        NoteList input = new NoteList();
        Scale scale = new HarmonicMinorScale(NoteName.Bb);
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 64, scale, 0));
        input.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, scale, 0));
        input.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 64, scale, 0));
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 64, scale, 0));
        input.add(Note.createRest(new Fraction(1, 1)));
        input.get(0).setIsFirstNoteOfGermCopy(true);
        
        Transformer t = new RhythmicDurationTransformer(new Fraction(1, 2));
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(2, 1), 64, scale, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 1), 64, scale, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 1), 64, scale, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(2, 1), 64, scale, 0));   
        expectedOutput.add(Note.createRest(new Fraction(2, 1)));
        expectedOutput.get(0).setIsFirstNoteOfGermCopy(true);
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new RhythmicDurationTransformer(new Fraction(2, 1));
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 2), 64, scale, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 4), 64, scale, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 4), 64, scale, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 2), 64, scale, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        expectedOutput.get(0).setIsFirstNoteOfGermCopy(true);
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void rhythmicTransformerZeroScaleFactor() {
        // this should throw an exception...
        Transformer t = new RhythmicDurationTransformer(new Fraction(0, 1));
    }    
    
}
