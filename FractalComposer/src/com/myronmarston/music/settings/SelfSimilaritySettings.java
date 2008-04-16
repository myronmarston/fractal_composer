package com.myronmarston.music.settings;

import java.util.Observable;

/**
 * Specifies settings for what to apply self-similarity to.
 * 
 * @author Myron
 */
public class SelfSimilaritySettings extends Observable {
    private boolean applyToPitch;
    private boolean applyToRhythm;
    private boolean applyToVolume;
      
    /**
     * Default Constructor.  Initializes all fields to the value of false.
     */
    public SelfSimilaritySettings() {
        this(false, false, false);
    }    
    
    /**
     * Constructor.
     * 
     * @param applyToPitch whether or not to apply self-similarity to the 
     *        pitches
     * @param applyToRhythm whether or not to apply self-similarity to the 
     *        rhythm
     * @param applyToVolume whether or not to apply self-similarity to the 
     *        volume
     */
    public SelfSimilaritySettings(boolean applyToPitch, boolean applyToRhythm, boolean applyToVolume) {
        this.applyToPitch = applyToPitch;
        this.applyToRhythm = applyToRhythm;
        this.applyToVolume = applyToVolume;
    }
    
    /**
     * Gets whether or not to apply self-similarity to the pitch of the germ
     * notes.  For example, if true, a germ of G A B G would become 
     * G A B G, A B C A, B C D B, G A B G.
     * 
     * @return whether or not to apply self-similarity to the pitch
     */
    public boolean getApplyToPitch() {
        return applyToPitch;
    }
    
    /**
     * Sets whether or not to apply self-similarity to the pitch of the germ
     * notes.  For example, if true, a germ of G A B G would become 
     * G A B G, A B C A, B C D B, G A B G.
     * 
     * @param val whether or not to apply self-similarity to the pitch
     */
    public void setApplyToPitch(boolean val) {
        this.applyToPitch = val;     
        setChangedAndNotifyObservers();
    }

    
    /**
     * Gets whether or not to apply self-similarity to the rhythm of the germ.
     * For example, if true, a germ rhythm of 1/4 1/8 1/4 would become
     * 1/4 1/8 1/4, 1/8 1/16 1/8, 1/4 1/8 1/4.
     * 
     * @return whether or not to apply self-similarity to the pitch
     */
    public boolean getApplyToRhythm() {
        return applyToRhythm;
    }

    /**
     * Sets whether or not to apply self-similarity to the rhythm of the germ.
     * For example, if true, a germ rhythm of 1/4 1/8 1/4 would become
     * 1/4 1/8 1/4, 1/8 1/16 1/8, 1/4 1/8 1/4.
     * 
     * @param val whether or not to apply self-similarity to the pitch
     */
    public void setApplyToRhythm(boolean val) {
        this.applyToRhythm = val;
        setChangedAndNotifyObservers();
    }

    /**
     * Gets whether or not to apply self-similarity to the volume of the germ
     * notes.  For example, if true, a germ with a middle note accent would 
     * generate a middle section louder than the surrounding sections, with
     * the middle note of that section still louder.
     * 
     * @return whether or not to apply self-similarity to the volume of the germ
     *         notes
     */
    public boolean getApplyToVolume() {
        return applyToVolume;
    }

    /**
     * Sets whether or not to apply self-similarity to the volume of the germ
     * notes.  For example, if true, a germ with a middle note accent would 
     * generate a middle section louder than the surrounding sections, with
     * the middle note of that section still louder.
     * 
     * @param val whether or not to apply self-similarity to the volume of the 
     *        germ notes
     */
    public void setApplyToVolume(boolean val) {
        this.applyToVolume = val;
        setChangedAndNotifyObservers();
    }
    
    /**
     * Calls setChanged() and notifyObservers().  Call this to notify the 
     * observers in one step.
     */
    private void setChangedAndNotifyObservers() {
        this.setChanged();
        this.notifyObservers();
    }
}

