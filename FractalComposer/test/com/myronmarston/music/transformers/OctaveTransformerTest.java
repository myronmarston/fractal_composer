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
public class OctaveTransformerTest {

    @Test
    public void octaveTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(2, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(4, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(0, 5, 0, new Fraction(1, 1), 64));
        input.add(Note.createRest(new Fraction(1, 1)));

        Transformer t = new OctaveTransformer(2);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 6, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(2, 6, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(4, 6, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(0, 7, 0, new Fraction(1, 1), 64));   
        expectedOutput.add(Note.createRest(new Fraction(1, 1)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new OctaveTransformer(-3);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 1, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(2, 1, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(4, 1, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(0, 2, 0, new Fraction(1, 1), 64));
        expectedOutput.add(Note.createRest(new Fraction(1, 1)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new OctaveTransformer(0);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(0, 5, 0, new Fraction(1, 1), 64));
        expectedOutput.add(Note.createRest(new Fraction(1, 1)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
    
}