package com.learning.uwuno.util;

public final class utils {
    // Should not be instantiated
    private utils() {
        // Should not run
    }

    // Returns a string containing all characters before the last symbol given
    // or an empty string if the symbol is not found
    static public String substrBeforeLastSymbol(String str, String symbol) {
        int indexHash = str.lastIndexOf(symbol);
        return (indexHash == -1 ? "" : str.substring(0, indexHash + 1));
    }

    // Returns a string containing all characters after the last symbol given
    // or an empty string if the symbol is not found
    static public String substrAfterLastSymbol(String str, String symbol) {
        int indexHash = str.lastIndexOf(symbol);
        return (indexHash == -1 ? "" : str.substring(indexHash + 1));
    }

}
