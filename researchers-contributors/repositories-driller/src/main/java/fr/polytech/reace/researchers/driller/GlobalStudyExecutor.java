package fr.polytech.reace.researchers.driller;

import fr.polytech.reace.researchers.driller.studies.ResearchersStudy;
import org.repodriller.Study;

import java.io.File;

public class GlobalStudyExecutor {

    private final File repositoriesFolder;
    private final String outputFolder;

    GlobalStudyExecutor(File repositoriesFolder, String outputFolder) {
        this.repositoriesFolder = repositoriesFolder;
        this.outputFolder = outputFolder;
    }

    void execute() {
        String[] projects = repositoriesFolder.list();

        assert projects != null;

        for (String repository : projects) {
            Study researcherStudy = new ResearchersStudy(repository, repositoriesFolder.getPath() + "/" + repository, outputFolder);

            researcherStudy.execute();
        }
    }
}
