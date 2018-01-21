package fr.polytech.rimel.comclass.studies;

import fr.polytech.rimel.comclass.categories.CommitCategory;
import fr.polytech.rimel.comclass.processes.CommitClassificationProcess;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

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
        new RepositoryMining()
                .in(GitRepository.singleProject(repositoryPath))
                .through(Commits.all())
                .visitorsAreThreadSafe(true)
                .withThreads()
                .process(new CommitClassificationProcess(), prepareOutput(outputPath))
                .mine();
    }

    private static CSVFile prepareOutput(String location) {
        CSVFile output = new CSVFile(location);

        List<String> header = new ArrayList<>();
        header.add("COMMIT_HASH");
        header.add("PREDICTED");
        header.addAll(Stream.of(CommitCategory.values()).map(Enum::toString).collect(Collectors.toList()));

        output.write(header.toArray());

        return output;
    }
}
