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
import java.lang.reflect.UndeclaredThrowableException;

/**
 * A class that redirects an input stream to the System.out stream.  This
 * runs on its own thread.
 * 
 * @author Myron
 */
public class ThreadedInputStreamRedirector extends Thread {
    private final InputStream inputStream;          

    /**
     * Constructor.
     * 
     * @param inputStream the input stream to redirect
     */
    public ThreadedInputStreamRedirector(InputStream inputStream) {
        this.inputStream = inputStream;
    }                

    @Override
    /**
     * Starts the process of redirecting the stream on its own thread.
     */
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));            
        String line = null;
        do {            
            try {
                line = bufferedReader.readLine();                
            } catch (IOException ex) {
                // we can't simply declare this exception on our run method 
                // since it's not declared on the superclass's run method,
                // so we wrap it in an unchecked exception to pass it on 
                // up the stack...
                throw new UndeclaredThrowableException(ex, "I/O Error reading line.");                
            }  

            if (line == null) break;
            System.out.println(line);
        } while (true);            
    }                      
}