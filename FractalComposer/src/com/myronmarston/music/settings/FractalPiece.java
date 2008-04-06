package com.myronmarston.music.settings;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;
import com.myronmarston.music.scales.Scale;
import com.myronmarston.util.MathHelper;

import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

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
    private VoiceOrSectionList<Voice, Section> voices = new VoiceOrSectionList<Voice, Section>(this.getVoiceSections());
    private VoiceOrSectionList<Section, Voice> sections = new VoiceOrSectionList<Section, Voice>(this.getVoiceSections());
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
    public List<Voice> getVoices() {        
        return voices;
    }
        
    /**
     * Gets a list of Sections for the FractalPiece.  To add a Section, use the
     * provided createSection() method, rather than attempting to add it to the
     * list on your own.
     * 
     * @return the list of Sections
     */
    public List<Section> getSections() {        
        return sections;
    }
    
    /**
     * Creates a Voice for the FractalPiece, and adds it to the Voice list.
     * 
     * @return the created Voice
     */
    public Voice createVoice() {
        Voice v = new Voice(this);
        this.voices.add(v);
        return v;
    }
    
    /**
     * Creates a Section for the FractalPiece, and adds it to the Section list.
     * 
     * @return the created Section
     */
    public Section createSection() {
        Section s = new Section(this);
        this.sections.add(s);
        return s;
    }      
    
    /**
     * Generates a midi sequence for the entire fractal piece, based on the 
     * settings on the voices and sections.
     * 
     * @return the generated fractal piece
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    public Sequence generatePiece() throws InvalidMidiDataException {
        // first, get all our voice results, and cache them in a list...
        ArrayList<NoteList> voiceResults = new ArrayList<NoteList>();
        for (Voice v : this.getVoices()) voiceResults.add(v.getEntireVoice());
        
        // next, figure out the resolution of our Midi sequence...
        ArrayList<Long> uniqueDurationDenominators = new ArrayList<Long>();
        for (NoteList nl : voiceResults) {
            for (Note n : nl) {
                if (!uniqueDurationDenominators.contains(n.getDuration().denominator())) {
                    uniqueDurationDenominators.add(n.getDuration().denominator());
                }                
            }
        }        
        long midiTickResolution = MathHelper.leastCommonMultiple(uniqueDurationDenominators);
         
        // next, contruct our sequence...
        Sequence sequence = new Sequence(Sequence.PPQ, (int) midiTickResolution);
        
        // finally, create and fill our midi tracks...
        for (NoteList nl : voiceResults) {            
            nl.createAndFillMidiTrack(sequence, scale, new Fraction(0, 1));                
        }        
        
        return sequence;
    }
}
