package fr.polytech.rimel.comclass.classifiers;

import org.repodriller.domain.Commit;

public abstract class CommitClassifier {

    private final Commit analyzedCommit;

    CommitClassifier(Commit analyzedCommit) {
        this.analyzedCommit = analyzedCommit;
    }

    Commit getAnalyzedCommit() {
        return analyzedCommit;
    }

    public abstract float getCategoryMembership();
    public abstract CommitCategory getCategory();
    public abstract CommitClassifier instantiateClassifier(Commit commit);
}
