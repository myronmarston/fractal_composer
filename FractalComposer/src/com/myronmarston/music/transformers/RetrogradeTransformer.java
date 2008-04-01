package com.myronmarston.music.transformers;

import com.myronmarston.music.NoteList;
import java.util.Collections;


/**
 * Transformer that applies retrograde to the given NoteList.
 * Example: C5 G5 D5 C5 -> C5 D5 G5 C5
 * 
 * @author Myron
 */
public class RetrogradeTransformer implements Transformer {
        
    public NoteList transform(NoteList input) {
        // make a copy to reverse...
        CopyTransformer copyTrans = new CopyTransformer();
        NoteList output = copyTrans.transform(input);
        
        // reverse the copy, rather than the original input...
        Collections.reverse(output);        
        return output;
    }
}
