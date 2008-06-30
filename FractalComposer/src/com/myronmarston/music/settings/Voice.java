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

import com.myronmarston.music.OutputManager;
import com.myronmarston.music.GermIsEmptyException;
import com.myronmarston.music.Instrument;
import com.myronmarston.music.NoteList;

import com.myronmarston.util.Publisher;
import org.simpleframework.xml.*;

import com.myronmarston.util.Fraction;
import java.util.*;

/**
 * A FractalPiece is composed of several voices.  A Voice is analogous to 
 * a contrapuntal voice in music theory.  It is composed of a series of 
 * sections, each of which is composed of a series of notes and or/rests, 
 * played consecutively.  One voice will never have multiple notes
 * sounded simultaneously.
 * 
 * @author Myron
 */
@Root
public class Voice extends AbstractVoiceOrSection<Voice, Section> {       
    private NoteList modifiedGerm;      
    
    @Attribute
    private String instrumentName = Instrument.DEFAULT.getName();
    
    @Element
    private final VoiceSettings settings;    
    
    /**
     * Constructor.
     * 
     * @param fractalPiece the FractalPiece this voice is a part of
     * @param uniqueIndex the unique index for this voice
     */
    protected Voice(FractalPiece fractalPiece, int uniqueIndex) {
        super(fractalPiece, uniqueIndex);
        settings = new VoiceSettings();
        settings.addSubscriber(this);
    }    
    
    /**
     * Provided for xml deserialization.
     */
    private Voice() {
        this(null, 0);
    }

    /**
     * Gets the settings for this voice.
     * 
     * @return the settings for this voice
     */
    public VoiceSettings getSettings() {
        return settings;
    }        
    
    /**
     * Gets the name of the instrument used by this voice.
     * 
     * @return the name of the instrument used by this voice.
     */
    public String getInstrumentName() {
        return instrumentName;
    }

    /**
     * Sets the name of the instrument used by this voice.  
     * 
     * @param instrumentName
     * @throws IllegalArgumentException thrown if the given instrument is
     *         unavailable
     */
    public void setInstrumentName(String instrumentName) throws IllegalArgumentException {
        if (!Instrument.AVAILABLE_INSTRUMENTS.contains(instrumentName)) {
            throw new IllegalArgumentException(instrumentName + " is not one of the available instruments.");
        }
        
        this.instrumentName = instrumentName;                
    }        
            
    /**
     * Gets the modified germ, based on the current settings.
     * 
     * @return the modified germ
     */
    public NoteList getModifiedGerm() {
        if (modifiedGerm == null) this.modifiedGerm = this.getSettings().applySettingsToNoteList(this.getFractalPiece().getGerm());
        
        // the germ instrument is a setting on the entire note list, so we just set it here
        // rather than having to regenerate our modified germ every time it changes
        this.modifiedGerm.setInstrument(Instrument.getInstrument(this.getInstrumentName()));
        return modifiedGerm;
    }
    
    /**
     * Gets a NoteList containing the notes for all sections of this voice.
     * 
     * @return a NoteList for the entire voice
     */
    public NoteList getEntireVoice() {        
        NoteList entireVoice = new NoteList();        
        Fraction sectionDuration;

        for (VoiceSection vs : this.getVoiceSections()) {
            sectionDuration = vs.getSection().getDuration();

            entireVoice.addAll(vs.getLengthenedVoiceSectionResult(sectionDuration));
        }

        entireVoice.setInstrument(Instrument.getInstrument(this.getInstrumentName()));
        return entireVoice;
    }   
    
    /**
     * Creates the output manager for this voice.
     * 
     * @return the output manager
     * @throws com.myronmarston.music.GermIsEmptyException if the germ is empty
     */
    public OutputManager createOutputManager() throws GermIsEmptyException {
        //TODO: cache this?
        return new OutputManager(this.getFractalPiece(), Arrays.asList(this.getEntireVoice()));
    }

    public void publisherNotification(Publisher p, Object args) {
        assert p == this.getSettings() : p;
        this.clearVoiceSectionResults();
        this.clearModifiedGerm();
    }            

    /**
     * Sets the modifiedGerm field to null.  Should be called anytime a field
     * that affects the modified germ changes.
     */
    private void clearModifiedGerm() {
        this.modifiedGerm = null;
    }       
    
    @Override
    protected VoiceOrSectionList<Voice, Section> getListOfMainType() {
        return this.getFractalPiece().getVoices();
    }
    
    @Override
    protected VoiceOrSectionList<Section, Voice> getListOfOtherType() {
        return this.getFractalPiece().getSections();
    }

    @Override
    protected VoiceSectionHashMapKey getHashMapKeyForOtherTypeIndex(int index) {
        Section indexedSection = this.getFractalPiece().getSections().get(index);
        return new VoiceSectionHashMapKey(this, indexedSection);
    }

    @Override
    protected VoiceSectionHashMapKey getHashMapKeyForOtherTypeUniqueIndex(int uniqueIndex) {
        Section indexedSection = this.getFractalPiece().getSections().getByUniqueIndex(uniqueIndex);
        return new VoiceSectionHashMapKey(this, indexedSection);
    }        

    @Override
    protected VoiceSection instantiateVoiceSection(Section vOrS) {
        return new VoiceSection(this, vOrS);
    }        
}