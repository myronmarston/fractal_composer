package com.myronmarston.music.settings;

import com.myronmarston.music.NoteList;
import com.myronmarston.music.transformers.OctaveTransformer;
import com.myronmarston.music.transformers.RhythmicDurationTransformer;
import java.util.List;

/**
 * A FractalPiece is composed of several voices.  A Voice is analogous to 
 * a contrapuntal voice in music theory.  It is composed of a series of 
 * sections, each of which is composed of a series of notes and or/rests, 
 * played consecutively.  One voice will never have multiple notes
 * sounded simultaneously.
 * 
 * @author Myron
 */
public class Voice extends AbstractVoiceOrSection<Voice, Section> {
    private int octaveAdjustment = 0;
    private double speedScaleFactor = 1d;
    private NoteList modifiedGerm;
    
    /**
     * Constructor.
     * 
     * @param fractalPiece the FractalPiece this voice is a part of
     */
    protected Voice(FractalPiece fractalPiece) {
        super(fractalPiece);
    }    
        
    /**
     * Gets how many octaves to adjust the germ for use by this voice.
     * 
     * @return number of octaves to adjust
     */
    public int getOctaveAdjustment() {
        return octaveAdjustment;
    }
    
    /**
     * Sets how many octaves to adjust the germ for use by this voice.
     * 
     * @param val number of octaves to adjust
     */
    public void setOctaveAdjustment(int val) {
        if (this.getOctaveAdjustment() != val) clearModifiedGerm();
        this.octaveAdjustment = val;
    }

    /**
     * Gets the speed scale factor to apply to the germ for use by this voice.
     * 
     * @return the speed scale factor
     */
    public double getSpeedScaleFactor() {
        return speedScaleFactor;
    }
    
    /**
     * Sets the speed scale factor to apply to the germ for use by this voice.
     * 
     * @param val the speed scale factor
     */
    public void setSpeedScaleFactor(double val) {
        if (this.speedScaleFactor != val) clearModifiedGerm();
        this.speedScaleFactor = val;
    }
    
    /**
     * Gets the modified germ, based on the current settings.
     * 
     * @return the modified germ
     */
    public NoteList getModifiedGerm() {
        if (modifiedGerm == null) this.modifiedGerm = this.generateModifiedGerm();
        return modifiedGerm;
    }
    
    /**
     * Gets a NoteList containing the notes for all sections of this voice.
     * 
     * @return a NoteList for the entire voice
     */
    public NoteList getEntireVoice() {
        NoteList entireVoice = new NoteList();        
        double sectionDuration;
        
        for (VoiceSection vs : this.getVoiceSections()) {
            sectionDuration = vs.getSection().getDuration();
            
            entireVoice.addAll(vs.getLengthenedVoiceSectionResult(sectionDuration));
        }
        
        return entireVoice;
    }
    
    /**
     * Sets the modifiedGerm field to null.  Should be called anytime a field
     * that affects the modified germ changes.
     */
    protected void clearModifiedGerm() {
        this.modifiedGerm = null;
    }
    
    /**
     * Generates the modified germ based on the current settings.
     * 
     * @return the modified germ.
     */
    protected NoteList generateModifiedGerm() {
        OctaveTransformer octaveT = new OctaveTransformer(this.getOctaveAdjustment());
        RhythmicDurationTransformer speedT = new RhythmicDurationTransformer(this.getSpeedScaleFactor());
        
        NoteList temp = octaveT.transform(this.getFractalPiece().getGerm());
        return speedT.transform(temp);
    }
    
    @Override
    protected List<Voice> getListOfMainType() {
        return this.getFractalPiece().getVoices();
    }
    
    @Override
    protected List<Section> getListOfOtherType() {
        return this.getFractalPiece().getSections();
    }

    @Override
    protected VoiceSectionHashMapKey getHashMapKeyForOtherTypeIndex(int index) {
        Section indexedSection = this.getFractalPiece().getSections().get(index);
        return new VoiceSectionHashMapKey(this, indexedSection);
    }

    @Override
    protected VoiceSection instantiateVoiceSection(Section vOrS) {
        return new VoiceSection(this, vOrS);
    }   
}

