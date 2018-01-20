package fr.polytech.rimel.comclass.studies;

import org.repodriller.Study;

public class DiffStudy implements Study {

    private final String repositoryPath;
    private final String outputPath;

    public DiffStudy(String repositoryPath, String outputPath) {
        this.repositoryPath = repositoryPath;
        this.outputPath = outputPath;
    }

    @Override
    public void execute() {

    }
}
