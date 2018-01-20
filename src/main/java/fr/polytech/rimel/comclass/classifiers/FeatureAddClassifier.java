package fr.polytech.rimel.comclass.classifiers;

import fr.polytech.rimel.comclass.classifiers.helpers.ClassificationHelper;
import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;

import java.util.Collections;
import java.util.List;

/**
 * Computes the coefficient to help us determine if the commit is a 'FEATURE ADD' commit.
 *
 * What is taken into account:
 * - The added functions.
 *
 * We sum all those lines and divide by the total number of modified lines.
 */
public class FeatureAddClassifier extends CommitClassifier {

    public FeatureAddClassifier(Commit analyzedCommit) {
        super(analyzedCommit);
    }

    @Override
    long computeExtraMatchingLines(Modification fileModification) {
        return 0;
    }

    @Override
    List<ClassificationHelper> listClassificationHelpers() {
        return Collections.emptyList();
    }

    @Override
    public CommitCategory getCategory() {
        return CommitCategory.FEATURE_ADD;
    }

    @Override
    public CommitClassifier instantiateClassifier(Commit commit) {
        return new FeatureAddClassifier(commit);
    }
}
