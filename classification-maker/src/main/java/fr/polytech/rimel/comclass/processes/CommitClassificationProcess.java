package fr.polytech.rimel.comclass.processes;

import fr.polytech.rimel.comclass.categories.*;
import fr.polytech.rimel.comclass.categories.classifiers.CommitClassifier;
import fr.polytech.rimel.comclass.categories.classifiers.DocumentationClassifier;
import fr.polytech.rimel.comclass.categories.classifiers.FeatureAddClassifier;
import fr.polytech.rimel.comclass.categories.classifiers.MaintenanceClassifier;
import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import java.util.*;

public class CommitClassificationProcess implements CommitVisitor {

    private static final List<CommitClassifier> COMMIT_CLASSIFIERS = Arrays.asList(
            new DocumentationClassifier(null),
            new FeatureAddClassifier(null),
            new MaintenanceClassifier(null)
    );

    @Override
    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism writer) {
        Map<CommitCategory, Long> categoriesCoefficients = computeCategoriesCoefficients(commit);

        List<Object> elements = new ArrayList<>();
        elements.add(commit.getHash());
        elements.add(classify(categoriesCoefficients));
        elements.addAll(categoriesCoefficients.values());

        writer.write(elements.toArray());
    }

    private Map<CommitCategory, Long> computeCategoriesCoefficients(Commit commit) {
        Map<CommitCategory, Long> categoriesCoefficients = new EnumMap<>(CommitCategory.class);

        for (CommitClassifier defaultClassifier : COMMIT_CLASSIFIERS) {
            CommitClassifier classifier = defaultClassifier.instantiateClassifier(commit);

            categoriesCoefficients.put(classifier.getCategory(), classifier.computeCategoryMembership());
        }

        return categoriesCoefficients;
    }

    private CommitCategory classify(Map<CommitCategory, Long> categoriesCoefficients) {
        CommitCategory commitCategory = null;
        float coefficient = -1;

        for (Map.Entry<CommitCategory, Long> entry : categoriesCoefficients.entrySet()) {
            if (coefficient < entry.getValue()) {
                commitCategory = entry.getKey();
                coefficient = entry.getValue();
            }
        }

        if (coefficient == 0) {
            return CommitCategory.UNKNOWN;
        }

        return commitCategory;
    }

    @Override
    public String name() {
        return "Commit Classification";
    }
}
