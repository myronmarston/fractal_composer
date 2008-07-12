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

import com.myronmarston.music.notation.LilypondRunException;
import com.myronmarston.util.ProcessRunner;
import com.myronmarston.util.FileHelper;
import java.io.*;
import java.util.regex.*;

/**
 * Class that manages the creation of sheet music files using Lilypond and GUIDO.
 * 
 * @author Myron
 */
public class SheetMusicCreator {    
    private final OutputManager outputManager;
    private static final String GUIDO_2_GIF_EXE_FILE = "guido2gif.exe";
    private static final String GUIDO_DIRECTORY = "guido";
    private static final String CURRENT_DIR = System.getProperty("user.dir");    
    private static String lilypondDirectory = CURRENT_DIR;
    private static final String LILYPOND_EXE_FILE = "lilypond.exe";
    private static final Pattern LILYPOND_OUTPUT_SUCCESS = Pattern.compile(".*?converting to .*?(pdf|png).*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);    
    private static final Pattern LILYPOND_OUTPUT_ERROR = Pattern.compile(".*?(warning|error).*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

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
     * Sets the directory to use while running the Lilypond process.  Defaults
     * to the current working directory.
     * 
     * @param dir the directory
     */
    synchronized public static void setLilypondDirectory(String dir) {
        lilypondDirectory = dir;
    }
    
    /**
     * Gets the directory to use while running the Lilypond process.  Defaults
     * to the current working directory.
     * 
     * @return the directory
     */
    synchronized public static String getLilypondDirectory() {
        return lilypondDirectory;
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
                
                //TODO: check GUIDO output for errors...
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
        FileHelper.createTextFile(fileName, this.outputManager.getGuidoNotation());        
    }        
    
    /**
     * Saves the lilypond results to a PDF file and one PNG file per page.
     * 
     * @param fileNameWithoutExtension the file name to save the results to. The
     *        pdf and png extensions, as well as page number, will be added
     *        automatically
     * @param lilypondContents the lilypond notation
     * @throws java.lang.Exception if there is an error
     */
    protected static void saveLilypondResults(final String fileNameWithoutExtension, final String lilypondContents) throws Exception {       
        final File psFile = new File(fileNameWithoutExtension + ".ps");
        final boolean psFileExistedBeforeLilypondRun = psFile.exists();
        try {
            FileHelper.createAndUseTempFile("Lilypond", ".ly", new FileHelper.TempFileUser() {
                public void useTempFile(String tempFileName) throws Exception {
                    FileHelper.createTextFile(tempFileName, lilypondContents);

                    ProcessBuilder pb = new ProcessBuilder();                   
                    pb.directory(new File(getLilypondDirectory()));

                    pb.command(
                        LILYPOND_EXE_FILE,        
                        "--pdf",
                        "--png",
                        "--output=" + fileNameWithoutExtension,
                        tempFileName);
                                        
                    String output = ProcessRunner.runProcess(pb);                    
                                                             
                    // if lilypond had a problem, throw an exception...
                    if (lilypondOutputIndicatesError(output)) {
                        throw new LilypondRunException(output);
                    } else {
                        System.out.println(output);   
                    }
                }
            });        
        } finally {
            // clean up the ps file created by Lilypond
            if (psFile.exists() && !psFileExistedBeforeLilypondRun) {
                psFile.delete();
            }
        }        
    }
    
    /**
     * Checks the logging messages produced by lilypond to see if it there was
     * an error.  
     * 
     * @param lilypondOutput the logging messages produced by lilypond
     * @return true if there was an error
     */
    private static boolean lilypondOutputIndicatesError(String lilypondOutput) {
        // We check two things here:
        // 1. The presence of the expected success message.  If it is
        //    missing, this is taken to be an indication of failure.
        // 2. The presence of a message containing the words "error" or 
        //    "warning".  This is taken to be an indication of failure.
        
        Matcher successMatches = SheetMusicCreator.LILYPOND_OUTPUT_SUCCESS.matcher(lilypondOutput);
        if (!successMatches.matches()) return true;
        
        Matcher errorMatches = SheetMusicCreator.LILYPOND_OUTPUT_ERROR.matcher(lilypondOutput);
        return (errorMatches.matches());
    }
    
    /**
     * Converts a guido gmn file to a gif image.
     * 
     * @param gmnFileName the file containing the gmn data
     * @param gifFileName the file to save the gif image to
     * @return string containing the std and error output of the GUIDO process
     * @throws java.io.IOException if an I/O error occurs
     * @throws java.lang.InterruptedException if the process is interrupted
     */
    private static String convertGmnFileToGifFile(String gmnFileName, String gifFileName) throws IOException, InterruptedException {        
        ProcessBuilder pb = new ProcessBuilder();           
        pb.directory(new File(CURRENT_DIR));
        
        pb.command(
             GUIDO_DIRECTORY + File.separator +  GUIDO_2_GIF_EXE_FILE, 
            "-i", gmnFileName,             
            "-o", gifFileName);
        
        return ProcessRunner.runProcess(pb);
    } 
}
