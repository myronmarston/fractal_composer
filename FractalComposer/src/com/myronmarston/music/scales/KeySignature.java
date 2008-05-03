package com.myronmarston.music.scales;

import com.myronmarston.music.NoteName;

import java.lang.reflect.UndeclaredThrowableException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;

/**
 * Represents a Midi key signature.
 * 
 * @author Myron
 */
public class KeySignature {       
    private NoteName keyName;
    private Tonality tonality;
    private MidiEvent keySignatureMidiEvent;           
    
    /**
     * Constructor.
     * 
     * @param tonality major or minor
     * @param keyName the tonal center of the key
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException 
     *         if the the key is invalid-e.g., a key that would have double 
     *         flats or sharps, such as A# major.
     */
    public KeySignature(Tonality tonality, NoteName keyName) throws InvalidKeySignatureException {
        checkValidityOfKeySignature(keyName, tonality);         
        this.tonality = tonality;
        this.keyName = keyName;
    }
    
    /**
     * Gets the tonality of this key.
     * 
     * @return either major or minor
     */
    public Tonality getTonality() {
        return tonality;
    }

    /**
     * Gets the number of flats or sharps for this key.  A negative number 
     * indicates flats and a positive number indicates sharps.
     * 
     * @return the number of flats or sharps
     */
    public int getNumberOfFlatsOrSharps() {
        return this.getTonality().getSharpsOrFlatsForKeyName(this.getKeyName());
    }

    /**
     * Gets the note name of the tonal center.
     * 
     * @return the name of the key
     */
    public NoteName getKeyName() {
        return keyName;
    }

    /**
     * Sets the note name of the tonal center.
     * 
     * @param keyName the name of the key
     * @throws com.myronmarston.music.scales.InvalidKeySignatureException
     *         if the the key is invalid-e.g., a key that would have double 
     *         flats or sharps, such as A# major.
     */
    public void setKeyName(NoteName keyName) throws InvalidKeySignatureException {
        checkValidityOfKeySignature(keyName, this.getTonality());
        this.keyName = keyName;
        this.clearMidiKeySignatureEvent();
    }        
    
    private static void checkValidityOfKeySignature(NoteName keyName, Tonality tonality) throws InvalidKeySignatureException {
        if (keyName == null || tonality == null) return;
        
        if (!tonality.isValidKeyName(keyName)) {
            throw new InvalidKeySignatureException(keyName.toString());
        }
    }
    
    /**
     * Gets the midi key signature event.  This is cached to improve performance.
     * 
     * @return the midi event     
     */
    public MidiEvent getKeySignatureMidiEvent() {
        if (this.keySignatureMidiEvent == null) { 
            try {
                this.keySignatureMidiEvent = generateMidiKeySignatureEvent();
            } catch (InvalidMidiDataException ex) {
                // our logic should prevent this exception from ever occurring, 
                // so we transform this to an unchecked exception instead of 
                // having to declare it on our method.
                throw new UndeclaredThrowableException(ex, "The key signature midi event could not be created.  This indicates a programming error of some sort.");                
            }
        }
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
        ksMessageData[1] = this.getTonality().getMidiValue();       
       
        ksMessage.setMessage(89,            // 89 is the type for a key signature message
                             ksMessageData, // the key signature data
                             ksMessageData.length); // the size of the data array
        
        return new MidiEvent(ksMessage, 0);
    }
    
    /**
     * Clears the midi key signature event field.  Should be called anytime a
     * field that is used to generate the midi event changes.
     */
    private void clearMidiKeySignatureEvent() {
        this.keySignatureMidiEvent = null;
    }
}
