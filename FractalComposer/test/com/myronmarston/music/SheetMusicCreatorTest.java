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
//import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class SheetMusicCreatorTest {

    @Test
    public void saveAsGifImage() throws Exception {
        final FractalPiece fp = new FractalPiece();
        fp.setGermString("G4");
        
        FileHelper.createAndUseTempFile("TestGifFile", ".gif", new FileHelper.TempFileUser() {
            public void useTempFile(String tempFileName) throws Exception {
                fp.createGermOutputManager().getSheetMusicCreator().saveAsGifImage(tempFileName);                
                File file = new File(tempFileName);
                BufferedImage image = ImageIO.read(file);
                assertNotNull(image.getData());
            }
        });
    }
}