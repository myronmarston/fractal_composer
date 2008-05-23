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
