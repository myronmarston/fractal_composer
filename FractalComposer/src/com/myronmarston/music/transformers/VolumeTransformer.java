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

package com.myronmarston.music.transformers;

import com.myronmarston.music.MidiNote;
import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;

/**
 * Transformer that adjusts the volume of each note based on some scale factor.
 * The scale factor represents the percentage of the remaining volume range to 
 * scale to. For example, a scale factor of 0.5 means to raise the volume to 
 * half way between the existing volume and the max volume. A scale factor of 
 * -0.5 means to lower the volume to half way between the existing volume and 
 * the min volume.
 * 
 * @author Myron
 */
public class VolumeTransformer implements Transformer {        
    private final double scaleFactor;

    /**
     * Gets the scale factor.  Should be between -1 and 1.  Negative values will
     * reduce the volume; positive values will increase the volume.
     * 
     * @return the scale factor.
     */
    public double getScaleFactor() {
        return scaleFactor;
    }
    
    /**
     * Constructor. 
     * 
     * @param scaleFactor Should be between -1 and 1.  Negative values will
     *        reduce the volume; positive values will increase the volume.
     */
    public VolumeTransformer(double scaleFactor) {
        if (Math.abs(scaleFactor) > 1) throw new IllegalArgumentException("The scale factor must be between -1 and 1.");
        this.scaleFactor = scaleFactor;
    }
    
    public NoteList transform(NoteList input) {
        int remainingVolumeRange = 0;
        int volumeAdjustment = 0;
        Note newNote;
        NoteList output = new NoteList(input.size());
        
        for (Note inputNote : input) {
            newNote = (Note) inputNote.clone();
            
            if (!newNote.isRest()) { // don't change the volume of rests...                    
                remainingVolumeRange = 
                    (this.scaleFactor < 0) ?
                    newNote.getVolume() - MidiNote.MIN_VELOCITY :
                    MidiNote.MAX_VELOCITY - newNote.getVolume();

                volumeAdjustment = (int) Math.round(remainingVolumeRange * this.scaleFactor);

                newNote.setVolume(newNote.getVolume() + volumeAdjustment);
            }
            
            output.add(newNote);
        }
        
        return output;
    }
}
