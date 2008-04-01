package com.myronmarston.music.scales;

import com.myronmarston.music.MidiNote;
import com.myronmarston.music.Note;

/**
 * The Scale interface will be used to convert a Note to a MidiNote.
 * Since each Note contains data on its identity relative to a given scale,
 * the Scale must be used to convert it to a concrete MidiNote (e.g., pitch, etc).
 * 
 * @author Myron 
 */
public interface Scale {
    
    /**
     * Converts the given note to a Midi Note, that can then be used to get the
     * actual Midi note on and note off events.
     * 
     * @param note the note to convert
     * @param startTime when the note should be sounded, in quarter notes
     * @return the MidiNote
     */
    MidiNote convertToMidiNote(Note note, double startTime);
}
