/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package exceptions;


/**
 * Thrown if the map file is not formatted properly.
 */
public class MapFileFormatException extends AllThoseTerritoriesException {
    private final String file;
    private final String line;

    public MapFileFormatException(String file, String line) {
        this.file = file;
        this.line = line;
    }

    public String getFile() {
        return file;
    }

    public String getLine() {
        return line;
    }
}
