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

import com.myronmarston.music.NoteList;
import com.myronmarston.music.scales.Scale;
import com.myronmarston.music.transformers.VolumeTransformer;
import com.myronmarston.music.transformers.TransposeTransformer;
import com.myronmarston.util.AbstractPublisher;

import org.simpleframework.xml.*;

/**
 * Class that has settings that can be set either on a voice or a section.  This
 * also contains some common logic used by both the VoiceSettings and 
 * SectionSetttings classes.
 * 
 * @author Myron
 */
@Root
public class AbstractVoiceOrSectionSettings extends AbstractPublisher {
    
    @Attribute
    private double volumeAdjustment; 
    
    @Attribute
    private int scaleStepOffset;
    
    @Attribute
    private boolean readOnly;    
        
    /**
     * Gets the scale step offset.  This can be used to move the music to a 
     * different pitch level.
     * 
     * @return the scale step offset
     */
    public int getScaleStepOffset() {
        return scaleStepOffset;
    }

    /**
     * Sets the scale step offset.  This can be used to move the music to a 
     * different pitch level.
     * 
     * @param scaleStepOffset the scale step offset
     * @throws IllegalArgumentException if the scale step offset is outside of
     *         the range -11 to 11
     * @throws UnsupportedOperationException if this object is read-only
     */
    public void setScaleStepOffset(int scaleStepOffset) throws IllegalArgumentException, UnsupportedOperationException {
        this.readOnlyException();
        if (Math.abs(scaleStepOffset) >= Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE) 
            throw new IllegalArgumentException("The scaleStepOffset must be between -11 and 11.");
        
        this.scaleStepOffset = scaleStepOffset;
        this.notifySubscribers(null);
    }

    /**
     * Gets the volume adjustment.  This will be used to scale the volume of
     * this voice or section.  Should be between -1 and 1.  Negative values
     * decrease the volume; positive values increase it.
     * 
     * @return the volume adjustment
     */
    public double getVolumeAdjustment() {
        return volumeAdjustment;
    }

    /**
     * Sets the volume adjustment.  This will be used to scale the volume of
     * this voice or section.  Should be between -1 and 1.  Negative values
     * decrease the volume; positive values increase it.
     * 
     * @param volumeAdjustment the volume adjustment
     * @throws IllegalArgumentException if the volume adjustment is outside of 
     *         the range -1 to 1
     * @throws UnsupportedOperationException if this is a read-only object
     */
    public void setVolumeAdjustment(double volumeAdjustment) throws IllegalArgumentException, UnsupportedOperationException {        
        this.readOnlyException();
        VolumeTransformer.checkVolumeScaleFactorValidity(volumeAdjustment);        
        this.volumeAdjustment = volumeAdjustment;
        this.notifySubscribers(null);
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
     * If the object is read-only, throws an UnsupportedOperationException. This
     * method should be called at the start of every setter in this class and
     * all sub classes.     
     * 
     * @throws UnsupportedOperationException if this object is read-only
     */
    protected void readOnlyException() throws UnsupportedOperationException {
        if (this.readOnly) 
            throw new UnsupportedOperationException("Cannot change values on a read-only object.");
    }
    
    /**
     * Applies these settings to the given note list.
     * 
     * @param noteList the note list to apply the settings to
     * @param scale the scale to use in conjunction with the settings
     * @return the result of applying the settings to the note list
     */
    public NoteList applySettingsToNoteList(NoteList noteList, Scale scale) {                        
        // we want the offset to be in the range -NumScaleSteps to +NumScaleSteps
        int offsetToUse = scale.getNormalizedScaleStep(scaleStepOffset);
        offsetToUse -= (scaleStepOffset < 0 ? scale.getScaleStepArray().length : 0);
        
        TransposeTransformer tTrans = new TransposeTransformer(offsetToUse, scale.getRecommendedTransposeLetterNumber(this.scaleStepOffset));
        VolumeTransformer vTrans = new VolumeTransformer(this.getVolumeAdjustment());                
        NoteList temp = vTrans.transform(noteList);                
        return tTrans.transform(temp);
    }
    
    /**
     * Gets a read-only copy of this object.  
     * 
     * @return a read-only copy of this object
     */
    public AbstractVoiceOrSectionSettings getReadOnlyCopy() {        
        AbstractVoiceOrSectionSettings settings = (AbstractVoiceOrSectionSettings) this.clone();                                            
        settings.readOnly = true;
        return settings;
    }

    // equals() and hashCode() were generated by Netbeans IDE
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractVoiceOrSectionSettings other = (AbstractVoiceOrSectionSettings) obj;
        if (this.volumeAdjustment != other.volumeAdjustment) {
            return false;
        }
        if (this.scaleStepOffset != other.scaleStepOffset) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.volumeAdjustment) ^ (Double.doubleToLongBits(this.volumeAdjustment) >>> 32));
        hash = 53 * hash + this.scaleStepOffset;
        return hash;
    }        
}
