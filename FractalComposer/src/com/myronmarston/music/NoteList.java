/*
 * Copyright 2008, Myron Marston <myron DOT marston AT gmail DOT com>
 *
 * This file is part of Fractal Composer.
 *
 * Fractal Composer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option any later version.
 *
 * Fractal Composer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Fractal Composer.  If not, see <http://www.gnu.org/licenses/>. 
 */

package com.myronmarston.music;

import com.myronmarston.music.scales.Scale;
import com.myronmarston.util.MathHelper;
import com.myronmarston.util.Fraction;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

/**
 * NoteList contains a sequence of notes.
 * 
 * @author Myron
 */
public class NoteList extends ArrayList<Note> {
    // Used to serialize the class.  Change this if the class has a change significant enough to change the way the class is serialized.
    private static final long serialVersionUID = 1L;
    
    private transient Instrument instrument;
    
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
     * Gets the instrument for this note list.
     * 
     * @return the instrument
     */
    public Instrument getInstrument() {
        return instrument;
    }

    /**
     * Sets the instrument to use for this note list.
     *  
     * @param instrument the instrument
     */     
    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
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
        // TODO: throw exception if the note list is just a single rest
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
    
    /**
     * Gets a list of notes with all rests normalized.  Adjacent rests are 
     * combined into one longer rest.
     * 
     * @return list of notes with normalized rests
     */
    public List<Note> getListWithNormalizedRests() {
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
     * Updates the scale on all the notes.  This should only be called when you
     * know that all the notes of this note list have the same note list.
     * 
     * @param scale the new scale for the notes
     */
    public void updateScale(Scale scale) {
        Scale originalScale = null;
        for (Note n : this) {
            if (n.isRest()) continue;
            if (originalScale == null) originalScale = n.getScale();
            
            // we should never update the scale on all the notes if they have 
            // mixed scales--in this case, this method is being used improperly
            assert n.getScale() == originalScale : n.getScale();
            n.setScale(scale);            
        }
    }
    
    @Override
    /**
     * Clones the note list.  Each individual note is also cloned.
     */
    public Object clone() {
        NoteList clone = (NoteList) super.clone();
        
        for (int i = 0; i < clone.size(); i++) {
            clone.set(i, (Note) clone.get(i).clone());
        }
        
        return clone;
    }        
}
