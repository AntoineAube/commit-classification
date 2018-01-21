package fr.polytech.rimel.comclass.processes;

import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;


public class CommitExplorationProcess implements CommitVisitor {

    @Override
    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism writer) {
        writer.write("Hash", commit.getHash());
        writer.write("Number of modifications", commit.getModifications().size());

        commit.getModifications().forEach(fileModification ->
                writer.write("File modification",
                        fileModification.getType(),
                        fileModification.wasDeleted()));
    }

    @Override
    public String name() {
        return "Commit Exploration";
    }
}
