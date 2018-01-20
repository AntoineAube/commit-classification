package fr.polytech.rimel.comclass.processes;

import fr.polytech.rimel.comclass.classifiers.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import java.util.Arrays;
import java.util.List;

public class CommitClassificationProcess implements CommitVisitor {

    private static final List<CommitClassifier> COMMIT_CLASSIFIERS = Arrays.asList(
            new DocumentationClassifier(),
            new FeatureAddClassifier(),
            new MaintenanceClassifier()
    );

    @Override
    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism writer) {
        CommitCategory foundCategory = classify(commit);

        writer.write(
                commit.getHash(),
                foundCategory == null ? "" : foundCategory.toString()
        );
    }

    private CommitCategory classify(Commit commit) {
        return COMMIT_CLASSIFIERS.stream()
                .map(classifier -> new ImmutablePair<>(classifier.getCategoryMembership(commit), classifier.getCategory()))
                .max((result, another) -> Float.compare(result.getLeft(), another.getLeft()))
                .map(ImmutablePair::getRight)
                .orElse(null);
    }

    @Override
    public String name() {
        return "Commit Classification";
    }
}
