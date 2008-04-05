package com.myronmarston.music.settings;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;
import com.myronmarston.music.transformers.InversionTransformer;
import com.myronmarston.music.transformers.RetrogradeTransformer;
import com.myronmarston.music.transformers.SelfSimilarityTransformer;
        
/**
 * Represents the smallest unit of the fractal piece for which the user can
 * specify settings.  One of these exists for each combination of a Voice and
 * a Section.  
 * 
 * @author Myron
 */
public class VoiceSection {
    private SelfSimilaritySettings selfSimilaritySettings;
    private boolean rest = false;
    private boolean applyInversion = false;
    private boolean applyRetrograde = false;    
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
    
    /**
     * Creates a hash map key using the Voice and Section for this VoiceSection.
     * @return the hash map key
     */
    protected VoiceSectionHashMapKey createHashMapKey() {
        return new VoiceSectionHashMapKey(this.getVoice(), this.getSection());
    }
    
    /**
     * Applies this VoiceSection's settings to the germ.
     * 
     * @return a NoteList containing the result of applying the settings to the 
     *         germ
     */
    public NoteList getVoiceSectionResult() {        
        NoteList temp = this.getVoice().getModifiedGerm();
        
        if (this.getRest()) {
            // create a note list of a single rest, the duration of the germ            
            NoteList restResult = new NoteList();
            restResult.add(Note.createRest(temp.getDuration()));            
            return restResult;
        } 
        
        if (this.getApplyInversion()) {
            InversionTransformer iT = new InversionTransformer();
            temp = iT.transform(temp);
        }
        
        if (this.getApplyRetrograde()) {
            RetrogradeTransformer rT = new RetrogradeTransformer();
            temp = rT.transform(temp);
        }
               
        SelfSimilarityTransformer ssT = new SelfSimilarityTransformer(this.getSelfSimilaritySettings());
        return ssT.transform(temp);                        
    }
}

