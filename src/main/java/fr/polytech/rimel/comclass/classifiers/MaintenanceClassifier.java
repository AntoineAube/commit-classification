package fr.polytech.rimel.comclass.classifiers;

import org.repodriller.domain.Commit;

public class MaintenanceClassifier extends CommitClassifier {

    public MaintenanceClassifier(Commit analyzedCommit) {
        super(analyzedCommit);
    }

    @Override
    public float getCategoryMembership() {
        return 0;
    }

    @Override
    public CommitCategory getCategory() {
        return CommitCategory.MAINTENANCE;
    }

    @Override
    public CommitClassifier instantiateClassifier(Commit commit) {
        return new MaintenanceClassifier(commit);
    }
}
