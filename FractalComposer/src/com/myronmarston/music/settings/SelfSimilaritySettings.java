package com.myronmarston.music.settings;


public class SelfSimilaritySettings {    
    private boolean applyToPitch;
    private boolean applyToRhythm;
    private boolean applyToVolume;
    
    public SelfSimilaritySettings () {
    }
    
    public SelfSimilaritySettings(boolean applyToPitch, boolean applyToRhythm, boolean applyToVolume) {
        this.applyToPitch = applyToPitch;
        this.applyToRhythm = applyToRhythm;
        this.applyToVolume = applyToVolume;
    }
    
    public boolean getApplyToPitch () {
        return applyToPitch;
    }

    public void setApplyToPitch (boolean val) {
        this.applyToPitch = val;
    }

    public boolean getApplyToRhythm () {
        return applyToRhythm;
    }

    public void setApplyToRhythm (boolean val) {
        this.applyToRhythm = val;
    }

    public boolean getApplyToVolume () {
        return applyToVolume;
    }

    public void setApplyToVolume (boolean val) {
        this.applyToVolume = val;
    }
}

