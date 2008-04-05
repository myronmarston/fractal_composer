package com.myronmarston.music.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * The entire fractal piece is composed of a series of sections.  Each section
 * represents a group of bars of the piece of music, e.g., bars 8-15. 
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
    
    /**
     * Gets the duration for this entire section.  This will be the duration of
     * the longest voice section.
     * 
     * @return the duration of this entire section
     */
    public double getDuration() {
        ArrayList<Double> voiceSectionDurations = new ArrayList<Double>(this.getListOfOtherType().size());
        
        for (VoiceSection vs : this.getVoiceSections()) {
            voiceSectionDurations.add(vs.getVoiceSectionResult().getDuration());
        }
        
        return Collections.max(voiceSectionDurations);
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
