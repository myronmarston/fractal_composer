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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class FileHelperTest {
    private String tempFileName;
    private BufferedWriter bufferedWriter;
    
    @Test
    public void createAndUseTempFile() throws Exception {        
        FileHelper.createAndUseTempFile("TestPrefix", ".TestSuffix", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                assertTrue(tempFileName.substring(tempFileName.lastIndexOf(File.separator) + 1).startsWith("TestPrefix"));
                assertTrue(tempFileName.endsWith(".TestSuffix"));
                                
                assertTrue((new File(tempFileName)).exists()); 
                FileHelperTest.this.tempFileName = tempFileName;
            }
        });
        
        assertFalse((new File(tempFileName)).exists()); 
    }
    
    @Test
    public void createAndUseFileBufferedWriter() throws Exception {
        FileHelper.createAndUseTempFile("TestPrefix", ".TestSuffix", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                FileHelper.createAndUseFileBufferedWriter(tempFileName, new FileHelper.BufferedWriterUser() {
                    public void useBufferedWriter(BufferedWriter bufferedWriter) throws IOException {
                        bufferedWriter.write("Here is a string.");                        
                        FileHelperTest.this.bufferedWriter = bufferedWriter;
                    }
                });
                
                // make sure the buffered writer was closed
                // flush() throws an IOException if the writer has been closed.
                try {
                    bufferedWriter.flush();
                    fail();
                } catch (IOException ex) {}                
            
                BufferedReader br = new BufferedReader(new FileReader(tempFileName));
                assertEquals("Here is a string.", br.readLine());
            }            
        });
    }           
    
    @Test
    public void readFileIntoString() throws Exception {
        String newLine = System.getProperty("line.separator");
        final String testString = "a string " + newLine + " another line" + newLine + " a third line";        
        
        FileHelper.createAndUseTempFile("Test", ".txt", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                FileHelper.createAndUseFileBufferedWriter(tempFileName, new FileHelper.BufferedWriterUser() {
                    public void useBufferedWriter(BufferedWriter bufferedWriter) throws IOException {
                        bufferedWriter.write(testString);
                    }
                });
                
                String stringFromFile = FileHelper.readFileIntoString(tempFileName);                
                assertEquals(testString, stringFromFile);
            }
        });
    }
}