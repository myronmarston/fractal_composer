import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

/**
 * Handles the Midi events (both note on and note off) for one note.
 */
public class MidiNote {    
    private int pitch; // 60 = middle C; each value is a half-step
    private long startTime; // when the note should start playing, in ticks
    private long duration; // how long the note should last for, in ticks
    private int channel; // which of the 16 channels this note is on
    private int velocity; // the loudness or softness of the note    
    private MidiEvent noteOnEvent;
    private MidiEvent noteOffEvent; 
    public static final int DEFAULT_VELOCITY = 64;
    public static final int DEFAULT_CHANNEL = 0;
    
    public MidiNote(int pitch, long startTime, long duration, int channel, int velocity) {
        this.setPitch(pitch);
        this.setStartTime(startTime);
        this.setDuration(duration);
        this.setChannel(channel);
        this.setVelocity(velocity);        
    }  

    public MidiNote(int pitch, long startTime, long duration, int channel) {
        this(pitch, startTime, duration, channel, DEFAULT_VELOCITY);
    }
    
    public MidiNote(int pitch, long startTime, long duration) {
        this(pitch, startTime, duration, DEFAULT_CHANNEL);
    }
    
    public MidiNote() {
        this.setChannel(DEFAULT_CHANNEL);
        this.setVelocity(DEFAULT_VELOCITY);
    }
                
    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        if (this.pitch != pitch) clearNoteEvents();
        this.pitch = pitch;        
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        if (this.duration != duration) clearNoteEvents();
        this.duration = duration;        
    }
    
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        if (this.startTime != startTime) clearNoteEvents();
        this.startTime = startTime;        
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        if (this.velocity != velocity) clearNoteEvents();
        this.velocity = velocity;        
    }    
    
    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        if (this.channel != channel) clearNoteEvents();
        this.channel = channel;        
    }   
    
    public MidiEvent getNoteOnEvent() throws javax.sound.midi.InvalidMidiDataException {
        if (null == noteOnEvent) createNoteEvents(); 
        return noteOnEvent;
    }

    public MidiEvent getNoteOffEvent() throws javax.sound.midi.InvalidMidiDataException {
        if (null == noteOffEvent) createNoteEvents();
        return noteOffEvent;
    }          
    
    private void clearNoteEvents() {
        this.noteOnEvent = null;
        this.noteOffEvent = null;
    }
                
    private void createNoteEvents() throws javax.sound.midi.InvalidMidiDataException {
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
               
    private static MidiEvent createNoteEvent(int command, int channel, int pitch, int velocity, long tick) throws javax.sound.midi.InvalidMidiDataException {	
        ShortMessage message = new ShortMessage();        
        message.setMessage(command, channel, pitch, velocity);
        MidiEvent event = new MidiEvent(message, tick);
        return event;
    }       
}
