package fr.polytech.reace.researchers.driller;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Main {

    private static final String REPOSITORIES = "repositories";
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

        File repositories = new File(parsedArguments.getOptionValue(REPOSITORIES));
        String output = parsedArguments.getOptionValue(OUTPUT);

        if (!repositories.exists()) {
            LOGGER.error("The repositories folder cannot be found.");
            System.exit(1);
        } else if (!repositories.isDirectory()) {
            LOGGER.error("The repositories folder is not a directory.");
            System.exit(1);
        } else {
            GlobalStudyExecutor executor = new GlobalStudyExecutor(repositories, output);

            executor.execute();
        }
    }

    private static CommandLine parseArguments(String[] args) throws ParseException {
        CommandLineParser argsParser = new DefaultParser();

        Options parserOptions = new Options();

        Option repositoryOption = Option.builder("r")
                .longOpt(REPOSITORIES)
                .argName("Repositories folder path")
                .desc("The path to the local repositories folder")
                .hasArg()
                .required()
                .build();

        Option outputOption = Option.builder("o")
                .longOpt(OUTPUT)
                .argName("Output file folder path")
                .desc("The path to the output folder for results")
                .hasArg()
                .required()
                .build();

        parserOptions.addOption(repositoryOption);
        parserOptions.addOption(outputOption);

        return argsParser.parse(parserOptions, args);
    }
}
