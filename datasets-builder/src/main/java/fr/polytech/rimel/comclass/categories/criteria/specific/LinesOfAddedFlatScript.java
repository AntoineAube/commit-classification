package fr.polytech.rimel.comclass.categories.criteria.specific;

import fr.polytech.rimel.comclass.categories.criteria.ClassificationCriterion;
import org.repodriller.domain.*;

import java.util.Collection;

/**
 * TODO: Count although the block 'for' and 'if'.
 */
public class LinesOfAddedFlatScript implements ClassificationCriterion {

    @Override
    public long countMatchingLines(Modification fileModification) {
        if (fileModification.fileNameEndsWith(".py") && !fileModification.fileNameMatches(".*test.*")) {
            DiffParser diff = new DiffParser(fileModification.getDiff());

            return diff.getBlocks().stream()
                    .map(DiffBlock::getLinesInNewFile)
                    .flatMap(Collection::stream)
                    .filter(line -> line.getType() == DiffLineType.ADDED)
                    .filter(LinesOfAddedFlatScript::isFlatScriptLine)
                    .count();
        }

        return 0;
    }

    private static boolean isFlatScriptLine(DiffLine line) {
        String lineContent = line.getLine();
        return !(lineContent.startsWith(" ") || lineContent.startsWith("\t"));
    }
}
