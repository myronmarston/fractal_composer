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
import com.myronmarston.music.transformers.*;
import org.simpleframework.xml.*;

/**
 * Specifies settings for a section.
 * 
 * @author Myron
 */
@Root
public class SectionSettings extends AbstractVoiceOrSectionSettings {
    
    @Attribute
    private boolean applyInversion = false;
    
    @Attribute
    private boolean applyRetrograde = false; 
       
    /**
     * Constructor.
     */
    public SectionSettings() {
        this(false, false);
    }
    
    /**
     * Constructor.
     * 
     * @param applyInversion whether or not to apply inversion to this section
     * @param applyRetrograde whether or not to apply retrograde to this section
     */
    public SectionSettings(boolean applyInversion, boolean applyRetrograde) {        
        this.setApplyInversion(applyInversion);
        this.setApplyRetrograde(applyRetrograde);
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
     * @throws UnsupportedOperationException if this object is read-only
     */
    public void setApplyInversion(boolean applyInversion) throws UnsupportedOperationException {
        this.readOnlyException();        
        this.applyInversion = applyInversion;
        this.notifySubscribers(null);        
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
     * @throws UnsupportedOperationException if this object is read-only
     */
    public void setApplyRetrograde(boolean applyRetrograde) throws UnsupportedOperationException {
        this.readOnlyException();
        this.applyRetrograde = applyRetrograde;
        this.notifySubscribers(null);
    }   
        
    @Override
    public NoteList applySettingsToNoteList(NoteList noteList, Scale scale) {     
        NoteList temp = (NoteList) noteList.clone();
        
        if (this.getApplyInversion()) {
            InversionTransformer iT = new InversionTransformer();
            temp = iT.transform(temp);
        }
        
        if (this.getApplyRetrograde()) {
            RetrogradeTransformer rT = new RetrogradeTransformer();
            temp = rT.transform(temp);
        } 
        
        return super.applySettingsToNoteList(temp, scale);
    }

    @Override
    public SectionSettings getReadOnlyCopy() {
        return (SectionSettings) super.getReadOnlyCopy();
    }
        
    // equals() and hashCode() were generated by NetBeans IDE,
    // then modified to include the calls to the super class.
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final SectionSettings other = (SectionSettings) obj;
        if (this.applyInversion != other.applyInversion) {
            return false;
        }
        if (this.applyRetrograde != other.applyRetrograde) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + super.hashCode();
        hash = 71 * hash + (this.applyInversion ? 1 : 0);
        hash = 71 * hash + (this.applyRetrograde ? 1 : 0);
        return hash;
    }        
}
