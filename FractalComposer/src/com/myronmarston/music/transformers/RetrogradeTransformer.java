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

package com.myronmarston.music.transformers;

import com.myronmarston.music.NoteList;
import java.util.Collections;


/**
 * Transformer that applies retrograde to the given NoteList.
 * Example: C5 G5 D5 C5 -> C5 D5 G5 C5
 * 
 * @author Myron
 */
public class RetrogradeTransformer implements Transformer {
        
    public NoteList transform(NoteList input) {
        // make a copy to reverse...
        CopyTransformer copyTrans = new CopyTransformer();
        NoteList output = copyTrans.transform(input);
        
        // reverse the copy, rather than the original input...
        Collections.reverse(output);        
        return output;
    }
}
