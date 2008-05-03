package com.myronmarston.music.scales;

import org.simpleframework.xml.*;

import com.myronmarston.music.NoteName;
import java.util.*;

/**
 * Enumerates the two main tonalities: Major and Minor.
 *    
 * @author Myron
 */
public enum Tonality {      
    /**
     * Major tonality.
     */    
    Major((byte) 0), 

    /**
     * Minor tonality.
     */    
    Minor((byte) 1);
    
    /**
     * Value for a NoteName's sharps or flats that indicates it is an invalid
     * key for a given tonality.
     */
    public final static int INVALID_KEY = Integer.MAX_VALUE;  
    private List<NoteName> validKeyNames;
    private final byte midiValue;

    private Tonality(byte midiValue) {
        this.midiValue = midiValue;
    } 
    
    /**
     * Gets the midi value for this tonality.
     * 
     * @return 0 for major, 1 for minor
     */
    public byte getMidiValue() {
        return midiValue;
    }       
    
    /**
     * Gets whether or not the given key name is valid for this tonality.  
     * A key name is invalid if it would produce a key signature with double
     * sharps or flats, such as A# major.
     * 
     * @param key the key name to test
     * @return true if the key is valid, false if it is invalid
     */
    public boolean isValidKeyName(NoteName key) {
        return this.getSharpsOrFlatsForKeyName(key) != INVALID_KEY;
    }

    /**
     * Gets a list of all valid key names for this tonality.  Key names that
     * have double sharps or flats in their key signature are note included.
     * This also sorts the list based on the circle of 5ths.
     * 
     * @return a list of valid key names
     */
    public synchronized List<NoteName> getValidKeyNames() {
        if (validKeyNames == null) {
            validKeyNames = new ArrayList<NoteName>();
            
            for (NoteName noteName : NoteName.values()) {
                if (this.isValidKeyName(noteName)) validKeyNames.add(noteName);
            }

            // sort the list...
            Collections.sort(validKeyNames, new Comparator<NoteName>() {
                    public int compare(NoteName n1, NoteName n2) {        
                        return getSharpsOrFlatsForKeyName(n1) - getSharpsOrFlatsForKeyName(n2);
                    }
                 }
            );
        }        

        return validKeyNames;
    }
    
    /**
     * Gets the number of flats or sharps used in the key signature of the 
     * given key for this tonality.
     * 
     * @param keyName the tonal center 
     * @return the number of flats (negative) or sharps (positive)
     */
    public int getSharpsOrFlatsForKeyName(NoteName keyName) {
        switch (this) {
            case Major: return keyName.getMajorKeySharpsOrFlats();
            case Minor: return keyName.getMinorKeySharpsOrFlats();
            default: throw new AssertionError("Unknown tonality.");
        }
    }
}
