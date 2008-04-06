package com.myronmarston.music.scales;

import com.myronmarston.music.MidiNote;
import com.myronmarston.music.Note;
import com.myronmarston.music.NoteName;

import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;

/**
 * An abstract implementation of the Scale interface for all Tonal scales.  A
 * tonal scale is any scale that has a tonal center.  Concrete scales (e.g., 
 * MajorScale, HarmonicMinorScale, MinorPentatonicScale, etc.) can extend this.
 * 
 * @author Myron
 */
public abstract class TonalScale implements Scale {
    private NoteName keyName;      
    private final static int MIDI_KEY_OFFSET = 12; //C0 is Midi pitch 12 (http://www.phys.unsw.edu.au/jw/notes.html)
    
    /**
     * Constructor.
     * 
     * @param keyName the tonal center of the scale
     */
    public TonalScale(NoteName keyName) {
        this.setKeyName(keyName);
    }

    /**
     * Gets the tonal center of the scale.
     * 
     * @return the key name
     */
    public NoteName getKeyName() {
        return keyName;
    }

    /**
     * Sets the tonal center of the scale.
     * 
     * @param keyName the key name
     */
    public void setKeyName(NoteName keyName) {
        this.keyName = keyName;
    }          
       
    /**
     * Gets how many half steps the given scale step is above the tonic.  This
     * will be different for each scale, so subtypes must implement this.
     * 
     * @param scaleStep the given scale step, normalized
     * @return the number of half steps between the tonic and the scale step
     */
    abstract protected int getHalfStepsAboveTonicForScaleStep(int scaleStep);
    
    
    /**
     * Gets how many scale steps are in one octave for this scale.  Examples:
     * major scales have 7.  Pentatonic scales have 5.  Subtypes must implement
     * this.
     * 
     * @return the number of scale steps in one octave
     */
    abstract protected int getNumScaleStepsInOctave();
    
    /**
     * Sometimes after transformations are applied to a note, the scale step is
     * less than 0 or greater than the number of scale steps in an octave.  
     * This method "normalizes" the note--adjusts its scaleStep and octave so 
     * that it is the same note, but the scaleStep is between 0 and the number 
     * of scale steps.
     * 
     * @param note the note to normalize
     */
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
        
    /**
     * Gets the midi pitch number for the tonic of this scale at the given 
     * octave.
     * 
     * @param octave the octave
     * @return the midi pitch number
     */
    protected int getMidiPitchNumberForTonicAtOctave(int octave) {
        return MIDI_KEY_OFFSET // A0, the lowest key on a piano
            + this.getKeyName().getNoteNumber() // the number of half steps to they key
            + (12 * octave); // 12 half steps in an octave
    }
    
    /**
     * Converts from quarter notes to ticks, based on the midi tick resolution.
     * 
     * @param quarterNotes the number of quarter notes
     * @param midiTickResolution the number of ticks per quarter note set on the
     *        midi sequence
     * @return the number of ticks
     */
    protected static long convertQuarterNotesToTicks(Fraction quarterNotes, int midiTickResolution) {
        Fraction converted = quarterNotes.times(midiTickResolution);
        
        // converting to midi ticks should result in an integral number of ticks
        // because our tick resolution should be chosen based on what will
        // produce this.  Test that this is in fact the case...
        assert converted.denominator() == 1 : converted;
     
        // since the denominator is 1, the converted value is equal to the numerator...
        return converted.numerator();
    }

    public MidiNote convertToMidiNote(Note note, Fraction startTime, int midiTickResolution) {
        this.normalizeNote(note);
        MidiNote midiNote = new MidiNote();
                        
        midiNote.setDuration(convertQuarterNotesToTicks(note.getDuration(), midiTickResolution));
        midiNote.setStartTime(convertQuarterNotesToTicks(startTime, midiTickResolution));
        midiNote.setVelocity(note.getVolume());
        
        midiNote.setPitch(this.getHalfStepsAboveTonicForScaleStep(note.getScaleStep()) 
            + this.getMidiPitchNumberForTonicAtOctave(note.getOctave()));
        
        return midiNote;
    }        
}
