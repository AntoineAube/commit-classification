package fr.polytech.rimel.comclass.studies;

import fr.polytech.rimel.comclass.processes.CommitClassificationProcess;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

public class DiffStudy implements Study {

    private final String repositoryPath;
    private final String outputPath;

    public DiffStudy(String repositoryPath, String outputPath) {
        this.repositoryPath = repositoryPath;
        this.outputPath = outputPath;
    }

    @Override
    public void execute() {
        new RepositoryMining()
                .in(GitRepository.singleProject(repositoryPath))
                .through(Commits.all())
                .process(new CommitClassificationProcess(), new CSVFile(outputPath))
                .mine();
    }
}
