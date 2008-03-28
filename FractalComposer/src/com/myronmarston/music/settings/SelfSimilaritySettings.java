package com.myronmarston.music.settings;


// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.21C5595C-22F3-EFF4-7A67-DE30EA20F21E]
// </editor-fold> 
public class SelfSimilaritySettings {    
    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.DBB3332B-D0BF-24FA-5946-AEDC71344824]
    // </editor-fold> 
    private boolean applyToPitch;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.421468F4-DB09-6A44-1229-F8A4D0332911]
    // </editor-fold> 
    private boolean applyToRhythm;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.E715BB19-1C2A-E1BE-8C87-9CE205258352]
    // </editor-fold> 
    private boolean applyToVolume;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.99FD8917-A24B-D908-D960-9F458AED45D3]
    // </editor-fold> 
    public SelfSimilaritySettings () {
    }
    
    public SelfSimilaritySettings(boolean applyToPitch, boolean applyToRhythm, boolean applyToVolume) {
        this.applyToPitch = applyToPitch;
        this.applyToRhythm = applyToRhythm;
        this.applyToVolume = applyToVolume;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.F9A1DFA9-A631-31C4-1724-D537B8305210]
    // </editor-fold> 
    public boolean getApplyToPitch () {
        return applyToPitch;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.D37847B8-4A9B-4DBB-2DF7-86F245048A7B]
    // </editor-fold> 
    public void setApplyToPitch (boolean val) {
        this.applyToPitch = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.6CE21C01-3BD1-83AB-EF78-3F9EC6F3FED3]
    // </editor-fold> 
    public boolean getApplyToRhythm () {
        return applyToRhythm;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.7E74484B-176D-53A3-6E48-8BD648D43ADF]
    // </editor-fold> 
    public void setApplyToRhythm (boolean val) {
        this.applyToRhythm = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.31B5ABD2-1143-0516-5988-7B8433D0EA07]
    // </editor-fold> 
    public boolean getApplyToVolume () {
        return applyToVolume;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.C5D511EC-7E35-65F2-79C6-21F572CB1920]
    // </editor-fold> 
    public void setApplyToVolume (boolean val) {
        this.applyToVolume = val;
    }

}

