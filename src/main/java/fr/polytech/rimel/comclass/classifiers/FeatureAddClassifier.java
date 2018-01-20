package fr.polytech.rimel.comclass.classifiers;

import org.repodriller.domain.Commit;

public class FeatureAddClassifier extends CommitClassifier {

    public FeatureAddClassifier(Commit analyzedCommit) {
        super(analyzedCommit);
    }

    @Override
    public float getCategoryMembership() {
        return 0.2f;
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
