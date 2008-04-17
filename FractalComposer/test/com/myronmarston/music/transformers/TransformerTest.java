package com.myronmarston.music.transformers;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;

import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class TransformerTest {

    public TransformerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of transform method, of class OctaveTransformer.
     */
    @Test
    public void octaveTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(2, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(4, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(0, 5, 0, new Fraction(1, 1), 64));

        Transformer t = new OctaveTransformer(2);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 6, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(2, 6, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(4, 6, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(0, 7, 0, new Fraction(1, 1), 64));        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new OctaveTransformer(-3);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 1, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(2, 1, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(4, 1, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(0, 2, 0, new Fraction(1, 1), 64));
        
        t = new OctaveTransformer(0);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(0, 5, 0, new Fraction(1, 1), 64));
    }
    
    @Test
    public void transposeTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(2, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        
        Transformer t = new TransposeTransformer(4);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(5, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(6, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 1), 64));        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new TransposeTransformer(-2);
        expectedOutput.clear();
        expectedOutput.add(new Note(-2, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(-1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(-2, 4, 0, new Fraction(1, 1), 64));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
    
    @Test
    public void rhythmicDurationTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(2, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        
        Transformer t = new RhythmicDurationTransformer(new Fraction(1, 2));
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(2, 1), 64));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 1), 64));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(2, 1), 64));        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new RhythmicDurationTransformer(new Fraction(2, 1));
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 4), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 64));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 64));        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void rhythmicTransformerZeroScaleFactor() {
        // this should throw an exception...
        Transformer t = new RhythmicDurationTransformer(new Fraction(0, 1));
    }    
    
    @Test
    public void volumeTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 96));
        input.add(new Note(2, 4, 0, new Fraction(1, 2), 32));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 64));
        
        Transformer t = new VolumeTransformer(0.5d);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 80));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new VolumeTransformer(-0.5d);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 32));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 48));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 16));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 32));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void volumeTransformerBadScaleFactor() {
        // a scale factor < -1 or > 1 should throw an exception
        Transformer t = new VolumeTransformer(-2);
    }
    
    @Test
    public void selfSimilarityTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        Transformer t = new SelfSimilarityTransformer(true, false, false);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 1), 96));
        
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 1), 96));
        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new SelfSimilarityTransformer(true, true, false);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 96));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 64));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 112));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 96));
        
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 96));
        expectedOutput.add(new Note(3, 4, 0, new Fraction(1, 4), 64));
        expectedOutput.add(new Note(4, 4, 0, new Fraction(1, 4), 112));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 96));
        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new SelfSimilarityTransformer(false, true, true);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 4), 43));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 75));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 64));
        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 4), 97));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 4), 120));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 2), 112));
        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new SelfSimilarityTransformer(false, false, false);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));                
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
    
    @Test
    public void inversionTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        input.add(new Note(5, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 112));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        Transformer t = new InversionTransformer();
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(-5, 4, 0, new Fraction(1, 2), 64));
        expectedOutput.add(new Note(-2, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(-1, 4, 0, new Fraction(1, 2), 112));
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        //inverting an inversion should produce the original
        assertTransformerProducesExpectedOutput(t, expectedOutput, input);
    }
    
    @Test
    public void retrogradeTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        input.add(new Note(5, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 112));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        Transformer t = new RetrogradeTransformer();
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        expectedOutput.add(new Note(1, 4, 0, new Fraction(1, 2), 112));        
        expectedOutput.add(new Note(2, 4, 0, new Fraction(1, 2), 112));        
        expectedOutput.add(new Note(5, 4, 0, new Fraction(1, 2), 64));        
        expectedOutput.add(new Note(0, 4, 0, new Fraction(1, 1), 96));        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        //inverting an inversion should produce the original
        assertTransformerProducesExpectedOutput(t, expectedOutput, input);
    }
    
    @Test 
    public void copyTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        input.add(new Note(5, 4, 0, new Fraction(1, 2), 64));
        input.add(new Note(2, 4, 0, new Fraction(1, 2), 112));
        input.add(new Note(1, 4, 0, new Fraction(1, 2), 112));
        input.add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        Transformer t = new CopyTransformer();             
        assertTransformerProducesExpectedOutput(t, input, input);
    }
    
    protected void assertTransformerProducesExpectedOutput(Transformer t, NoteList input, NoteList expectedOutput) {
        NoteList result = t.transform(input);
        assertEquals(expectedOutput.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(expectedOutput.get(i), result.get(i));
        }                                
    }

}