package com.myronmarston.music.settings;

import com.myronmarston.music.*;
import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;      

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class VoiceSectionTest {
    
    @Test
    public void voiceSectionGetVoiceSectionResult() {
        FractalPiece fp = new FractalPiece();
        fp.getGerm().add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        fp.getGerm().add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        fp.getGerm().add(new Note(2, 4, 0, new Fraction(1, 2), 64));
        fp.getGerm().add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        Voice v1 = fp.createVoice();
        v1.setOctaveAdjustment(2);
        v1.setSpeedScaleFactor(new Fraction(4, 1));
        
        Section s1 = fp.createSection();
        VoiceSection vs1 = v1.getVoiceSections().get(0);
        
        NoteList expected = new NoteList();
        expected.add(new Note(0, 6, 0, new Fraction(1, 4), 96));
        expected.add(new Note(1, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(2, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(0, 6, 0, new Fraction(1, 4), 96));                
        NoteListTest.assertNoteListsEqual(expected, vs1.getVoiceSectionResult());
                
        expected.clear();
        vs1.setApplyInversion(true);
        vs1.setApplyRetrograde(true);
        vs1.getSelfSimilaritySettings().setApplyToPitch(true);
        vs1.getSelfSimilaritySettings().setApplyToRhythm(true);
        vs1.getSelfSimilaritySettings().setApplyToVolume(true);
        
        expected.add(new Note(0, 6, 0, new Fraction(1, 4), 96));
        expected.add(new Note(-2, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-1, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(0, 6, 0, new Fraction(1, 4), 96));
        
        expected.add(new Note(-2, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-4, 6, 0, new Fraction(1, 16), 43));
        expected.add(new Note(-3, 6, 0, new Fraction(1, 16), 43));
        expected.add(new Note(-2, 6, 0, new Fraction(1, 8), 64));
        
        expected.add(new Note(-1, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-3, 6, 0, new Fraction(1, 16), 43));
        expected.add(new Note(-2, 6, 0, new Fraction(1, 16), 43));
        expected.add(new Note(-1, 6, 0, new Fraction(1, 8), 64));
        
        expected.add(new Note(0, 6, 0, new Fraction(1, 4), 96));
        expected.add(new Note(-2, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-1, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(0, 6, 0, new Fraction(1, 4), 96));
        NoteListTest.assertNoteListsEqual(expected, vs1.getVoiceSectionResult());
        
        expected.clear();
        vs1.getSelfSimilaritySettings().setApplyToVolume(false);
        expected.add(new Note(0, 6, 0, new Fraction(1, 4), 96));
        expected.add(new Note(-2, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-1, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(0, 6, 0, new Fraction(1, 4), 96));
        
        expected.add(new Note(-2, 6, 0, new Fraction(1, 8), 96));
        expected.add(new Note(-4, 6, 0, new Fraction(1, 16), 64));
        expected.add(new Note(-3, 6, 0, new Fraction(1, 16), 64));
        expected.add(new Note(-2, 6, 0, new Fraction(1, 8), 96));
        
        expected.add(new Note(-1, 6, 0, new Fraction(1, 8), 96));
        expected.add(new Note(-3, 6, 0, new Fraction(1, 16), 64));
        expected.add(new Note(-2, 6, 0, new Fraction(1, 16), 64));
        expected.add(new Note(-1, 6, 0, new Fraction(1, 8), 96));
        
        expected.add(new Note(0, 6, 0, new Fraction(1, 4), 96));
        expected.add(new Note(-2, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-1, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(0, 6, 0, new Fraction(1, 4), 96));
        NoteListTest.assertNoteListsEqual(expected, vs1.getVoiceSectionResult());
        
        vs1.setRest(true);
        expected.clear();
        expected.add(new Note(0, 0, 0, new Fraction(3, 4), 0));
        NoteListTest.assertNoteListsEqual(expected, vs1.getVoiceSectionResult());
    }
        
    @Test(expected=IllegalArgumentException.class)    
    public void getLengthenedVoiceSectionResultErrorIfLengthTooShort() {
        FractalPiece fp = new FractalPiece();
        fp.getGerm().add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        fp.getGerm().add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        fp.getGerm().add(new Note(2, 4, 0, new Fraction(1, 2), 64));
        fp.getGerm().add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        Voice v1 = fp.createVoice();                
        Section s1 = fp.createSection();
        VoiceSection vs1 = v1.getVoiceSections().get(0);
        
        vs1.getLengthenedVoiceSectionResult(new Fraction(5, 2));
    }
            
    @Test
    public void getLengthenedVoiceSectionResult() {
        FractalPiece fp = new FractalPiece();
        fp.getGerm().add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        fp.getGerm().add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        fp.getGerm().add(new Note(2, 4, 0, new Fraction(1, 2), 64));
        fp.getGerm().add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        Voice v1 = fp.createVoice();                
        Section s1 = fp.createSection();
        VoiceSection vs1 = v1.getVoiceSections().get(0);
                
        assertEquals(new Fraction(3, 1), vs1.getVoiceSectionResult().getDuration());
        
        NoteList expected = new NoteList();
        expected.addAll(vs1.getVoiceSectionResult());        
        NoteListTest.assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(new Fraction(3, 1)));
        
        expected.add(Note.createRest(new Fraction(5, 2)));
        NoteListTest.assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(new Fraction(11, 2)));
        
        expected.clear();
        expected.addAll(vs1.getVoiceSectionResult());
        expected.addAll(vs1.getVoiceSectionResult());
        NoteListTest.assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(new Fraction(6, 1)));
        
        expected.add(Note.createRest(new Fraction(2, 1)));
        NoteListTest.assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(new Fraction(8, 1)));
        
        expected.clear();
        expected.addAll(vs1.getVoiceSectionResult());
        expected.addAll(vs1.getVoiceSectionResult());
        expected.addAll(vs1.getVoiceSectionResult());
        expected.addAll(vs1.getVoiceSectionResult());
        expected.add(Note.createRest(new Fraction(1, 2)));
        NoteListTest.assertNoteListsEqual(expected, vs1.getLengthenedVoiceSectionResult(new Fraction(25, 2)));        
    }    
}