package com.myronmarston.music.settings;

import com.myronmarston.util.MathHelper;

import java.lang.reflect.UndeclaredThrowableException;
import org.simpleframework.xml.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;

import java.util.regex.*;

/**
 * Class that holds the time signature for the fractal piece.  A time signature
 * is defined by a numerator and a denominator.  Both values must be positive
 * and the denominator must be a power of 2 (i.e., 1, 2, 4, 8, 16, etc).
 * 
 * @author Myron
 */
@Root
public class TimeSignature {
    @Attribute
    private int numerator;
    
    @Attribute
    private int denominator;
    
    @Attribute
    private int denominatorPowerOf2;
    private MidiEvent midiTimeSignatureEvent;
    
    /**
     * The time signature that should be used if the user does not specify
     * one--common time, i.e. 4/4.
     */
    public final static TimeSignature DEFAULT;        
        
    /**
     * Taken from http://www.sonicspot.com/guide/midifiles.html.
     */    
    private final static int TIME_SIGNATURE_META_MESSAGE_TYPE = 88;    
    
    /**
     * A Regex pattern that 
     */
    public final static String REGEX_PATTERN_STRING = "^(\\d+)\\s*\\/\\s*(1|2|4|8|16|32|64|128|256|512)$";
    private final static Pattern REGEX_PATTERN;
    
    /**
     * Initializes the default time signature.
     */
    static {
        try {
            // create a default time signature
            DEFAULT = new TimeSignature(4, 4);
        } catch (InvalidTimeSignatureException ex) {                
            // 4/4 should always be a valid time signature, so just catch this exception 
            // and throw an unchecked exception so we don't have to declare it...
            throw new UndeclaredThrowableException(ex, "An exception occurred while creating the default 4/4 time signature.  This indicates a programming error.");            
        }
        
        REGEX_PATTERN = Pattern.compile(REGEX_PATTERN_STRING);
    }

    /**
     * Constructor.
     * 
     * @param numerator the number of counts per measure
     * @param denominator the kind of note that gets one count
     * @throws com.myronmarston.music.settings.InvalidTimeSignatureException
     *         thrown when invalid values are passed 
     */
    public TimeSignature(int numerator, int denominator) throws InvalidTimeSignatureException {
        // call the setters instead of setting the fields directly in order to invoke their logic
        this.setDenominator(denominator);
        this.setNumerator(numerator);        
    }
    
    /**
     * Constructor.
     * 
     * @param timeSigStr a time signature in a form like '4/4'
     * @throws com.myronmarston.music.settings.InvalidTimeSignatureException if
     *         the string cannot be parsed
     */
    public TimeSignature(String timeSigStr) throws InvalidTimeSignatureException {
        Matcher m = REGEX_PATTERN.matcher(timeSigStr);
        if (m.matches()) {
            this.setNumerator(Integer.parseInt(m.group(1)));
            this.setDenominator(Integer.parseInt(m.group(2)));
        } else {
            throw new InvalidTimeSignatureException("The given time signature string " + timeSigStr + " is not a valid time signature string.  The time signature should be given in a form like '4/4'.");
        }
    }
    
    /**
     * Provided for xml deserialization.
     */
    private TimeSignature() {};
        
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
     * @throws com.myronmarston.music.settings.InvalidTimeSignatureException 
     *         thrown when the denominator is non-positive or not a power of 2.     
     */
    public void setDenominator(int denominator) throws InvalidTimeSignatureException {
        if (denominator <= 0) throw new NonPositiveTimeSignatureException();
        
        double log2Result = MathHelper.log2(denominator);
        if (log2Result != Math.floor(log2Result)) {
            // the denominator is not an integer power of 2--throw an exception
            throw new TimeSignatureDenominatorNotAPowerOf2Exception(denominator);            
        }
        
        this.denominatorPowerOf2 = (int) log2Result;
        this.denominator = denominator;
        this.clearMidiTimeSignatureEvent();
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
     * @throws com.myronmarston.music.settings.NonPositiveTimeSignatureException 
     *         thrown when the passed value is not positive
     */
    public void setNumerator(int numerator) throws NonPositiveTimeSignatureException {
        if (numerator <= 0) throw new NonPositiveTimeSignatureException();
        this.numerator = numerator;
        this.clearMidiTimeSignatureEvent();
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
    public MidiEvent getMidiTimeSignatureEvent() throws InvalidMidiDataException {
        if (this.midiTimeSignatureEvent == null) this.midiTimeSignatureEvent = this.createMidiTimeSignatureEvent();
        return this.midiTimeSignatureEvent;
    }
    
    private void clearMidiTimeSignatureEvent() {
        this.midiTimeSignatureEvent = null;
    }
    
    // TODO: our code should ensure that an InvalidMidiDataException is never thrown,
    // so catch it and wrap it in an unchecked exception istead.
    private MidiEvent createMidiTimeSignatureEvent() throws InvalidMidiDataException {
        // See http://www.sonicspot.com/guide/midifiles.html for a description of the contents of this message.
        MetaMessage tsMessage = new MetaMessage();
        
        byte[] tsMessageData = new byte[4];
        tsMessageData[0] = (byte) this.getNumerator();
        tsMessageData[1] = (byte) this.getDenominatorPowerOf2();
        tsMessageData[2] = 24; // metronome pulse
        tsMessageData[3] = 8;  // number of 32nds per quarter note
               
        tsMessage.setMessage(TIME_SIGNATURE_META_MESSAGE_TYPE,  
                             tsMessageData,         // the time signature data
                             tsMessageData.length); // the size of the data array
        
        return new MidiEvent(tsMessage, 0);
    }

    @Override
    public String toString() {
        return this.getNumerator() + "/" + this.getDenominator();
    }        
    
    // equals and hashCode were generated by Netbeans IDE
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TimeSignature other = (TimeSignature) obj;
        if (this.numerator != other.numerator) {
            return false;
        }
        if (this.denominator != other.denominator) {
            return false;
        }
        if (this.denominatorPowerOf2 != other.denominatorPowerOf2) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.numerator;
        hash = 47 * hash + this.denominator;
        hash = 47 * hash + this.denominatorPowerOf2;
        return hash;
    }        
}
