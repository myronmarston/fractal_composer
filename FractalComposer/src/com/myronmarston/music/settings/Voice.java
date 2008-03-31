package com.myronmarston.music.settings;

import java.util.List;

/**
 * A FractalPiece is composed of several voices.  A Voice is analogous to 
 * a contrapuntal voice in music theory.  It is composed of a series of 
 * sections, each of which is composed of a series of notes and or/rests, 
 * played consecutively.  One voice will never have multiple notes
 * sounded simultaneously.
 * 
 * @author Myron
 */
public class Voice extends AbstractVoiceOrSection {
    private int octaveAdjustment;
    private double speedScaleFactor;    
    
    /**
     * Constructor.
     * 
     * @param fractalPiece the FractalPiece this voice is a part of
     */
    protected Voice(FractalPiece fractalPiece) {
        super(fractalPiece);
    }    
        
    /**
     * Gets how many octaves to adjust the germ for use by this voice.
     * 
     * @return number of octaves to adjust
     */
    public int getOctaveAdjustment() {
        return octaveAdjustment;
    }

    
    /**
     * Sets how many octaves to adjust the germ for use by this voice.
     * 
     * @param val number of octaves to adjust
     */
    public void setOctaveAdjustment(int val) {
        this.octaveAdjustment = val;
    }

    /**
     * Gets the speed scale factor to apply to the germ for use by this voice.
     * 
     * @return the speed scale factor
     */
    public double getSpeedScaleFactor() {
        return speedScaleFactor;
    }

    
    /**
     * Sets the speed scale factor to apply to the germ for use by this voice.
     * 
     * @param val the speed scale factor
     */
    public void setSpeedScaleFactor(double val) {
        this.speedScaleFactor = val;
    }
    
    @Override
    public List getListOfIntersectingType() {
        return this.getFractalPiece().getSections();
    }

    @Override
    public VoiceSectionHashMapKey getHashMapKeyForIntersectingTypeIndex(int index) {
        Section indexedSection = this.getFractalPiece().getSections().get(index);
        return new VoiceSectionHashMapKey(this, indexedSection);
    }
}

