package com.myronmarston.music.transformers;

/**
 * Creates a copy of the input.  It uses the octave transformer and 
 * transforms it zero octaves.
 * 
 * @author Myron
 */
public class CopyTransformer extends OctaveTransformer {
    
    /**
     * Constructor.
     */
    public CopyTransformer() {
        super(0);
    }
}
