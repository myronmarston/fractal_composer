package com.myronmarston.music.settings;

import java.util.List;

/**
 * The entire fractal piece is composed of a series of sections.  Each section
 * represents a group of bars of the piece of music, e.g., bars 8-15. The 
 * section is responsible to make sure each voice in this section is the same 
 * length.
 * 
 * @author Myron
 */
public class Section extends AbstractVoiceOrSection<Section, Voice> {    
    
    /**
     * Constructor.
     * 
     * @param fractalPiece the FractalPiece this section is a part of
     */
    protected Section(FractalPiece fractalPiece) {
        super(fractalPiece);
    }      
    
    @Override
    protected List<Section> getListOfMainType() {
        return this.getFractalPiece().getSections();
    }
    
    @Override
    protected List<Voice> getListOfOtherType() {
        return this.getFractalPiece().getVoices();
    }    

    @Override
    protected VoiceSectionHashMapKey getHashMapKeyForOtherTypeIndex(int index) {
        Voice indexedVoice = this.getFractalPiece().getVoices().get(index);
        return new VoiceSectionHashMapKey(indexedVoice, this);
    }

    @Override
    protected VoiceSection instantiateVoiceSection(Voice vOrS) {
        return new VoiceSection(vOrS, this);
    }   
}
