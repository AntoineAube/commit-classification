package fr.polytech.rimel.comclass.classifiers;

import fr.polytech.rimel.comclass.classifiers.helpers.ClassificationHelper;
import fr.polytech.rimel.comclass.classifiers.helpers.LinesOfMatchingFilename;
import fr.polytech.rimel.comclass.classifiers.helpers.LinesOfMatchingFilenameExtension;
import fr.polytech.rimel.comclass.classifiers.helpers.MatchingLinesInFile;
import org.repodriller.domain.Commit;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Computes the coefficient to help us determine if the commit is a 'DOCUMENTATION' commit.
 *
 * What is taken into account:
 * - The modified lines in documentation files (HTML page, Markdown document).
 * - The modified commentary lines (beginning with '#' when trimmed) in code files.
 * - The modified lines in tests files.
 *
 * We sum all those lines and divide by the total number of modified lines.
 */
public class DocumentationClassifier extends CommitClassifier {

    private static final List<ClassificationHelper> HELPERS = Arrays.asList(
            // Documentation files.
            new LinesOfMatchingFilenameExtension("html"),
            new LinesOfMatchingFilenameExtension("md"),

            // Test files.
            new LinesOfMatchingFilename(Pattern.compile(".*test.*")),

            // Modified commentary lines.
            fileModification -> {
                if (fileModification.fileNameEndsWith(".py")) {
                    return new MatchingLinesInFile(DocumentationClassifier::isCommentary)
                            .countMatchingLines(fileModification);
                }

                return 0;
            }
    );

    private static boolean isCommentary(String line) {
        return line.trim().startsWith("#");
    }

    public DocumentationClassifier(Commit analyzedCommit) {
        super(analyzedCommit);
    }

    @Override
    List<ClassificationHelper> listClassificationHelpers() {
        return HELPERS;
    }

    @Override
    public CommitCategory getCategory() {
        return CommitCategory.DOCUMENTATION;
    }

    @Override
    public CommitClassifier instantiateClassifier(Commit commit) {
        return new DocumentationClassifier(commit);
    }
}
