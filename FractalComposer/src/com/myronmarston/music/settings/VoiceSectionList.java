package com.myronmarston.music.settings;

import java.util.AbstractList;
import java.util.HashMap;

/**
 * An instance of this contains a subset of all the VoiceSections of the
 * entire fractal piece.  Specifically, it contains all the VoiceSections
 * for a particular voice or for a particular section.
 * None of the VoiceSections are actually stored here; instead, all voice 
 * sections for a fractal piece are stored in a hash table, and this delegates 
 * to that.  This list is unmodifiable by design.  
 * According to the javadocs, I only have to implement get(int index) 
 * and size() to implement a unmodifiable list.  The AbstractList implements 
 * the other methods by using these.
 * 
 * @author Myron
 */
public class VoiceSectionList extends AbstractList<VoiceSection> {       
    private HashMap<VoiceSectionHashMapKey, VoiceSection> voiceSectionHash;
    private AbstractVoiceOrSection constantVoiceOrSection;

    /**
     * Constructor.
     * 
     * @param voiceSectionHash HashMap that stores all the VoiceSections for
     *        the entire FractalPiece
     * @param constantVoiceOrSection the Voice or Section that is constant for
     *        all VoiceSections in this list
     */
    public VoiceSectionList(HashMap<VoiceSectionHashMapKey, VoiceSection> voiceSectionHash, AbstractVoiceOrSection constantVoiceOrSection) {
        this.voiceSectionHash = voiceSectionHash;
        this.constantVoiceOrSection = constantVoiceOrSection;
    }    
    
    @Override
    public VoiceSection get(int index) {
        VoiceSectionHashMapKey key = this.constantVoiceOrSection.getHashMapKeyForOtherTypeIndex(index);
        return this.voiceSectionHash.get(key);
    }

    @Override
    public int size() {
        if (this.constantVoiceOrSection.getListOfMainType().contains(this.constantVoiceOrSection)) {
            return this.constantVoiceOrSection.getListOfOtherType().size();
        }        
        
        return 0;
    }
    
    /**
     * Increments the modCount, in order to properly trigger a 
     * ConcurrentModificationException.  Should be called when a voice section 
     * is added or removed from the hash map.  This is a bit hack-ish, but with 
     * Java's lack of built in event support (a la .NET), this is the easiest 
     * work around.  I would have liked to use Observable and Observer, but with
     * Observable being a class rather than an interface, and Java's lack of
     * multiple inheritance, that's not feasible.
     */
    protected void incrementModCount() {
        this.modCount++;
    }         
}
