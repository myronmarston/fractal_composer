package com.myronmarston.music;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

/**
 * This class is responsible to create the Midi events for a note.  You should 
 * not create Midi note on and note off events on your own; use this class
 * instead.
 * 
 * @author Myron
 */
public class MidiNote {
    private int pitch; // 60 = middle C; each value is a half-step
    private long startTime; // when the note should start playing, in midi ticks
    private long duration; // how long the note should last, in midi ticks
    private int channel; // which of the 16 channels this note is on
    private int velocity; // the loudness or softness of the note    
    private MidiEvent noteOnEvent;
    private MidiEvent noteOffEvent; 
    
    /**
     * The default velocity (volume) of the note--64.
     */
    public static final int DEFAULT_VELOCITY = 64;
    
    
    /**
     * The default channel of the note--0.
     */
    public static final int DEFAULT_CHANNEL = 0;      
    
    /**
     * The minimum chanel value.
     */
    public static final int MIN_CHANNEL = 0;      
    
    /**
     * The maximum channel value.
     */
    public static final int MAX_CHANNEL = 15;
    
    /**
     * The maximum allowed velocity, 127.
     */
    public static final int MAX_VELOCITY = 127;
    
    
    /**
     * The minimum allowed velocity, 0.
     */
    public static final int MIN_VELOCITY = 0;
    
    /**
     * Constructor.  Takes all of the main parameters of a MidiNote.
     * 
     * @param pitch the pitch of the note.  60 is middle C
     * @param startTime when the note should start playing, in midi ticks
     * @param duration how long the note should last, in midi ticks
     * @param channel which of the 16 channels (0-15) to place the note on
     * @param velocity the volume of the note, 0-127
     */
    public MidiNote(int pitch, long startTime, long duration, int channel, int velocity) {
        this.setPitch(pitch);
        this.setStartTime(startTime);
        this.setDuration(duration);
        this.setChannel(channel);
        this.setVelocity(velocity);        
    }  

    /**
     * Constructor.  Creates a MidiNote with the default velocity.
     * 
     * @param pitch the pitch of the note.  60 is middle C
     * @param startTime when the note should start playing, in midi ticks
     * @param duration how long the note should last, in midi ticks
     * @param channel which of the 16 channels (0-15) to place the note on
     */
    public MidiNote(int pitch, long startTime, long duration, int channel) {
        this(pitch, startTime, duration, channel, DEFAULT_VELOCITY);
    }
    
    /**
     * Constructor.  Creates a MidiNote with the default velocity and channel.
     * 
     * @param pitch the pitch of the note.  60 is middle C
     * @param startTime when the note should start playing, in midi ticks
     * @param duration how long the note should last, in midi ticks
     */
    public MidiNote(int pitch, long startTime, long duration) {
        this(pitch, startTime, duration, DEFAULT_CHANNEL);
    }
    
    
    /**
     * Default Constructor.  You should use the field setters to provide values 
     * for each parameter.
     */
    public MidiNote() {
        this.setChannel(DEFAULT_CHANNEL);
        this.setVelocity(DEFAULT_VELOCITY);
    }
                   
    /**
     * Gets the note's pitch, in half-step increments.
     * 
     * @return the pitch of the note, in half-step increments
     */
    public int getPitch() {
        return pitch;
    }
    
    /**
     * Sets the note's pitch, in half-step increments.
     * 
     * @param pitch the pitch to use, in half-step increments; 60 = middle C
     */
    public void setPitch(int pitch) {
        if (this.pitch != pitch) clearNoteEvents();
        this.pitch = pitch;        
    }
       
    /**
     * Gets how long the note should be, in midi ticks.
     * 
     * @return how long the note should be, in midi ticks
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Sets how long the note should be, in midi ticks.
     * 
     * @param duration how long the note should be, in midi ticks
     */
    public void setDuration(long duration) {
        if (this.duration != duration) clearNoteEvents();
        this.duration = duration;        
    }
    
    /**
     * Gets when this note should sound, in midi ticks.
     * 
     * @return when this note should sound, in midi ticks
     */
    public long getStartTime() {
        return startTime;
    }       

