package fr.polytech.rimel.comclass.processes;

import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import java.util.ArrayList;
import java.util.List;


public class CommitExplorationProcess implements CommitVisitor {

    @Override
    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism writer) {
        List<Object> keptFields = new ArrayList<>();

        keptFields.add(commit.getHash());
        keptFields.add(commit.getAuthor().getName());
        keptFields.add(commit.getAuthor().getEmail());
        keptFields.add(commit.getModifications().size());
        keptFields.add(commit.getDate().getTimeInMillis());

        writer.write(keptFields.toArray());
    }

    @Override
    public String name() {
        return "Commit Exploration";
    }
}
