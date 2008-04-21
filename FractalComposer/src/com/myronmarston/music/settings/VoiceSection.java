package com.myronmarston.music.settings;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;
import com.myronmarston.music.transformers.InversionTransformer;
import com.myronmarston.music.transformers.RetrogradeTransformer;
import com.myronmarston.music.transformers.SelfSimilarityTransformer;
import java.util.Observable;
import java.util.Observer;

import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;
        
/**
 * Represents the smallest unit of the fractal piece for which the user can
 * specify settings.  One of these exists for each combination of a Voice and
 * a Section.  
 * 
 * @author Myron
 */
public class VoiceSection implements Observer {
    private SelfSimilaritySettings selfSimilaritySettings;
    private boolean rest = false;
    private boolean applyInversion = false;
    private boolean applyRetrograde = false;    
    private Voice voice;
    private Section section;
    private NoteList voiceSectionResult;

    /**
     * Constructor.
     * 
     * @param voice the voice this is a section of
     * @param section the section this VoiceSection plays with simultaneously
     */
    protected VoiceSection(Voice voice, Section section) {
        this.voice = voice;
        this.section = section;    
        
        // observe our self similarity settings...
        selfSimilaritySettings = new SelfSimilaritySettings();
        selfSimilaritySettings.addObserver(this);
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
        if (val != this.applyInversion) clearVoiceSectionResult();
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
        if (val != this.applyRetrograde) clearVoiceSectionResult();
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
        if (val != this.rest) clearVoiceSectionResult();
        this.rest = val;
    }

    /**
     * Gets the self-similarity settings to be used by this voice section.
     * Guaranteed to never be null.  
     * 
     * @return the self-similarity settings to be used by this voice section
     */
    public SelfSimilaritySettings getSelfSimilaritySettings() {        
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
     * Gets the result of applying this VoiceSection's settings to the germ.
     * 
     * @return a NoteList containing the result of applying the settings to the 
     *         germ
     */
    public NoteList getVoiceSectionResult() {
        if (voiceSectionResult == null) voiceSectionResult = this.generateVoiceSectionResult();
        return voiceSectionResult;
    }
    
    /**
     * Returns the voice section result, set to a particular length by padding 
     * it with repeats and/or rests as appropriate.
     * 
     * @param length the length to set the voice section to
     * @return the voice section result, set to the given length
     */
    public NoteList getLengthenedVoiceSectionResult(Fraction length) {
        // get a clone of the result, so we can modify the clone rather than the original result.
        NoteList temp = (NoteList) this.getVoiceSectionResult().clone();
        Fraction originalVoiceSectionLength = temp.getDuration();
        if (originalVoiceSectionLength.compareTo(length) > 0) {
            throw new IllegalArgumentException(String.format("The voice section length (%f) is longer than the passed argument (%f).  The passed argument must be greater than or equal to the voice section length.", originalVoiceSectionLength.asDouble(), length.asDouble()));
        }
                
        // pad the length with additional copies of the entire voice section 
        // while there is space left...
        while (temp.getDuration().plus(originalVoiceSectionLength).compareTo(length) <= 0) {
            temp.addAll(this.getVoiceSectionResult());
        }
        
        // fill in the rest of the length with a rest...
        if (temp.getDuration().compareTo(length) < 0) {
            temp.add(Note.createRest(length.minus(temp.getDuration())));
        }
        
        assert temp.getDuration().equals(length) : temp;
        return temp;
    }
    
    /**
     * Sets the voiceSectionResult field to null.  Should be called anytime a field
     * that affects the voiceSectionResult changes. 
     */
    private void clearVoiceSectionResult() {
        this.voiceSectionResult = null;
    }
    
    /**
     * Generates the NoteList containing the result of applying this 
     * VoiceSection's settings to the germ.
     * 
     * @return a NoteList containing the result of applying the settings to the 
     *         germ
     */
    private NoteList generateVoiceSectionResult() {
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

    public void update(Observable o, Object arg) {
        // we expect the observable object to be our selfSimilaritySettings...
        assert o == this.getSelfSimilaritySettings() : o;
        
        // if the self similarity settings change, it affects our result, so clear it...
        this.clearVoiceSectionResult();
    }
}

