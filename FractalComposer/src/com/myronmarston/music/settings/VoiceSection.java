package com.myronmarston.music.settings;

import com.myronmarston.music.GermIsEmptyException;
import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;
import com.myronmarston.music.transformers.InversionTransformer;
import com.myronmarston.music.transformers.RetrogradeTransformer;
import com.myronmarston.music.transformers.SelfSimilarityTransformer;
import java.util.*;

import org.simpleframework.xml.*;

import com.myronmarston.util.Fraction;
import java.io.IOException;
        
/**
 * Represents the smallest unit of the fractal piece for which the user can
 * specify settings.  One of these exists for each combination of a Voice and
 * a Section.  
 * 
 * @author Myron
 */
@Root
public class VoiceSection implements Observer {
    @Element
    private SelfSimilaritySettings selfSimilaritySettings;
    
    @Attribute
    private boolean rest = false;
    
    @Attribute(required=false)
    private Boolean applyInversion;
        
    @Attribute(required=false)
    private Boolean applyRetrograde;
    
    @Element
    private Voice voice;
    
    @Element
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
     * Provided for xml deserialization.
     */
    private VoiceSection() {}
        
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
     * Gets the other voice or section--the one that is not passed as a
     * parameter to this method.
     * 
     * @param vOrS a voice or section
     * @return the other voice or section
     */
    public AbstractVoiceOrSection getOtherVoiceOrSection(AbstractVoiceOrSection vOrS) {        
        if (vOrS.getClass() == Voice.class) {
            return this.getSection();
        }
        else {
            assert vOrS.getClass() == Section.class : vOrS;
            return this.getVoice();
        }        
    }

    /**
     * Gets whether or not to invert the germ for this section before applying
     * self-similarity.  Can be null, which indicates that the section default
     * should be used instead.
     * 
     * @return whether or not to invert the germ
     */
    public Boolean getApplyInversion() {
        return applyInversion;
    }
    
    /**
     * Gets whether or not to invert the germ for this section before applying
     * self-similarity.  If the setting is null on this voice section, gets the
     * section default.
     *  
     * @return whether or not to invert the germ
     */
    protected boolean getGuarenteedApplyInversion() {
        if (this.getApplyInversion() == null) return this.getSection().getApplyInversion();
        return this.getApplyInversion();
    }        

    /**
     * Sets whether or not to invert the germ for this section before applying
     * self-similarity.  Can be null, which indicates that the section default
     * should be used instead.
     * 
     * @param val whether or not to invert the germ
     */
    public void setApplyInversion(Boolean val) {        
        if (!booleanEquals(val, this.applyInversion)) clearVoiceSectionResult();        
        this.applyInversion = val;
    }
    
    /**
     * Gets whether or not to use the retrograde of the germ before applying
     * self-similarity.  Can be null, which indicates that the section default
     * should be used instead.
     * 
     * @return whether or not to use the retrograde of the germ
     */
    public Boolean getApplyRetrograde() {        
        return applyRetrograde;
    }
    
    /**
     * Gets whether or not to apply retrograde to the germ for this section 
     * before applying self-similarity.  If the setting is null on this voice 
     * section, gets the section default.
     *  
     * @return whether or not to apply retrograde to the germ
     */
    protected boolean getGuarenteedApplyRetrograde() {
        if (this.getApplyRetrograde() == null) return this.getSection().getApplyRetrograde();
        return this.getApplyRetrograde();
    }
    
    /**
     * Sets whether or not to use the retrograde of the germ before applying
     * self-similarity.  Can be null, which indicates that the section default
     * should be used instead.
     * 
     * @param val whether or not to use the retrograde of the germ
     */
    public void setApplyRetrograde(Boolean val) {          
        if (!booleanEquals(val, this.applyRetrograde)) clearVoiceSectionResult();        
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
     * Constructs a midi sequence using this voice section and saves it to a
     * file.
     * 
     * @param fileName the filename to use
     * @throws java.io.IOException if there is a problem writing to the file
     * @throws GermIsEmptyException if the germ is empty     
     */
    public void saveVoiceSectionResultToMidiFile(String fileName) throws IOException, GermIsEmptyException {
        this.getVoice().getFractalPiece().saveNoteListsAsMidiFile(Arrays.asList(this.getVoiceSectionResult()), fileName);
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
                        
        if (temp.getDuration().compareTo(0) > 0) { // only do this if we have something...
            // pad the length with additional copies of the entire voice section 
            // while there is space left...
            while (temp.getDuration().plus(originalVoiceSectionLength).compareTo(length) <= 0) {
                temp.addAll(this.getVoiceSectionResult());
            }
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
    protected void clearVoiceSectionResult() {
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
            Fraction duration = temp.getDuration();
            if (duration.compareTo(0L) > 0) restResult.add(Note.createRest(duration));            
            return restResult;
        } 
        
        if (this.getGuarenteedApplyInversion()) {
            InversionTransformer iT = new InversionTransformer();
            temp = iT.transform(temp);
        }
        
        if (this.getGuarenteedApplyRetrograde()) {
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
    
    private boolean booleanEquals(Boolean b1, Boolean b2) {
        // if both are null, or both are the same reference, they are equal
        if (b1 == b2) return true;
        
        // otherwise, use the equals method, calling it on whichever object is non-null
        return (b1 == null ? b2.equals(b1) : b1.equals(b2));        
    }
}