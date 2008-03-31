package com.myronmarston.music.settings;

import java.util.List;

/**
 * An abstract class containing a common interface and common logic shared
 * by the Voice and Section classes.
 *   
 * @author Myron
 */
public abstract class AbstractVoiceOrSection {
    private FractalPiece fractalPiece;
    private VoiceSectionList voiceSections;

    /**
     * Constructor.
     * 
     * @param fractalPiece The FractalPiece that this Voice or 
     *        Section is a part of
     */
    protected AbstractVoiceOrSection(FractalPiece fractalPiece) {
        this.fractalPiece = fractalPiece;
    } 
    
    /**
     * Gets the FractalPiece that this Voice or Section is a part of.
     * 
     * @return The FractalPiece that this Voice or Section is a part of
     */
    public FractalPiece getFractalPiece() {
        return this.fractalPiece;
    }
    
    
    /**
     * Gets a list of VoiceSections.  This is guarenteed to never return null.
     * The list of VoiceSections will be a subset of all the VoiceSections for
     * the entire FractalPiece.  Specifically, if this is a Voice, the list will
     * contain each VoiceSection for this Voice, and for each Section.  If this
     * is a Section, the list will contain each VoiceSection for this Section, 
     * and for each Voice.
     * 
     * @return the list of VoiceSections
     */
    public VoiceSectionList getVoiceSections() {
        if (voiceSections == null) voiceSections = new VoiceSectionList(this.getFractalPiece().getVoiceSections(), this);                           
        return voiceSections;
    }
    
    /**
     * When implemented by the Voice class, this should 
     * return a list of all the Sections in the fractal piece.
     * When implemented by the Section class, this should 
     * return a list of all the Voices in the fractal piece.
     * 
     * @return a List<Voice> (if the implementing type is Section) 
     *         or List<Section> (if the implementing type is Voice)
     */
    public abstract List getListOfIntersectingType();
    
    /**
     * Creates a VoiceSectionHashMapKey, combining this object with an object 
     * of the intersecting type, based on the index.
     * 
     * @param index The index in the list of the intersecting type to combine 
     *        with to create the hash map key
     * @return A VoiceSectionHashMapKey that can be used to get a 
     *         specific VoiceSection
     */
    public abstract VoiceSectionHashMapKey getHashMapKeyForIntersectingTypeIndex(int index);
}
