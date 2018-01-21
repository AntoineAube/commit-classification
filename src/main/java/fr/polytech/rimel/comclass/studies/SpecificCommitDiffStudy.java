package fr.polytech.rimel.comclass.studies;

import fr.polytech.rimel.comclass.processes.CommitClassificationProcess;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.GitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpecificCommitDiffStudy implements Study {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecificCommitDiffStudy.class);

    private final String repositoryPath;
    private final String commitHash;

    public SpecificCommitDiffStudy(String repositoryPath, String commitHash) {
        this.repositoryPath = repositoryPath;
        this.commitHash = commitHash;
    }

    @Override
    public void execute() {
        new RepositoryMining()
                .in(GitRepository.singleProject(repositoryPath))
                .through(Commits.single(commitHash))
                .visitorsAreThreadSafe(true)
                .withThreads()
                .process(new CommitClassificationProcess(), new StandardOutputDisplay())
                .mine();
    }

    private static class StandardOutputDisplay implements PersistenceMechanism {

        @Override
        public void write(Object... objects) {
            LOGGER.info("");

            for (Object object : objects) {
                String logLine = "- " + object.toString();
                LOGGER.info(logLine);
            }

            LOGGER.info("");
        }

        @Override
        public void close() {
            // Do nothing. Needed by the interface.
        }
    }
}
