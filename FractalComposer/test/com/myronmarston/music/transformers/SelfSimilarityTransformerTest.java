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
        
        Transformer t = new SelfSimilarityTransformer(true, false, false);
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
        
        t = new SelfSimilarityTransformer(true, true, false);
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
        
        t = new SelfSimilarityTransformer(false, true, true);
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
        
        t = new SelfSimilarityTransformer(false, false, false);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
    
}
