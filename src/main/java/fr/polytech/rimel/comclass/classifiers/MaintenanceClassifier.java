package fr.polytech.rimel.comclass.classifiers;

import org.repodriller.domain.Commit;

public class MaintenanceClassifier implements CommitClassifier {

    @Override
    public float getCategoryMembership(Commit commit) {
        return 0.3f;
    }

    @Override
    public CommitCategory getCategory() {
        return CommitCategory.MAINTENANCE;
    }
}
