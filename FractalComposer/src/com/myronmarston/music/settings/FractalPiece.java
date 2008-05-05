package com.myronmarston.music.settings;

import com.myronmarston.music.Note;
import com.myronmarston.music.NoteList;
import com.myronmarston.music.NoteStringParseException;
import com.myronmarston.music.scales.Scale;

import EDU.oswego.cs.dl.util.concurrent.misc.Fraction;

import org.simpleframework.xml.*;
import org.simpleframework.xml.graph.CycleStrategy;
import org.simpleframework.xml.load.Persister;

import java.io.*;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;
import javax.sound.midi.*;

/**
 * The GrandDaddy of them all.  This class controls the entire piece of music.
 * Before generating the piece, you will need to provide values for each
 * of the appropriate settings: the voices (via getVoices()), the sections 
 * (via getSections()), the germ, the scale, etc.
 * 
 * @author Myron
 */
@Root
public class FractalPiece {    
    private static interface InsertIndexProvider { int getInsertIndex(List l); }
    
    @ElementList(type=Note.class, required=false)
    private NoteList germ = new NoteList();
    
    @Attribute
    private String germString = "";
    
    @Element
    private Scale scale = Scale.DEFAULT;
    
    @Element
    private TimeSignature timeSignature = TimeSignature.DEFAULT;
    
    @ElementList(type=Voice.class)
    private VoiceOrSectionList<Voice, Section> voices = new VoiceOrSectionList<Voice, Section>(this.getVoiceSections());
    
    @ElementList(type=Section.class)
    private VoiceOrSectionList<Section, Voice> sections = new VoiceOrSectionList<Section, Voice>(this.getVoiceSections());
    
    @ElementMap(entry="voiceSection")
    private HashMap<VoiceSectionHashMapKey, VoiceSection> voiceSections;
    
    @Attribute
    private boolean generateLayeredIntro = true;
    
    @Attribute
    private boolean generateLayeredOutro = true;
    private List<Section> tempIntroOutroSections = new ArrayList<Section>();
    
    /**
     * Returns the germ NoteList.  Guarenteed to never be null.  
     * The germ is the short melody from which the entire piece is generated.
     * 
     * @return the germ NoteList
     */
    public NoteList getGerm() {   
        assert germ != null : germ; // germ should never be null!
        return germ;
    }

    /**
     * Gets the germ string.
     * 
     * @return the germ string
     */
    public String getGermString() {
        assert germString != null : germString; // germString should never be null!
        return germString;
    }        
    
    /**
     * Sets the notes for the germ.
     * 
     * @param germString string containing a list of notes
     * @throws com.myronmarston.music.NoteStringParseException if the note list
     *         string cannot be parsed
     */
    public void setGermString(String germString) throws NoteStringParseException {
        this.germ = NoteList.parseNoteListString(germString, this.getScale());
        this.germString = germString;
    }

    /**
     * Returns the Scale.  The Scale is used to determine the tonality of the
     * piece, and will also be used to set the key signature of the Midi 
     * sequence.
     * 
     * @return the Scale used by this FractalPiece
     */
    public Scale getScale() {
        assert scale != null : scale; // scale should never be null!
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
        if (scale == null) throw new IllegalArgumentException("Scale cannot be set to null.");        
        
        if (this.getGermString() != null && !this.getGermString().isEmpty()) {
            try {
                this.germ = NoteList.parseNoteListString(this.getGermString(), scale);
            } catch (NoteStringParseException ex) {                
                // All scales should be able to handle a valid note list string.
                // if we have a germString, it was valid with the existing scale,
                // so it should also be valid with this scale.  We should only
                // get a NoteStringParseException in the case of a programming
                // error.
                throw new UndeclaredThrowableException(ex, "An error occured while parsing the note list string '" + this.getGermString() + "' using the scale " + scale.toString() + ".  This indicates a programming error.");        
            }
        }     
        
        this.scale = scale;
    }

    /**
     * Gets whether or not a layered intro should be included in the generated
     * fractal piece.
     * 
     * @return whether or not to generate the layered intro
     */
    public boolean getGenerateLayeredIntro() {
        return generateLayeredIntro;
    }

