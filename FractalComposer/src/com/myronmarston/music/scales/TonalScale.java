package com.myronmarston.music.scales;

import com.myronmarston.music.MidiNote;
import com.myronmarston.music.Note;
import com.myronmarston.music.NoteName;
/**
 *
 * @author Myron
 */
public abstract class TonalScale implements Scale {
    private NoteName keyName;      
    private final static int MIDI_KEY_OFFSET = 12; //C0 is Midi pitch 12 (http://www.phys.unsw.edu.au/jw/notes.html)
    
    public TonalScale(NoteName keyName) {
        this.setKeyName(keyName);
    }

    public NoteName getKeyName() {
        return keyName;
    }

    public void setKeyName(NoteName keyName) {
        this.keyName = keyName;
    }          
       
    abstract protected int getHalfStepsAboveTonicForScaleStep(int scaleStep);
    abstract protected int getNumScaleStepsInOctave();
    
    protected void normalizeNote(Note note) {
        int numScaleStepsInOctave = this.getNumScaleStepsInOctave(); // cache it
        
        // put the note's scaleStep into the normal range (0..numScaleSteps - 1), adjusting the octaves.
        while(note.getScaleStep() < 0) {
            note.setScaleStep(note.getScaleStep() + numScaleStepsInOctave);
            note.setOctave(note.getOctave() - 1);
        }                
        
        while(note.getScaleStep() > numScaleStepsInOctave - 1) {
            note.setScaleStep(note.getScaleStep() - numScaleStepsInOctave);
            note.setOctave(note.getOctave() + 1);
        }
        
        assert note.getScaleStep() >= 0 && note.getScaleStep() < numScaleStepsInOctave : note.getScaleStep();
    }
        
    protected int getMidiPitchNumberForTonicAtOctave(int octave) {
        return MIDI_KEY_OFFSET // A0, the lowest key on a piano
            + this.getKeyName().getNoteNumber() // the number of half steps to they key
            + (12 * octave); // 12 half steps in an octave
    }
    
    public MidiNote convertToMidiNote(Note note, double startTime) {
        this.normalizeNote(note);
        MidiNote midiNote = new MidiNote();
        
        midiNote.setDuration(note.getDuration());
        midiNote.setStartTime(startTime);
        midiNote.setVelocity(note.getVolume());
        
        midiNote.setPitch(this.getHalfStepsAboveTonicForScaleStep(note.getScaleStep()) 
            + this.getMidiPitchNumberForTonicAtOctave(note.getOctave()));
        
        return midiNote;
    }
}
