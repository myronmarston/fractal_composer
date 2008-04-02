package com.myronmarston.music.settings;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * List of Voices or Sections that manages the creation and deletion of the 
 * VoiceSection objects as necessitated by add() and remove().
 * 
 * @param M the main type for this list (Voice or Section)
 * @param O the other type (Voice or Section)
 * @author Myron
 */
public class VoiceOrSectionList<M extends AbstractVoiceOrSection, O extends AbstractVoiceOrSection> extends AbstractList<M> {
    private ArrayList<M> internalList = new ArrayList<M>();
    private HashMap<VoiceSectionHashMapKey, VoiceSection> voiceSections;

    /**
     * Constructor.
     * 
     * @param voiceSections the hash map of VoiceSections for the FractalPiece.
     */
    public VoiceOrSectionList(HashMap<VoiceSectionHashMapKey, VoiceSection> voiceSections) {
        this.voiceSections = voiceSections;
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
    @SuppressWarnings("unchecked")
    public boolean add(M mainVorS) {
        int otherTypeIndex = 0;
        int mainTypeIndex = this.size();
        boolean result = internalList.add(mainVorS);        
        VoiceSection vs;                
        
        for (AbstractVoiceOrSection otherVOrS : (List<O>) mainVorS.getListOfOtherType()) {
            vs = mainVorS.instantiateVoiceSection(otherVOrS);
            this.voiceSections.put(vs.createHashMapKey(), vs);
            
            // assert that our lists have it...
            assert mainVorS.getVoiceSections().get(otherTypeIndex++) == vs;
            assert otherVOrS.getVoiceSections().get(mainTypeIndex) == vs;
            
            // notify the VoiceSectionLists that they have been modified...
            vs.getVoice().getVoiceSections().incrementModCount();
            vs.getSection().getVoiceSections().incrementModCount();
        }
        
        this.modCount++;
        return result;
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
        
        for (VoiceSection vs : voiceSectionsToRemove) {
            this.voiceSections.remove(vs.createHashMapKey());
                        
            // assert that our list has shrunk...
            assert itemToRemove.getVoiceSections().size() == --voiceSectionListSize;            
            
            // notify the VoiceSectionLists that they have been modified...
            vs.getVoice().getVoiceSections().incrementModCount();
            vs.getSection().getVoiceSections().incrementModCount();
        }
                                        
        this.modCount++;
        return this.internalList.remove(index);
    }        
}
