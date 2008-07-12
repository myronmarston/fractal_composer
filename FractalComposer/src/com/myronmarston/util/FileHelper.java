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

package com.myronmarston.util;

import java.io.*;
import java.util.logging.*;

/**
 * A class that provides static methods to manage temporary file resources.
 * 
 * @author Myron
 */
public class FileHelper {
    
    /**
     * Interface for using a temporary file.
     */
    public interface TempFileUser {
        /**
         * Uses a temporary file.
         * 
         * @param tempFileName the name of the temp file to use
         * @throws java.lang.Exception if there is an error
         */
        public void useTempFile(String tempFileName) throws Exception;
    }
    
    /**
     * Interface for using a buffered writer.
     */
    public interface BufferedWriterUser {
        /**
         * Uses a buffered writer.
         * 
         * @param bufferedWriter the bufferd writer to use
         * @throws java.io.IOException if there is an I/O error
         */
        public void useBufferedWriter(BufferedWriter bufferedWriter) throws IOException ;
    }
            
    /**
     * Creates a temporary file, and allows a TempFileUser to use it.  When it 
     * is complete, the file is deleted.
     * 
     * @param tempFilePrefix the temp file prefix
     * @param tempFileSuffix the temp file suffix
     * @param tempFileUser the object that will use the temp file
     * @throws java.lang.Exception if an error occurs
     */
    public static void createAndUseTempFile(String tempFilePrefix, String tempFileSuffix, TempFileUser tempFileUser) throws Exception {
        File tempFile = null;
        try {
            tempFile = File.createTempFile(tempFilePrefix, tempFileSuffix);
            String tempFileName = tempFile.getCanonicalPath();                
            tempFileUser.useTempFile(tempFileName);            
        } finally {        
            if (tempFile != null) {
                try { 
                    tempFile.delete();
                } catch (Exception ex) {
                    // It's a temp file, so if we can't delete it, it's not really a big deal.
                    // We catch the exception and just log the error instead of passing
                    // the exception up the call stack.
                    Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * Creates a buffered writer to write to a file using UTF-8 encoding and 
     * allows a BufferedWriterUser to use it.  When it is complete, the buffered
     * writer is flushed and all the consumed resources are released.
     * 
     * @param fileName the name of the file
     * @param bufferedWriterUser the object that will use the buffered writer
     * @throws java.io.IOException if an I/O error occurs
     */
    public static void createAndUseFileBufferedWriter(String fileName, BufferedWriterUser bufferedWriterUser) throws IOException {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(fileName);
            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);            
            bufferedWriterUser.useBufferedWriter(bw);
        } finally {                   
            // first, flush the buffered writer...
            if (bw != null) bw.flush();
            
            // close our streams, in the order we opened them
            if (fos != null) fos.close();                    
            if (osw != null) osw.close();
            if (bw != null) bw.close();
        }   
    }
   
    /**
     * Creates a text file, using UTF-8 encoding.
     * 
     * @param fileName the name of the text file
     * @param fileContents the text contents to put in the text file
     * @throws java.io.IOException if there is an I/O error
     */
    public static void createTextFile(final String fileName, final String fileContents) throws IOException {   
        FileHelper.createAndUseFileBufferedWriter(fileName, new FileHelper.BufferedWriterUser() {
            public void useBufferedWriter(BufferedWriter bufferedWriter) throws IOException {                                
                boolean firstLineWritten = false;
                for (String line : fileContents.split("\n")) {
                    if (firstLineWritten) bufferedWriter.newLine();
                    bufferedWriter.write(line);
                    firstLineWritten = true;
                }                                
            }
        });
    }
    
    /**
     * Reads the contents of a file into a string using UTF-8 encoding.
     * 
     * @param fileName the name of the file
     * @return the string contents of the file
     * @throws java.io.IOException if an I/O error occurs
     */
    public static String readFileIntoString(String fileName) throws IOException {
        StringBuilder strBuilder = new StringBuilder();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;     
        String newLine = System.getProperty("line.separator");
        String line;
        boolean firstLineRead = false;
        try {
            fis = new FileInputStream(fileName);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
                        
            while (true) {                
                line = br.readLine();
                if (line == null) break;
                
                if (firstLineRead) strBuilder.append(newLine);
                strBuilder.append(line);                
                firstLineRead = true;
            }
            
            return strBuilder.toString();
        } finally {            
            if (fis != null) fis.close();
            if (isr != null) isr.close();
            if (br != null) br.close();
        }    
    }
}
