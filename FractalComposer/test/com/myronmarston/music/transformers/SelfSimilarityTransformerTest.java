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
import static com.myronmarston.music.transformers.TransformerTest.*;

/**
 *
 * @author Myron
 */
public class SelfSimilarityTransformerTest {
    
    @Test
    public void selfSimilarityTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        input.add(new Note(2, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        input.add(new Note(1, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        input.add(Note.createRest(new Fraction(1, 2)));
        input.get(0).setIsFirstNoteOfGermCopy(true);
        
        Transformer t = new SelfSimilarityTransformer(true, false, false, 1);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));        
        
        expectedOutput.add(new Note(2, 1, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(4, 2, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 1, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));        
        
        expectedOutput.add(new Note(1, 2, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 4, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 2, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));        
        
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));        
        
        expectedOutput.add(Note.createRest(new Fraction(7, 2)));                
        expectedOutput.setfirstNotesOfGermCopy(0, 5, 10, 15, 20);
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        // we tested above that the letter number and scale step are 
        // self-similarized independently.  For the rest of this test,
        // just make it the easy/normal case...
        input.get(0).setLetterNumber(0);
        input.get(1).setLetterNumber(1);
        input.get(2).setLetterNumber(2);
        input.get(3).setLetterNumber(0);
        t = new SelfSimilarityTransformer(true, true, false, 1);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
                
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 4), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));        
        
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(4, 4, 4, 0, new Fraction(1, 4), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));
        
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));        
        
        expectedOutput.add(Note.createRest(new Fraction(7, 4)));
        
        expectedOutput.setfirstNotesOfGermCopy(0, 5, 10, 15, 20);
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new SelfSimilarityTransformer(false, true, true, 1);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));                
        
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 4), 43, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 4), 75, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));        
        
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 4), 97, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 4), 120, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));                
        
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));        
        
        expectedOutput.add(Note.createRest(new Fraction(7, 4)));
        expectedOutput.setfirstNotesOfGermCopy(0, 5, 10, 15, 20);
        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new SelfSimilarityTransformer(false, false, false, 1);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        expectedOutput.get(0).setIsFirstNoteOfGermCopy(true);
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
    
    @Test
    public void multipleIterations() {
        NoteList input = new NoteList();
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        input.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        input.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        input.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        input.add(Note.createRest(new Fraction(1, 2)));
        input.get(0).setIsFirstNoteOfGermCopy(true);
        
        Transformer t = new SelfSimilarityTransformer(true, true, true, 2);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));        
        
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 4), 43, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 75, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));        
        
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 97, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(4, 4, 4, 0, new Fraction(1, 4), 120, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));        
        
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));           
        
        expectedOutput.add(Note.createRest(new Fraction(7, 4)));        
        
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 4), 43, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 75, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));        
                
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 4), 43, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 8), 29, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(4, 4, 4, 0, new Fraction(1, 8), 50, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 4), 43, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 8)));        
        
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 75, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(4, 4, 4, 0, new Fraction(1, 8), 65, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(5, 5, 4, 0, new Fraction(1, 8), 80, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 75, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 8)));       
        
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 4), 43, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 75, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));                
        
        expectedOutput.add(Note.createRest(new Fraction(7, 8)));                                
        
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 97, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(4, 4, 4, 0, new Fraction(1, 4), 120, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));        
        
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 97, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(4, 4, 4, 0, new Fraction(1, 8), 86, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(5, 5, 4, 0, new Fraction(1, 8), 102, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 97, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 8)));        
        
        expectedOutput.add(new Note(4, 4, 4, 0, new Fraction(1, 4), 120, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(5, 5, 4, 0, new Fraction(1, 8), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(6, 6, 4, 0, new Fraction(1, 8), 124, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(4, 4, 4, 0, new Fraction(1, 4), 120, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 8)));        
        
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 97, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(4, 4, 4, 0, new Fraction(1, 4), 120, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));         
        
        expectedOutput.add(Note.createRest(new Fraction(7, 8)));                 
        
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));        
        
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 4), 43, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 75, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));        
        
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(3, 3, 4, 0, new Fraction(1, 4), 97, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(4, 4, 4, 0, new Fraction(1, 4), 120, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 4)));        
        
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(1, 1, 4, 0, new Fraction(1, 2), 64, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(2, 2, 4, 0, new Fraction(1, 2), 112, Scale.DEFAULT, 0));
        expectedOutput.add(new Note(0, 0, 4, 0, new Fraction(1, 1), 96, Scale.DEFAULT, 0));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));         
        
        expectedOutput.add(Note.createRest(new Fraction(7, 4)));        
        expectedOutput.add(Note.createRest(new Fraction(49, 8)));        
        
        expectedOutput.setfirstNotesOfGermCopy(0, 5, 10, 15, 20, 21, 26, 31, 36, 41, 42, 47, 52, 57, 62, 63, 68, 73, 78, 83, 84);
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
            
    
}
