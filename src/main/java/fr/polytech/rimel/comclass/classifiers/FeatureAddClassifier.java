package fr.polytech.rimel.comclass.classifiers;

import org.repodriller.domain.Commit;

public class FeatureAddClassifier implements CommitClassifier {

    @Override
    public float getCategoryMembership(Commit commit) {
        return 0.2f;
    }

    @Override
    public CommitCategory getCategory() {
        return CommitCategory.FEATURE_ADD;
    }
}
