package com.myronmarston.music.settings;

import com.myronmarston.music.NoteList; 


/**
 * Represents the smallest unit of the fractal piece for which the user can
 * specify settings.  One of these exists for each combination of a Voice and
 * a Section.  
 * 
 * @author Myron
 */
public class VoiceSection {
    private SelfSimilaritySettings selfSimilaritySettings;
    private boolean rest;
    private boolean applyInversion;
    private boolean applyRetrograde;    
    private Voice voice;
    private Section section;

    /**
     * Constructor.
     * 
     * @param voice the voice this is a section of
     * @param section the section this VoiceSection plays with simultaneously
     */
    protected VoiceSection(Voice voice, Section section) {
        this.voice = voice;
        this.section = section;
        
        this.voice.getVoiceSections().voiceSectionCreated();
        this.section.getVoiceSections().voiceSectionCreated();
    }
    
    
    /**
     * The Section this VoiceSection plays with simultaneously.
     * 
     * @return the Section this VoiceSection plays with simultaneously
     */
    public Section getSection() {
        return section;
    }

    /**
     * The voice this is a section of.
     * 
     * @return the voice this is a section of
     */
    public Voice getVoice() {
        return voice;
    }        

    /**
     * Gets whether or not to invert the germ for this section before applying
     * self-similarity.
     * 
     * @return whether or not to invert the germ
     */
    public boolean getApplyInversion() {
        return applyInversion;
    }

    /**
     * Sets whether or not to invert the germ for this section before applying
     * self-similarity.
     * 
     * @param val whether or not to invert the germ
     */
    public void setApplyInversion(boolean val) {
        this.applyInversion = val;
    }
    
    /**
     * Gets whether or not to use the retrograde of the germ before applying
     * self-similarity.
     * 
     * @return whether or not to use the retrograde of the germ
     */
    public boolean getApplyRetrograde() {
        return applyRetrograde;
    }
    
    /**
     * Sets whether or not to use the retrograde of the germ before applying
     * self-similarity.
     * 
     * @param val whether or not to use the retrograde of the germ
     */
    public void setApplyRetrograde(boolean val) {
        this.applyRetrograde = val;
    }

    /**
     * Gets whether or not to make this VoiceSection one long rest.  This
     * overrides all other settings. 
     * 
     * @return whether or not to make this VoiceSection one long rest
     */
    public boolean getRest() {
        return rest;
    }
    
    /**
     * Sets whether or not to make this VoiceSection one long rest.  This
     * overrides all other settings. 
     * 
     * @param val whether or not to make this VoiceSection one long rest
     */
    public void setRest(boolean val) {
        this.rest = val;
    }

    /**
     * Gets the self-similarity settings to be used by this voice section.
     * Guaranteed to never be null.  
     * 
     * @return the self-similarity settings to be used by this voice section
     */
    public SelfSimilaritySettings getSelfSimilaritySettings() {
        if (selfSimilaritySettings == null) selfSimilaritySettings = new SelfSimilaritySettings();
        return selfSimilaritySettings;
    }
}

