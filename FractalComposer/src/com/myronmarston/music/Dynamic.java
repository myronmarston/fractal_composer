package com.myronmarston.music;

/**
 * Pre-defined volume levels based on musical dynamics.
 * 
 * @author Myron
 */
public enum Dynamic {
    PPP(10),
    PP(25),
    P(50),
    MP(60),
    MF(70),
    F(85),
    FF(100),
    FFF(120);
    
    private final int midiVolume;
    private static String regexPattern;

    private Dynamic(int midiVolume) {
        this.midiVolume = midiVolume;
    }
    
    /**
     * Gets the midi volume for this dynamic.
     * 
     * @return midi volume
     */
    public int getMidiVolume() {
        return this.midiVolume;
    }
    
    /**
     * Gets a list of possible dynamic values.
     * 
     * @return a string containing a list of possible dynamic values
     */
    public static String getDynamicExampleString() {
        StringBuilder str = new StringBuilder();
        Dynamic[] values = Dynamic.values(); // cache the array...
        
        for (int i = 0; i < values.length; i++) {
            str.append(values[i].toString());            
            
            if (i == values.length - 2) str.append(" or ");                
            else if (i < values.length - 2) str.append(", ");            
        }
        
        return str.toString();        
    }
    
    /**
     * Gets a regular expression pattern string based on this dynamic enum that 
     * can be used to parse a note string.
     * 
     * @return a regular expression pattern string
     */
    public static String getRegexPattern() {
        if (regexPattern == null) regexPattern = "(?:,(.*))?";        
        return regexPattern;
    }
}
