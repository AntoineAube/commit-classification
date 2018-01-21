package fr.polytech.rimel.comclass.categories.classifiers;

import fr.polytech.rimel.comclass.categories.CommitCategory;
import fr.polytech.rimel.comclass.categories.helpers.ClassificationHelper;
import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;

import java.util.Collections;
import java.util.List;

public abstract class CommitClassifier {

    private final Commit analyzedCommit;

    public CommitClassifier(Commit analyzedCommit) {
        this.analyzedCommit = analyzedCommit;
    }

    public long computeCategoryMembership() {
        long relatedModificationsCount = 0;

        for (Modification fileModification : analyzedCommit.getModifications()) {
            relatedModificationsCount += computeMatchingLines(fileModification);
        }

        return relatedModificationsCount;
    }

    private long computeMatchingLines(Modification fileModification) {
        return listClassificationHelpers().stream()
                .map(helper -> helper.countMatchingLines(fileModification))
                .reduce(Long::sum)
                .orElse((long) 0);
    }

    private long computeModifiedLines(Modification fileModification) {
        return fileModification.getAdded() + (long) fileModification.getRemoved();
    }

    protected List<ClassificationHelper> listClassificationHelpers() {
        return Collections.emptyList();
    }

    public abstract CommitCategory getCategory();
    public abstract CommitClassifier instantiateClassifier(Commit commit);
}
