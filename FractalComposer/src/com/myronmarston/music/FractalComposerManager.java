package com.myronmarston.music;

import com.myronmarston.music.scales.Scale;
import com.myronmarston.music.transformers.Transformer;

import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.sound.midi.InvalidMidiDataException;

/**
 *
 * @author Myron
 */
public class FractalComposerManager {   
    private NoteList germ;
    private Scale scale;
    private Transformer transformer;   
    
    public FractalComposerManager(NoteList germ, Scale scale, Transformer transformer) {
        this.germ = germ;
        this.scale = scale;
        this.transformer = transformer;
    }
    
    public NoteList getGerm() {
        if (germ == null) germ = new NoteList();
        return germ;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }
    
    public Transformer getTransformer() {
        return transformer;
    }

    public void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }
    
    public Sequence createSequence() throws InvalidMidiDataException {
        NoteList notes = this.getGerm();
        if (null != this.getTransformer()) {
            notes = this.getTransformer().transform(notes);
        }
                    
        Sequence sequence = new Sequence(Sequence.PPQ, MidiNote.TICKS_PER_QUARTER_NOTE);
        Track track = sequence.createTrack();
        notes.fillMidiTrack(track, scale, 0d);        
        return sequence;
    }
}
