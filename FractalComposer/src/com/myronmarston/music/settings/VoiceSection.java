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
import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;
import com.myronmarston.util.Fraction;
import com.myronmarston.util.Publisher;
import com.myronmarston.util.Subscriber;

import org.simpleframework.xml.*;

import java.io.IOException;
import java.util.*;
        
/**
 * Represents the smallest unit of the fractal piece for which the user can
 * specify settings.  One of these exists for each combination of a Voice and
 * a Section.  
 * 
 * @author Myron
 */
@Root
public class VoiceSection implements Subscriber {

    @Attribute
    private boolean rest = false;
    
    @Attribute
    private boolean useDefaultVoiceSettings = true;
    
    @Element(required=false)
    private VoiceSettings voiceSettings;
    
    @Attribute
    private boolean useDefaultSectionSettings = true;        
    
    @Element(required=false)
    private SectionSettings sectionSettings;
            
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
    }      
    
    /**
     * Provided for xml deserialization.
     */
    private VoiceSection() { }
        
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
     * Gets a value indicating whether or not this voice section will use the
     * default section settings.
     * 
     * @return true if the default settings will be used, false if the
     *         settings will be overriden
     */
    public boolean getUseDefaultSectionSettings() {
        return useDefaultSectionSettings;
    }

    /**
     * Set a value indicating whether or not this voice section will use the
     * default section settings.
     * 
     * @param useDefaultSectionSettings true if the default settings will be 
     *        used, false if the settings will be overriden
     */
    public void setUseDefaultSectionSettings(boolean useDefaultSectionSettings) {
        if (this.useDefaultSectionSettings && !useDefaultSectionSettings) {
            // the value is changing from using the default settings to overriding them,
            // so create a local settings object that is initially identical to
            // the default            
            this.setSectionSettings((SectionSettings) this.getSection().getSettings().clone());            
        }
        
        this.useDefaultSectionSettings = useDefaultSectionSettings;
    }

    /**
     * Gets a value indicating whether or not this voice section will use the
     * default voice settings.
     * 
     * @return true if the default settings will be used, false if the
     *         settings will be overriden
     */
    public boolean getUseDefaultVoiceSettings() {
        return useDefaultVoiceSettings;
    }

    /**
     * Set a value indicating whether or not this voice section will use the
     * default voice settings.
     * 
     * @param useDefaultVoiceSettings true if the default settings will be 
     *        used, false if the settings will be overriden
     */
    public void setUseDefaultVoiceSettings(boolean useDefaultVoiceSettings) {
        if (this.useDefaultVoiceSettings && !useDefaultVoiceSettings) {
            // the value is changing from using the default settings to overriding them,
            // so create a local settings object that is initially identical to
            // the default            
            this.setVoiceSettings((VoiceSettings) this.getVoice().getSettings().clone());            
        }
        
        this.useDefaultVoiceSettings = useDefaultVoiceSettings;
    }

    /**
     * Gets the section settings for this voice section.  If the 
     * useDefaultSectionSettings flag is set, a read-only copy of the section's
     * settings will be returned.  Otherwise, a local, editable copy will be
     * returned.
     * 
     * @return the section settings for this voice section
     */
    public SectionSettings getSectionSettings() {
        if (this.getUseDefaultSectionSettings()) return this.getSection().getSettings().getReadOnlyCopy();
        return sectionSettings;
    }
    
    private void setSectionSettings(SectionSettings sectionSettings) {
        if (this.sectionSettings != null) this.sectionSettings.removeSubscriber(this);
        this.sectionSettings = sectionSettings;
        if (this.sectionSettings != null) this.sectionSettings.addSubscriber(this);
    }

    /**
     * Gets the voice settings for this voice section.  If the 
     * useDefaultVoiceSettings flag is set, a read-only copy of the voice's
     * settings will be returned.  Otherwise, a local, editable copy will be
     * returned.
     * 
     * @return the voice settings for this voice section
     */
    public VoiceSettings getVoiceSettings() {
        if (this.getUseDefaultVoiceSettings()) return this.getVoice().getSettings().getReadOnlyCopy();
        return voiceSettings;
    }       
    
    private void setVoiceSettings(VoiceSettings voiceSettings) {
        if (this.voiceSettings != null) this.voiceSettings.removeSubscriber(this);
        this.voiceSettings = voiceSettings;
        if (this.voiceSettings != null) this.voiceSettings.addSubscriber(this);
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
        NoteList germ = this.getVoice().getFractalPiece().getGerm();
        
        if (this.getRest()) {            
            // scale the duration according to the speed of this voice...
            Fraction duration = germ.getDuration();            
            duration = duration.dividedBy(this.getVoiceSettings().getSpeedScaleFactor());
            
            // create a note list of a single rest, the duration of the germ            
            NoteList restResult = new NoteList();
            if (duration.compareTo(0L) > 0) restResult.add(Note.createRest(duration));            
            return restResult;
        }
        
        NoteList temp = this.getSectionSettings().applySettingsToNoteList(germ);
        return this.getVoiceSettings().applySettingsToNoteList(temp);                               
    }

    public void publisherNotification(Publisher p, Object args) {   
        assert p == this.sectionSettings || p == this.voiceSettings : p;        
        this.clearVoiceSectionResult();
    }
}