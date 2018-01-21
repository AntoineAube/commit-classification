package fr.polytech.rimel.comclass.categories.classifiers;

import fr.polytech.rimel.comclass.categories.CommitCategory;
import fr.polytech.rimel.comclass.categories.helpers.ClassificationHelper;
import fr.polytech.rimel.comclass.categories.helpers.LinesOfMatchingFilename;
import fr.polytech.rimel.comclass.categories.helpers.specific.LinesOfAddedFlatScript;
import fr.polytech.rimel.comclass.categories.helpers.specific.LinesOfNewFunctions;
import org.repodriller.domain.Commit;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Computes the coefficient to help us determine if the commit is a 'FEATURE ADD' commit.
 *
 * What is taken into account:
 * - The added functions lines.
 * - The added flat scripts.
 * - The modified tests lines.
 *
 * We sum all those lines and divide by the total number of modified lines.
 */
public class FeatureAddClassifier extends CommitClassifier {

    private static final List<ClassificationHelper> HELPERS = Arrays.asList(
            // Added functions.
            new LinesOfNewFunctions(),

            // Added flat script.
            new LinesOfAddedFlatScript(),

            // Modified test files.
            new LinesOfMatchingFilename(Pattern.compile(".*test.*"))
    );

    public FeatureAddClassifier(Commit analyzedCommit) {
        super(analyzedCommit);
    }

    @Override
    protected List<ClassificationHelper> listClassificationHelpers() {
        return HELPERS;
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
