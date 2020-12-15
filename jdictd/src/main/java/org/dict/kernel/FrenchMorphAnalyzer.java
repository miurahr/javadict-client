package org.dict.kernel;

public class FrenchMorphAnalyzer implements org.dict.kernel.IMorphAnalyzer {
    /**
     * FrenchMorphAnalyzer constructor comment.
     */
    public FrenchMorphAnalyzer() {
        super();
    }

    /**
     * getPossibleBases method comment.
     */
    public java.lang.String[] getPossibleBases(String word) {
        if (word.endsWith("es")) {
            return new String[]{word.substring(0, word.length() - 1), word.substring(0, word.length() - 2)};
        }
        if (word.endsWith("s") || word.endsWith("e")) {
            return new String[]{word.substring(0, word.length() - 1)};
        }
        return new String[0];
    }
}
