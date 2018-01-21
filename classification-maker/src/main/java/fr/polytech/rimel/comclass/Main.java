package fr.polytech.rimel.comclass;

import fr.polytech.rimel.comclass.studies.DiffStudy;
import fr.polytech.rimel.comclass.studies.SpecificCommitDiffStudy;
import org.apache.commons.cli.*;
import org.repodriller.Study;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final String COMMIT = "commit";
    private static final String REPOSITORY = "repository";
    private static final String OUTPUT = "output";

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CommandLine parsedArguments = null;
        try {
            parsedArguments = parseArguments(args);
        } catch (MissingOptionException e) {
            LOGGER.error(e.getMessage());
            System.exit(1);
        } catch (ParseException e) {
            LOGGER.error("Error while parsing CLI arguments", e);
            System.exit(1);
        }

        String repository = parsedArguments.getOptionValue(REPOSITORY);
        String output = parsedArguments.getOptionValue(OUTPUT);

        Study diffStudy;

        if (parsedArguments.hasOption(COMMIT)) {
            diffStudy = new SpecificCommitDiffStudy(repository, parsedArguments.getOptionValue(COMMIT));
        } else {
            diffStudy = new DiffStudy(repository, output);
        }

        diffStudy.execute();
    }

    private static CommandLine parseArguments(String[] args) throws ParseException {
        CommandLineParser argsParser = new DefaultParser();

        Options parserOptions = new Options();

        Option repositoryOption = Option.builder("r")
                .longOpt(REPOSITORY)
                .argName("Repository path")
                .desc("The path to the local repository")
                .hasArg()
                .required()
                .build();

        Option outputOption = Option.builder("o")
                .longOpt(OUTPUT)
                .argName("Output file path")
                .desc("The path to the output file for results")
                .hasArg()
                .required()
                .build();

        parserOptions.addOption(repositoryOption);
        parserOptions.addOption(outputOption);
        parserOptions.addOption("c", COMMIT, true, "Hash of the specific commit to study");

        return argsParser.parse(parserOptions, args);
    }
}
