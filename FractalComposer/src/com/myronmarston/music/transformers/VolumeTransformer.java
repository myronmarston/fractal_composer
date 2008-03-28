package com.myronmarston.music.transformers;

import com.myronmarston.music.MidiNote;
import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;

/**
 *
 * @author Myron
 */
public class VolumeTransformer implements Transformer {    
    // the sacle factor represents the percentage of the remaining volume range to scale to.
    // For example, a scale factor of 0.5 means to raise the volume to 
    // half way between the existing volume and the max volume.
    // A scale factor of -0.5 means to lower the volume to half way between the existing
    // volume and the min volume.
    private double scaleFactor;

    public double getScaleFactor() {
        return scaleFactor;
    }
    
    public VolumeTransformer(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
    
    public NoteList transform(NoteList input) {
        int remainingVolumeRange = 0;
        int volumeAdjustment = 0;
        Note newNote;
        NoteList output = new NoteList(input.size());
        
        for (Note inputNote : input) {
            newNote = new Note(inputNote);
            
            remainingVolumeRange = 
                (this.getScaleFactor() < 0) ?
                newNote.getVolume() - MidiNote.MIN_VELOCITY :
                MidiNote.MAX_VELOCITY - newNote.getVolume();
            
            volumeAdjustment = (int) Math.round(remainingVolumeRange * this.getScaleFactor());
            
            newNote.setVolume(newNote.getVolume() + volumeAdjustment);
            
            output.add(newNote);
        }
        
        return output;
    }
}
