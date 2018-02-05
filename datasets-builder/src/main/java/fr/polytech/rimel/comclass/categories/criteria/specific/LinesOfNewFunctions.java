package fr.polytech.rimel.comclass.categories.criteria.specific;

import fr.polytech.rimel.comclass.categories.criteria.ClassificationCriterion;
import org.repodriller.domain.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LinesOfNewFunctions implements ClassificationCriterion {

    @Override
    public long countMatchingLines(Modification fileModification) {
        if (fileModification.fileNameEndsWith(".py") && !fileModification.fileNameMatches(".*test.*")) {
            DiffParser diff = new DiffParser(fileModification.getDiff());

            List<String> newFunctions = diff.getBlocks().stream()
                    .map(LinesOfNewFunctions::listsNewFunctionsNames)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            return countNewFunctionsLines(fileModification.getSourceCode(), newFunctions);
        }

        return 0;
    }

    private static long countNewFunctionsLines(String sourceCode, List<String> newFunctionsNames) {
        String[] lines = sourceCode.split("");
        long newFunctionsLines = 0;

        for (int i = 0; i < lines.length; i++) {
            String currentLine = lines[i];

            if (currentLine.trim().startsWith("def ") && newFunctionsNames.contains(functionName(currentLine))) {
                newFunctionsLines++;
                int indentation = indentationOf(currentLine);

                int j = i + 1;
                String functionLine = lines[j];
                int functionLineIndentation = indentationOf(functionLine);

                while (j < lines.length && !functionLine.startsWith("def ") && (functionLineIndentation == 0 || functionLineIndentation > indentation)) {
                    newFunctionsLines++;

                    functionLine = lines[j];
                    functionLineIndentation = indentationOf(functionLine);
                }
            }
        }

        return newFunctionsLines;
    }

    private static String functionName(String lineWithDeclaration) {
        return lineWithDeclaration.trim().substring("def ".length()).split("\\(")[0];
    }

    private static List<String> listsNewFunctionsNames(DiffBlock diffBlock) {
        List<String> newFunctions = new ArrayList<>();

        for (DiffLine newLine : diffBlock.getLinesInNewFile()) {
            if (isFunctionStart(newLine)) {
                newFunctions.add(functionName(newLine.getLine()));
            }
        }

        return newFunctions;
    }

    private static boolean isFunctionStart(DiffLine line) {
        return line.getLine().trim().startsWith("def") && line.getType() == DiffLineType.ADDED;
    }

    private static int indentationOf(String line) {
        int i = 0;
        for (; i < line.length(); i++) {
            if (line.charAt(i) != '\t' && line.charAt(i) != ' ') {
                break;
            }
        }

        return i;
    }
}
