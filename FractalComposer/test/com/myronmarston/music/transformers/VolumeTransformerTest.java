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
public class VolumeTransformerTest {
    
    @Test
    public void volumeTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 96));
        input.add(new Note(2, 4, 0, new Fraction(1, 2), 32));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        input.add(Note.createRest(new Fraction(1, 2)));
        
        Transformer t = new VolumeTransformer(0.5d);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 80));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new VolumeTransformer(-0.5d);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 32));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 48));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 16));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 32));
        expectedOutput.add(Note.createRest(new Fraction(1, 2)));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void volumeTransformerBadScaleFactor() {
        // a scale factor < -1 or > 1 should throw an exception
        Transformer t = new VolumeTransformer(-2);
    }
    
}
