package com.myronmarston.music.settings;

import java.util.*;
import org.simpleframework.xml.load.*;

/**
 * List of Voices or Sections that manages the creation and deletion of the 
 * VoiceSection objects as necessitated by add() and remove().
 * 
 * @param <M> the main type for this list (Voice or Section)
 * @param <O> the other type (Voice or Section)
 * @author Myron
 */
public class VoiceOrSectionList<M extends AbstractVoiceOrSection, O extends AbstractVoiceOrSection> extends AbstractList<M> {
    private ArrayList<M> internalList = new ArrayList<M>();
    private HashMap<VoiceSectionHashMapKey, VoiceSection> voiceSections;
    private boolean isDeserializing = false;

    /**
     * Constructor.
     * 
     * @param voiceSections the hash map of VoiceSections for the FractalPiece.
     */
    public VoiceOrSectionList(HashMap<VoiceSectionHashMapKey, VoiceSection> voiceSections) {
        this.voiceSections = voiceSections;
    }
    
    /**
     * Provided for xml deserialization.
     */
    private VoiceOrSectionList() {
        isDeserializing = true;
    }
    
    @Override
    public M get(int index) {
        return internalList.get(index);
    }

    @Override
    public int size() {
        return internalList.size();
    }

    @Override        
    public boolean add(M mainVorS) {
        this.add(this.size(), mainVorS);
        return true; // return true since our collection changed...
    }

    /**
     * Insert the given Voice or Section into the list at the given index.
     * 
     * @param index the insertion index
     * @param mainVorS the voice or section to add
     */
    @Override
    @SuppressWarnings("unchecked")
    public void add(int index, M mainVorS) {               
        internalList.add(index, mainVorS); 
                
        // the rest of this method is only intended to be run during normal
        // object usage, not during deserialization.
        if (isDeserializing) return;
        
        int otherTypeIndex = 0; 
        VoiceSection vs;             
        VoiceSectionHashMapKey key;
        
        // create the necessary Voice Sections...
        for (AbstractVoiceOrSection otherVOrS : (List<O>) mainVorS.getListOfOtherType()) {
            vs = mainVorS.instantiateVoiceSection(otherVOrS);
            
            key = vs.createHashMapKey();
            assert !this.voiceSections.containsKey(key) : this.voiceSections.get(key);
            this.voiceSections.put(key, vs);
            
            // assert that our lists have it...
            assert mainVorS.getVoiceSections().get(otherTypeIndex++) == vs;
            assert otherVOrS.getVoiceSections().get(index) == vs;
            
            // notify the VoiceSectionLists that they have been modified...
            vs.getVoice().getVoiceSections().incrementModCount();
            vs.getSection().getVoiceSections().incrementModCount();
        }
        
        this.modCount++;        
    }
            
    @Override
    public M remove(int index) {
        M itemToRemove = this.internalList.get(index);
        int voiceSectionListSize = itemToRemove.getVoiceSections().size();                
        
        // remove the related voice sections...
        // we can't modify the list while iterating over it or we'll get a
        // ConcurrentModificationException, so we temporarily store the ones
        // to remove in another list.
        ArrayList<VoiceSection> voiceSectionsToRemove = new ArrayList<VoiceSection>(voiceSectionListSize);
        for (VoiceSection vs : itemToRemove.getVoiceSections()) {
            voiceSectionsToRemove.add((vs));
        }
        
        this.modCount++;
        this.internalList.remove(index);
        
        for (VoiceSection vs : voiceSectionsToRemove) {
            this.voiceSections.remove(vs.createHashMapKey());
                                               
            // notify the VoiceSectionLists that they have been modified...
            vs.getVoice().getVoiceSections().incrementModCount();
            vs.getSection().getVoiceSections().incrementModCount();                        
        }      
        
        assert itemToRemove.getVoiceSections().size() == 0 : itemToRemove.getVoiceSections().size();
        
        return itemToRemove;
    }        
    
    @Commit
    private void deserializationComplete() {
        this.isDeserializing = false;
    }
}
