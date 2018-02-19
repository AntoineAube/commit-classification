package fr.polytech.reace.researchers.driller.studies;

import fr.polytech.reace.researchers.driller.processes.CommitExplorationProcess;
import fr.polytech.reace.researchers.driller.processes.CommitMessageProcess;
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
        createDirectories();

        new RepositoryMining()
                .in(GitRepository.singleProject(repositoryPath))
                .through(Commits.all())
                .process(new CommitExplorationProcess(), outputFile())
                .process(new CommitMessageProcess(), commitsMessagesFile())
                .mine();
    }

    private JSONFile commitsMessagesFile() {
        String[] labels = {"COMMIT_HASH", "COMMIT_MESSAGE"};

        return new JSONFile(outputDirectoryPath + "/commits-messages/" + projectName + ".json", labels);
    }

    private CSVFile outputFile() {
        String[] labels = {"COMMIT_HASH", "AUTHOR_NAME", "AUTHOR_EMAIL",
                "MODIFICATIONS_COUNT", "TIMESTAMP", "ADDED_LINES",
                "DELETED_LINES", "DELETED_FILES"};

        return new CSVFile(outputDirectoryPath + "/commits-information/" + projectName + ".csv", labels);
    }

    private void createDirectories() {
        makeDirectory(new File(outputDirectoryPath));
        makeDirectory(new File(outputDirectoryPath + "/commits-information/"));
        makeDirectory(new File(outputDirectoryPath + "/commits-messages/"));
    }

    private static void makeDirectory(File directory) {
        if (directory.exists()) {
            if (directory.isFile()) {
                throw new RuntimeException(directory + " is not a directory.");
            }
        } else if (!directory.mkdirs()) {
            throw new RuntimeException("Could not create the directory '" + directory + "'.");
        }
    }
}
