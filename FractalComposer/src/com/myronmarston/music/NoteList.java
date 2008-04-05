package com.myronmarston.music;

import com.myronmarston.music.scales.Scale;

import java.util.ArrayList;
import javax.sound.midi.Track;
import javax.sound.midi.InvalidMidiDataException;

/**
 * NoteList contains a sequence of notes.
 * 
 * @author Myron
 */
public class NoteList extends ArrayList<Note> {
    // Used to serialize the class.  Change this if the class has a change significant enough to change the way the class is serialized.
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor.
     */
    public NoteList() {
        super();
    }
    
    /**
     * Constructor.
     * 
     * @param initialCapacity initial capacity for the list
     */
    public NoteList(int initialCapacity) {
        super(initialCapacity);        
    }
    
    /**
     * Gets the first note you can hear--the first note that's not a rest.
     * 
     * @return the first note that is audible, or null
     */
    public Note getFirstAudibleNote() {
        for (Note n : this) {
            if (!n.isRest()) {
                return n;
            }                
        }
        
        return null;
    }
    
    /**
     * Gets the total length of the note list.
     * 
     * @return the duration of the note list
     */
    public double getDuration() {
        double duration = 0d;
        for (Note n : this) duration += n.getDuration();        
        return duration;        
    }
    
    /**
     * Fills the given midi track with the notes from this NoteList.
     * 
     * @param track the midi track to fill
     * @param scale the scale to use
     * @param startTime the time the first note should be played, in quarter 
     *        notes
     * @throws javax.sound.midi.InvalidMidiDataException if there is any invalid 
     *         midi data
     */
    public void fillMidiTrack(Track track, Scale scale, double startTime) throws InvalidMidiDataException {
        MidiNote midiNote = null;
        
        for (Note note : this) {
            midiNote = scale.convertToMidiNote(note, startTime);
            
            track.add(midiNote.getNoteOnEvent());
            track.add(midiNote.getNoteOffEvent());
            
            //The next note start time will be the end of this note...
            startTime = midiNote.getNoteEnd();
        }                
    }
}
