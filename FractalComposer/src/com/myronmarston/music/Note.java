package com.myronmarston.music;

import com.myronmarston.music.settings.SegmentSettings;
import com.myronmarston.music.scales.Scale;
import com.myronmarston.music.NoteStringInvalidPartException.NoteStringPart;

import edu.oswego.cs.dl.util.concurrent.misc.Fraction;

import org.simpleframework.xml.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.*;

/**
 * Represents a note, relative to a particular scale.  This note will need to be
 * converted to midi events using the MidiNote class before it can be used in
 * a Midi track.  
 * 
 * @author Myron
 */
@Root
public class Note {
    @Attribute
    private int scaleStep; // number of scale steps above the tonic; 0 = tonic, 7 = octave, 9 = third an octave above, etc.
    
    @Attribute
    private int octave; // which octave the note should be in.  0 begins the first octave in Midi that contains the tonic.
    
    @Attribute
    private int chromaticAdjustment; // the number of half steps to adjust from the diatonic note; used if this note is an accidental
    
    @Element(required=false)
    private Fraction duration; // how long the note should last, in whole notes.
    
    @Attribute
    private int volume = MidiNote.DEFAULT_VELOCITY; // how loud the note should be on a scale from 0 to 127.    
    
    @Element(required=false)
    private SegmentSettings segmentSettings; // an object containing settings to apply to a segment of notes
    private static Pattern noteParser;
    
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
     * @param duration how long the note should last, in whole notes
     * @param volume how loud the note should be (0-127)
     */
    public Note(int scaleStep, int octave, int chromaticAdjustment, Fraction duration, int volume) {
        this.setScaleStep(scaleStep);
        this.setOctave(octave);
        this.setChromaticAdjustment(chromaticAdjustment);
        this.setDuration(duration);
        this.setVolume(volume);        
    }
    
    /**
     * Constructor.
     * 
     * @param scaleStep number of scale steps above the tonic; 0 = tonic, 
     *        7 = octave, 9 = third an octave above, etc.
     * @param octave which octave the note should be in.  0 begins the first 
     *        octave in Midi that contains the tonic.
     * @param chromaticAdjustment the number of half steps to adjust from the
     *        diatonic note; used if this note is an accidental
     * @param duration how long the note should last, in whole notes
     * @param volume how loud the note should be (0-127)
     * @param segmentSettings additional settings for a group of notes this note
     *        is a part of
     */
    public Note(int scaleStep, int octave, int chromaticAdjustment, Fraction duration, int volume, SegmentSettings segmentSettings) {
        this(scaleStep, octave, chromaticAdjustment, duration, volume);
        this.setSegmentSettings(segmentSettings);               
    }        
    
    /**
     * Copy Constructor.
     * 
     * @param inputNote note to copy     
     */
    public Note(Note inputNote) {
        this(inputNote.getScaleStep(), inputNote.getOctave(), inputNote.getChromaticAdjustment(), inputNote.getDuration(), inputNote.getVolume(), inputNote.getSegmentSettings());
    }
        
