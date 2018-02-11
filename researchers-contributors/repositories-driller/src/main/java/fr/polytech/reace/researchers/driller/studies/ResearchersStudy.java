package fr.polytech.reace.researchers.driller.studies;

import fr.polytech.reace.researchers.driller.processes.CommitExplorationProcess;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

import java.io.File;

public class ResearchersStudy implements Study {

    private final String projectName;
    private final String repositoryPath;
    private final String outputDirectoryPath;

    public ResearchersStudy(String projectName, String repositoryPath, String outputDirectoryPath) {
        this.projectName = projectName;
        this.repositoryPath = repositoryPath;
        this.outputDirectoryPath = outputDirectoryPath;
    }

    @Override
    public void execute() {
        File outputDirectory = new File(outputDirectoryPath);

        if (outputDirectory.exists()) {
            if (outputDirectory.isFile()) {
                throw new RuntimeException(outputDirectoryPath + " is not a directory.");
            }
        } else if (!outputDirectory.mkdirs()) {
            throw new RuntimeException("Could not create the directory '" + outputDirectoryPath + "'.");
        }

        new RepositoryMining()
                .in(GitRepository.singleProject(repositoryPath))
                .through(Commits.all())
                .visitorsAreThreadSafe(true)
                .withThreads()
                .process(new CommitExplorationProcess(), outputFile())
                .mine();
    }

    private CSVFile outputFile() {
        CSVFile output = new CSVFile(outputDirectoryPath + "/" + projectName + ".csv");

        output.write("COMMIT_HASH", "AUTHOR_NAME", "AUTHOR_EMAIL",
                "MODIFICATIONS_COUNT", "TIMESTAMP", "ADDED_LINES",
                "DELETED_LINES", "DELETED_FILES");

        return output;
    }
}
