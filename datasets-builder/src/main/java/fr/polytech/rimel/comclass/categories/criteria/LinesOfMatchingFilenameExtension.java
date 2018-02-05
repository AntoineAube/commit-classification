package fr.polytech.rimel.comclass.categories.criteria;

import java.util.regex.Pattern;

public class LinesOfMatchingFilenameExtension extends LinesOfMatchingFilename {

    public LinesOfMatchingFilenameExtension(String extension) {
        super(Pattern.compile(".*\\." + extension + "$"));
    }
}
