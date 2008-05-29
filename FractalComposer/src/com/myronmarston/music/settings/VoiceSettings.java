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

import com.myronmarston.util.Fraction;
import com.myronmarston.util.AbstractPublisher;
import com.myronmarston.util.Publisher;
import com.myronmarston.util.Subscriber;
import com.myronmarston.music.NoteList;
import com.myronmarston.music.transformers.*;
import org.simpleframework.xml.*;

/**
 * Specifies settings for a voice.  
 * 
 * @author Myron
 */
@Root
public class VoiceSettings extends AbstractPublisher implements Subscriber {
    
    @Attribute
    private int octaveAdjustment;
    
    @Element
    private Fraction speedScaleFactor;
    
    @Element
    private SelfSimilaritySettings selfSimilaritySettings;
    
    @Attribute
    private boolean readOnly;    

    /**
     * Constructor.
     */
    public VoiceSettings() {
        this(0, new Fraction(1, 1), new SelfSimilaritySettings());
    }        
    
    /**
     * Constructor.
     * 
     * @param octaveAdjustment the octave adjustment 
     * @param speedScaleFactor the speed scale factor
     * @param selfSimilaritySettings the self-similarity settings
     */
    public VoiceSettings(int octaveAdjustment, Fraction speedScaleFactor, SelfSimilaritySettings selfSimilaritySettings) {        
        this.setOctaveAdjustment(octaveAdjustment);        
        this.setSpeedScaleFactor(speedScaleFactor);
        
        
        this.setSelfSimilaritySettings(selfSimilaritySettings == null ? new SelfSimilaritySettings() : selfSimilaritySettings);        
    }

    /**
     * Gets a value indicating whether or not this settings object is read-only.
     * 
     * @return true if this object is read-only
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Sets the self-similarity settings on this object.
     * 
     * @param selfSimilaritySettings the new self-similarity settings
     * @throws java.lang.UnsupportedOperationException if this object is 
     *         read-only
     */
    public void setSelfSimilaritySettings(SelfSimilaritySettings selfSimilaritySettings) throws UnsupportedOperationException {
        if (this.readOnly) throw new UnsupportedOperationException("Cannot change values on a read-only object.");
        if (this.selfSimilaritySettings != null) this.selfSimilaritySettings.removeSubscriber(this);        
        this.selfSimilaritySettings = selfSimilaritySettings;                
        if (this.selfSimilaritySettings != null) this.selfSimilaritySettings.addSubscriber(this);        
        
        // the new self similarity settings might have different values,
        // so notify observers that we've changed...
        this.notifySubscribers(null);  
    }        
        
    /**
     * Gets how many octaves to adjust the germ for use by this voice.
     * 
     * @return number of octaves to adjust
     */
    public int getOctaveAdjustment() {
        return octaveAdjustment;
    }
    
    /**
     * Sets how many octaves to adjust the germ for use by this voice.
     * 
     * @param val number of octaves to adjust
     * @throws UnsupportedOperationException if this object is read-only
     */
    public void setOctaveAdjustment(int val) throws UnsupportedOperationException {     
        if (this.readOnly) throw new UnsupportedOperationException("Cannot change values on a read-only object.");
        // TODO: is there a way to tell if the octave adjustment value would 
        // give us a note outside of the standard midi range? should we 
        // test and throw an IllegalArgumentException here?        
        this.octaveAdjustment = val;
        this.notifySubscribers(null);  
    }

    /**
     * Gets the speed scale factor to apply to the germ for use by this voice.
     * 
     * @return the speed scale factor
     */
    public Fraction getSpeedScaleFactor() {
        return speedScaleFactor;
    }
    
    /**
     * Sets the speed scale factor to apply to the germ for use by this voice.
     * 
     * @param val the speed scale factor
     * @throws UnsupportedOperationException if this object is read-only
     */
    public void setSpeedScaleFactor(Fraction val) throws UnsupportedOperationException {   
        if (this.readOnly) throw new UnsupportedOperationException("Cannot change values on a read-only object.");
        RhythmicDurationTransformer.checkScaleFactorValidity(val);
        this.speedScaleFactor = val;
        this.notifySubscribers(null);  
    }

    /**
     * Gets the self-similarity settings to be used by these voice settings.
     * Guaranteed to never be null.  
     * 
     * @return the self-similarity settings to be used by these voice settings
     */
    public SelfSimilaritySettings getSelfSimilaritySettings() {        
        return selfSimilaritySettings;
    }    

    /**
     * Applies these voice settings to the given note list.
     * 
     * @param noteList the note list to apply the settings to
     * @return the result of applying the settings to the note list
     */
    public NoteList applySettingsToNoteList(NoteList noteList) {        
        OctaveTransformer octaveT = new OctaveTransformer(this.getOctaveAdjustment());
        RhythmicDurationTransformer speedT = new RhythmicDurationTransformer(this.getSpeedScaleFactor());
        SelfSimilarityTransformer selfSimilarityT = new SelfSimilarityTransformer(this.getSelfSimilaritySettings());
        
        NoteList temp = octaveT.transform(noteList);
        temp = speedT.transform(temp);  
        temp = selfSimilarityT.transform(temp);
        return temp;
    }

    public void publisherNotification(Publisher p, Object args) {        
        assert p == this.getSelfSimilaritySettings() : p;
        this.notifySubscribers(null);        
    }            
    
    /**
     * Gets a read-only copy of this object.  
     * 
     * @return a read-only copy of this object
     */
    public VoiceSettings getReadOnlyCopy() {        
        VoiceSettings vs = (VoiceSettings) this.clone();                                    
        vs.setSelfSimilaritySettings(vs.getSelfSimilaritySettings().getReadOnlyCopy());        
        vs.readOnly = true;
        return vs;
    }
    
    @Override
    public Object clone() {        
        VoiceSettings cloned = (VoiceSettings) super.clone();                
        SelfSimilaritySettings clonedSSS = (SelfSimilaritySettings) cloned.getSelfSimilaritySettings().clone();                
        cloned.setSelfSimilaritySettings(clonedSSS);
        return cloned;
    }

    // equals() and hashCode() were generated by NetBeans IDE
    @Override
    public boolean equals(Object obj) {      
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VoiceSettings other = (VoiceSettings) obj;
        if (this.octaveAdjustment != other.octaveAdjustment) {
            return false;
        }
        if (this.speedScaleFactor != other.speedScaleFactor && (this.speedScaleFactor == null || !this.speedScaleFactor.equals(other.speedScaleFactor))) {
            return false;
        }
        if (this.selfSimilaritySettings != other.selfSimilaritySettings && (this.selfSimilaritySettings == null || !this.selfSimilaritySettings.equals(other.selfSimilaritySettings))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.octaveAdjustment;
        hash = 37 * hash + (this.speedScaleFactor != null ? this.speedScaleFactor.hashCode() : 0);
        hash = 37 * hash + (this.selfSimilaritySettings != null ? this.selfSimilaritySettings.hashCode() : 0);
        return hash;
    }      
}
