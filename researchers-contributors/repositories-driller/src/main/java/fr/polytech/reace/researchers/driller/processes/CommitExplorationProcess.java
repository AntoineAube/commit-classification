package fr.polytech.reace.researchers.driller.processes;

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
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
        keptFields.add(commit.getModifications().stream().map(Modification::getAdded).reduce(Integer::sum).orElse(0));
        keptFields.add(commit.getModifications().stream().map(Modification::getRemoved).reduce(Integer::sum).orElse(0));
        keptFields.add(commit.getModifications().stream().map(m -> m.wasDeleted() ? 1 : 0).reduce(Integer::sum).orElse(0));

        writer.write(keptFields.toArray());
    }

    @Override
    public String name() {
        return "Commit Exploration";
    }
}