    /**
     * Sets whether or not a layered intro should be included in the generated
     * fractal piece.
     * 
     * @param generateLayeredIntro whether or not to generate the layered intro
     */
    public void setGenerateLayeredIntro(boolean generateLayeredIntro) {
        this.generateLayeredIntro = generateLayeredIntro;
    }

    /**
     * Gets whether or not a layered outro should be included in the generated
     * fractal piece.
     * 
     * @return whether or not to generate the layered outro
     */
    public boolean getGenerateLayeredOutro() {
        return generateLayeredOutro;
    }

    /**
     * Sets whether or not a layered outro should be included in the generated
     * fractal piece.
     * 
     * @param generateLayeredOutro whether or not to generate the layered outro
     */
    public void setGenerateLayeredOutro(boolean generateLayeredOutro) {
        this.generateLayeredOutro = generateLayeredOutro;
    }
        
    /**
     * Gets the time signature for this piece.  If none has been set, a default
     * signature of 4/4 will be created.
     * 
     * @return the time signature
     */
    public TimeSignature getTimeSignature() {  
        assert timeSignature != null : timeSignature; // timeSignature should never be null...
        return timeSignature;
    }

    /**
     * Sets the time signature for this piece.
     * 
     * @param timeSignature the time signature
     */
    public void setTimeSignature(TimeSignature timeSignature) {
        if (timeSignature == null) throw new IllegalArgumentException("TimeSignature cannot be set to null.");
        this.timeSignature = timeSignature;
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
        assert voices != null : voices; // voices should never be null!
        return voices;
    }
    
    /**
     * Gets an unmodifiable list of voices, in order from the fastest to the
     * slowest.
     * 
     * @return an unmodifiable list
     */
    private List<Voice> getVoices_FastToSlow() {
        List<Voice> sortableVoiceList = new ArrayList<Voice>(this.getVoices());  
        
        // sort the list using a fast-to-slow comparator...
        Collections.sort(sortableVoiceList, new Comparator<Voice>() {
                public int compare(Voice v1, Voice v2) {        
                    return v2.getSpeedScaleFactor().compareTo(v1.getSpeedScaleFactor());
                }
             }
        );
        
        return Collections.unmodifiableList(sortableVoiceList);
    }
        
    /**
     * Gets a list of Sections for the FractalPiece.  To add a Section, use the
     * provided createSection() method, rather than attempting to add it to the
     * list on your own.
     * 
     * @return the list of Sections
     */
    public List<Section> getSections() {  
        assert sections != null : sections; // sections should never be null!
        return sections;
    }
    
    /**
     * Creates a Voice for the FractalPiece, and adds it to the Voice list.
     * 
     * @return the created Voice
     */
    public Voice createVoice() {
        return this.createVoice(this.getVoices().size());
    }
    
    /**
     * Creates a Voice for the FractalPiece, and adds it to a particular point 
     * in the voice list.
     * 
     * @param index the point to insert the voice
     * @return the created voice
     */
    public Voice createVoice(int index) {
        Voice v = new Voice(this);
        this.voices.add(index, v);
        return v;
    }
    
    /**
     * Creates a Section for the FractalPiece, and adds it to the Section list.
     * 
     * @return the created Section
     */
    public Section createSection() {
        return this.createSection(this.getSections().size());
    }   
    
    /**
     * Creates a Section for the FractalPiece, and inserts it at a particular
     * point in the Section list.
     * 
     * @param index the point in the list to insert the section
     * @return the created Section
     */
    public Section createSection(int index) {
        Section s = new Section(this);
        this.sections.add(index, s);        
        return s;
    }
    
    /**
     * Sets up the default settings.  If there are already voices, they will be 
     * left alone and no new voices will be created.  Any section settings will
     * be overriden with new ones.
     */
    public void createDefaultSettings() {
        createDefaultVoices();
        createDefaultSections();
    }
    
    /**
     * Creates the default voice settings.  If there are already voices, this
     * method will not do anything.
     */
    public void createDefaultVoices() {
        // if there are already voices, then leave them alone...
        if (this.getVoices().size() > 0) return;
        
        int defaultVoiceCount = 3;
        Fraction voiceSpeedFactor = new Fraction(2, 1);
        int voiceOctaveAdjustment = 1;
        
        while(this.getVoices().size() < defaultVoiceCount) {
            Voice v = this.createVoice();
            v.setSpeedScaleFactor(voiceSpeedFactor);
            v.setOctaveAdjustment(voiceOctaveAdjustment);
         
            voiceSpeedFactor = voiceSpeedFactor.dividedBy(2L);
            voiceOctaveAdjustment -= 1;
        }
    }
    
