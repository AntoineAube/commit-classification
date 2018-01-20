package fr.polytech.rimel.comclass;

import fr.polytech.rimel.comclass.studies.DiffStudy;
import org.apache.commons.cli.*;
import org.repodriller.Study;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

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

        Study diffStudy = new DiffStudy(parsedArguments.getOptionValue("repository"), parsedArguments.getOptionValue("output"));

        diffStudy.execute();
    }

    private static CommandLine parseArguments(String[] args) throws ParseException {
        CommandLineParser argsParser = new DefaultParser();

        Options parserOptions = new Options();

        Option repositoryOption = Option.builder("r")
                .longOpt("repository")
                .argName("Repository path")
                .desc("The path to the local repository")
                .hasArg()
                .required()
                .build();

        Option outputOption = Option.builder("o")
                .longOpt("output")
                .argName("Output file path")
                .desc("The path to the output file for results")
                .hasArg()
                .required()
                .build();

        parserOptions.addOption(repositoryOption);
        parserOptions.addOption(outputOption);

        return argsParser.parse(parserOptions, args);
    }
}
