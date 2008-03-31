import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;
import com.myronmarston.music.transformers.*;

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
        input.add(new Note(0, 4, 0, 1d, 64));
        input.add(new Note(2, 4, 0, 1d, 64));
        input.add(new Note(4, 4, 0, 1d, 64));
        input.add(new Note(0, 5, 0, 1d, 64));

        Transformer t = new OctaveTransformer(2);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 6, 0, 1d, 64));
        expectedOutput.add(new Note(2, 6, 0, 1d, 64));
        expectedOutput.add(new Note(4, 6, 0, 1d, 64));
        expectedOutput.add(new Note(0, 7, 0, 1d, 64));        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new OctaveTransformer(-3);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 1, 0, 1d, 64));
        expectedOutput.add(new Note(2, 1, 0, 1d, 64));
        expectedOutput.add(new Note(4, 1, 0, 1d, 64));
        expectedOutput.add(new Note(0, 2, 0, 1d, 64));
        
        t = new OctaveTransformer(0);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, 1d, 64));
        expectedOutput.add(new Note(2, 4, 0, 1d, 64));
        expectedOutput.add(new Note(4, 4, 0, 1d, 64));
        expectedOutput.add(new Note(0, 5, 0, 1d, 64));
    }
    
    @Test
    public void transposeTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, 1d, 64));
        input.add(new Note(1, 4, 0, 0.5d, 64));
        input.add(new Note(2, 4, 0, 0.5d, 64));
        input.add(new Note(0, 4, 0, 1d, 64));
        
        Transformer t = new TransposeTransformer(4);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(4, 4, 0, 1d, 64));
        expectedOutput.add(new Note(5, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(6, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(4, 4, 0, 1d, 64));        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new TransposeTransformer(-2);
        expectedOutput.clear();
        expectedOutput.add(new Note(-2, 4, 0, 1d, 64));
        expectedOutput.add(new Note(-1, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(0, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(-2, 4, 0, 1d, 64));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
    
    @Test
    public void rhythmicDurationTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, 1d, 64));
        input.add(new Note(1, 4, 0, 0.5d, 64));
        input.add(new Note(2, 4, 0, 0.5d, 64));
        input.add(new Note(0, 4, 0, 1d, 64));
        
        Transformer t = new RhythmicDurationTransformer(2);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, 2d, 64));
        expectedOutput.add(new Note(1, 4, 0, 1d, 64));
        expectedOutput.add(new Note(2, 4, 0, 1d, 64));
        expectedOutput.add(new Note(0, 4, 0, 2d, 64));        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new RhythmicDurationTransformer(0.5d);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(1, 4, 0, 0.25d, 64));
        expectedOutput.add(new Note(2, 4, 0, 0.25d, 64));
        expectedOutput.add(new Note(0, 4, 0, 0.5d, 64));        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
    
    @Test
    public void volumeTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, 1d, 64));
        input.add(new Note(1, 4, 0, 0.5d, 96));
        input.add(new Note(2, 4, 0, 0.5d, 32));
        input.add(new Note(0, 4, 0, 1d, 64));
        
        Transformer t = new VolumeTransformer(0.5d);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 80));
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new VolumeTransformer(-0.5d);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, 1d, 32));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 48));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 16));
        expectedOutput.add(new Note(0, 4, 0, 1d, 32));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);        
    }
    
    @Test
    public void selfSimilarityTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, 1d, 96));
        input.add(new Note(1, 4, 0, 0.5d, 64));
        input.add(new Note(2, 4, 0, 0.5d, 112));
        input.add(new Note(0, 4, 0, 1d, 96));
        
        Transformer t = new SelfSimilarityTransformer(true, false, false);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        
        expectedOutput.add(new Note(1, 4, 0, 1d, 96));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(3, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(1, 4, 0, 1d, 96));
        
        expectedOutput.add(new Note(2, 4, 0, 1d, 96));
        expectedOutput.add(new Note(3, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(4, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(2, 4, 0, 1d, 96));
        
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new SelfSimilarityTransformer(true, true, false);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        
        expectedOutput.add(new Note(1, 4, 0, 0.5, 96));
        expectedOutput.add(new Note(2, 4, 0, 0.25d, 64));
        expectedOutput.add(new Note(3, 4, 0, 0.25d, 112));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 96));
        
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 96));
        expectedOutput.add(new Note(3, 4, 0, 0.25d, 64));
        expectedOutput.add(new Note(4, 4, 0, 0.25d, 112));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 96));
        
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new SelfSimilarityTransformer(false, true, true);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        
        expectedOutput.add(new Note(0, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(1, 4, 0, 0.25d, 43));
        expectedOutput.add(new Note(2, 4, 0, 0.25d, 75));
        expectedOutput.add(new Note(0, 4, 0, 0.5d, 64));
        
        expectedOutput.add(new Note(0, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(1, 4, 0, 0.25d, 97));
        expectedOutput.add(new Note(2, 4, 0, 0.25d, 120));
        expectedOutput.add(new Note(0, 4, 0, 0.5d, 112));
        
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new SelfSimilarityTransformer(false, false, false);
        expectedOutput.clear();
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));   
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
    }
    
    @Test
    public void inversionTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, 1d, 96));
        input.add(new Note(5, 4, 0, 0.5d, 64));
        input.add(new Note(2, 4, 0, 0.5d, 112));
        input.add(new Note(1, 4, 0, 0.5d, 112));
        input.add(new Note(0, 4, 0, 1d, 96));
        
        Transformer t = new InversionTransformer();
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        expectedOutput.add(new Note(-5, 4, 0, 0.5d, 64));
        expectedOutput.add(new Note(-2, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(-1, 4, 0, 0.5d, 112));
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        //inverting an inversion should produce the original
        assertTransformerProducesExpectedOutput(t, expectedOutput, input);
    }
    
    @Test
    public void retrogradeTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, 1d, 96));
        input.add(new Note(5, 4, 0, 0.5d, 64));
        input.add(new Note(2, 4, 0, 0.5d, 112));
        input.add(new Note(1, 4, 0, 0.5d, 112));
        input.add(new Note(0, 4, 0, 1d, 96));
        
        Transformer t = new RetrogradeTransformer();
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));
        expectedOutput.add(new Note(1, 4, 0, 0.5d, 112));        
        expectedOutput.add(new Note(2, 4, 0, 0.5d, 112));        
        expectedOutput.add(new Note(5, 4, 0, 0.5d, 64));        
        expectedOutput.add(new Note(0, 4, 0, 1d, 96));        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        //inverting an inversion should produce the original
        assertTransformerProducesExpectedOutput(t, expectedOutput, input);
    }
    
    @Test 
    public void copyTransformer() {
        NoteList input = new NoteList();
        input.add(new Note(0, 4, 0, 1d, 96));
        input.add(new Note(5, 4, 0, 0.5d, 64));
        input.add(new Note(2, 4, 0, 0.5d, 112));
        input.add(new Note(1, 4, 0, 0.5d, 112));
        input.add(new Note(0, 4, 0, 1d, 96));
        
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