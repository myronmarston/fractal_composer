package com.myronmarston.music.settings;

import com.myronmarston.music.*;
import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;        
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class VoiceTest {
            
    @Test
    public void generateVoiceGerm() {
        FractalPiece fp = new FractalPiece();
        fp.getGerm().add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        fp.getGerm().add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        fp.getGerm().add(new Note(2, 4, 0, new Fraction(1, 2), 64));
        fp.getGerm().add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        Voice v1 = fp.createVoice();
        v1.setOctaveAdjustment(2);
        v1.setSpeedScaleFactor(new Fraction(4, 1));
        
        NoteList expected = new NoteList();
        expected.add(new Note(0, 6, 0, new Fraction(1, 4), 96));
        expected.add(new Note(1, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(2, 6, 0, new Fraction(1, 8), 64));
        expected.add(new Note(0, 6, 0, new Fraction(1, 4), 96));
        NoteListTest.assertNoteListsEqual(expected, v1.getModifiedGerm());
        
        v1.setSpeedScaleFactor(new Fraction(1, 2));
        expected.clear();
        expected.add(new Note(0, 6, 0, new Fraction(2, 1), 96));
        expected.add(new Note(1, 6, 0, new Fraction(1, 1), 64));
        expected.add(new Note(2, 6, 0, new Fraction(1, 1), 64));
        expected.add(new Note(0, 6, 0, new Fraction(2, 1), 96));
        NoteListTest.assertNoteListsEqual(expected, v1.getModifiedGerm());
        
        v1.setOctaveAdjustment(-1);
        expected.clear();
        expected.add(new Note(0, 3, 0, new Fraction(2, 1), 96));
        expected.add(new Note(1, 3, 0, new Fraction(1, 1), 64));
        expected.add(new Note(2, 3, 0, new Fraction(1, 1), 64));
        expected.add(new Note(0, 3, 0, new Fraction(2, 1), 96));
        NoteListTest.assertNoteListsEqual(expected, v1.getModifiedGerm());
    }
          
    @Test
    public void getEntireVoiceResult() {
        FractalPiece fp = new FractalPiece();
        fp.getGerm().add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        fp.getGerm().add(new Note(1, 4, 0, new Fraction(1, 2), 64));
        fp.getGerm().add(new Note(2, 4, 0, new Fraction(1, 2), 64));
        fp.getGerm().add(new Note(0, 4, 0, new Fraction(1, 1), 96));
        
        Voice v1 = fp.createVoice();                
        Voice v2 = fp.createVoice();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        
        v1.setOctaveAdjustment(1);
        v1.setSpeedScaleFactor(new Fraction(2, 1));
        
        v1.getVoiceSections().get(0).getSelfSimilaritySettings().setApplyToPitch(true);
        v1.getVoiceSections().get(0).getSelfSimilaritySettings().setApplyToRhythm(true);
        v1.getVoiceSections().get(1).setApplyInversion(true);
        v1.getVoiceSections().get(1).getSelfSimilaritySettings().setApplyToPitch(true);
        v1.getVoiceSections().get(1).getSelfSimilaritySettings().setApplyToRhythm(true);
        
        v2.getVoiceSections().get(0).getSelfSimilaritySettings().setApplyToPitch(true);
        v2.getVoiceSections().get(0).getSelfSimilaritySettings().setApplyToRhythm(true);
        v2.getVoiceSections().get(1).setApplyInversion(true);
        v2.getVoiceSections().get(1).getSelfSimilaritySettings().setApplyToPitch(true);
        v2.getVoiceSections().get(1).getSelfSimilaritySettings().setApplyToRhythm(true);
        
        NoteList expected = new NoteList();
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        expected.add(new Note(1, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(2, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        
        expected.add(new Note(1, 5, 0, new Fraction(1, 4), 96));
        expected.add(new Note(2, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(3, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(1, 5, 0, new Fraction(1, 4), 96));
        
        expected.add(new Note(2, 5, 0, new Fraction(1, 4), 96));
        expected.add(new Note(3, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(4, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(2, 5, 0, new Fraction(1, 4), 96));
        
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        expected.add(new Note(1, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(2, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        
        // repeat that whole thing again since this voice is twice as fast
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        expected.add(new Note(1, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(2, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        
        expected.add(new Note(1, 5, 0, new Fraction(1, 4), 96));
        expected.add(new Note(2, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(3, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(1, 5, 0, new Fraction(1, 4), 96));
        
        expected.add(new Note(2, 5, 0, new Fraction(1, 4), 96));
        expected.add(new Note(3, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(4, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(2, 5, 0, new Fraction(1, 4), 96));
        
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        expected.add(new Note(1, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(2, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        
        // now do the inversion...
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        expected.add(new Note(-1, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(-2, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        
        expected.add(new Note(-1, 5, 0, new Fraction(1, 4), 96));
        expected.add(new Note(-2, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-3, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-1, 5, 0, new Fraction(1, 4), 96));
        
        expected.add(new Note(-2, 5, 0, new Fraction(1, 4), 96));
        expected.add(new Note(-3, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-4, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-2, 5, 0, new Fraction(1, 4), 96));
        
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        expected.add(new Note(-1, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(-2, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        
        // and repeat the inversion once more...
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        expected.add(new Note(-1, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(-2, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        
        expected.add(new Note(-1, 5, 0, new Fraction(1, 4), 96));
        expected.add(new Note(-2, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-3, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-1, 5, 0, new Fraction(1, 4), 96));
        
        expected.add(new Note(-2, 5, 0, new Fraction(1, 4), 96));
        expected.add(new Note(-3, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-4, 5, 0, new Fraction(1, 8), 64));
        expected.add(new Note(-2, 5, 0, new Fraction(1, 4), 96));
        
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        expected.add(new Note(-1, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(-2, 5, 0, new Fraction(1, 4), 64));
        expected.add(new Note(0, 5, 0, new Fraction(1, 2), 96));
        
        NoteListTest.assertNoteListsEqual(expected, v1.getEntireVoice());
    }
}