/*
 * Copyright 2008, Myron Marston <myron DOT marston AT gmail DOT com>
 *
 * This file is part of Fractal Composer.
 *
 * Fractal Composer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option any later version.
 *
 * Fractal Composer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Fractal Composer.  If not, see <http://www.gnu.org/licenses/>. 
 */

package com.myronmarston.music.settings;

import com.myronmarston.music.GermIsEmptyException;
import com.myronmarston.music.NoteList;
import com.myronmarston.util.Fraction;
import com.myronmarston.util.Publisher;
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
    
    @Element
    private final SectionSettings settings;
    
    /**
     * Constructor.
     * 
     * @param fractalPiece the FractalPiece this section is a part of
     * @param uniqueIndex the unique index for this section
     */
    protected Section(FractalPiece fractalPiece, int uniqueIndex) {
        super(fractalPiece, uniqueIndex);
        settings = new SectionSettings();
        settings.addSubscriber(this);
    }     
    
    /**
     * Provided for xml deserialization.
     */
    private Section() {
        this(null, 0);
    }

    /**
     * Gets the settings for this section.
     * 
     * @return the settings for this section
     */
    public SectionSettings getSettings() {
        return settings;
    } 
    
    /**
     * Updates the self-similarity settings of all voice sections.  This also
     * updates the useDefaultVoiceSettings property to false.
     * 
     * @param applyToPitch new value
     * @param applyToRhythm new value
     * @param applyToVolume new value
     */
    public void setSelfSimilaritySettingsOnAllVoiceSections(boolean applyToPitch, boolean applyToRhythm, boolean applyToVolume, int selfSimilarityIterations) {
        for (VoiceSection vs : this.getVoiceSections()) {
            vs.setUseDefaultVoiceSettings(false);
            SelfSimilaritySettings sss = vs.getVoiceSettings().getSelfSimilaritySettings();
            
            sss.setApplyToPitch(applyToPitch);
            sss.setApplyToRhythm(applyToRhythm);
            sss.setApplyToVolume(applyToVolume);
            sss.setSelfSimilarityIterations(selfSimilarityIterations);
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

    public void publisherNotification(Publisher p, Object args) {
        assert p == this.getSettings() : p;
        this.clearVoiceSectionResults();
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
