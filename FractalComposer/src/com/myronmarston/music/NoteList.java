package com.myronmarston.music;

import com.myronmarston.music.scales.Scale;
import com.myronmarston.util.MathHelper;
import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;

import java.util.*;
import javax.sound.midi.*;

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
     * Creates a new note list based on the given note list string.
     * 
     * @param noteListString string containing space-seperated notes, each of 
     *        the form 'F#4,1/4,PP'
     * @param scale the scale to use to determine the note's pitch information
     * @return the new note list
     * @throws com.myronmarston.music.NoteStringParseException thrown if the
     *         note list string is invalid
     */
    public static NoteList parseNoteListString(String noteListString, Scale scale) throws NoteStringParseException {                
        Note note = null;
        Fraction defaultDuration = null;
        Integer defaultVolume = null;                        
        StringTokenizer st = new StringTokenizer(noteListString);
        NoteList list = new NoteList();
        
        while (st.hasMoreTokens()) {
            note = Note.parseNoteString(st.nextToken(), scale, defaultDuration, defaultVolume);
            
            // get our defaults for the next note from this note...
            defaultDuration = note.getDuration();            
            if (!note.isRest()) defaultVolume = note.getVolume();
            
            list.add(note);
        }
        
        return list;
    }
    
    private List<Note> getListWithNormalizedRests() {
        ArrayList<Note> newList = new ArrayList<Note>();
        
        Fraction currentRestDuration = new Fraction(0, 1);
        for (Note n : this) {
            if (n.isRest()) {
                // sum up the rest durations...
                currentRestDuration = currentRestDuration.plus(n.getDuration());
            } else {
                if (currentRestDuration.numerator() != 0) {
                    // the note(s) previous to this one were rests, and we now have
                    // the total duration.  Create a rest of this length.
                    newList.add(Note.createRest(currentRestDuration));
                    
                    // rest the current rest duration to zero
                    currentRestDuration = new Fraction(0, 1);
                } 
                
                newList.add(n);
            }
        }
        
        if (currentRestDuration.numerator() != 0) newList.add(Note.createRest(currentRestDuration)); 
        
        return newList;
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
        MidiNote thisMidiNote, lastMidiNote = null;
        Note lastNote = null;
        
        // make each track be on a different channel, but make sure we don't go over our total number of channels...
        int midiChannel = sequence.getTracks().length % MidiNote.MAX_CHANNEL;
        Track track = sequence.createTrack();
        // in Midi, the tick resolution is based on quarter notes, but we use whole notes...
        int midiTicksPerWholeNote = sequence.getResolution() * 4; 
        
        for (Note thisNote : this.getListWithNormalizedRests()) {
            thisMidiNote = thisNote.convertToMidiNote(scale, startTime, midiTicksPerWholeNote, midiChannel, true);                        
            
            if (lastMidiNote != null) {
                assert lastNote != null : lastNote;
                                
                if (thisMidiNote.getPitch() == lastMidiNote.getPitch() && lastNote.getScaleStep() != thisNote.getScaleStep()) {               
                    // the notes are different scale steps and should have different pitches.
                    // This can happen with notes like B# and C in the key of C.

                    if (lastNote.getChromaticAdjustment() != 0) {
                        lastMidiNote = lastNote.convertToMidiNote(scale, startTime.minus(thisNote.getDuration()), midiTicksPerWholeNote, midiChannel, false);
                    } else if (thisNote.getChromaticAdjustment() != 0) {
                        thisMidiNote = thisNote.convertToMidiNote(scale, startTime, midiTicksPerWholeNote, midiChannel, false);
                    } else {
                        // one of these notes should always have a chromatic 
                        // adjustment--otherwise, how do they have the same pitches
                        // but different scale steps?
                        assert false : "Neither last note '" + lastNote.toString() + "' nor this note '" + thisNote.toString() + "' have a chromatic adjustment.";
                    }
                    
                    assert thisMidiNote.getPitch() != lastMidiNote.getPitch() : "The midi notes have the same pitch and should not: " + thisMidiNote.getPitch();
                }                
                track.add(lastMidiNote.getNoteOnEvent());
                track.add(lastMidiNote.getNoteOffEvent());
            }                                      
            
            //The next note start time will be the end of this note...
            startTime = startTime.plus(thisNote.getDuration());
            
            lastMidiNote = thisMidiNote;
            lastNote = thisNote;
        }           
        
        track.add(lastMidiNote.getNoteOnEvent());
        track.add(lastMidiNote.getNoteOffEvent());
        
        return track;
    }        

    /**
     * Calculates the optimal midi tick resolution for the given collection of 
     * noteLists, based on the duration of the notes.
     * 
     * @param noteLists collection of noteLists
     * @return the midi tick resolution
     */
    public static int getMidiTickResolution(Collection<NoteList> noteLists) {        
        // next, figure out the resolution of our Midi sequence...
        ArrayList<Long> uniqueDurationDenominators = new ArrayList<Long>();
        for (NoteList nl : noteLists) {
            for (Note n : nl) {
                if (!uniqueDurationDenominators.contains(n.getDuration().denominator())) {
                    uniqueDurationDenominators.add(n.getDuration().denominator());
                }                
            }
        }        
        
        long resolution = MathHelper.leastCommonMultiple(uniqueDurationDenominators);
        assert resolution < Integer.MAX_VALUE;
        return (int) resolution;
    }
}
