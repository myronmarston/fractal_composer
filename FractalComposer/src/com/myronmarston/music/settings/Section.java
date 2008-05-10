package com.myronmarston.music.settings;

import com.myronmarston.util.Fraction;
import com.myronmarston.music.NoteList;
import java.io.IOException;
import org.simpleframework.xml.*;
import java.util.*;
import javax.sound.midi.InvalidMidiDataException;


/**
 * The entire fractal piece is composed of a series of sections.  Each section
 * represents a group of bars of the piece of music, e.g., bars 8-15. 
 * 
 * @author Myron
 */
@Root
public class Section extends AbstractVoiceOrSection<Section, Voice> {    
    
    /**
     * Constructor.
     * 
     * @param fractalPiece the FractalPiece this section is a part of
     */
    protected Section(FractalPiece fractalPiece) {
        super(fractalPiece);
    }     
    
    /**
     * Provided for xml deserialization.
     */
    private Section() {
        this(null);
    }
    
    /**
     * Gets the duration for this entire section.  This will be the duration of
     * the longest voice section.
     * 
     * @return the duration of this entire section
     */
    @SuppressWarnings("unchecked")
    public Fraction getDuration() {
        ArrayList<Fraction> voiceSectionDurations = new ArrayList<Fraction>(this.getListOfOtherType().size());
        
        for (VoiceSection vs : this.getVoiceSections()) {
            voiceSectionDurations.add(vs.getVoiceSectionResult().getDuration());
        }
        
        return Collections.max(voiceSectionDurations);
    }
    
    /**
     * Constructs a midi sequence for this section and saves it to a midi file.
     * 
     * @param fileName the filename to use
     * @throws java.io.IOException if there is a problem writing to the file
     * @throws javax.sound.midi.InvalidMidiDataException if there is invalid
     *         midi data
     */
    public void saveSectionResultToMidiFile(String fileName) throws IOException, InvalidMidiDataException {
        List<NoteList> voiceSectionResults = new ArrayList<NoteList>(this.getVoiceSections().size());
        for (VoiceSection vs : this.getVoiceSections()) {
            voiceSectionResults.add(vs.getVoiceSectionResult());
        }
        
        this.getFractalPiece().saveNoteListsAsMidiFile(voiceSectionResults, fileName);
    }
    
    @Override
    protected List<Section> getListOfMainType() {
        return this.getFractalPiece().getSections();
    }
    
    @Override
    protected List<Voice> getListOfOtherType() {
        return this.getFractalPiece().getVoices();
    }    

    @Override
    protected VoiceSectionHashMapKey getHashMapKeyForOtherTypeIndex(int index) {
        Voice indexedVoice = this.getFractalPiece().getVoices().get(index);
        return new VoiceSectionHashMapKey(indexedVoice, this);
    }

    @Override
    protected VoiceSection instantiateVoiceSection(Voice vOrS) {
        return new VoiceSection(vOrS, this);
    }           
}
