package com.myronmarston.music;

import com.myronmarston.music.scales.Scale;

import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;

import java.util.ArrayList;
import javax.sound.midi.Sequence;
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
    public Fraction getDuration() {
        Fraction duration = new Fraction(0, 1);
        for (Note n : this) duration = duration.plus(n.getDuration());        
        return duration;        
    }
    
    /**
     * Fills the given midi track with the notes from this NoteList.
     * 
     * @param sequence the midi sequence to add the track to
     * @param scale the scale to use
     * @param startTime the time the first note should be played, in whole 
     *        notes
     * @return the midi track that was created and filled
     * @throws javax.sound.midi.InvalidMidiDataException if there is any invalid
     *         midi data
     */
    public Track createAndFillMidiTrack(Sequence sequence, Scale scale, Fraction startTime) throws InvalidMidiDataException {
        MidiNote midiNote = null;
        Track track = sequence.createTrack();
        // in Midi, the tick resolution is based on quarter notes, but we use whole notes...
        int midiTicksPerWholeNote = sequence.getResolution() * 4; 
        
        for (Note note : this) {
            midiNote = note.convertToMidiNote(scale, startTime, midiTicksPerWholeNote);
            
            track.add(midiNote.getNoteOnEvent());
            track.add(midiNote.getNoteOffEvent());
            
            //The next note start time will be the end of this note...
            startTime = startTime.plus(note.getDuration());
        }       
        
        return track;
    }
}