    /**
     * Sets up default section settings.  Any existing sections will be 
     * overriden.  This generates one normal section, one inversion section,
     * one retrograde inversion section, and one retrograde section.  For each
     * section, the self-similarity is applied to only the fastest voice.
     */
    public void createDefaultSections() {
        this.sections.clear();
        
        createDefaultSection(false, false); // normal
        createDefaultSection(true, false);  // inversion
        createDefaultSection(true, true);   // retrograde inversion     
        createDefaultSection(false, true);  // retrograde       
    }
    
    /**
     * Creates a default section. 
     *
     * @param applyInversion whether or not to apply inversion to this section
     * @param applyRetrograde whether or not to apply retrograde to this section
     */
    private void createDefaultSection(boolean applyInversion, boolean applyRetrograde) {       
        Section section = this.createSection();
        
        // apply inversion and retrograde based on the passed settings...
        section.setApplyInversionOnAllVoiceSections(applyInversion);
        section.setApplyRetrogradeOnAllVoiceSections(applyRetrograde);
        
        // we can't set the options on the voice sections if there are no voices...
        if (this.getVoices().size() == 0) return;
        
        // get the fastest voice...
        Voice fastestVoice = this.getVoices_FastToSlow().get(0);
        
        // apply self-similarity to the fastest voice...        
        for (Voice v : this.getVoices()) {
            boolean isFastestVoice = (v == fastestVoice);
            VoiceSection vs = this.getVoiceSections().get(new VoiceSectionHashMapKey(v, section));
            
            vs.getSelfSimilaritySettings().setApplyToPitch(isFastestVoice);
            vs.getSelfSimilaritySettings().setApplyToRhythm(isFastestVoice);
            vs.getSelfSimilaritySettings().setApplyToVolume(isFastestVoice);
        }
    }
    
    /**
     * Creates the layered intro sections.
     */
    protected void createIntroSections() {
        if (!this.getGenerateLayeredIntro()) return;
        
        createLayeredSections(
            new InsertIndexProvider() { 
                public int getInsertIndex(List l) { return 0; }                
            }
        );
    }
    
    /**
     * Creates the layered outro sections.
     */
    protected void createOutroSections() {
        if (!this.getGenerateLayeredOutro()) return;
        
        createLayeredSections(
            new InsertIndexProvider() {                 
                public int getInsertIndex(List l) { return l.size(); }                
            }
        );
    }
        
    /**
     * Creates the necessary layered intro or outro sections.
     * 
     * @param insertIndexProvider object that provides index into the start of
     *        the list (for the intro) or the end of the list (for the outro)
     */
    private void createLayeredSections(InsertIndexProvider insertIndexProvider) {
        List<Voice> fastToSlowVoices = this.getVoices_FastToSlow();
        
        for (int sectionIndex = 0; sectionIndex < fastToSlowVoices.size(); sectionIndex++) {
            // create the section at the appropriate index...
            Section s = this.createSection(insertIndexProvider.getInsertIndex(this.getSections()));
            
            // add our section to our temp list, since the layered sections are
            // only created during fractal piece generation and should never be
            // available for editing
            this.tempIntroOutroSections.add(s);    
            
            // set defaults...
            s.setApplyInversionOnAllVoiceSections(false);
            s.setApplyRetrogradeOnAllVoiceSections(false);
            s.setSelfSimilaritySettingsOnAllVoiceSections(false, false, false);
            s.setRestOnAllVoiceSections(false);
            
            // set some of the voice sections to rests, to create our layered effect...
            for (int voiceIndex = 0; voiceIndex < sectionIndex; voiceIndex++) {  
                VoiceSection vs = this.getVoiceSections().get(new VoiceSectionHashMapKey(fastToSlowVoices.get(voiceIndex), s));
                vs.setRest(true);
            }                        
        }
    }            
    
    /**
     * Clears out any temporary intro or outro sections.  These are created 
     * during fractal piece generation and should not be available the rest of 
     * the time.
     */
    protected void clearTempIntroOutroSections() {
        // clear out any sections that were temporarily created...
        for (Section s : this.tempIntroOutroSections) {
            this.getSections().remove(s);
        }
        this.tempIntroOutroSections.clear();
    }        
                
