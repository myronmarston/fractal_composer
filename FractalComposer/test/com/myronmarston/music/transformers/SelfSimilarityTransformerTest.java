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
public class SelfSimilarityTransformerTest {
    
    @Test
    public void selfSimilarityTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        input.add(Note.createRest(new Fraction(1, 2)));
        
        Transformer t = new SelfSimilarityTransformer(true, false, false, 1);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        
        expectedOutput.add(Note.createRest(new Fraction(7, 2)));        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new SelfSimilarityTransformer(true, true, false, 1);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 96));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 64));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 112));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));
        
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 96));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 64));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 4), 112));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));
        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        
        expectedOutput.add(Note.createRest(new Fraction(7, 4)));
        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new SelfSimilarityTransformer(false, true, true, 1);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 4), 43));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 75));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));
        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 4), 97));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 120));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));
        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        
        expectedOutput.add(Note.createRest(new Fraction(7, 4)));
        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new SelfSimilarityTransformer(false, false, false, 1);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
    
    @Test
    public void multipleLevels() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        input.add(Note.createRest(new Fraction(1, 2)));
        
        Transformer t = new SelfSimilarityTransformer(true, true, true, 2);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 43));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 75));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));
        
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 97));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 4), 120));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));
        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));   
        
        expectedOutput.add(Note.createRest(new Fraction(7, 4)));
        
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 43));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 75));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));
                
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 43));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 8), 29));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 8), 50));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 43));
        expectedOutput.add(Note.createRest(new Fraction(1, 8)));
        
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 75));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 8), 65));
        expectedOutput.add(new Note(5, 4, 0, new Fraction(1, 8), 80));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 75));
        expectedOutput.add(Note.createRest(new Fraction(1, 8)));
        
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 43));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 75));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));        
        expectedOutput.add(Note.createRest(new Fraction(7, 8)));                
        
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 97));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 4), 120));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));
        
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 97));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 8), 86));
        expectedOutput.add(new Note(5, 4, 0, new Fraction(1, 8), 102));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 97));
        expectedOutput.add(Note.createRest(new Fraction(1, 8)));
        
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 4), 120));
        expectedOutput.add(new Note(5, 4, 0, new Fraction(1, 8), 112));
        expectedOutput.add(new Note(6, 4, 0, new Fraction(1, 8), 124));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 4), 120));
        expectedOutput.add(Note.createRest(new Fraction(1, 8)));
        
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 97));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 4), 120));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));        
        expectedOutput.add(Note.createRest(new Fraction(7, 8))); 
        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 43));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 75));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));
        
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 97));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 4), 120));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));
        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));        
        expectedOutput.add(Note.createRest(new Fraction(7, 4)));
        expectedOutput.add(Note.createRest(new Fraction(49, 8)));
        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
            
    
}