    /**
     * Sets when this note should sound, in midi ticks.
     *      
     * @param startTime when this note should sound, in midi ticks
     */
    public void setStartTime(long startTime) {
        if (this.startTime != startTime) clearNoteEvents();
        this.startTime = startTime;        
    }

    /**
     * Gets the velocity (volume) of the note, on a scale of 0 to 127.
     * 
     * @return the velocity (volume) of the note, on a scale of 0 to 127
     */
    public int getVelocity() {
        return velocity;
    }

    /**
     * Sets the velocity (volume) of the note, on a scale of 0 to 127.
     * 
     * @param velocity the velocity (volume) of the note, on a scale of 0 to 127
     */
    public void setVelocity(int velocity) {
        if (this.velocity != velocity) clearNoteEvents();
        this.velocity = velocity;        
    }    
    
    /**
     * Gets which channel (0-15) this note should be on.
     * 
     * @return which channel (0-15) this note should be on
     */
    public int getChannel() {
        return channel;
    }

    /**
     * Sets which channel (0-15) this note should be on.
     * 
     * @param channel which channel (0-15) this note should be on
     */
    public void setChannel(int channel) {
        if (channel < MIN_CHANNEL || channel > MAX_CHANNEL) throw new IllegalArgumentException(String.format("The channel must be between %d and %d.", MIN_CHANNEL, MAX_CHANNEL));
        if (this.channel != channel) clearNoteEvents();
        this.channel = channel;        
    }   
    
    /**
     * Gets the note on event for this note.  This must be added to the Midi 
     * track before the note off event.
     * 
     * @return the note on event
     * @throws javax.sound.midi.InvalidMidiDataException if there is something
     *         invalid about the midi data
     */
    public MidiEvent getNoteOnEvent() throws InvalidMidiDataException {
        if (noteOnEvent == null) createNoteEvents(); 
        return noteOnEvent;
    }

    /**
     * Gets the note off event for this note.  This must be added to the Midi 
     * track after the note on event.
     * 
     * @return the note off event
     * @throws javax.sound.midi.InvalidMidiDataException if there is something
     *         invalid about the midi data
     */
    public MidiEvent getNoteOffEvent() throws InvalidMidiDataException {
        if (noteOffEvent == null) createNoteEvents();
        return noteOffEvent;
    }
    
    /**
     * Gets the time, in midi ticks, when this note will end.
     * 
     * @return the start time plus the duration of the note 
     */
    public long getNoteEnd() {
        return this.getStartTime() + this.getDuration();
    }
    
    /**
     * Clears the private note on and note off event fields.  Should be called 
     * when any field used to generate the midi events changes.
     */
    private void clearNoteEvents() {
        this.noteOnEvent = null;
        this.noteOffEvent = null;
    }
    
    /**
     * Creates the note on and note off events, and sets the values of the 
     * private fields.
     * 
     * @throws InvalidMidiDataException if any of the midi data is invalid
     */
    private void createNoteEvents() throws InvalidMidiDataException {
        this.noteOnEvent = createNoteEvent(
            ShortMessage.NOTE_ON, 
            this.getChannel(), 
            this.getPitch(), 
            this.getVelocity(),
            this.getStartTime());  
        
        this.noteOffEvent = createNoteEvent(
            ShortMessage.NOTE_OFF, 
            this.getChannel(), 
            this.getPitch(), 
            0, // velocity should always be 0 for note off events
            this.getStartTime() + this.getDuration());
    }
               
    /**
     * Creates a raw Midi note event based on the given arguments.
     * 
     * @throws InvalidMidiDataException if any of the midi data is invalid
     */    
    private static MidiEvent createNoteEvent(int command, int channel, int pitch, int velocity, long tick) throws javax.sound.midi.InvalidMidiDataException {	
        ShortMessage message = new ShortMessage();        
        message.setMessage(command, channel, pitch, velocity);
        MidiEvent event = new MidiEvent(message, tick);
        return event;
    }       
}
