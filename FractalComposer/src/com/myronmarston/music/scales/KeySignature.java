package com.myronmarston.music.scales;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;

/**
 * Represents a Midi key signature.
 * 
 * @author Myron
 */
public class KeySignature {
    /**
     * Enumerates the two main tonalities: Major and Minor.
     */
    public enum MajorOrMinor {
        /**
         * Major tonality.
         */
        Major((byte) 0),
        
        
        /**
         * Minor tonality.
         */
        Minor((byte) 1);
        
        private final byte midiValue;

        /**
         * Gets the midi value for this tonality.
         * 
         * @return 0 for major, 1 for minor
         */
        public byte getMidiValue() {
            return midiValue;
        }

        private MajorOrMinor(byte midiValue) {
            this.midiValue = midiValue;
        }        
    }
    
    private int numberOfFlatsOrSharps;
    private MajorOrMinor majorOrMinor;
    private MidiEvent keySignatureMidiEvent;
        
    /**
     * Constructor.
     * 
     * @param numberOfFlatsOrSharps a negative value indicates a number of flats
     *        and a positive value indicates a number of sharps
     * @param majorOrMinor this tonality of this key
     */
    public KeySignature(int numberOfFlatsOrSharps, MajorOrMinor majorOrMinor) {
        this.setNumberOfFlatsOrSharps(numberOfFlatsOrSharps);
        this.setMajorOrMinor(majorOrMinor);        
    }
    
    /**
     * Gets the tonality of this key.
     * 
     * @return either major or minor
     */
    public MajorOrMinor getMajorOrMinor() {
        return majorOrMinor;
    }

    /**
     * Sets the tonality of this key.
     * 
     * @param majorOrMinor either major or minor
     */
    public void setMajorOrMinor(MajorOrMinor majorOrMinor) {
        this.majorOrMinor = majorOrMinor;
        this.clearMidiKeySignatureEvent();
    }

    /**
     * Gets the number of flats or sharps for this key.  A negative number 
     * indicates flats and a positive number indicates sharps.
     * 
     * @return the number of flats or sharps
     */
    public int getNumberOfFlatsOrSharps() {
        return numberOfFlatsOrSharps;
    }

    /**
     * Sets the number of flats or sharps for this key.  A negative number 
     * indicates flats and a positive number indicates sharps.
     * 
     * @param numberOfFlatsOrSharps the number of flats or sharps
     */
    public void setNumberOfFlatsOrSharps(int numberOfFlatsOrSharps) {
        // Only values between -7 and 7 are valid
        // Users will never set the key signature directly; they will just choose
        // a scale.  So, I'm using an assertion rather than an exception since
        // a bad value will be a programmer error, not a user error.
        assert numberOfFlatsOrSharps >= -7 && numberOfFlatsOrSharps <= 7 : numberOfFlatsOrSharps;
        
        this.numberOfFlatsOrSharps = numberOfFlatsOrSharps;  
        this.clearMidiKeySignatureEvent();
    }
    
    /**
     * Gets the midi key signature event.  This is cached to improve performance.
     * 
     * @return the midi event
     * @throws javax.sound.midi.InvalidMidiDataException thrown if there is
     *         invalid midi data
     */
    public MidiEvent getKeySignatureMidiEvent() throws InvalidMidiDataException {
        if (this.keySignatureMidiEvent == null) this.keySignatureMidiEvent = generateMidiKeySignatureEvent();
        return this.keySignatureMidiEvent;
    }
    
    /**
     * Generates the midi key signature event.
     * 
     * @return the midi event
     * @throws javax.sound.midi.InvalidMidiDataException thrown if there is 
     *         invalid midi data
     */
    protected MidiEvent generateMidiKeySignatureEvent() throws InvalidMidiDataException {
        // See http://www.sonicspot.com/guide/midifiles.html for a description of the contents of this message.
        MetaMessage ksMessage = new MetaMessage();
        
        byte[] ksMessageData = new byte[2];
        ksMessageData[0] = (byte) this.getNumberOfFlatsOrSharps();
        ksMessageData[1] = this.getMajorOrMinor().getMidiValue();       
       
        ksMessage.setMessage(89,            // 89 is the type for a key signature message
                             ksMessageData, // the key signature data
                             ksMessageData.length); // the size of the data array
        
        return new MidiEvent(ksMessage, 0);
    }
    
    /**
     * Clears the midi key signature event field.  Should be called anytime a
     * field that is used to generate the midi event changes.
     */
    protected void clearMidiKeySignatureEvent() {
        this.keySignatureMidiEvent = null;
    }
}
