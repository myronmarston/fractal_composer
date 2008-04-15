package com.myronmarston.music.settings;

import com.myronmarston.util.MathHelper;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;

/**
 * Class that holds the time signature for the fractal piece.  A time signature
 * is defined by a numerator and a denominator.  Both values must be positive
 * and the denominator must be a power of 2 (i.e., 1, 2, 4, 8, 16, etc).
 * 
 * @author Myron
 */
public class TimeSignature {
    private int numerator;
    private int denominator;
    private int denominatorPowerOf2;

    /**
     * Constructor.
     * 
     * @param numerator the number of counts per measure
     * @param denominator the kind of note that gets one count
     * @throws com.myronmarston.music.InvalidTimeSignatureException thrown when
     *         invalid values are passed
     */
    public TimeSignature(int numerator, int denominator) throws InvalidTimeSignatureException {
        // call the setters instead of setting the fields directly in order to invoke their logic
        this.setDenominator(denominator);
        this.setNumerator(numerator);        
    }
        
    /**
     * Gets the kind of note that gets one count.
     * 
     * @return the denominator
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * Sets the kind of note that gets one count.  Should be a power of 2.
     * 
     * @param denominator the value
     * @throws com.myronmarston.music.InvalidTimeSignatureException thrown when
     *         the denominator is non-positive or not a power of 2.
     */
    public void setDenominator(int denominator) throws InvalidTimeSignatureException {
        if (denominator <= 0) throw new NonPositiveTimeSignatureException(denominator);
        
        double log2Result = MathHelper.log2(denominator);
        if (log2Result != Math.floor(log2Result)) {
            // the denominator is not an integer power of 2--throw an exception
            throw new TimeSignatureDenominatorNotAPowerOf2Exception(denominator);            
        }
        
        this.denominatorPowerOf2 = (int) log2Result;
        this.denominator = denominator;
    }

    /**
     * Gets the number of counts per bar.
     * 
     * @return the numerator
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * Sets the number of counts per bar.
     * 
     * @param numerator the value
     * @throws com.myronmarston.music.NonPositiveTimeSignatureException thrown 
     *         when the passed value is not positive
     */
    public void setNumerator(int numerator) throws NonPositiveTimeSignatureException {
        if (numerator <= 0) throw new NonPositiveTimeSignatureException(numerator);
        this.numerator = numerator;
    }

    /**
     * Gets the base 2 logarithm of the denominator.
     * 
     * @return the base 2 logarithm of the denominator
     */
    protected int getDenominatorPowerOf2() {
        return denominatorPowerOf2;
    }      
    
    /**
     * Creates the Midi time signature event.
     * 
     * @return the midi time signature event
     * @throws javax.sound.midi.InvalidMidiDataException thrown when the midi 
     *         data is invalid
     */
    public MidiEvent createMidiTimeSignatureEvent() throws InvalidMidiDataException {
        // See http://www.sonicspot.com/guide/midifiles.html for a description of the contents of this message.
        MetaMessage tsMessage = new MetaMessage();
        
        byte[] tsMessageData = new byte[4];
        tsMessageData[0] = (byte) this.getNumerator();
        tsMessageData[1] = (byte) this.getDenominatorPowerOf2();
        tsMessageData[2] = 24; // metronome pulse
        tsMessageData[3] = 8;  // number of 32nds per quarter note
       
        tsMessage.setMessage(88,            // 88 is the type for a time signature message
                             tsMessageData, // the time signature data
                             4);            // the size of the data array
        
        return new MidiEvent(tsMessage, 0);
    }
}
