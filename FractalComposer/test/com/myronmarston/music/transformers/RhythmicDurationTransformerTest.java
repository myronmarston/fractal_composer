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
public class RhythmicDurationTransformerTest {
       
    @Test
    public void rhythmicDurationTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(2, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        input.add(Note.createRest(new Fraction(1, 1)));
        
        Transformer t = new RhythmicDurationTransformer(new Fraction(1, 2));
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(2, 1), 64));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(2, 1), 64));   
        expectedOutput.add(Note.createRest(new Fraction(2, 1)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new RhythmicDurationTransformer(new Fraction(2, 1));
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 4), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 64));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void rhythmicTransformerZeroScaleFactor() {
        // this should throw an exception...
        Transformer t = new RhythmicDurationTransformer(new Fraction(0, 1));
    }    
    
}
