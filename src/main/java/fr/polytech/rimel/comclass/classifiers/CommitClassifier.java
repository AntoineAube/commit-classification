package fr.polytech.rimel.comclass.classifiers;

import fr.polytech.rimel.comclass.classifiers.helpers.ClassificationHelper;
import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;

import java.util.Collections;
import java.util.List;

public abstract class CommitClassifier {

    private final Commit analyzedCommit;

    CommitClassifier(Commit analyzedCommit) {
        this.analyzedCommit = analyzedCommit;
    }

    public float computeCategoryMembership() {
        long modificationsCount = 0;
        long relatedModificationsCount = 0;

        for (Modification fileModification : analyzedCommit.getModifications()) {
            modificationsCount += computeModifiedLines(fileModification);
            relatedModificationsCount += computeMatchingLines(fileModification);
        }

        if (modificationsCount == 0) {
            return 0;
        }

        return relatedModificationsCount / (float) modificationsCount;
    }

    private long computeMatchingLines(Modification fileModification) {
        long count = 0;

        count += computeExtraMatchingLines(fileModification);

        count += listClassificationHelpers().stream()
                .map(helper -> helper.countMatchingLines(fileModification))
                .reduce(Long::sum)
                .orElse((long) 0);

        return count;
    }

    private long computeModifiedLines(Modification fileModification) {
        return fileModification.getAdded() + (long) fileModification.getRemoved();
    }

    long computeExtraMatchingLines(Modification fileModification) {
        return 0;
    }

    List<ClassificationHelper> listClassificationHelpers() {
        return Collections.emptyList();
    }

    public abstract CommitCategory getCategory();
    public abstract CommitClassifier instantiateClassifier(Commit commit);
}
