package com.myronmarston.music.transformers;

import com.myronmarston.music.NoteList;
import java.util.Collections;


/**
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
