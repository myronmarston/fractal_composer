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

package com.myronmarston.music.notation;

import com.myronmarston.music.*;
import com.myronmarston.music.scales.Scale;
import com.myronmarston.music.settings.*;
import com.myronmarston.util.FileHelper;
import com.myronmarston.util.Fraction;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class PartTest {
        
    @Test
    public void toLilypondString() throws Exception {
        StringBuilder expectedLilypondString = new StringBuilder();
        Part part = createTestPart(expectedLilypondString, null, true, true);
        assertEquals(expectedLilypondString.toString(), part.toLilypondString());
        
        expectedLilypondString = new StringBuilder();
        part = createTestPart(expectedLilypondString, null, false, false);
        assertEquals(expectedLilypondString.toString(), part.toLilypondString());
    }
    
    @Test
    public void toGuidoString() throws Exception {
        StringBuilder expectedGuidoString = new StringBuilder();
        Part part = createTestPart(null, expectedGuidoString, true, true, "Etude 6", "Myron");
        part.setPieceTitle("Etude 6");
        part.setPieceComposer("Myron");
        assertEquals(expectedGuidoString.toString(), part.toGuidoString());
        
        expectedGuidoString = new StringBuilder();
        part = createTestPart(null, expectedGuidoString, false, false);
        assertEquals(expectedGuidoString.toString(), part.toGuidoString());
    }
    
    protected static PartSection createTestPart_barebones(boolean includeTempo, boolean includeInstrument) throws Exception {
        Piece piece = new Piece(Scale.DEFAULT.getKeySignature(), new TimeSignature(6, 8), 93, includeTempo, includeInstrument);        
        Part part = new Part(piece, Instrument.getInstrument("Cello"));
        PartSection partSection = new PartSection(part, null);
                
        return partSection;
    }
    
    protected static Part createTestPart(StringBuilder expectedLilypondPartString, StringBuilder expectedGuidoPartString, boolean includeTempo, boolean includeInstrument) throws Exception {
        return createTestPart(expectedLilypondPartString, expectedGuidoPartString, includeTempo, includeInstrument, null, null);
    }
    
    protected static Part createTestPart(StringBuilder expectedLilypondPartString, StringBuilder expectedGuidoPartString, boolean includeTempo, boolean includeInstrument, String title, String composer) throws Exception {
        PartSection partSection = createTestPart_barebones(includeTempo, includeInstrument);
                
        partSection.getNotationElements().add(new NotationNote(partSection, 'c', 4, 0, new Fraction(1, 4), new Fraction(6, 8), MidiNote.DEFAULT_VELOCITY, true));
        partSection.getNotationElements().add(new NotationNote(partSection, 'd', 4, 0, new Fraction(1, 6), new Fraction(1, 2), MidiNote.DEFAULT_VELOCITY, false));
        partSection.getNotationElements().add(new NotationNote(partSection, 'e', 4, 0, new Fraction(1, 6), new Fraction(1, 3), MidiNote.DEFAULT_VELOCITY, false));
        partSection.getNotationElements().add(new NotationNote(partSection, 'f', 4, 0, new Fraction(1, 6), new Fraction(1, 6), MidiNote.DEFAULT_VELOCITY, false));
        
        if (expectedLilypondPartString != null) {            
            expectedLilypondPartString.append("\\new Voice " + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("{" + FileHelper.NEW_LINE);
            if (includeTempo) expectedLilypondPartString.append("       \\tempo 4=93" + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("       \\time 6/8" + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("       \\key c \\major" + FileHelper.NEW_LINE);
            if (includeInstrument) expectedLilypondPartString.append("       \\set Staff.instrumentName = \"Cello\"" + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("       \\clef \"treble_8\"" + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("       c'4\\mf \\times 2/3 { d'4 e'4 f'4 }");
            expectedLilypondPartString.append("       \\bar \"|.\"" + FileHelper.NEW_LINE);
            expectedLilypondPartString.append("}" + FileHelper.NEW_LINE);            
        }
        
        if (expectedGuidoPartString != null) {
            expectedGuidoPartString.append("[");
            if (includeInstrument) expectedGuidoPartString.append("\\instr<\"Cello\", \"MIDI 42\"> ");
            expectedGuidoPartString.append("\\key<0> \\meter<\"6/8\"> \\clef<\"g2-8\"> ");
            if (includeTempo) expectedGuidoPartString.append("\\tempo<\"Andante\",\"1/4=93\"> ");
            if (title != null) expectedGuidoPartString.append("\\title<\"" + title + "\">");
            if (composer != null) expectedGuidoPartString.append("\\composer<\"" + composer + "\">");
            expectedGuidoPartString.append("\\intens<\"mf\"> c1/4 d1/6 e1/6 f1/6]");            
        }
        
        return partSection.getPart();
    }
    
    @Test
    public void isPartFirstPartOfPiece() throws Exception {
        Piece piece = new Piece(Scale.DEFAULT.getKeySignature(), new TimeSignature(6, 8), 93, true, true);        
        Part part1 = new Part(piece, Instrument.getInstrument("Cello"));
        Part part2 = new Part(piece, Instrument.getInstrument("Trumpet"));
        
        assertTrue(part1.isPartFirstPartOfPiece());
        assertFalse(part2.isPartFirstPartOfPiece());
    }
    
    @Test
    public void getLargestDurationDenominator() throws Exception {
        // this method should create tuplets if necessay, and use the values from that...
        testGetLargestDurationDenominator(64, "3/16", "1/96", "1/96", "1/96");
    }
            
    private static void testGetLargestDurationDenominator(long expected, String ... durations) throws Exception {
        PartSection partSection = createTestPart_barebones(false, false);
        
        for (String duration : durations) {
            partSection.getNotationElements().add(NotationNoteTest.instantiateTestNote(duration));
        }
                        
        assertEquals(expected, partSection.getPart().getLargestDurationDenominator());
    }
    
    @Test
    public void scaleDurations() throws Exception {
        testScaleDurations(8L, Arrays.asList("1/12", "1/24", "1/24", "1/12"), Arrays.asList("2/3", "1/3", "1/3", "2/3"));
    }
    
    private static void testScaleDurations(long scaleFactor, List<String> originalDurations, List<String> expectedDurations) throws Exception {
        assertEquals(originalDurations.size(), expectedDurations.size());
        PartSection partSection = createTestPart_barebones(false, false);
        for (String duration : originalDurations) {
            partSection.getNotationElements().add(NotationNoteTest.instantiateTestNote(duration));
        }
        
        partSection.getPart().scaleDurations(scaleFactor);
        
        for (int i = 0; i < originalDurations.size(); i++) {
            assertEquals(new Fraction(expectedDurations.get(i)), ((NotationNote) partSection.getNotationElements().get(i)).getDuration());
        }
    }
    
    @Test
    public void getNextIndexOfFirstNoteOfAGermCopy() throws Exception {
        testGetNextIndexOfFirstNoteOfAGermCopy(20, 0, 5, 8, 12, 13, 19);        
    }
    
    private static void testGetNextIndexOfFirstNoteOfAGermCopy(int numNotes, int ... indicesOfFirstNotes) throws Exception {                
        PartSection partSection = createTestPart_barebones(false, false);
        for (int i = 0; i < numNotes; i++) {
            partSection.getNotationElements().add(new NotationNote(partSection, 'c', 4, 0, new Fraction(1, 4), new Fraction(4, 4), MidiNote.DEFAULT_VELOCITY, Arrays.binarySearch(indicesOfFirstNotes, i) >= 0));
        }
        
        List<NotationNote> notes = partSection.getNotationNotes();        
        
        int testIndex = 0;
        for (int index : indicesOfFirstNotes) {
            assertEquals(index, Part.getNextIndexOfFirstNoteOfAGermCopy(notes, testIndex));
            testIndex = index + 1;
        }                
        
        // when it can't find any more, it should return -1
        assertEquals(-1, Part.getNextIndexOfFirstNoteOfAGermCopy(notes, testIndex));
    }
    
    @Test
    public void setDynamicsOnGermCopy() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.setGermString("G4,FF G4,MP G4,MF G4,MP G4,F");
        OutputManager om = fp.createGermOutputManager();
        Part part = (Part) om.getPieceNotation().getParts().get(0);
        List<NotationNote> notes = part.getNotationNotes();
        
        // pass the same dynamic as what will be used...
        Dynamic returnedDynamic = Part.setDynamicsOnGermCopy(notes, Dynamic.MP);
        assertEquals(Dynamic.MP, returnedDynamic);                
        assertEquals(new NotationDynamic(null, NotationDynamic.Articulation.MARCATO), notes.get(0).getDynamic());
        assertEquals(new NotationDynamic(null, NotationDynamic.Articulation.NONE), notes.get(1).getDynamic());
        assertEquals(new NotationDynamic(null, NotationDynamic.Articulation.ACCENT), notes.get(2).getDynamic());
        assertEquals(new NotationDynamic(null, NotationDynamic.Articulation.NONE), notes.get(3).getDynamic());
        assertEquals(new NotationDynamic(null, NotationDynamic.Articulation.ACCENT), notes.get(4).getDynamic());
          
        // pass a different dynamic than what will be used...
        returnedDynamic = Part.setDynamicsOnGermCopy(notes, Dynamic.FF);
        assertEquals(Dynamic.MP, returnedDynamic);                
        assertEquals(new NotationDynamic(Dynamic.MP, NotationDynamic.Articulation.MARCATO), notes.get(0).getDynamic());
        assertEquals(new NotationDynamic(null, NotationDynamic.Articulation.NONE), notes.get(1).getDynamic());
        assertEquals(new NotationDynamic(null, NotationDynamic.Articulation.ACCENT), notes.get(2).getDynamic());
        assertEquals(new NotationDynamic(null, NotationDynamic.Articulation.NONE), notes.get(3).getDynamic());
        assertEquals(new NotationDynamic(null, NotationDynamic.Articulation.ACCENT), notes.get(4).getDynamic());        
    }
    
    @Test
    public void setNotationNoteDynamics() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        fp.setGermString("G4,F A4,MF G4");                
        VoiceSection vs = fp.getVoices().get(0).getVoiceSections().get(0);
        vs.setOverrideVoiceSettings(true);
        vs.getVoiceSettings().setSelfSimilaritySettings(new SelfSimilaritySettings(true, true, true, 1));
        OutputManager om = vs.createOutputManager();
        Part part = (Part) om.getPieceNotation().getParts().get(0);
        part.setNotationNoteDynamics();
        List<NotationNote> notes = part.getNotationNotes();
        
        assertEquals(new NotationDynamic(Dynamic.MF, NotationDynamic.Articulation.ACCENT), notes.get(0).getDynamic());
        assertEquals(NotationDynamic.DEFAULT_EMPTY, notes.get(1).getDynamic());
        assertEquals(NotationDynamic.DEFAULT_EMPTY, notes.get(2).getDynamic());
        
        assertEquals(new NotationDynamic(Dynamic.MP, NotationDynamic.Articulation.ACCENT), notes.get(3).getDynamic());
        assertEquals(NotationDynamic.DEFAULT_EMPTY, notes.get(4).getDynamic());
        assertEquals(NotationDynamic.DEFAULT_EMPTY, notes.get(5).getDynamic());
        
        assertEquals(new NotationDynamic(null, NotationDynamic.Articulation.ACCENT), notes.get(6).getDynamic());
        assertEquals(NotationDynamic.DEFAULT_EMPTY, notes.get(7).getDynamic());
        assertEquals(NotationDynamic.DEFAULT_EMPTY, notes.get(8).getDynamic());
    }
}
