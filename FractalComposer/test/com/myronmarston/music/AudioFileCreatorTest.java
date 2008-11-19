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

import com.myronmarston.music.settings.*;
import com.myronmarston.util.*;
import java.io.*;
import javax.sound.sampled.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class AudioFileCreatorTest {

    @Test
    public void saveWavFile() throws Exception {
        final FractalPiece fp = new FractalPiece();
        fp.setGermString("G4 A4");
        
        FileHelper.createAndUseTempFile("TestWavFile", ".wav", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                fp.createGermOutputManager().saveWavFile(tempFileName);
                File file = new File(tempFileName);
                AudioInputStream ais = null;
                try {
                    ais = AudioSystem.getAudioInputStream(file);
                    assertNotNull(ais);
                } finally {
                    if (ais != null) ais.close();
                }                
            }
        });        
    }

    @Test
    public void saveMp3File() throws Exception {
        // I haven't found a way to install tritonus on OS X...
        if (OSHelper.isMacOSX()) return;

        final FractalPiece fp = new FractalPiece();
        fp.setGermString("G4 A4");
        
        FileHelper.createAndUseTempFile("TestMp3File", ".mp3", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                fp.createGermOutputManager().saveMp3File(tempFileName);
                File file = new File(tempFileName);
                AudioInputStream ais = null;
                try {
                    ais = AudioSystem.getAudioInputStream(file);
                    assertNotNull(ais);
                } finally {
                    if (ais != null) ais.close();
                }                
            }
        });
    }

}