/**
 * Represents a note, relative to a particular scale.
 * The note doesn't know or care what scale is being used, but to get a concrete
 * MidiNote, the scale must be used to convert it.
 * @author Myron
 */
public class Note {   
    private int scaleStep; //1 = tonic, 8 = octave, 10 = third an octave above, etc.
    private int octave; //which octave the note should be in.  0 begins the first octave in Midi that contains the tonic.
    private int chromaticAdjustment; //-2...2.  If this is an accidental, the number of half steps to adjust it from the diatonic scale note.
    private double duration; //how long the note should last, in quarter notes.
    private int volume = MidiNote.DEFAULT_VELOCITY; //how loud the note should be on a scale from 0 to 127.
    private boolean rest = false; // true if this note should be a rest.
    
    public Note() {}
            
    public Note(int scaleStep, int octave, int chromaticAdjustment, double duration, int volume) {
        this.scaleStep = scaleStep;
        this.octave = octave;
        this.chromaticAdjustment = chromaticAdjustment;
        this.duration = duration;
        this.volume = volume;
    }
    
    static public Note createRest(double duration) {
        Note rest = new Note();
        
        rest.rest = true;
        rest.duration = duration;
        rest.volume = 0; 
        rest.scaleStep = 1;
        rest.octave = 0;
        
        return rest;
    }

    public int getScaleStep() {
        return scaleStep;
    }

    public void setScaleStep(int pitch) {
        this.scaleStep = pitch;
    }

    public int getOctave() {
        return octave;
    }

    public void setOctave(int octave) {
        this.octave = octave;
    }

    public int getChromaticAdjustment() {
        return chromaticAdjustment;
    }

    public void setChromaticAdjustment(int chromaticAdjustment) {
        this.chromaticAdjustment = chromaticAdjustment;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
    
    public boolean getRest() {
        return rest;
    }
}
