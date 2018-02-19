package fr.polytech.reace.researchers.driller.processes;

import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

public class CommitMessageProcess implements CommitVisitor {

    @Override
    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism writer) {
        writer.write(commit.getHash(), commit.getMsg());
    }

    @Override
    public String name() {
        return "Commits Messages Extraction";
    }
}
