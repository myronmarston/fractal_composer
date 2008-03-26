import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.sound.midi.InvalidMidiDataException;

/**
 *
 * @author Myron
 */
public class FractalComposerManager {   
    private NoteList germ;
    private Scale scale;
    
    public FractalComposerManager(NoteList germ, Scale scale) {
        this.germ = germ;
        this.scale = scale;
    }
    
    public NoteList getGerm() {
        if (null == germ) germ = new NoteList();
        return germ;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }
    
    public Sequence createSequence() throws InvalidMidiDataException {
        Sequence sequence = new Sequence(Sequence.PPQ, MidiNote.TICKS_PER_QUARTER_NOTE);
        Track track = sequence.createTrack();
        this.getGerm().fillMidiTrack(track, scale, 0d);        
        return sequence;
    }
}
