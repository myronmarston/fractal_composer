package com.myronmarston.music.settings;

import com.myronmarston.music.GermIsEmptyException;
import com.myronmarston.music.NoteList;
import com.myronmarston.util.Fraction;
import java.io.IOException;
import org.simpleframework.xml.*;
import java.util.*;


/**
 * The entire fractal piece is composed of a series of sections.  Each section
 * represents a group of bars of the piece of music, e.g., bars 8-15. 
 * 
 * @author Myron
 */
@Root
public class Section extends AbstractVoiceOrSection<Section, Voice> {    
    
    @Attribute
    private boolean applyInversion = false;
    
    @Attribute
    private boolean applyRetrograde = false;        
    
    /**
     * Constructor.
     * 
     * @param fractalPiece the FractalPiece this section is a part of
     * @param uniqueIndex the unique index for this section
     */
    protected Section(FractalPiece fractalPiece, int uniqueIndex) {
        super(fractalPiece, uniqueIndex);
    }     
    
    /**
     * Provided for xml deserialization.
     */
    private Section() {
        this(null, 0);
    }

    /**
     * Gets whether or not to apply inversion to this section.  This is the default 
     * for the voice sections in this section; they can individually override
     * this setting.
     * 
     * @return whether or not to apply inversion to this section
     */
    public boolean getApplyInversion() {
        return applyInversion;
    }

    /**
     * Sets whether or not to apply inversion to this section.  This is the default 
     * for the voice sections in this section; they can individually override
     * this setting.
     * 
     * @param applyInversion whether or not to apply inversion to this section     
     */
    public void setApplyInversion(boolean applyInversion) {
        this.applyInversion = applyInversion;
        clearVoiceSectionResults();
    }

    /**
     * Gets whether or not to apply retrograde to this section.  This is the default 
     * for the voice sections in this section; they can individually override
     * this setting.
     * 
     * @return whether or not to apply retrograde to this section     
     */
    public boolean getApplyRetrograde() {
        return applyRetrograde;
    }

    /**
     * Sets whether or not to apply retrograde to this section.  This is the default 
     * for the voice sections in this section; they can individually override
     * this setting.
     * 
     * @param applyRetrograde whether or not to apply retrograde to this section     
     */
    public void setApplyRetrograde(boolean applyRetrograde) {
        this.applyRetrograde = applyRetrograde;
        clearVoiceSectionResults();
    }        
    
    /**
     * Clears the cached voice section results.  Should be called whenever a 
     * section field that is used by the voice sections changes.
     */
    private void clearVoiceSectionResults() {
        for (VoiceSection vs : this.getVoiceSections()) {
            vs.clearVoiceSectionResult();
        }
    }
    
    /**
     * Gets the duration for this entire section.  This will be the duration of
     * the longest voice section.
     * 
     * @return the duration of this entire section
     */
    @SuppressWarnings("unchecked")
    public Fraction getDuration() {
        ArrayList<Fraction> voiceSectionDurations = new ArrayList<Fraction>(this.getListOfOtherType().size());
        
        for (VoiceSection vs : this.getVoiceSections()) {
            voiceSectionDurations.add(vs.getVoiceSectionResult().getDuration());
        }
        
        return Collections.max(voiceSectionDurations);
    }
    
    /**
     * Constructs a midi sequence for this section and saves it to a midi file.
     * 
     * @param fileName the filename to use
     * @throws java.io.IOException if there is a problem writing to the file 
     * @throws GermIsEmptyException if the germ is empty    
     */
    public void saveSectionResultToMidiFile(String fileName) throws IOException, GermIsEmptyException {
        List<NoteList> voiceSectionResults = new ArrayList<NoteList>(this.getVoiceSections().size());
        for (VoiceSection vs : this.getVoiceSections()) {
            voiceSectionResults.add(vs.getVoiceSectionResult());
        }
        
        this.getFractalPiece().saveNoteListsAsMidiFile(voiceSectionResults, fileName);
    }
    
    @Override
    protected VoiceOrSectionList<Section, Voice> getListOfMainType() {
        return this.getFractalPiece().getSections();
    }
    
    @Override
    protected VoiceOrSectionList<Voice, Section> getListOfOtherType() {
        return this.getFractalPiece().getVoices();
    }    

    @Override
    protected VoiceSectionHashMapKey getHashMapKeyForOtherTypeIndex(int index) {
        Voice indexedVoice = this.getFractalPiece().getVoices().get(index);
        return new VoiceSectionHashMapKey(indexedVoice, this);
    }

    @Override
    protected VoiceSectionHashMapKey getHashMapKeyForOtherTypeUniqueIndex(int uniqueIndex) {
        Voice indexedVoice = this.getFractalPiece().getVoices().getByUniqueIndex(uniqueIndex);
        return new VoiceSectionHashMapKey(indexedVoice, this);
    }        

    @Override
    protected VoiceSection instantiateVoiceSection(Voice vOrS) {
        return new VoiceSection(vOrS, this);
    }           
}
