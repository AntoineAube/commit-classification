package fr.polytech.rimel.comclass.classifiers;

import org.repodriller.domain.Commit;

public class DocumentationClassifier implements CommitClassifier {

    @Override
    public float getCategoryMembership(Commit commit) {
        return 1;
    }

    @Override
    public CommitCategory getCategory() {
        return CommitCategory.DOCUMENTATION;
    }
}
