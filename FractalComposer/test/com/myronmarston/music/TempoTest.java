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

import javax.sound.midi.MidiEvent;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class TempoTest {

    @Test(expected=IllegalArgumentException.class)
    public void testCheckTempoValidity_TooBig() {
        Tempo.checkTempoValidity(Tempo.MAX_TEMPO_BPM + 1);        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCheckTempoValidity_TooSmall() {
        Tempo.checkTempoValidity(Tempo.MIN_TEMPO_BPM - 1);        
    }
    
    @Test
    public void testCheckTempoValidity() {
        Tempo.checkTempoValidity(Tempo.MIN_TEMPO_BPM);        
        Tempo.checkTempoValidity(Tempo.MIN_TEMPO_BPM + 1);        
        Tempo.checkTempoValidity(Tempo.MAX_TEMPO_BPM - 1);  
        Tempo.checkTempoValidity(Tempo.MAX_TEMPO_BPM);        
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetMidiTempoEvent_TooBig() {
        Tempo.getMidiTempoEvent(Tempo.MAX_TEMPO_BPM + 1);        
    }
    
    @Test
    public void testEdgeTemposSucceed() {
        MidiEvent event = Tempo.getMidiTempoEvent(Tempo.MIN_TEMPO_BPM); 
        event = Tempo.getMidiTempoEvent(Tempo.MAX_TEMPO_BPM); 
        
        // we don't care to check the results, just that no exception is thrown
    }
        
    @Test
    public void testGetMidiTempoEvent() {
        MidiEvent event = Tempo.getMidiTempoEvent(120);   
        assertMidiTempoEventIsValidFor120BPM(event);
    }
    
    @Test
    public void toGuidoString() {
        assertEquals("\\tempo<\"Allegro\",\"1/4=140\">", Tempo.toGuidoString(140));
        assertEquals("\\tempo<\"Andante\",\"1/4=93\">", Tempo.toGuidoString(93));
    }

    public static void assertMidiTempoEventIsValidFor120BPM(MidiEvent event) {
        // the midi event has a byte array representing microseconds per quarter note
        // rather than redoing the calculation here, we provide one assert method for a 
        // particular tempo
        
        assertEquals(0L, event.getTick());
        assertEquals(6, event.getMessage().getMessage().length);
        assertEquals((byte) -1, event.getMessage().getMessage()[0]);
        assertEquals((byte) 81, event.getMessage().getMessage()[1]);
        assertEquals((byte) 3, event.getMessage().getMessage()[2]);
        assertEquals((byte) 7, event.getMessage().getMessage()[3]);
        assertEquals((byte) -95, event.getMessage().getMessage()[4]);
        assertEquals((byte) 32, event.getMessage().getMessage()[5]);
    }
    
    public static void assertMidiTempoEventIsValidFor100BPM(MidiEvent event) {
        // the midi event has a byte array representing microseconds per quarter note
        // rather than redoing the calculation here, we provide one assert method for a 
        // particular tempo
        
        assertEquals(0L, event.getTick());
        assertEquals(6, event.getMessage().getMessage().length);
        assertEquals((byte) -1, event.getMessage().getMessage()[0]);
        assertEquals((byte) 81, event.getMessage().getMessage()[1]);
        assertEquals((byte) 3, event.getMessage().getMessage()[2]);
        assertEquals((byte) 9, event.getMessage().getMessage()[3]);
        assertEquals((byte) 39, event.getMessage().getMessage()[4]);
        assertEquals((byte) -64, event.getMessage().getMessage()[5]);
    }
}