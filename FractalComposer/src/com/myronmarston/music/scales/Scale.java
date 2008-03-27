package com.myronmarston.music.scales;

import com.myronmarston.music.MidiNote;
import com.myronmarston.music.Note;

/**
 * The Scale interface will be used to convert a Note to a MidiNote.
 * Since each Note contains data on its identity relative to a given scale,
 * the Scale must be used to convert it to a concrete MidiNote (i.e. pitch, etc).
 * @author Myron 
 */
public interface Scale {
    MidiNote convertToMidiNote(Note note, double startTime);
}
