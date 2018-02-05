package fr.polytech.rimel.comclass.categories.criteria;

import org.repodriller.domain.Modification;

import java.util.regex.Pattern;

public class DeletedFilesByMatchingFilename implements ClassificationCriterion {

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
