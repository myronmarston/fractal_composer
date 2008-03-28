package com.myronmarston.music;

import com.myronmarston.music.scales.Scale;

import java.util.ArrayList;
import javax.sound.midi.Track;
import javax.sound.midi.InvalidMidiDataException;

/**
 * NoteList contains a list of notes that can be used to fill a track.
 * @author Myron
 */
public class NoteList extends ArrayList<Note> {
    // Used to serialize the class.  Change this if the class has a change significant enough to change the way the class is serialized.
    private static final long serialVersionUID = 1L;
    
    public NoteList() {
        super();
    }
    
    public NoteList(int initialCapacity) {
        super(initialCapacity);        
    }
    
    /**
     * Gets the first note you can hear--the first note that's not a rest;
     * @return
     */
    public Note getFirstAudibleNote() {
        for (Note n : this) {
            if (!n.isRest()) {
                return n;
            }                
        }
        
        return null;
    }
    
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
