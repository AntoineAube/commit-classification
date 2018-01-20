package fr.polytech.rimel.comclass.processes;

import fr.polytech.rimel.comclass.classifiers.*;
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
        Map<CommitCategory, Float> categoriesCoefficients = computeCategoriesCoefficients(commit);

        List<Object> elements = new ArrayList<>();
        elements.add(commit.getHash());
        elements.add(classify(categoriesCoefficients));
        elements.addAll(categoriesCoefficients.values());

        writer.write(elements.toArray());
    }

    private Map<CommitCategory, Float> computeCategoriesCoefficients(Commit commit) {
        Map<CommitCategory, Float> categoriesCoefficients = new EnumMap<>(CommitCategory.class);

        for (CommitClassifier defaultClassifier : COMMIT_CLASSIFIERS) {
            CommitClassifier classifier = defaultClassifier.instantiateClassifier(commit);

            categoriesCoefficients.put(classifier.getCategory(), classifier.getCategoryMembership());
        }

        return categoriesCoefficients;
    }

    private CommitCategory classify(Map<CommitCategory, Float> categoriesCoefficients) {
        CommitCategory commitCategory = null;
        float coefficient = -1;

        for (Map.Entry<CommitCategory, Float> entry : categoriesCoefficients.entrySet()) {
            if (coefficient < entry.getValue()) {
                commitCategory = entry.getKey();
                coefficient = entry.getValue();
            }
        }

        return commitCategory;
    }

    @Override
    public String name() {
        return "Commit Classification";
    }
}
