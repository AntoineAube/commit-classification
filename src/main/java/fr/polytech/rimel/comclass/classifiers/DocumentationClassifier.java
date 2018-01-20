package fr.polytech.rimel.comclass.classifiers;

import org.repodriller.domain.*;

import java.util.stream.Stream;

/**
 * Computes the coefficient to help us determine if the commit is a 'DOCUMENTATION' commit.
 *
 * What is taken into account:
 * - The modified lines in documentation files (HTML page, Markdown document).
 * - The modified commentary lines (beginning with '#' when trimmed) in code files.
 *
 * We sum all those lines and divide by the total number of modified lines.
 */
public class DocumentationClassifier extends CommitClassifier {

    private static final String[] DOCUMENTATION_FILE_EXTENSIONS = {".html", ".md"};

    private static boolean isDocumentationFile(String fileName) {
        return Stream.of(DOCUMENTATION_FILE_EXTENSIONS)
                .map(fileName::endsWith)
                .reduce(Boolean::logicalOr)
                .orElse(false);
    }

    private static boolean isCommentary(String line) {
        return line.trim().startsWith("#");
    }

    public DocumentationClassifier(Commit analyzedCommit) {
        super(analyzedCommit);
    }

    @Override
    public float getCategoryMembership() {
        int modificationsCount = 0;
        int relatedModificationsCount = 0;

        for (Modification fileModification : getAnalyzedCommit().getModifications()) {
            modificationsCount += fileModification.getAdded();
            modificationsCount += fileModification.getRemoved();

            relatedModificationsCount += computeFileModificationMembership(fileModification);
        }

        if (modificationsCount == 0) {
            return 0;
        }

        return relatedModificationsCount / (float) modificationsCount;
    }

    private int computeFileModificationMembership(Modification fileModification) {
        if (fileModification.fileNameEndsWith(".py")) {
            return computeModifiedCommentaries(fileModification);
        } else if (isDocumentationFile(fileModification.getFileName())) {
            return fileModification.getAdded() + fileModification.getRemoved();
        }

        return 0;
    }

    private int computeModifiedCommentaries(Modification modification) {
        DiffParser diff = new DiffParser(modification.getDiff());

        int commentsCount = 0;

        for (DiffBlock block : diff.getBlocks()) {
            commentsCount += Stream.concat(block.getLinesInOldFile().stream(), block.getLinesInNewFile().stream())
                    .filter(line -> line.getType() == DiffLineType.ADDED || line.getType() == DiffLineType.REMOVED)
                    .filter(line -> isCommentary(line.getLine()))
                    .count();
        }

        return commentsCount;
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
