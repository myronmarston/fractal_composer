package com.myronmarston.music.settings;

import com.myronmarston.music.NoteList;
import com.myronmarston.music.scales.Scale;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The GrandDaddy of them all.  This class controls the entire piece of music.
 * Before generating the piece, you will need to provide values for each
 * of the appropriate settings: the voices (via getVoices()), the sections 
 * (via getSections()), the germ, the scale, etc.
 * 
 * @author Myron
 */
public class FractalPiece {
    private NoteList germ;
    private Scale scale;
    private ArrayList<Voice> voices;
    private ArrayList<Section> sections;
    private HashMap<VoiceSectionHashMapKey, VoiceSection> voiceSections;

    /**
     * Returns the germ NoteList.  Guarenteed to never be null.  
     * The germ is the short melody from which the entire piece is generated.
     * 
     * @return the germ NoteList
     */
    public NoteList getGerm() {
        if (germ == null) germ = new NoteList();
        return germ;
    }

    /**
     * Returns the Scale.  The Scale is used to determine the tonality of the
     * piece, and will also be used to set the key signature of the Midi 
     * sequence.
     * 
     * @return the Scale used by this FractalPiece
     */
    public Scale getScale() {
        return scale;
    }

    /**
     * Sets the Scale used by this FractalPiece.  The Scale is used to 
     * determine the tonality of the piece, and will also be used to set the 
     * key signature of the Midi sequence.
     * 
     * @param scale the Scale to be used by this FractalPiece
     */
    public void setScale(Scale scale) {
        this.scale = scale;
    }
    
    /**
     * Gets the hash table containing all the VoiceSections for the entire
     * piece.  
     * 
     * @return the hash table containing all VoiceSections
     */
    protected HashMap<VoiceSectionHashMapKey, VoiceSection> getVoiceSections() {
        if (voiceSections == null) voiceSections = new HashMap<VoiceSectionHashMapKey, VoiceSection>();
        return voiceSections;
    }
    
    /**
     * Gets a list of Voices for the FractalPiece.  To add a Voice, use the 
     * provided createVoice() method, rather than attempting to add it to the
     * list on your own.
     * 
     * @return the list of Voices
     */
    public ArrayList<Voice> getVoices() {
        if (voices == null) voices = new ArrayList<Voice>();
        return voices;
    }
        
    /**
     * Gets a list of Sections for the FractalPiece.  To add a Section, use the
     * provided createSection() method, rather than attempting to add it to the
     * list on your own.
     * 
     * @return the list of Sections
     */
    public ArrayList<Section> getSections() {
        if (sections == null) sections = new ArrayList<Section>();
        return sections;
    }
    
    
    /**
     * Creates a Voice for the FractalPiece, and adds it to the Voice list.
     * 
     * @return the created Voice
     */
    public Voice createVoice() {
        int sectionIndex = 0;
        int voiceIndex = this.getVoices().size();
        VoiceSection vs;
        Voice v = new Voice(this);
        this.getVoices().add(v);
        
        // add the voice sections to our hash map...
        for (Section s : this.getSections()) {  
            vs = new VoiceSection(v, s);
            this.getVoiceSections().put(new VoiceSectionHashMapKey(v, s), vs);
            
            // assert that our lists have it...
            assert v.getVoiceSections().get(sectionIndex++) == vs;
            assert s.getVoiceSections().get(voiceIndex) == vs;
        }        
        
        return v;        
    }    
    
    /**
     * Creates a Section for the FractalPiece, and adds it to the Section list.
     * 
     * @return the created Section
     */
    public Section createSection() {
        int sectionIndex = this.getSections().size();
        int voiceIndex = 0;
        VoiceSection vs;
        Section s = new Section(this);
        this.getSections().add(s);
        
        // add the voice sections to our hash map...
        for (Voice v : this.getVoices()) {  
            vs = new VoiceSection(v, s);
            this.getVoiceSections().put(new VoiceSectionHashMapKey(v, s), vs);
            
            // assert that our lists have it...
            assert v.getVoiceSections().get(sectionIndex) == vs;
            assert s.getVoiceSections().get(voiceIndex++) == vs;
        }        
        
        return s;       
    }           
}