    /**
     * Creates a note that is a rest.
     * 
     * @param duration how long the rest should last, in whole notes
     * @return the rest     
     */
    static public Note createRest(Fraction duration) {
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
     * Gets the settings for the segment this note is a part of.
     * 
     * @return the segment settings
     */
    public SegmentSettings getSegmentSettings() {
        return segmentSettings;
    }

    /**
     * Sets the settings for the segment this note is a part of.
     * 
     * @param segmentSettings the segment settings
     */
    public void setSegmentSettings(SegmentSettings segmentSettings) {
        this.segmentSettings = segmentSettings;
    }    
    
    /**
     * Gets the chromatic adjustment on the segment settings, or the default 
     * chromatic adjustment if there are no settings.
     * 
     * @return the chromatic adjustment on the segment settings
     */
    private int getSegmentSettingsChromaticAdjustment() {
        if (this.getSegmentSettings() == null) return 0;
        return this.getSegmentSettings().getChromaticAdjustment();
    }
    
    /**
     * Gets how long the note should last, in whole notes.
     * 
     * @return the duration
     */
    public Fraction getDuration() {
        return duration;
    }       

    /**
     * Sets how long the note should last, in whole notes.
     * 
     * @param duration the duration
     */
    public void setDuration(Fraction duration) {
        if (duration != null && duration.asDouble() <= 0) throw new IllegalArgumentException("The duration must be greater than zero.");
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
    
    /**
     * Gets a regular expression pattern that can be used to parse a note string.
     * 
     * @return the regEx pattern
     */
    static private Pattern getNoteParser() {
        if (Note.noteParser == null) {
                               
            Note.noteParser = Pattern.compile(                   
                NoteName.getRegexPattern() +                // NoteName--everything before the first digit or comma                
                "([^,])?" +                                 // octave, optional if the note name is a rest; can be 0..9
                "(?:,(\\d?\\d?\\d?)\\/?(\\d?\\d?\\d?))?" +  // duration in fractional form, e.g. 1/4; up to 3 digits allowed to allow for 128th notes                
                "(?:,(.*))?",                               // the dynamic--this will catch the rest of the string; our parsing code will handle this or throw an exception as necessary
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.CANON_EQ);
        }        
        
        return Note.noteParser;
    }
    
    /**
     * Parses a note string such as F#4,1/8,MF.
     * 
     * @param noteString string to parse
     * @param scale the scale to use to determine the scaleStep and 
     *        chromaticAdjustment
     * @param defaultDuration duration to use if the noteString does not contain
     *        a duration
     * @param defaultVolume volume to use if the noteString does not contain a 
     *        volume
     * @return a new Note with fields set based on the parsed noteString
     * @throws com.myronmarston.music.NoteStringParseException thrown when the
     *         noteString cannot be parsed
     */
    public static Note parseNoteString(String noteString, Scale scale, Fraction defaultDuration, Integer defaultVolume) throws NoteStringParseException {
        if (defaultDuration == null) defaultDuration = new Fraction(1, 4);
        if (defaultVolume == null) defaultVolume = Dynamic.MF.getMidiVolume();
        
        Matcher match = Note.getNoteParser().matcher(noteString);        
        if (match.matches()) {     
            NoteName noteName = parseNoteString_getNoteName(noteString, match, 1);   
            Fraction duration = parseNoteString_getDuration(noteString, match, 3, 4, defaultDuration);
            Note newNote;
            
            if (noteName == null) { // indicates the note is a rest...
                newNote = Note.createRest(duration);
            } else {                
                int octave = parseNoteString_getOctave(noteString, match, 2);                
                int volume = parseNoteString_getVolume(noteString, match, 5, defaultVolume);                
            
                newNote = new Note();
                newNote.setDuration(duration);
                newNote.setVolume(volume);
                scale.setNotePitchValues(newNote, noteName);
                                
                // The octave number is dependent on the scale.  For example, 
                // the note C4 (middle C) should parse as scale step 2, octave 3 
                // for the A minor scale, because the A octave is higher than 
                // the C octave.
                // I tried figuring out the logic for this, but it's very 
                // complicated and I couldn't figure it out after a few hours.
                // So, instead we use the result of parsing the note using
                // the chromatic scale as our baseline, and compare to that,
                // using the difference between the baseline midi pitch number
                // and the midi pitch number we get from our scale to calculate
                // the correct octave.                
                if (scale == Scale.DEFAULT) {
                    // this is the chromatic scale, so just set the octave directly...
                    newNote.setOctave(octave);                                
                } else {                    
                    Note testNote = parseNoteString(noteString, Scale.DEFAULT, defaultDuration, defaultVolume);
                    int midiPitchNum = testNote.getMidiPitchNumber(Scale.DEFAULT, true); 
                    int midiPitchNumWithoutOctave = newNote.getMidiPitchNumber(scale, true);
                    int difference = midiPitchNum - midiPitchNumWithoutOctave;
                    
                    assert difference % Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE == 0 : difference;
                    octave = difference / Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE;
                    newNote.setOctave(octave);
                    
                    assert newNote.getMidiPitchNumber(scale, true) == midiPitchNum;
                }                
            }            
            
            return newNote;
        } else {
            throw new IncorrectNoteStringException(noteString);            
        }                
    }             
    
    /**
     * Gets the note name for the parseNoteString method.
     * 
     * @param noteString the noteString being parsed
     * @param match the regex match object
     * @param matchGroupIndex the index for the captured group containing 
     *        noteName
     * @return the NoteName, or null if the note should be a rest
     * @throws com.myronmarston.music.NoteStringInvalidPartException
     *         thrown if the noteString is missing a note name or 'R' for rest
     */
    static private NoteName parseNoteString_getNoteName(String noteString, Matcher match, int matchGroupIndex) throws NoteStringInvalidPartException {
        assert match.matches() : match;
        String noteNameStr = match.group(matchGroupIndex);
        
        if (noteNameStr == null) throw new NoteStringInvalidPartException(noteString, NoteStringPart.NOTE_NAME);
        if (noteNameStr.toUpperCase(Locale.ENGLISH).contentEquals("R")) return null;
        
        NoteName noteName = NoteName.getNoteName(noteNameStr);
        if (noteName == null) throw new NoteStringInvalidPartException(noteString, NoteStringPart.NOTE_NAME);
        return noteName;         
    }
    
    /**
     * Gets the octave for the parseNoteString method.
     * 
     * @param noteString the noteString being parsed
     * @param match the regex match object
     * @param matchGroupIndex the index for the captured group containing the
     *        octave
     * @return the octave
     * @throws com.myronmarston.music.NoteStringInvalidPartException 
     *         thrown if the noteString does not contain an octave
     */
    private static int parseNoteString_getOctave(String noteString, Matcher match, int matchGroupIndex) throws NoteStringInvalidPartException {
        assert match.matches() : match;
        String octaveStr = match.group(matchGroupIndex);
        if (octaveStr == null) throw new NoteStringInvalidPartException(noteString, NoteStringPart.OCTAVE);
        
        try {
            return Integer.parseInt(octaveStr);
        } catch (NumberFormatException ex) {
            throw new NoteStringInvalidPartException(noteString, NoteStringPart.OCTAVE);
        }        
    }
    
    /**
     * Gets the duration fraction for the parseNoteString method.
     * 
     * @param noteString the noteString being parsed
     * @param match the regex match object
     * @param numeratorMatchGroupIndex the index for the captured group 
     *        containing the duration fraction numerator
     * @param denominatorMatchGroupIndex the index for the captured group 
     *        containing the duration fraction denominator
     * @param defaultDuration duration to use if the noteString does not have a
     *        duration
     * @return the duration fraction
     * @throws com.myronmarston.music.NoteStringInvalidPartException
     *         thrown if the noteString contains part of the duration fraction
     *         but not all of it
     */
    private static Fraction parseNoteString_getDuration(String noteString, Matcher match, int numeratorMatchGroupIndex, int denominatorMatchGroupIndex, Fraction defaultDuration) throws NoteStringInvalidPartException {        
        assert match.matches() : match;
        String durationNumStr = match.group(numeratorMatchGroupIndex);
        String durationDenStr = match.group(denominatorMatchGroupIndex);
        if (durationNumStr == null && durationDenStr == null) {
            // This note doesn't have a duration; instead use the default duration
            return defaultDuration;
        } else if (durationNumStr == null || durationNumStr.isEmpty() || durationDenStr == null || durationDenStr.isEmpty()) {
            // The note string contains part of a duration, but not a complete one.
            throw new NoteStringInvalidPartException(noteString, NoteStringPart.RHYTHMIC_DURATION);
        } else {
            int durationNum = Integer.parseInt(durationNumStr);
            int durationDen = Integer.parseInt(durationDenStr);

            if (durationDen == 0) {
                throw new NoteStringInvalidPartException(noteString, NoteStringPart.RHYTHMIC_DURATION);
            }
            return new Fraction(durationNum, durationDen);
        }
    }
    
    /**
     * Gets the volume for the parseNoteString method.
     * 
     * @param match the regex match object
     * @param matchGroupIndex the index for the captured group containing the
     *        dynamic
     * @param defaultVolume volume to use if the noteString does not have a 
     *        dynamic
     * @return the volume
     */
    private static int parseNoteString_getVolume(String noteString, Matcher match, int matchGroupIndex, int defaultVolume) throws NoteStringInvalidPartException {        
        String dynamicStr = match.group(matchGroupIndex);
        if (dynamicStr == null || dynamicStr.isEmpty()) {
            return defaultVolume;
        } else {             
            try {
                return Dynamic.valueOf(dynamicStr.toUpperCase(Locale.ENGLISH)).getMidiVolume();
            } catch (IllegalArgumentException ex) {
                throw new NoteStringInvalidPartException(noteString, NoteStringPart.DYNAMIC);
            }            
        }
    }
            
    /**
     * Sometimes after transformations are applied to a note, the scale step is
     * less than 0 or greater than the number of scale steps in an octave.  
     * This method "normalizes" the note--adjusts its scaleStep and octave so 
     * that it is the same note, but the scaleStep is between 0 and the number 
     * of scale steps.
     * 
     * @param note the note to normalize
     * @return the normalized note
     */
    private Note getNormalizedNote(Scale scale) {
        Note tempNote = new Note(this);
        int numScaleStepsInOctave = scale.getScaleStepArray().length; // cache it
        
        // put the note's scaleStep into the normal range (0..numScaleSteps - 1), adjusting the octaves.
        while(tempNote.getScaleStep() < 0) {
            tempNote.setScaleStep(tempNote.getScaleStep() + numScaleStepsInOctave);
            tempNote.setOctave(tempNote.getOctave() - 1);
        }                
        
        while(tempNote.getScaleStep() > numScaleStepsInOctave - 1) {
            tempNote.setScaleStep(tempNote.getScaleStep() - numScaleStepsInOctave);
            tempNote.setOctave(tempNote.getOctave() + 1);
        }
        
        assert tempNote.getScaleStep() >= 0 && tempNote.getScaleStep() < numScaleStepsInOctave : tempNote.getScaleStep();
        
        return tempNote;
    }
    
    /**
     * Converts from whole notes to ticks, based on the midi tick resolution.
     * 
     * @param wholeNotes the number of whole notes
     * @param midiTickResolution the number of ticks per whole note
     * @return the number of ticks
     */
    private static long convertWholeNotesToTicks(Fraction wholeNotes, int midiTickResolution) {
        Fraction converted = wholeNotes.times(midiTickResolution);        
        
        // converting to midi ticks should result in an integral number of ticks
        // because our tick resolution should be chosen based on what will
        // produce this.  Test that this is in fact the case...
        assert converted.denominator() == 1 : converted;
     
        // since the denominator is 1, the converted value is equal to the numerator...
        return converted.numerator();
    }        
    
    /**
     * Gets the midi pitch number for this note, taking into account the scale
     * step, octave and chromatic adjustment.
     * 
     * @param scale the scale to use to determine the pitch
     * @param keepExactPitch true to keep the exact pitch specified by the note
     *        parameters; false to allow the chromaticAdjustment to be changed
     *        if it would create a note that is already found in the scale
     * @return the midi pitch number
     */
    private int getMidiPitchNumber(Scale scale, boolean keepExactPitch) {
        if (this.isRest()) return 0;
        
        int[] scaleSteps = scale.getScaleStepArray(); // cache it
        Note normalizedNote = this.getNormalizedNote(scale);        
        int chromaticAdj = normalizedNote.getChromaticAdjustment();
        int chromaticAdjDecrementer = chromaticAdj > 0 ? 1 : -1;
        int testPitchNum;
        
        // Figure out the chromatic adjustment to use for the note.  We attempt to
        // use the given chromatic adjustment, but if it results in producing a pitch
        // that is already present in our scale, we reduce the chromatic adjustment
        // until we get a pitch not present in our scale or we reach 0.
        // We do this because of cases such as a phrase F# G in the key of C.
        // When our fractal algorithm transposes this and it becomes B# C, we
        // wind up with two of the same pitches in a row.  This loses the "shape"
        // that we are dealing with, so we adjust the accidental as necessary.        
        while (!keepExactPitch && Math.abs(chromaticAdj) > 0) {
            testPitchNum = (scaleSteps[normalizedNote.getScaleStep()] // the raw (natural) pitch number                    
                    + chromaticAdj                                    // the chromatic adjustment to test...                    
                    + Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE)         // necessary for notes like Cb, to prevent testPitchNum from being negative
                    % Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE;         // mod it, to put it in the range 0-12
            
            assert testPitchNum >= 0 && testPitchNum < Scale.NUM_CHROMATIC_PITCHES_PER_OCTAVE : testPitchNum;                        
            
            // stop decrementing our chromatic adjustment if our scale lacks this pitch...
            if (Arrays.binarySearch(scaleSteps, testPitchNum) < 0) break;
            
            // move the chromatic adjustment towards zero...
            chromaticAdj -= chromaticAdjDecrementer;
        }
        
        return scaleSteps[normalizedNote.getScaleStep()]       // half steps above tonic
                + chromaticAdj                                 // the note's chromatic adjustment
                + this.getSegmentSettingsChromaticAdjustment() // chromatic adjustment for the segment
                + scale.getKeyName().getMidiPitchNumberAtOctave(normalizedNote.getOctave()); // take into account the octave
    }
    
    /**
     * Converts the note to a Midi Note, that can then be used to get the
     * actual Midi note on and note off events.
     * 
     * @param scale the scale to use
     * @param startTime the time this note should be played
     * @param midiTickResolution the number of ticks per whole note for the
     *        midi sequence
     * @param channel the midi channel for this note, 0-15
     * @param keepExactPitch true to keep the exact pitch specified by the note
     *        parameters; false to allow the chromaticAdjustment to be changed
     *        if it would create a note that is already found in the scale
     * @return the MidiNote
     */
    public MidiNote convertToMidiNote(Scale scale, Fraction startTime, int midiTickResolution, int channel, boolean keepExactPitch) {        
        MidiNote midiNote = new MidiNote();       
            
        midiNote.setDuration(convertWholeNotesToTicks(this.getDuration(), midiTickResolution));
        midiNote.setStartTime(convertWholeNotesToTicks(startTime, midiTickResolution));
        midiNote.setVelocity(this.getVolume());        
        midiNote.setPitch(this.getMidiPitchNumber(scale, keepExactPitch));
        midiNote.setChannel(channel);
        
        return midiNote;
    }               
    
    @Override
    public String toString() {
        return String.format("Note = scaleStep %d, octave %d, chromaticAdjustment %d, duration %s, volume %d", this.scaleStep, this.octave, this.chromaticAdjustment, this.duration.toString(), this.volume);
    }

    // equals and hashCode were auto-generated by the Netbeans IDE, and modified
    // by hand to deal with the segment settings chromatic adjustment
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
        if (this.duration != other.duration && (this.duration == null || !this.duration.equals(other.duration))) {
            return false;
        }
        if (this.volume != other.volume) {
            return false;
        }
        
        // this part was coded by hand; the rest was auto-generated
        if (!this.isRest()) {
            // we only care about comparing segment settings if the note is not a rest
            if (this.getSegmentSettingsChromaticAdjustment() != other.getSegmentSettingsChromaticAdjustment()) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.scaleStep;
        hash = 89 * hash + this.octave;
        hash = 89 * hash + this.chromaticAdjustment;
        hash = 89 * hash + (this.duration != null ? this.duration.hashCode() : 0);
        hash = 89 * hash + this.volume;
        
        // this part was coded by hand; the rest was auto-generated
        hash = 89 * hash + (this.getSegmentSettingsChromaticAdjustment());
        
        return hash;
    }                   
}
