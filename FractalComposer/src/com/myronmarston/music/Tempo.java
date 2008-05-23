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

import javax.sound.midi.*;
import java.lang.reflect.UndeclaredThrowableException;

/**
 *
 * @author Myron
 */
public class Tempo {        
    /**
     * These constants were taken from http://www.sonicspot.com/guide/midifiles.html
     */
    private final static int MICROSECONDS_PER_MINUTE = 60000000;
    private final static int MAX_MICROSECONDS_PER_QTR_NOTE = 8355711;
    private final static int MIN_MICROSECONDS_PER_QTR_NOTE = 0;        
    private final static int TEMPO_META_MESSAGE_TYPE = 81;
    
    /**
     * The minimum tempo allowed, in beats per minute.
     */    
    public final static int MIN_TEMPO_BPM = (int) Math.ceil((double) MICROSECONDS_PER_MINUTE / (double) MAX_MICROSECONDS_PER_QTR_NOTE);
        
    /** 
     * The maximum tempo allowed, in beats per minute.
     */
    public final static int MAX_TEMPO_BPM = 499;
    
    /**
     * The default tempo to use.  This is different from the default tempo of 
     * the midi standard but will be the default for our pieces.
     */
    public final static int DEFAULT = 90;
    
    /**
     * Checks the validity of the tempo.
     * 
     * @param tempo the tempo to check
     * @throws IllegalArgumentException if the tempo is not in the acceptable
     *         range
     */    
    public static void checkTempoValidity(int tempo) throws IllegalArgumentException {             
        if (tempo < MIN_TEMPO_BPM || tempo > MAX_TEMPO_BPM) {
            throw new IllegalArgumentException("The tempo must be between " + MIN_TEMPO_BPM + " and " + MAX_TEMPO_BPM + ".");
        }        
    }
    
    /**
     * Converts the tempo to a byte array for the midi event.
     * 
     * @param tempoInBPM the tempo, in beats per minute
     * @return the byte array
     */
    private static byte[] convertTempoToMidiByteArray(int tempoInBPM) {        
        // convert our tempo to the unit midi expects
        int microsecondsPerQuarterNote = MICROSECONDS_PER_MINUTE / tempoInBPM;        
        assert microsecondsPerQuarterNote > MIN_MICROSECONDS_PER_QTR_NOTE && microsecondsPerQuarterNote <= MAX_MICROSECONDS_PER_QTR_NOTE : microsecondsPerQuarterNote;        
        
        // convert our value to a 6-digit hex string, with leading zeroes if necessary
        String hexValue = Integer.toHexString(microsecondsPerQuarterNote);
        assert hexValue.length() <= 6;
        while (hexValue.length() < 6) hexValue = "0" + hexValue;
                        
        // convert it to 3 bytes...
        byte[] byteArray = new byte[3];        
        for (int i = 0; i < byteArray.length; i++) {
            // I would like to use Byte.parseByte(), but it fails for values outside
            // the range -128..127 because java uses signed bytes.
            // Instead, we use Integer.parseInt() because it can handle values
            // 128-255, and then we cast it to a byte.  The cast takes care of
            // wrapping the values around to the negatives if necessary.            
            byteArray[i] = (byte) Integer.parseInt(hexValue.substring(i * 2, (i * 2) + 2), 16);
        }
        
        return byteArray;
    }
    
    /**
     * Gets a midi event for a given tempo.
     * 
     * @param tempoInBPM the tempo, in beats per minute
     * @return the midi tempo event
     */
    public static MidiEvent getMidiTempoEvent(int tempoInBPM) {
        checkTempoValidity(tempoInBPM); // make sure out tempo is valid
        byte[] tMessageData = convertTempoToMidiByteArray(tempoInBPM);
        
        // See http://www.sonicspot.com/guide/midifiles.html for a description of the contents of this message.
        MetaMessage tMessage = new MetaMessage();
                   
        try {
            tMessage.setMessage(TEMPO_META_MESSAGE_TYPE, tMessageData, tMessageData.length); // the size of the data array
        } catch (InvalidMidiDataException ex) {
            // wrap it in an undeclared exception rather than declaring it on our method since
            // our code should prevent an InvalidMidiDataException from ever occuring...
            throw new UndeclaredThrowableException(ex, "An unexpected exception occurred while greating the midi tempo event.  This indicates a programming error.");
        }
        
        return new MidiEvent(tMessage, 0);
    }
}
