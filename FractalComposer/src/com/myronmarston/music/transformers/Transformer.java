package com.myronmarston.music.transformers;

import com.myronmarston.music.NoteList;

/**
 * Implementations of this interface are used to take a NoteList and transform 
 * it into a new NoteList, using some kind of musical operation.  hese will be 
 * combined in various ways to generate the fractal music.
 * 
 * @author Myron
 */
public interface Transformer {
    
    /**
     * Transforms the given NoteList to a new note list.
     * 
     * @param input the NoteList to transform
     * @return the transformed NoteList
     */
    NoteList transform(NoteList input);
}
