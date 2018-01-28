package fr.polytech.rimel.comclass.studies;

import fr.polytech.rimel.comclass.categories.CommitCategory;
import fr.polytech.rimel.comclass.processes.CommitClassificationProcess;
import fr.polytech.rimel.comclass.processes.CommitExplorationProcess;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiffStudy implements Study {

    private final String repositoryPath;
    private final String outputPath;

    public DiffStudy(String repositoryPath, String outputPath) {
        this.repositoryPath = repositoryPath;
        this.outputPath = outputPath;
    }

    @Override
    public void execute() {
        File outputDirectory = new File(outputPath);
        if (outputDirectory.exists()) {
            if (outputDirectory.isFile()) {
                throw new RuntimeException("The specified output directory is a file, in fact.");
            }
        } else {
            if (!outputDirectory.mkdirs()) {
                throw new RuntimeException("Could not create the output directory.");
            }
        }

        new RepositoryMining()
                .in(GitRepository.singleProject(repositoryPath))
                .through(Commits.all())
                .visitorsAreThreadSafe(true)
                .withThreads()
                .process(new CommitClassificationProcess(), prepareClassificationOutput(outputPath + "/classification.csv"))
                .process(new CommitExplorationProcess(), prepareExplorationOutput(outputPath + "/exploration.csv"))
                .mine();
    }

    private static CSVFile prepareClassificationOutput(String location) {
        CSVFile output = new CSVFile(location);

        List<String> header = new ArrayList<>();
        header.add("COMMIT_HASH");
        header.add("PREDICTED");

        header.addAll(Stream.of(CommitCategory.values()).filter(category -> category != CommitCategory.UNKNOWN).map(Enum::toString).collect(Collectors.toList()));

        output.write(header.toArray());

        return output;
    }

    private static CSVFile prepareExplorationOutput(String location) {
        CSVFile output = new CSVFile(location);

        List<String> header = new ArrayList<>();
        header.add("COMMIT_HASH");
        header.add("AUTHOR_NAME");
        header.add("AUTHOR_EMAIL");
        header.add("MODIFICATIONS_COUNT");
        header.add("TIMESTAMP");
        header.add("ADDED_LINES");
        header.add("DELETED_LINES");
        header.add("DELETED_FILES");

        output.write(header.toArray());

        return output;
    }
}
