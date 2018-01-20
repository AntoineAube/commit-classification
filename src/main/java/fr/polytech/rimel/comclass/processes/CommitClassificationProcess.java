package fr.polytech.rimel.comclass.processes;

import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

public class CommitClassificationProcess implements CommitVisitor {

    @Override
    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism persistenceMechanism) {

    }

    @Override
    public String name() {
        return "Commit Classification";
    }
}
