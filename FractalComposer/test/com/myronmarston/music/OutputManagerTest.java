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

import com.myronmarston.music.notation.GuidoRunException;
import com.myronmarston.music.scales.*;
import com.myronmarston.music.settings.*;
import com.myronmarston.util.*;
import com.myronmarston.music.notation.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.sound.midi.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class OutputManagerTest {

    private OutputManager outputManager;
    
    @Before
    public void setUp() throws Exception {
        FractalPiece fp = new FractalPiece();
        // set the germ string so that we don't get a GermIsEmptyException...
        fp.setGermString("C4");
        NoteList nl = NoteList.parseNoteListString("D4 E4 C4", Scale.DEFAULT);
        
        outputManager = new OutputManager(fp, Arrays.asList(nl));
    } 
            
    @Test
    public void getGuidoNotation() throws Exception {
        String guidoNotationFormatString = "{" + FileHelper.NEW_LINE + "[%s\\key<\"0\"> \\meter<\"C\"> \\clef<\"g2-8\"> %s\\intens<\"mf\"> d1/4 e1/4 c1/4]" + FileHelper.NEW_LINE + "}";
        String instrumentString = "\\instr<\"Piano 1\", \"MIDI 0\"> ";
        String tempoString = "\\tempo<\"Andante\",\"1/4=90\"> ";
        String fullGuidoString = String.format(guidoNotationFormatString, instrumentString, tempoString);
        String noInstrOrTempoGuidoString = String.format(guidoNotationFormatString, "", "");
                           
        testGuidoNotation(outputManager, fullGuidoString);
                       
        OutputManager om = new OutputManager(this.outputManager.getFractalPiece(), this.outputManager.getNoteLists(), false, false);        
        testGuidoNotation(om, noInstrOrTempoGuidoString);        
    }
    
    public static void testGuidoNotation(final OutputManager om, final String expectedNotation) throws Exception {
        assertEquals(expectedNotation, om.getPieceNotation().toGuidoString());
        
        FileHelper.createAndUseTempFile("TestGuido", ".gmn", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                om.saveGuidoFile(tempFileName);
                String contents = FileHelper.readFileIntoString(tempFileName);
                
                assertEquals(expectedNotation, contents);
            }
        });
    }
    
    @Test
    public void testGuidoError() throws Exception {
           final OutputManager om = new OutputManager(this.outputManager.getFractalPiece(), this.outputManager.getNoteLists(), false, false);        
           om.setTestNotationError(true);
           FileHelper.createAndUseTempFile("TestGif", ".gif", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                try {
                    om.saveGifImage(tempFileName);
                    fail();
                } catch (GuidoRunException ex) {}                
            }
        });           
    }

    @Test
    public void getGuidoNotation_forPentatonicScale() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.setTimeSignature(new TimeSignature(5, 4));
        fp.setScale(new MajorPentatonicScale(NoteName.G));
        fp.setGermString("G4,1/2 A4,1/4 B4 G4,1/2 E5 D5");
        fp.createDefaultSettings();
        
        String expected = "g2/4 a2/8 b2/8 g2/4 e3/4 d3/4 a2/4 b2/8 d3/8 a2/4 g3/4 e3/4 b2/4 d3/8 e3/8 b2/4 a3/4 g3/4 g2/4 a2/8 b2/8 g2/4 e3/4 d3/4 e3/4 g3/8 a3/8 e3/4 d4/4 b3/4 d3/4 e3/8 g3/8 d3/4 b3/4 a3/4";        
        String notation = fp.getVoices().get(0).getVoiceSections().get(0).createOutputManager().getPieceNotation().toGuidoString();
        assertTrue(notation.contains(expected));
        
        fp.setScale(new MinorPentatonicScale(NoteName.G));
        fp.setGermString("G4,1/2 Bb4,1/4 C5 G4,1/2 F5 D5");
        fp.createDefaultSettings();
        
        expected = "g2/4 b&2/8 c3/8 g2/4 f3/4 d3/4 b&2/4 c3/8 d3/8 b&2/4 g3/4 f3/4 c3/4 d3/8 f3/8 c3/4 b&3/4 g3/4 g2/4 b&2/8 c3/8 g2/4 f3/4 d3/4 f3/4 g3/8 b&3/8 f3/4 d4/4 c4/4 d3/4 f3/8 g3/8 d3/4 c4/4 b&3/4";    
        notation = fp.getVoices().get(0).getVoiceSections().get(0).createOutputManager().getPieceNotation().toGuidoString();
        assertTrue(notation.contains(expected));
    }
    
    @Test
    public void getGuidoNotation_forConfusingOctaves() throws Exception {        
        FractalPiece fp = new FractalPiece();
        fp.setGermString("Cb4 C4 Cbb4 Bx4 B4 B#4");
        
        Pattern pat = Pattern.compile(".*c&1\\/4 c1\\/4 c&&1\\/4 b##1\\/4 b1\\/4 b#1\\/4.*", Pattern.DOTALL);
        Matcher matcher = pat.matcher(fp.createGermOutputManager().getPieceNotation().toGuidoString());
        assertTrue(matcher.matches());        
    }
    
    @Test
    public void getPieceNotation() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.setGermString("G4 A4 B4 C5,1/8");
        fp.createDefaultSettings();
        OutputManager om = fp.createPieceResultOutputManager();
        
        // we'll just check that a piece notation object with the appropriate 
        // number of parts was created.  We test the actual notation elsewhere.
        assertEquals(3, om.getPieceNotation().getParts().size());
    }       
    
    @Test
    public void getSequence_forConfusingOctaves() throws Exception {
        // It's unclear which octave a note like Cb4 or B#4 should be on.
        // Lilypond interprets Cb4 to be half step below C4 and B#4 to be a
        // half step above B4, so that's what we'll do, too.
        
        FractalPiece fp = new FractalPiece();
        fp.setGermString("Cb4 C4 Cbb4 Bx4 B4 B#4");
        
        Sequence seq = fp.createGermOutputManager().getSequence();
        Track t = seq.getTracks()[1];
        int[] pitchNumbers = {59, 60, 58, 73, 71, 72};
        // make sure we have the right number of events. 
        // there are always two extra events - instrument event and the end-of-track event, so we add 1 for that.
        assertEquals(pitchNumbers.length * 2 + 2, t.size());
        
        for (int i = 0; i < pitchNumbers.length; i++) {            
            com.myronmarston.music.settings.PieceTest.assertTrackMidiNoteEqual(t, i, i * 4, 4, pitchNumbers[i], MidiNote.DEFAULT_VELOCITY, 0);
        }
    }
    
    @Test
    public void getSequence() throws Exception {                
        // We'll just check that we have a sequence object, since
        // other tests check the actual contents of the midi sequence to make 
        // sure it's correct.
        assertNotNull(this.outputManager.getSequence());
    }

    @Test
    public void getSheetMusicCreator() throws Exception {
        // we have other tests to test that the sheet music creator works properly...
        assertNotNull(this.outputManager.getSheetMusicCreator());
    }

    @Test
    public void saveMidiFile() throws Exception {
        FileHelper.createAndUseTempFile("TestMidiFile", ".mid", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                OutputManagerTest.this.outputManager.saveMidiFile(tempFileName);
                
                // this will throw an exception if a valid midi file was not saved...
                Sequence seq = MidiSystem.getSequence(new File(tempFileName)); 
            }
        });
    }
    
    @Test
    public void saveGifImage() throws Exception {
        FileHelper.createAndUseTempFile("TestGifFile", ".gif", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                OutputManagerTest.this.outputManager.saveGifImage(tempFileName);
                File file = new File(tempFileName);
                BufferedImage image = ImageIO.read(file);
                assertNotNull(image.getData()); 
            }
        });
    }
    
    @Test
    public void saveLilypondResults() throws Exception {
        FileHelper.createAndUseTempFile("TestLilypond", "", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                OutputManagerTest.this.outputManager.setTestNotationError(true);
                try {
                    OutputManagerTest.this.outputManager.savePdfFile(tempFileName + ".pdf");                
                    fail();
                } catch (LilypondRunException ex) {}
                
                try {
                    OutputManagerTest.this.outputManager.savePngFile(tempFileName + ".png", 500);                
                    fail();
                } catch (LilypondRunException ex) {}
                
                OutputManagerTest.this.outputManager.setTestNotationError(false);
                
                OutputManagerTest.this.outputManager.savePdfFile(tempFileName + ".pdf");
                File file = new File(tempFileName + ".pdf");
                assertTrue(file.exists());
                
                OutputManagerTest.this.outputManager.savePngFile(tempFileName + ".png", 500);
                file = new File(tempFileName + ".png");
                BufferedImage image = ImageIO.read(file);
                assertNotNull(image.getData());                
            }
        });
    }    
    
    @Test(expected=GermIsEmptyException.class)
    public void errorIfGermIsEmpty() throws Exception {
        FractalPiece fp = new FractalPiece();        
        OutputManager om = new OutputManager(fp, this.outputManager.getNoteLists());
    }

    @Test
    public void multipleKeySignature() throws Exception {
        FractalPiece fp = new FractalPiece();     
        fp.setScale(new MajorScale(NoteName.C));
        fp.setGermString("C4");        
        fp.createVoice();
        Section s1 = fp.createSection();
        Section s2 = fp.createSection();
        Section s3 = fp.createSection();
        Section s4 = fp.createSection();
        
        s1.setOverridePieceScale(true);
        s1.setScale(new NaturalMinorScale(NoteName.G));
        s2.setOverridePieceScale(true);
        s2.setScale(new MajorScale(NoteName.G));
        s3.setOverridePieceScale(true);
        s3.setScale(new MajorPentatonicScale(NoteName.G));
        Sequence seq = fp.createPieceResultOutputManager().getSequence();
        Track track0 = seq.getTracks()[0];
        // Note: a section w/o a scale that comes after a section w/ a scale 
        // should also have a key sig event to restore the key sig to the piece default...
        KeySignatureTest.assertKeySignatureEventEqual(KeySignatureTest.getIndexedKeySigEvent(track0, 0), (byte) 0, (byte) 0, 0L);
        KeySignatureTest.assertKeySignatureEventEqual(KeySignatureTest.getIndexedKeySigEvent(track0, 1), (byte) -2, (byte) 1, 4L);        
        KeySignatureTest.assertKeySignatureEventEqual(KeySignatureTest.getIndexedKeySigEvent(track0, 2), (byte) 1, (byte) 0, 8L);
        KeySignatureTest.assertKeySignatureEventEqual(KeySignatureTest.getIndexedKeySigEvent(track0, 3), (byte) 0, (byte) 0, 16L);                
    }
    
    @Test
    public void getMidiTickResolution() throws Exception {
        NoteList nl = NoteList.parseNoteListString("C4,1/8 C4,1/4 C4,1/16 C4,3/8 C4,1/3", new MajorScale(NoteName.C));
        OutputManager om = new OutputManager(this.outputManager.getFractalPiece(), Arrays.asList(nl));
        assertEquals(48, om.getMidiTickResolution());
    }       
    
    @Test
    public void tempoIsCached() throws Exception {
        FractalPiece fp = new FractalPiece();
        fp.setTempo(87);
        fp.setGermString("G4 A4");
        fp.createDefaultSettings();
        
        OutputManager outputManager = fp.createPieceResultOutputManager();
        assertEquals(87, outputManager.getTempo());
        
        fp.setTempo(117);
        
        // the output manager should have the tempo at the time it was created
        assertEquals(87, outputManager.getTempo());
    }
    
    @Test
    public void testLastFileNameMethods() throws Exception {
        testALastFileNameMethod("GifTest", ".gif", new OutputManagerTest.LastFileName() {
            public String getLastFileName() throws Exception {
                return outputManager.getLastGifFileName();
            }
            public void saveFile(String fileName) throws Exception {
                outputManager.saveGifImage(fileName);
            }
        });
        
        testALastFileNameMethod("GmnTest", ".gmn", new OutputManagerTest.LastFileName() {
            public String getLastFileName() throws Exception {
                return outputManager.getLastGuidoFileName();
            }
            public void saveFile(String fileName) throws Exception {
                outputManager.saveGuidoFile(fileName);
            }
        });
        
        testALastFileNameMethod("MidiTest", ".mid", new OutputManagerTest.LastFileName() {
            public String getLastFileName() throws Exception {
                return outputManager.getLastMidiFileName();
            }
            public void saveFile(String fileName) throws Exception {
                outputManager.saveMidiFile(fileName);
            }
        });
        
        testALastFileNameMethod("WavTest", ".wav", new OutputManagerTest.LastFileName() {
            public String getLastFileName() throws Exception {
                return outputManager.getLastWavFileName();
            }
            public void saveFile(String fileName) throws Exception {
                outputManager.saveWavFile(fileName);
            }
        });
            
        testALastFileNameMethod("Mp3Test", ".mp3", new OutputManagerTest.LastFileName() {
            public String getLastFileName() throws Exception {
                return outputManager.getLastMp3FileName();
            }
            public void saveFile(String fileName) throws Exception {
                outputManager.saveMp3File(fileName);
            }
        });              
        
        testALastFileNameMethod("LilypondPdf", ".pdf", new OutputManagerTest.LastFileName() {
            public String getLastFileName() throws Exception {
                return outputManager.getLastPdfFileName();
            }
            
            public void saveFile(String fileName) throws Exception {
                outputManager.savePdfFile(fileName);
            }
        });              
        
        testALastFileNameMethod("LilypondPng", ".png", new OutputManagerTest.LastFileName() {
            public String getLastFileName() throws Exception {
                return outputManager.getLastPngFileName();
            }
            
            public void saveFile(String fileName) throws Exception {
                outputManager.savePngFile(fileName, 500);
            }
        });              
    
        testALastFileNameMethod("Lilypond", ".ly", new OutputManagerTest.LastFileName() {
            public String getLastFileName() throws Exception {
                return outputManager.getLastLilypondFileName();
            }
            
            public void saveFile(String fileName) throws Exception {
                outputManager.saveLilypondFile(fileName);
            }
        });              
    }    
    
    private interface LastFileName {
        String getLastFileName() throws Exception;
        void saveFile(String fileName) throws Exception;
    }
    
    private void testALastFileNameMethod(String filePrefix, String fileSuffix, final LastFileName lastFileNameMethod) throws Exception {
        FileHelper.createAndUseTempFile(filePrefix, fileSuffix, new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                lastFileNameMethod.saveFile(tempFileName);           
                assertEquals(tempFileName, lastFileNameMethod.getLastFileName());
            }            
        }); 
    }
    
    @Test
    public void pieceWithReallyFastNotes() throws Exception {
        // Lilypond and Guido don't allow notes faster than 64ths.  We have some code to
        // handle this, so just test that this runs and doesn't throw an exception...
        FractalPiece fp = new FractalPiece();
        fp.createDefaultSettings();
        fp.setGermString("G4,1/128 A4,1/256");
        final OutputManager om = fp.createPieceResultOutputManager();
        
        FileHelper.createAndUseTempFile("LilypondTest", ".pdf", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                om.savePdfFile(tempFileName);
            }
        });
        
        FileHelper.createAndUseTempFile("LilypondTest", ".png", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                om.savePngFile(tempFileName, 500);
            }
        });
        
        FileHelper.createAndUseTempFile("GuidoTest", ".gif", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                om.saveGifImage(tempFileName);
            }
        });
    }
    
    @Test
    public void timeLeftInBarSetOnNotationNotes() throws Exception {
        FractalPiece fp = new FractalPiece();       
        fp.setTimeSignature(new TimeSignature(4, 4));
        fp.setGermString("G4,1/3 R,1/6 A4,1/4 G4,1/4 B4,1/2 C4,1/4 R,1/1 D4,3/1 E4,1/4");
        OutputManager om = fp.createGermOutputManager();        
        Part part = (Part) om.getPieceNotation().getParts().get(0);
        PartSection partSection = (PartSection) part.getPartSections().get(0);
        testTimeLeftInBarOnNotes(partSection.getNotationElements(), "1/1", "2/3", "1/2", "1/4", "1/1", "1/2", "1/4", "1/4", "1/4");
    }
    
    private static void testTimeLeftInBarOnNotes(NotationElementList notes, String ... expectedTimesLeft) throws Exception {
        assertEquals(expectedTimesLeft.length, notes.size());
        for (int i = 0; i < expectedTimesLeft.length; i++) {
            NotationNote nn = (NotationNote) notes.get(i);
            Fraction expected = new Fraction(expectedTimesLeft[i]);
            assertEquals(expected, nn.getTimeLeftInBar());
        }       
    }
    
    @Test
    public void transientFilesDeletedProperly() throws Exception {
        testTransientFilesDeletedProperly(new OutputManagerTest.TransientFilesTest() {
            public String createTransientFiles(OutputManager om, File directory) throws Exception {
                String filename = "Test.pdf";
                om.savePdfFile(directory.getCanonicalPath() + File.separator + filename);
                return filename;
            }
        });
        
        testTransientFilesDeletedProperly(new OutputManagerTest.TransientFilesTest() {
            public String createTransientFiles(OutputManager om, File directory) throws Exception {
                String filename = "Test.png";
                om.savePngFile(directory.getCanonicalPath() + File.separator + filename, 500);
                return filename;
            }
        });        
    }
    
    public void testTransientFilesDeletedProperly(final TransientFilesTest test) throws Exception {
        FileHelper.createAndUseTempFile("TempDirectory", "", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                File directory = new File(tempFileName);
                FileHelper.attemptTempFileDelete(directory);
                assertFalse(directory.exists());
                directory.mkdir();
                assertTrue(directory.exists());
                assertTrue(directory.isDirectory());
                try {                    
                    FractalPiece fp = new FractalPiece();
                    fp.setGermString("G4");
                    OutputManager om = fp.createGermOutputManager();
                    final String nonTransientFileName = test.createTransientFiles(om, directory);
                    
                    File[] transientFilesNotDeleted = directory.listFiles(new java.io.FileFilter() {
                        public boolean accept(File pathname) {
                            String name = pathname.getName();
                            return !name.endsWith(nonTransientFileName);
                        }
                    });
                    
                    assertEquals(0, transientFilesNotDeleted.length);
                } finally {
                    for (File f : directory.listFiles()) {
                        f.delete();
                    }
                }                                               
            }
        });
    }
    
    public interface TransientFilesTest {
        /**
         * Creates the transient files and returns the file name of the
         * non-transient file.
         * 
         * @return the nontransient file
         */
        public String createTransientFiles(OutputManager om, File directory) throws Exception;
    }
}