package fr.polytech.rimel.comclass.categories.helpers;

import java.util.regex.Pattern;

public class LinesOfMatchingFilenameExtension extends LinesOfMatchingFilename {

    public LinesOfMatchingFilenameExtension(String extension) {
        super(Pattern.compile(".*\\." + extension + "$"));
    }
}
