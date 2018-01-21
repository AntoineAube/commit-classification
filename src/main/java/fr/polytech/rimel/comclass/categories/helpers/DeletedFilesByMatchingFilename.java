package fr.polytech.rimel.comclass.categories.helpers;

import org.repodriller.domain.Modification;

import java.util.regex.Pattern;

public class DeletedFilesByMatchingFilename implements ClassificationHelper {

    private final Pattern filenamePattern;
    private final long defaultAddedLines;

    public DeletedFilesByMatchingFilename(Pattern filenamePattern, long defaultAddedLines) {
        this.filenamePattern = filenamePattern;
        this.defaultAddedLines = defaultAddedLines;
    }

    @Override
    public long countMatchingLines(Modification modification) {
        if (filenamePattern.matcher(modification.getFileName()).matches() && modification.wasDeleted()) {
            return defaultAddedLines;
        }

        return 0;
    }
}
