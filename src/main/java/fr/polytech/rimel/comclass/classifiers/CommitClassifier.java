package fr.polytech.rimel.comclass.classifiers;

import org.repodriller.domain.Commit;

public interface CommitClassifier {

    float getCategoryMembership(Commit commit);
    CommitCategory getCategory();
}
