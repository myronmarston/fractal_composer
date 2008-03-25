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
    private long duration; //how long the note should last
    private int volume = MidiNote.DEFAULT_VELOCITY; //how loud the note should be on a scale from 0 to 127.
    
    public Note() {}
    
    public Note(int scaleStep, int octave, int chromaticAdjustment, long duration, int volume) {
        this.scaleStep = scaleStep;
        this.octave = octave;
        this.chromaticAdjustment = chromaticAdjustment;
        this.duration = duration;
        this.volume = volume;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
