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

package com.myronmarston.music.notation;

import com.myronmarston.music.Dynamic;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Myron
 */
public class NotationDynamicTest {  

    @Test
    public void toGuidoString() {
        testToGuidoString(NotationNote.NOTE_PLACEHOLDER, null, NotationDynamic.Articulation.NONE);
        testToGuidoString("\\intens<\"ppp\"> " + NotationNote.NOTE_PLACEHOLDER, Dynamic.PPP, NotationDynamic.Articulation.NONE);
        testToGuidoString("\\intens<\"pp\"> " + NotationNote.NOTE_PLACEHOLDER, Dynamic.PP, NotationDynamic.Articulation.NONE);
        testToGuidoString("\\intens<\"p\"> " + NotationNote.NOTE_PLACEHOLDER, Dynamic.P, NotationDynamic.Articulation.NONE);
        testToGuidoString("\\intens<\"mp\"> " + NotationNote.NOTE_PLACEHOLDER, Dynamic.MP, NotationDynamic.Articulation.NONE);
        testToGuidoString("\\intens<\"mf\"> " + NotationNote.NOTE_PLACEHOLDER, Dynamic.MF, NotationDynamic.Articulation.NONE);
        testToGuidoString("\\intens<\"f\"> " + NotationNote.NOTE_PLACEHOLDER, Dynamic.F, NotationDynamic.Articulation.NONE);
        testToGuidoString("\\intens<\"ff\"> " + NotationNote.NOTE_PLACEHOLDER, Dynamic.FF, NotationDynamic.Articulation.NONE);
        testToGuidoString("\\intens<\"fff\"> " + NotationNote.NOTE_PLACEHOLDER, Dynamic.FFF, NotationDynamic.Articulation.NONE);                
        
        testToGuidoString("\\accent(" + NotationNote.NOTE_PLACEHOLDER + ")", null, NotationDynamic.Articulation.ACCENT);
        testToGuidoString("\\intens<\"ppp\"> \\accent(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.PPP, NotationDynamic.Articulation.ACCENT);
        testToGuidoString("\\intens<\"pp\"> \\accent(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.PP, NotationDynamic.Articulation.ACCENT);
        testToGuidoString("\\intens<\"p\"> \\accent(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.P, NotationDynamic.Articulation.ACCENT);
        testToGuidoString("\\intens<\"mp\"> \\accent(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.MP, NotationDynamic.Articulation.ACCENT);
        testToGuidoString("\\intens<\"mf\"> \\accent(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.MF, NotationDynamic.Articulation.ACCENT);
        testToGuidoString("\\intens<\"f\"> \\accent(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.F, NotationDynamic.Articulation.ACCENT);
        testToGuidoString("\\intens<\"ff\"> \\accent(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.FF, NotationDynamic.Articulation.ACCENT);
        testToGuidoString("\\intens<\"fff\"> \\accent(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.FFF, NotationDynamic.Articulation.ACCENT);
        
        testToGuidoString("\\marcato(" + NotationNote.NOTE_PLACEHOLDER + ")", null, NotationDynamic.Articulation.MARCATO);
        testToGuidoString("\\intens<\"ppp\"> \\marcato(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.PPP, NotationDynamic.Articulation.MARCATO);
        testToGuidoString("\\intens<\"pp\"> \\marcato(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.PP, NotationDynamic.Articulation.MARCATO);
        testToGuidoString("\\intens<\"p\"> \\marcato(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.P, NotationDynamic.Articulation.MARCATO);
        testToGuidoString("\\intens<\"mp\"> \\marcato(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.MP, NotationDynamic.Articulation.MARCATO);
        testToGuidoString("\\intens<\"mf\"> \\marcato(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.MF, NotationDynamic.Articulation.MARCATO);
        testToGuidoString("\\intens<\"f\"> \\marcato(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.F, NotationDynamic.Articulation.MARCATO);
        testToGuidoString("\\intens<\"ff\"> \\marcato(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.FF, NotationDynamic.Articulation.MARCATO);
        testToGuidoString("\\intens<\"fff\"> \\marcato(" + NotationNote.NOTE_PLACEHOLDER + ")", Dynamic.FFF, NotationDynamic.Articulation.MARCATO);
    }

    private static void testToGuidoString(String expected, Dynamic dynamic, NotationDynamic.Articulation articulation) {
        NotationDynamic nd = new NotationDynamic(dynamic, articulation);
        assertEquals(expected, nd.toGuidoString());
    }
    
    @Test
    public void toLilypondString() {
        testToLilypondString("", null, NotationDynamic.Articulation.NONE);
        testToLilypondString("\\ppp", Dynamic.PPP, NotationDynamic.Articulation.NONE);
        testToLilypondString("\\pp", Dynamic.PP, NotationDynamic.Articulation.NONE);
        testToLilypondString("\\p", Dynamic.P, NotationDynamic.Articulation.NONE);
        testToLilypondString("\\mp", Dynamic.MP, NotationDynamic.Articulation.NONE);
        testToLilypondString("\\mf", Dynamic.MF, NotationDynamic.Articulation.NONE);
        testToLilypondString("\\f", Dynamic.F, NotationDynamic.Articulation.NONE);
        testToLilypondString("\\ff", Dynamic.FF, NotationDynamic.Articulation.NONE);
        testToLilypondString("\\fff", Dynamic.FFF, NotationDynamic.Articulation.NONE);                
        
        testToLilypondString("->", null, NotationDynamic.Articulation.ACCENT);
        testToLilypondString("\\ppp->", Dynamic.PPP, NotationDynamic.Articulation.ACCENT);
        testToLilypondString("\\pp->", Dynamic.PP, NotationDynamic.Articulation.ACCENT);
        testToLilypondString("\\p->", Dynamic.P, NotationDynamic.Articulation.ACCENT);
        testToLilypondString("\\mp->", Dynamic.MP, NotationDynamic.Articulation.ACCENT);
        testToLilypondString("\\mf->", Dynamic.MF, NotationDynamic.Articulation.ACCENT);
        testToLilypondString("\\f->", Dynamic.F, NotationDynamic.Articulation.ACCENT);
        testToLilypondString("\\ff->", Dynamic.FF, NotationDynamic.Articulation.ACCENT);
        testToLilypondString("\\fff->", Dynamic.FFF, NotationDynamic.Articulation.ACCENT);        
        
        testToLilypondString("-^", null, NotationDynamic.Articulation.MARCATO);
        testToLilypondString("\\ppp-^", Dynamic.PPP, NotationDynamic.Articulation.MARCATO);
        testToLilypondString("\\pp-^", Dynamic.PP, NotationDynamic.Articulation.MARCATO);
        testToLilypondString("\\p-^", Dynamic.P, NotationDynamic.Articulation.MARCATO);
        testToLilypondString("\\mp-^", Dynamic.MP, NotationDynamic.Articulation.MARCATO);
        testToLilypondString("\\mf-^", Dynamic.MF, NotationDynamic.Articulation.MARCATO);
        testToLilypondString("\\f-^", Dynamic.F, NotationDynamic.Articulation.MARCATO);
        testToLilypondString("\\ff-^", Dynamic.FF, NotationDynamic.Articulation.MARCATO);
        testToLilypondString("\\fff-^", Dynamic.FFF, NotationDynamic.Articulation.MARCATO);        
    }

    private static void testToLilypondString(String expected, Dynamic dynamic, NotationDynamic.Articulation articulation) {
        NotationDynamic nd = new NotationDynamic(dynamic, articulation);
        assertEquals(expected, nd.toLilypondString());
    }
    
    @Test
    public void equalsAndHashCodeAndClone() throws Exception {
        NotationDynamic nd1 = new NotationDynamic(Dynamic.FF, NotationDynamic.Articulation.NONE);
        NotationDynamic nd2 = nd1.clone();
        
        assertTrue(nd1.equals(nd2));
        assertTrue(nd1.hashCode() == nd2.hashCode());
        
        // different dynamic
        nd2 = new NotationDynamic(Dynamic.F, NotationDynamic.Articulation.NONE);
        assertFalse(nd1.equals(nd2));
        assertFalse(nd1.hashCode() == nd2.hashCode());
        
        // null dynamic
        nd2 = new NotationDynamic(null, NotationDynamic.Articulation.NONE);
        assertFalse(nd1.equals(nd2));
        assertFalse(nd1.hashCode() == nd2.hashCode());
        
        // different articulation
        nd2 = new NotationDynamic(Dynamic.FF, NotationDynamic.Articulation.ACCENT);
        assertFalse(nd1.equals(nd2));
        assertFalse(nd1.hashCode() == nd2.hashCode());
    }        
}