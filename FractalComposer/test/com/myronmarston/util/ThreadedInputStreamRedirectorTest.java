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
public class ThreadedInputStreamRedirectorTest {
    
    @Test
    public void run() throws Exception {
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        try {
            String aString = "Here is a string.";        
            byte[] byteArray = aString.getBytes("UTF-8");
            bais = new ByteArrayInputStream(byteArray);
            baos = new ByteArrayOutputStream();
            ThreadedInputStreamRedirector tisr = new ThreadedInputStreamRedirector(bais, baos);

            tisr.start();

            // wait for it to complete...
            while (tisr.getState() != Thread.State.TERMINATED) {
                Thread.sleep(10);
            }

            assertEquals(0, bais.available());        
            assertEquals(aString, baos.toString("UTF-8"));
        } finally {
            if (bais != null) bais.close();
            if (baos != null) baos.close();
        }        
    }
}