    /**
     * Generates a midi sequence for the entire fractal piece, based on the 
     * settings on the voices and sections.
     * 
     * @return the generated fractal piece
     * @throws javax.sound.midi.InvalidMidiDataException
     */
    public Sequence generatePiece() throws InvalidMidiDataException {
        try {
            // first, create our intro and outro...
            this.createIntroSections();
            this.createOutroSections();
            
            // next, get all our voice results, and cache them in a list...
            ArrayList<NoteList> voiceResults = new ArrayList<NoteList>();
            for (Voice v : this.getVoices()) voiceResults.add(v.getEntireVoice());
            
            // next, create our sequence...
            Sequence sequence = new Sequence(Sequence.PPQ, NoteList.getMidiTickResolution(voiceResults));

            // next, use the first track to set key signature and time signature...
            Track track1 = sequence.createTrack();
            track1.add(this.getScale().getKeySignature().getKeySignatureMidiEvent());        
            track1.add(this.getTimeSignature().getMidiTimeSignatureEvent());

            // finally, create and fill our midi tracks...
            for (NoteList nl : voiceResults) {            
                nl.createAndFillMidiTrack(sequence, scale, new Fraction(0, 1));                
            }        

            return sequence;
        } finally {
            this.clearTempIntroOutroSections();
        }        
    }
    
    /**
     * Creates and saves a midi file based on the existing fractal piece 
     * settings.
     * 
     * @param fileName the file name for the midi file
     * @throws javax.sound.midi.InvalidMidiDataException if there is some 
     *         invalid midi data
     * @throws java.io.IOException if the file cannot be written
     */
    public void createAndSaveMidiFile(String fileName) throws InvalidMidiDataException, IOException {
        File outputFile = new File(fileName);        
        Sequence sequence = this.generatePiece();   
        
        // Midi file type 1 is for multi-track sequences
        MidiSystem.write(sequence, 1, outputFile);
    }
    
    /**
     * Saves the germ to a midi file so that the user can listen to it.
     * 
     * @param fileName the filename for the midi file
     * @throws javax.sound.midi.InvalidMidiDataException if there is some 
     *         invalid midi data
     * @throws java.io.IOException if there is an IO problem with the file
     */
    public void saveGermToMidiFile(String fileName) throws InvalidMidiDataException, IOException {        
        // get our midi tick resolution...
        int midiTickResolution = NoteList.getMidiTickResolution(Arrays.asList(this.getGerm()));        

        // next, create our sequence...
        Sequence sequence = new Sequence(Sequence.PPQ, midiTickResolution);   
        
        // next, use the first track to set key signature and time signature...
        Track track1 = sequence.createTrack();
        track1.add(this.getScale().getKeySignature().getKeySignatureMidiEvent());        
        track1.add(this.getTimeSignature().getMidiTimeSignatureEvent());
        
        this.getGerm().createAndFillMidiTrack(sequence, scale, new Fraction(0, 1));                        

        File outputFile = new File(fileName);
         
        // Midi file type 1 is for multi-track sequences
        MidiSystem.write(sequence, 1, outputFile);
    }
    
    /**
     * Creates and loads a fractal piece from a serialized xml string.
     * 
     * @param xml string containing an xml representation of the fractal piece
     * @return the new fractal piece
     * @throws java.lang.Exception if there is a deserialization error
     */
    public static FractalPiece loadFromXml(String xml) throws Exception {
        Serializer serializer = new Persister(new CycleStrategy());
        return serializer.read(FractalPiece.class, xml);        
    }
    
    /**
     * Serializes the fractal piece to xml.
     * 
     * @return the xml representation of the fractal piece
     */
    public String getXmlRepresentation() {
        Serializer serializer = new Persister(new CycleStrategy());
        StringWriter xml = new StringWriter();
        
        try {
          serializer.write(this, xml);    
        } catch (Exception ex) {
            // Our serialization annotations and accompanying code should prevent
            // this from ever occuring; if it does it is a programming error.
            // We do this so that we don't have to declare it as a checked exception.
            throw new UndeclaredThrowableException(ex, "An error occurred while serializing the fractal piece to xml.");
        }        
        
        return xml.toString();
    }
}
