package fr.polytech.rimel.comclass.classifiers;

import fr.polytech.rimel.comclass.classifiers.helpers.ClassificationHelper;
import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;

import java.util.Collections;
import java.util.List;

public class MaintenanceClassifier extends CommitClassifier {

    public MaintenanceClassifier(Commit analyzedCommit) {
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
        return CommitCategory.MAINTENANCE;
    }

    @Override
    public CommitClassifier instantiateClassifier(Commit commit) {
        return new MaintenanceClassifier(commit);
    }
}
