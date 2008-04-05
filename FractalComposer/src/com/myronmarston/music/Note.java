package com.myronmarston.music;

/**
 * Represents a note, relative to a particular scale.  This note will need to be
 * converted to midi events using the MidiNote class before it can be used in
 * a Midi track.  The note doesn't know or care what scale is being used, but to 
 * get a concrete MidiNote, the scale must be used to convert it.
 * 
 * @author Myron
 */
public class Note {   
    private int scaleStep; // number of scale steps above the tonic; 0 = tonic, 7 = octave, 9 = third an octave above, etc.
    private int octave; // which octave the note should be in.  0 begins the first octave in Midi that contains the tonic.
    private int chromaticAdjustment; // the number of half steps to adjust from the diatonic note; used if this note is an accidental
    private double duration; //how long the note should last, in quarter notes.
    private int volume = MidiNote.DEFAULT_VELOCITY; //how loud the note should be on a scale from 0 to 127.    
    
    /**
     * Default constructor.
     */
    public Note() {}
                
    /**
     * Constructor.
     * 
     * @param scaleStep number of scale steps above the tonic; 0 = tonic, 
     *        7 = octave, 9 = third an octave above, etc.
     * @param octave which octave the note should be in.  0 begins the first 
     *        octave in Midi that contains the tonic.
     * @param chromaticAdjustment the number of half steps to adjust from the
     *        diatonic note; used if this note is an accidental
     * @param duration how long the note should last, in quarter notes
     * @param volume how loud the note should be (0-127)
     */
    public Note(int scaleStep, int octave, int chromaticAdjustment, double duration, int volume) {
        this.setScaleStep(scaleStep);
        this.setOctave(octave);
        this.setChromaticAdjustment(chromaticAdjustment);
        this.setDuration(duration);
        this.setVolume(volume);        
    }
    
    /**
     * Copy Constructor.
     * 
     * @param inputNote note to copy     
     */
    public Note(Note inputNote) {
        this(inputNote.getScaleStep(), inputNote.getOctave(), inputNote.getChromaticAdjustment(), inputNote.getDuration(), inputNote.getVolume());        
    }
        
    /**
     * Creates a note that is a rest.
     * 
     * @param duration how long the rest should last, in quarter notes
     * @return the rest     
     */
    static public Note createRest(double duration) {
        Note rest = new Note();
                
        rest.setDuration(duration); // use the setter so we get an exception if the duration is 0
        rest.setVolume(MidiNote.MIN_VELOCITY);              
        
        assert rest.isRest() : rest;
        return rest;
    }

    /**
     * Gets the number of scale steps above the tonic; In an 7-note scale, 
     * 0 = tonic, 7 = octave, 9 = third an octave above, etc.
     * 
     * @return the scale step
     */
    public int getScaleStep() {
        return scaleStep;
    }

    /**
     * Sets the number of scale steps above the tonic; In an 7-note scale, 
     * 0 = tonic, 7 = octave, 9 = third an octave above, etc.
     * 
     * @param scaleStep the scale step
     * @throws UnsupportedOperationException if the note is a rest
     */
    public void setScaleStep(int scaleStep) {
        throwUnsupportedOperationExceptionIfRest("scaleStep", scaleStep);
        this.scaleStep = scaleStep;        
    }

    /**
     * Gets which octave the note should be in.  0 begins the first octave in 
     * Midi that contains the tonic.
     * 
     * @return the octave
     */
    public int getOctave() {
        return octave;
    }

    /**
     * Sets which octave the note should be in.  0 begins the first octave in 
     * Midi that contains the tonic.
     * 
     * @param octave the octave
     */
    public void setOctave(int octave) {
        throwUnsupportedOperationExceptionIfRest("octave", octave);
        this.octave = octave;        
    }

    /**
     * Gets the number of half steps to adjust from the diatonic note; used if 
     * this note is an accidental.
     * 
     * @return the chromatic adjustment
     */
    public int getChromaticAdjustment() {
        return chromaticAdjustment;
    }

    /**
     * Sets the number of half steps to adjust from the diatonic note; used if 
     * this note is an accidental.
     * 
     * @param chromaticAdjustment the chromatic adjustment
     */
    public void setChromaticAdjustment(int chromaticAdjustment) {
        throwUnsupportedOperationExceptionIfRest("chromaticAdjustment", chromaticAdjustment);
        this.chromaticAdjustment = chromaticAdjustment;
    }

    /**
     * Gets how long the note should last, in quarter notes.
     * 
     * @return the duration
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Sets how long the note should last, in quarter notes.
     * 
     * @param duration the duration
     */
    public void setDuration(double duration) {
        if (duration <= 0) throw new IllegalArgumentException("The duration must be greater than zero.");
        this.duration = duration;
    }

    /**
     * Gets how loud the note should be (0-127).  0 makes it a rest.
     * 
     * @return the volume
     */
    public int getVolume() {
        return volume;
    }
    
    /**
     * Sets how loud the note should be (0-127).  0 makes it a rest.
     * 
     * @param volume the volume
     */
    public void setVolume(int volume) {
        if (volume < MidiNote.MIN_VELOCITY || volume > MidiNote.MAX_VELOCITY) {
            throw new IllegalArgumentException(String.format("The volume must be between %d and %d.  The passed volume was %d.", MidiNote.MIN_VELOCITY, MidiNote.MAX_VELOCITY, volume));
        }
        
        this.volume = volume;
        
        // set the pitch fields to default values. We do this so that our
        // hashCode and equals work properly.  All rests of the same length
        // should be equal.
        if (this.isRest()) { 
            this.setScaleStep(0);
            this.setOctave(0);
            this.setChromaticAdjustment(0);
        }
    }
    
    /**
     * Gets whether or not this note is a rest (i.e., has a volume of 0).
     * 
     * @return true if the volume is 0
     */
    public boolean isRest() {
        return (this.getVolume() == 0);
    }    
    
    /**
     * Throws an exception if the changing field is not being changed to zero
     * and the note is a rest.  Called from the setters.
     * 
     * @param changingField the field being changed
     * @param value the value the field is being changed to
     */
    protected void throwUnsupportedOperationExceptionIfRest(String changingField, int value) {
        if (this.isRest() && value != 0) { // each of the fields can be zero...
            throw new UnsupportedOperationException(String.format("The Note is a rest.  The %s field cannot be changed on a rest.", changingField));
        }
    }
    
    @Override
    public String toString() {
        return String.format("Note = scaleStep %d, octave %d, chromaticAdjustment %d, duration %f, volume %d", this.scaleStep, this.octave, this.chromaticAdjustment, this.duration, this.volume);
    }  
        
    // equals and hashCode were auto-generated by the Netbeans IDE
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Note other = (Note) obj;
        if (this.scaleStep != other.scaleStep) {
            return false;
        }
        if (this.octave != other.octave) {
            return false;
        }
        if (this.chromaticAdjustment != other.chromaticAdjustment) {
            return false;
        }
        if (this.duration != other.duration) {
            return false;
        }
        if (this.volume != other.volume) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.scaleStep;
        hash = 89 * hash + this.octave;
        hash = 89 * hash + this.chromaticAdjustment;
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.duration) ^ (Double.doubleToLongBits(this.duration) >>> 32));
        hash = 89 * hash + this.volume;
        return hash;
    }         
}
