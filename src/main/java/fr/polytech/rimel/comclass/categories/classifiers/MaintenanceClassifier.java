package fr.polytech.rimel.comclass.categories.classifiers;

import fr.polytech.rimel.comclass.categories.CommitCategory;
import fr.polytech.rimel.comclass.categories.helpers.ClassificationHelper;
import fr.polytech.rimel.comclass.categories.helpers.DeletedFilesByMatchingFilename;
import org.repodriller.domain.Commit;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class MaintenanceClassifier extends CommitClassifier {

    private static final List<ClassificationHelper> HELPERS = Arrays.asList(
            new DeletedFilesByMatchingFilename(Pattern.compile(".*"), 20)
    );

    public MaintenanceClassifier(Commit analyzedCommit) {
        super(analyzedCommit);
    }

    @Override
    protected List<ClassificationHelper> listClassificationHelpers() {
        return HELPERS;
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
