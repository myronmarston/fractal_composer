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

import com.myronmarston.util.ProcessRunner;
import com.myronmarston.util.FileHelper;
import java.io.*;

/**
 * Class that manages the creation of sheet music files.
 * 
 * @author Myron
 */
public class SheetMusicCreator {    
    private final OutputManager outputManager;
    private static final String GUIDO_2_GIF_EXE_FILE = "guido2gif.exe";
    private static final String GUIDO_DIRECTORY = "guido";
    private static final String CURRENT_DIR = System.getProperty("user.dir");

    /**
     * Constructor.
     * 
     * @param outputManager the output manager containing the piece of music to
     *        turn into sheet music
     */
    public SheetMusicCreator(OutputManager outputManager) {
        if (outputManager == null) throw new NullPointerException();
        this.outputManager = outputManager;
    }       
    
    /**
     * Saves the sheet music as a gif image.
     * 
     * @param gifFileName the name of the gif file to save the image to
     * @throws java.lang.Exception if an error occurs
     */
    protected void saveAsGifImage(final String gifFileName) throws Exception {
        // create a temp file...
        FileHelper.createAndUseTempFile("TempGuido", ".gmn", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {                    
                saveGuidoFile(tempFileName);                                                
                convertGmnFileToGifFile(tempFileName, gifFileName);
            }
        });
    }
    
    /**
     * Saves the guido notation to a file.
     * 
     * @param fileName the name of the file to save the guido notation to
     * @throws java.io.IOException if an I/O error occurs
     */
    protected void saveGuidoFile(String fileName) throws IOException {
        // create a buffered writer...
        FileHelper.createAndUseFileBufferedWriter(fileName, new FileHelper.BufferedWriterUser() {
            public void useBufferedWriter(BufferedWriter bufferedWriter) throws IOException  {                
                bufferedWriter.write(SheetMusicCreator.this.outputManager.getGuidoNotation());                    
            }
        });
    }
    
    /**
     * Converts a guido gmn file to a gif image.
     * 
     * @param gmnFileName the file containing the gmn data
     * @param gifFileName the file to save the gif image to
     * @return the exit code of the guido process
     * @throws java.io.IOException if an I/O error occurs
     * @throws java.lang.InterruptedException if the process is interrupted
     */
    private static int convertGmnFileToGifFile(String gmnFileName, String gifFileName) throws IOException, InterruptedException {        
        ProcessBuilder pb = new ProcessBuilder();           
        pb.directory(new File(CURRENT_DIR));
        
        pb.command(
             GUIDO_DIRECTORY + File.separator +  GUIDO_2_GIF_EXE_FILE, 
            "-i", gmnFileName,             
            "-o", gifFileName);
        
        return ProcessRunner.runProcess(pb);
    } 
}
