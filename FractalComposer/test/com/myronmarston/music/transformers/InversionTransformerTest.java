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
public class InversionTransformerTest {
    
    @Test
    public void inversionTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, -1));
        input.add(new Note(3, 5, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        input.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 2));
        input.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        input.add(new Note(-1, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, -2));
        input.add(Note.createRest(new Fraction(1, 2)));
        
        Transformer t = new InversionTransformer();
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 1));
        expectedOutput.add(new Note(-3, -5, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(-2, -2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, -2));
        expectedOutput.add(new Note(-1, -1, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 2));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        //inverting an inversion should produce the original
        assertTransformerProducesExpectedOutput(t, expectedOutput, input);
    }
    
    @Test
    public void inversionTransformerCrossOctaves() {        
        // test that it works properly when the germ crosses octaves...
        NoteList input = new NoteList();
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0));
        input.add(new Note(4, 4, 4, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0));
        input.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        input.add(new Note(6, 6, 3, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0));
        
        Transformer t = new InversionTransformer();
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(-4, -4, 4, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(-1, -1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(-6, -6, 5, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0));
        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        //inverting an inversion should produce the original
        assertTransformerProducesExpectedOutput(t, expectedOutput, input);
        
        //test the germ when it crosses octaves up...
        input = new NoteList();
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0));
        input.add(new Note(4, 4, 4, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0));
        input.add(new Note(1, 1, 5, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        input.add(new Note(6, 6, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        input.add(new Note(0, 0, 5, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0));
        
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(-4, -4, 4, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(-1, -1, 3, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(-6, -6, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 3, 0, new Fraction(1, 1), 64, Scale.DEFAULT, 0));
        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        //inverting an inversion should produce the original
        assertTransformerProducesExpectedOutput(t, expectedOutput, input);
    }
    
    @Test
    public void firstNoteNotTonic() throws Exception {
        // originally my inversion code worked properly if the first note is the tonic, 
        // but not in other cases--this is a test for that.
        
        NoteList input = NoteList.parseNoteListString("G4 E4", new MajorScale(NoteName.C));
        NoteList expectedOutput = NoteList.parseNoteListString("G4 B4", new MajorScale(NoteName.C));
        
        Transformer t = new InversionTransformer();
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        //inverting an inversion should produce the original
        assertTransformerProducesExpectedOutput(t, expectedOutput, input);
    }
}
