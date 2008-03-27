import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;
import com.myronmarston.music.transformers.Transformer;
import com.myronmarston.music.transformers.OctaveTransformer;

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
    public void transform() {
        NoteList input = new NoteList();
        input.add(new Note(1, 4, 0, 1d, 64));
        input.add(new Note(3, 4, 0, 1d, 64));
        input.add(new Note(5, 4, 0, 1d, 64));
        input.add(new Note(1, 5, 0, 1d, 64));

        Transformer t = new OctaveTransformer(2);
        NoteList expectedOutput = new NoteList();
        expectedOutput.add(new Note(1, 6, 0, 1d, 64));
        expectedOutput.add(new Note(3, 6, 0, 1d, 64));
        expectedOutput.add(new Note(5, 6, 0, 1d, 64));
        expectedOutput.add(new Note(1, 7, 0, 1d, 64));        
        assertTransformerProducesExpectedOutput(t, input, expectedOutput);
        
        t = new OctaveTransformer(-3);
        expectedOutput.clear();
        expectedOutput.add(new Note(1, 1, 0, 1d, 64));
        expectedOutput.add(new Note(3, 1, 0, 1d, 64));
        expectedOutput.add(new Note(5, 1, 0, 1d, 64));
        expectedOutput.add(new Note(1, 2, 0, 1d, 64));
        
        t = new OctaveTransformer(0);
        expectedOutput.clear();
        expectedOutput.add(new Note(1, 4, 0, 1d, 64));
        expectedOutput.add(new Note(3, 4, 0, 1d, 64));
        expectedOutput.add(new Note(5, 4, 0, 1d, 64));
        expectedOutput.add(new Note(1, 5, 0, 1d, 64));
    }
    
    protected void assertTransformerProducesExpectedOutput(Transformer t, NoteList input, NoteList expectedOutput) {
        NoteList result = t.transform(input);
        assertEquals(expectedOutput.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(expectedOutput.get(i), result.get(i));
        }                                
    }

}